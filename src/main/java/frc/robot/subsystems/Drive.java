package frc.robot.subsystems;

import java.util.List;

import ch.fridolins.fridowpi.initializer.Initialisable;
import ch.fridolins.fridowpi.joystick.Binding;
import ch.fridolins.fridowpi.joystick.IJoystickButtonId;
import ch.fridolins.fridowpi.joystick.IJoystickId;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.motors.FridoFalcon500;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.sensors.Navx;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveKinematicsConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Joysticks;
import frc.robot.autonomous.RecordTrajectoryCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.SpeedCommand;
import frc.robot.subsystems.base.DriveBase;

public class Drive extends DriveBase {
    private static final boolean enabled = false;
    private static DriveBase instance = null;
    private LinearFilter driveFilter;
    private int driveDirection = 1;
    public static final class Constants {
        public static final class Speeds {
            public static final double slow = 0.375;
            public static final double normal = 1;
            public static final double turningSpeed = 0.5;
        }

        public static final int filterSamples = 20;

        public static final class Odometry {
            public static final double wheelPerimeter = 321.6;
            public static final double transmission = 6.5625;
            public static final int encoderResolution = 2048;

            public static final double encoderToMetersConversion = (1000 / wheelPerimeter) * transmission
                    * encoderResolution;
            public static final double trackWidthMeters = 0.58;
        }

        public static final class PathWeaver {
            public static final double ksMeters = 0.72105;
            public static final double kvMetersPerSecoond = 2.1639;
            public static final double ka = 0.21993;

            public static final double kMaxSpeed = 1.5;
            public static final double kMaxAcceleration = 0.7;
            public static final double kMaxCentripetalAcceleration = 0.5;

            public static final double kRamseteB = 2;
            public static final double kRamseteZeta = 0.7;

            public static final double kP = 2.7;
            public static final double kI = 0;
            public static final double kD = 0;
        }

        public static final class Motors {
            public static final class IDs {
                public static final int leftMaster = 11;
                public static final int leftBackFollower = 15;
                public static final int leftFrontFollower = 13;
                public static final int rightMaster = 12;
                public static final int rightBackFollower = 14;
                public static final int rightFrontFollower = 10;
            }
        }

        public static final class ButtonBindings {
            public static final IJoystickId joystick = Joysticks.Drive;
            public static final IJoystickButtonId slowButton = () -> 8;
            public static final IJoystickButtonId recordButton = () -> 2;
        }

        public static class Autonomous {
            public static final double recordingCooldownSeconds = 0.1;
            public static final double velocityThresholdStart = 0.01;
            public static final double velocityThresholdEnd = 0.02;
            public static final double positionCorrection = 0.9875;
        }
    }

    private class Motors implements Initialisable {
        private boolean initialized = true;

        public FridolinsMotor right;
        private FridolinsMotor rightFrontFollower;
        public FridolinsMotor left;
        private FridolinsMotor leftBackFollower;
        private FridolinsMotor rightBackFollower;
        private FridolinsMotor leftFrontFollower;

        public Motors() {
            // Initializer.getInstance().addInitialisable(this);
            right = new FridoFalcon500(Constants.Motors.IDs.rightMaster);
            rightFrontFollower = new FridoFalcon500(Constants.Motors.IDs.rightFrontFollower);
            rightBackFollower = new FridoFalcon500(Constants.Motors.IDs.rightBackFollower);

            left = new FridoFalcon500(Constants.Motors.IDs.leftMaster);
            leftFrontFollower = new FridoFalcon500(Constants.Motors.IDs.leftFrontFollower);
            leftBackFollower = new FridoFalcon500(Constants.Motors.IDs.leftBackFollower);
        }

        @Override
        public void init() {
            right.factoryDefault();
            rightFrontFollower.factoryDefault();
            rightFrontFollower.factoryDefault();
            left.factoryDefault();
            leftBackFollower.factoryDefault();
            leftFrontFollower.factoryDefault();

            right.setIdleMode(FridolinsMotor.IdleMode.kBrake);
            rightFrontFollower.setIdleMode(FridolinsMotor.IdleMode.kBrake);
            rightBackFollower.setIdleMode(FridolinsMotor.IdleMode.kBrake);
            left.setIdleMode(FridolinsMotor.IdleMode.kBrake);
            leftBackFollower.setIdleMode(FridolinsMotor.IdleMode.kBrake);
            leftFrontFollower.setIdleMode(FridolinsMotor.IdleMode.kBrake);

            rightFrontFollower.follow(right, FridolinsMotor.DirectionType.followMaster);
            rightBackFollower.follow(right, FridolinsMotor.DirectionType.followMaster);
            leftBackFollower.follow(left, FridolinsMotor.DirectionType.followMaster);
            leftFrontFollower.follow(left, FridolinsMotor.DirectionType.followMaster);

            System.out.println("Motors init completed");
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }
    }

    private Motors motors = new Motors();

