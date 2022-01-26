package frc.robot.subsystems;

import ch.fridolins.fridowpi.Initializer;
import ch.fridolins.fridowpi.base.Initialisable;
import ch.fridolins.fridowpi.base.motors.FridolinsMotor;
import ch.fridolins.fridowpi.command.Command;
import ch.fridolins.fridowpi.joystick.IJoystickButtonId;
import ch.fridolins.fridowpi.joystick.IJoystickId;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.joystick.joysticks.LogitechExtreme;
import ch.fridolins.fridowpi.sensors.Navx;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveKinematicsConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Joysticks;
import frc.robot.subsystems.base.DriveBase;

public class Drive extends DriveBase {

    private static final boolean enabled = true;
    private static DriveBase instance = null;

    private static final class Constants {
        public static final class Speeds {
            public static final double slow = 0.375;
            public static final double normal = 1;
        }

        public static final double encoderToMetersConversion = -22.14274406;
        public static final double trackWidthMeters = 0.5;

        public static final class PathWeaver {
            public static final double ksMeters = 0.12;
            public static final double kvMetersPerSecoond = 2.89;
            public static final double ka = 0.45;

            public static final double kMaxSpeed = 1.5;
            public static final double kMaxAcceleration = 0.7;
            public static final double kMaxCentripetalAcceleration = 0.5;

            public static final double kRamseteB = 2;
            public static final double kRamseteZeta = 0.7;

            public static final double kP = 0.035271;
            public static final double kI = 0;
            public static final double kD = 0;
        }

        public static final class Motors {
            public static final class IDs {
                public static final int leftMaster = 10;
                public static final int leftFollower = 11;
                public static final int rightMaster = 12;
                public static final int rightFollower = 13;
            }
        }

        public static final class ButtonBindings {
            public static final IJoystickId joystick = Joysticks.Drive;
            public static final IJoystickButtonId slowButton = LogitechExtreme._8;
        }
    }

    private class Motors implements Initialisable {
        private boolean initialized = false;

        public FridolinsMotor right;
        private FridolinsMotor rightFollower;
        public FridolinsMotor left;
        private FridolinsMotor leftFollower;

        public Motors() {
            Initializer.getInstance().addInitialisable(this);
        }

        @Override
        public void init() {
            initialized = true;

            right.factoryDefault();
            rightFollower.factoryDefault();
            left.factoryDefault();
            leftFollower.factoryDefault();

            right.setIdleMode(FridolinsMotor.IdleModeType.kBrake);
            rightFollower.setIdleMode(FridolinsMotor.IdleModeType.kBrake);
            left.setIdleMode(FridolinsMotor.IdleModeType.kBrake);
            leftFollower.setIdleMode(FridolinsMotor.IdleModeType.kBrake);

            rightFollower.follow(right, FridolinsMotor.DirectionType.invertMaster);
            leftFollower.follow(left, FridolinsMotor.DirectionType.invertMaster);

            Drive.this.registerSubmodule(right);
            Drive.this.registerSubmodule(rightFollower);
            Drive.this.registerSubmodule(left);
            Drive.this.registerSubmodule(leftFollower);
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }
    }

    private Motors motors = new Motors();

    private Drive() {
        Initializer.getInstance().addInitialisable(this);
    }

    public static DriveBase getInstance() {
        if (instance == null) {
            if (enabled)
                instance = new Drive();
            else
                instance = new DriveBase();
        }
        return instance;
    }

    private DifferentialDriveOdometry odometry;
    private DifferentialDriveKinematics kinematics;
    private DifferentialDrive tankDrive;

    private double speed = Constants.Speeds.normal;

    private SimpleMotorFeedforward motorFeedforward;
    private DifferentialDriveVoltageConstraint voltageConstraint;
    private DifferentialDriveKinematicsConstraint kinematicsConstraint;
    private CentripetalAccelerationConstraint centripetalAccelerationConstraint;

    private PIDController rightVelocityController;
    private PIDController leftVelocityController;

    private TrajectoryConfig trajectoryConfig;

    @Override
    public void init() {
        super.init();
        setDefaultCommand(new Command());

        odometry = new DifferentialDriveOdometry(new Rotation2d(0), new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
        kinematics = new DifferentialDriveKinematics(Constants.trackWidthMeters);

        configMotors();
        tankDrive = new DifferentialDrive(motors.left, motors.right);

        odometry = new DifferentialDriveOdometry(new Rotation2d(0), new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
        kinematics = new DifferentialDriveKinematics(Constants.trackWidthMeters);

        resetSensors();

        setDefaultCommand(new DriveCommand());

        configSimpleMotorFeedforward();

        configConstrains();

        configTrajectoryConfig();

        rightVelocityController = new PIDController(Constants.PathWeaver.kP, Constants.PathWeaver.kI, Constants.PathWeaver.kD);
        leftVelocityController = new PIDController(Constants.PathWeaver.kP, Constants.PathWeaver.kI, Constants.PathWeaver.kD);
    }

    public void resetSensors() {
        Navx.getInstance().reset();
        odometry.resetPosition(new Pose2d(0, 0, new Rotation2d(0)), new Rotation2d(0));
        // TODO: use FridolinsMotor interface
        // motors.right.resetEncoder();
        // motors.left.resetEncoder();
    }

    public DifferentialDriveKinematics getDriveKinematics() {
        return kinematics;
    }

    public DifferentialDriveVoltageConstraint getVoltageConstrain() {
        return voltageConstraint;
    }

    public DifferentialDriveKinematicsConstraint getKinematicsConstrain() {
        return kinematicsConstraint;
    }

    public CentripetalAccelerationConstraint getCentripetConstraint() {
        return centripetalAccelerationConstraint;
    }

    public TrajectoryConfig getTrajectoryConfig() {
        return trajectoryConfig;
    }

    public PIDController getLeftVelocityController() {
        return leftVelocityController;
    }

    public PIDController getRightVelocityController() {
        return rightVelocityController;
    }

    private double getLeftWheelDistance() {
        // TODO: user FridolinsMotor encoders
//        return encoderL.getPosition() / Constants.Drive.encoderToMetersConversion;
        return 0;
    }

    private double getRightWheelDistance() {
        // TODO: user FridolinsMotor encoders
//        return encoderR.getPosition() / -Constants.Drive.encoderToMetersConversion;
        return 0;
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        // TODO: user FridolinsMotor encoders
//        return new DifferentialDriveWheelSpeeds(encoderL.getVelocity() / (60 * Constants.Drive.encoderToMetersConversion),
//                -encoderR.getVelocity() / (60 * Constants.Drive.encoderToMetersConversion));
        return new DifferentialDriveWheelSpeeds(0, 0);
    }

    public Pose2d getPosition() {
        return odometry.getPoseMeters();
    }

    public SimpleMotorFeedforward getMotorFeedforward() {
        return motorFeedforward;
    }

    private void configMotors() {

    }

    private void configSimpleMotorFeedforward() {
        motorFeedforward = new SimpleMotorFeedforward(
                Constants.PathWeaver.ksMeters,
                Constants.PathWeaver.kvMetersPerSecoond,
                Constants.PathWeaver.ka);
    }

    private void configConstrains() {
        configVoltageConstrain();
        configKinematicsConstrain();
        configCetripedalAccelerationConstrain();
    }

    private void configVoltageConstrain() {
        voltageConstraint = new DifferentialDriveVoltageConstraint(
                motorFeedforward,
                kinematics,
                10);
    }

    private void configKinematicsConstrain() {
        kinematicsConstraint = new DifferentialDriveKinematicsConstraint(
                kinematics,
                Constants.PathWeaver.kMaxSpeed);
    }

    private void configCetripedalAccelerationConstrain() {
        centripetalAccelerationConstraint = new CentripetalAccelerationConstraint(
                Constants.PathWeaver.kMaxCentripetalAcceleration);
    }

    private void configTrajectoryConfig() {
        trajectoryConfig = new TrajectoryConfig(
                Constants.PathWeaver.kMaxSpeed,
                Constants.PathWeaver.kMaxAcceleration).setKinematics(kinematics).addConstraint(voltageConstraint).addConstraint(kinematicsConstraint).addConstraint(centripetalAccelerationConstraint);
    }

    public void resetOdometry(Pose2d setPoint) {
        odometry.resetPosition(setPoint, setPoint.getRotation());
        Navx.setYawOffset(setPoint.getRotation().getDegrees());
    }

    private void updateOdometry() {
        odometry.update(Rotation2d.fromDegrees(Navx.getInstance().getAngle()), getLeftWheelDistance(), getRightWheelDistance());
    }

    public void drive() {
        tankDrive.arcadeDrive(-JoystickHandler.getInstance().getJoystick(Joysticks.Drive).getX() * this.speed, JoystickHandler.getInstance().getJoystick(Joysticks.Drive).getY() * this.speed);
    }

    public void setSpeed(double maxSpeed) {
        this.speed = maxSpeed;
    }

    public void stop() {
        tankDrive.stopMotor();
    }

    public void tankDriveVolts(double left, double right) {
        motors.left.setVoltage(-left);
        motors.right.setVoltage(right);
        tankDrive.feed();
    }

    @Override
    public void periodic() {
        updateOdometry();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addStringProperty("OdometryPoseX", () -> getPosition().toString(), null);
        builder.addStringProperty("LeftDist", () -> getWheelSpeeds().toString(), null);
        builder.addDoubleProperty("Angle", () -> Navx.getInstance().getAngle(), null);
        builder.addDoubleProperty("ANGLE_ODOMETRY", () -> getPosition().getRotation().getDegrees(), null);
        builder.addDoubleProperty("rightSpeed", () -> getWheelSpeeds().rightMetersPerSecond, null);
        builder.addDoubleProperty("leftSpeed", () -> getWheelSpeeds().leftMetersPerSecond, null);
        super.initSendable(builder);
    }
}