    private Drive() {
        JoystickHandler.getInstance().bind(this);

        driveFilter = LinearFilter.movingAverage(Constants.filterSamples);
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
        motors.init();
        setDefaultCommand(new DriveCommand());

        odometry = new DifferentialDriveOdometry(new Rotation2d(0),
                new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
        kinematics = new DifferentialDriveKinematics(Constants.Odometry.trackWidthMeters);

        configMotors();
        tankDrive = new DifferentialDrive(motors.left, motors.right);

        odometry = new DifferentialDriveOdometry(new Rotation2d(0),
                new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
        kinematics = new DifferentialDriveKinematics(Constants.Odometry.trackWidthMeters);

        resetSensors();

        setDefaultCommand(new DriveCommand());

        configSimpleMotorFeedforward();

        configConstrains();

        configTrajectoryConfig();

        rightVelocityController = new PIDController(Constants.PathWeaver.kP, Constants.PathWeaver.kI,
                Constants.PathWeaver.kD);
        leftVelocityController = new PIDController(Constants.PathWeaver.kP, Constants.PathWeaver.kI,
                Constants.PathWeaver.kD);

        System.out.println("Drive init completed");
    }

    @Override
    public void setDirectionToForward() {
        driveDirection = 1;
    }

    @Override
    public void setDirectionToReverse() {
        driveDirection = -1;
    }

    public void resetSensors() {
        Navx.getInstance().reset();
        odometry.resetPosition(new Pose2d(0, 0, new Rotation2d(0)), new Rotation2d(0));

        motors.right.setEncoderPosition(0.0);
        motors.left.setEncoderPosition(0.0);
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
        return -motors.left.getEncoderTicks() / Constants.Odometry.encoderToMetersConversion * driveDirection;
    }

    private double getRightWheelDistance() {
        return motors.right.getEncoderTicks() / Constants.Odometry.encoderToMetersConversion * driveDirection;
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(
                -motors.left.getEncoderVelocity() * 10 / (Constants.Odometry.encoderToMetersConversion) * driveDirection,
                motors.right.getEncoderVelocity() * 10 / (Constants.Odometry.encoderToMetersConversion) * driveDirection);
    }

    public Pose2d getPosition() {
        return odometry.getPoseMeters();
    }

    public SimpleMotorFeedforward getMotorFeedforward() {
        return motorFeedforward;
    }

    public ChassisSpeeds getChassisSpeeds() {
        return kinematics.toChassisSpeeds(getWheelSpeeds());
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
                Constants.PathWeaver.kMaxAcceleration).setKinematics(kinematics).addConstraint(voltageConstraint)
                        .addConstraint(kinematicsConstraint).addConstraint(centripetalAccelerationConstraint);
    }

    public void resetOdometry(Pose2d setPoint) {
        resetSensors();
        odometry.resetPosition(setPoint, setPoint.getRotation());
        Navx.setYawOffset(setPoint.getRotation().getDegrees());
    }

    private void updateOdometry() {
        odometry.update(Rotation2d.fromDegrees(-Navx.getInstance().getAngle() * driveDirection), getLeftWheelDistance(),
                getRightWheelDistance());
    }

    public void drive() {
        // Getting the steer values from the joystick and the steering wheel
        double steer = JoystickHandler.getInstance().getJoystick(Joysticks.SteeringWheel).getX() * 2 * driveDirection;
        double velocity = driveFilter.calculate(JoystickHandler.getInstance().getJoystick(Joysticks.Drive).getY()) * speed * driveDirection;

        // Getting the sign of velocity and steer
        double velocitySign = Math.signum(velocity);
        double steerSign = Math.signum(steer);

        // Squaring the velocity, keeping it's sign
        velocity = Math.pow(velocity, 2) * velocitySign;

        // Calculating the mapped steer and velocity values
        double mappedSteer = Math.min(Math.abs(steer), 1) * velocity * steerSign;
        double mappedVelocity = Math.min(Math.abs(Math.abs(velocity) + Math.abs(velocity) * Math.min(0, 1 - Math.abs(steer))), Math.abs(velocity)) * velocitySign;

        // Driving with those values
        tankDrive.arcadeDrive(mappedSteer,
            mappedVelocity, false);
    }

    public void setSpeed(double maxSpeed) {
        this.speed = maxSpeed;
    }

    public void stop() {
        tankDrive.stopMotor();
    }

    public void tankDriveVolts(double left, double right) {
        motors.left.setVoltage(-left * driveDirection);
        motors.right.setVoltage(right * driveDirection);
        tankDrive.feed();
    }

    @Override
    public void periodic() {
        updateOdometry();
    }

    @Override
    public List<Binding> getMappings() {
        return List.of(
                new Binding(Constants.ButtonBindings.joystick, Constants.ButtonBindings.slowButton,
                        Button::toggleWhenPressed, new SpeedCommand()),
                new Binding(Constants.ButtonBindings.joystick, Constants.ButtonBindings.recordButton, Button::whileHeld,
                        new RecordTrajectoryCommand()),
                new Binding(Constants.ButtonBindings.joystick, () -> 11,
                        Button::whenPressed, new InstantCommand(() -> setDirectionToForward())),
                new Binding(Constants.ButtonBindings.joystick, () -> 12,
                        Button::whenPressed, new InstantCommand(() -> setDirectionToReverse())));
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addStringProperty("OdometryPoseX", () -> getPosition().toString(), null);
        builder.addDoubleProperty("Angle", () -> Navx.getInstance().getAngle(), null);
        builder.addDoubleProperty("ANGLE_ODOMETRY", () -> getPosition().getRotation().getDegrees(), null);
        builder.addDoubleProperty("rightSpeedmeterspersecond", () -> getWheelSpeeds().rightMetersPerSecond, null);
        builder.addDoubleProperty("leftSpeedmeterspersecond", () -> getWheelSpeeds().leftMetersPerSecond, null);
        builder.addDoubleProperty("leftSpeed", () -> motors.left.getEncoderVelocity(), null);
        builder.addDoubleProperty("rightSpeed", () -> motors.right.getEncoderVelocity(), null);
        super.initSendable(builder);
    }
}
