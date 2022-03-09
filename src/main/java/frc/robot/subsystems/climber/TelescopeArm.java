package frc.robot.subsystems.climber;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import ch.fridolins.fridowpi.command.Command;
import ch.fridolins.fridowpi.motors.utils.PidValues;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ch.fridolins.fridowpi.initializer.Initialisable;
import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.Binding;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.LimitSwitch;
import ch.fridolins.fridowpi.pneumatics.FridoSolenoid;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Button;
import frc.robot.Joysticks;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.Tilter.Constants;
import frc.robot.subsystems.climber.base.TelescopeArmBase;

import static java.lang.Math.abs;

public class TelescopeArm extends TelescopeArmBase {
    private static TelescopeArmBase instance = null;
    private static final boolean enabled = true;

    public static final class Constants {
        public static final FridolinsMotor.LimitSwitchPolarity limitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kNormallyClosed;
        public static final double zeroSpeed = -0.05;

        public static final boolean rightInverted = true;
        public static final boolean leftInverted = false;

        public static final class PID {
            public static final PidValues climbingPid = new PidValues(0.12, 0, 0.00112725, -0.045);
            public static final PidValues expandRetractPid = new PidValues(4e-5, 0, 0, 1.56e-4);

            static {
                expandRetractPid.cruiseVelocity = Optional.of(5800.0);
//                expandRetractPid.acceleration = Optional.of(17400.0);
                expandRetractPid.acceleration = Optional.of(10400.0);
            }
        }

        public static final class Ids {
            public static final int right = 22;
            public static final int rightFollower = 24;
            public static final int left = 21;
            public static final int leftFollower = 23;
        }

        public static final class Heights {
            public static final double firstWrung = 145;
            public static final double checkFirst = 135;
            public static final double traversalPreparationHeight = 100;
            public static final double traversalHeight = 125;
            public static final double traversalCheck = 115;
            public static final double handover = 0;
        }

        public static final class MayExceeded {
            public static final double limit = 147;
            public static final double softMax = 145;
            public static final double speed = -0.05;
        }
    }

    private static class Motors implements Initialisable {
        private boolean initialized = false;
        private FridolinsMotor rightFollower;
        private FridolinsMotor leftFollower;
        public FridolinsMotor left;
        public FridolinsMotor right;

        public Motors() {
            Initializer.getInstance().addInitialisable(this);

            right = new FridoCanSparkMax(Constants.Ids.right, MotorType.kBrushless);
            rightFollower = new FridoCanSparkMax(Constants.Ids.rightFollower, MotorType.kBrushless);
            left = new FridoCanSparkMax(Constants.Ids.left, MotorType.kBrushless);
            leftFollower = new FridoCanSparkMax(Constants.Ids.leftFollower, MotorType.kBrushless);
        }

        @Override
        public void init() {
            initialized = true;

            right.setInverted(Constants.rightInverted);
            left.setInverted(Constants.leftInverted);
            rightFollower.setInverted(Constants.rightInverted);
            leftFollower.setInverted(Constants.leftInverted);

            right.setPID(Constants.PID.expandRetractPid);
            left.setPID(Constants.PID.expandRetractPid);

            rightFollower.follow(right, FridolinsMotor.DirectionType.followMaster);
            leftFollower.follow(left, FridolinsMotor.DirectionType.followMaster);
            right.setIdleMode(FridolinsMotor.IdleMode.kBrake);
            left.setIdleMode(FridolinsMotor.IdleMode.kBrake);
            rightFollower.setIdleMode(FridolinsMotor.IdleMode.kBrake);
            leftFollower.setIdleMode(FridolinsMotor.IdleMode.kBrake);

            left.configEncoder(ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice.kBuildin, 1);
            right.configEncoder(ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice.kBuildin, 1);


            right.enableReverseLimitSwitch(Constants.limitSwitchPolarity, true);
            left.enableReverseLimitSwitch(Constants.limitSwitchPolarity, true);

            right.enableForwardLimitSwitch(Constants.limitSwitchPolarity, false);
            left.enableForwardLimitSwitch(Constants.limitSwitchPolarity, false);
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }
    }

    Motors motors = new Motors();
    LimitSwitch bottomLimitSwitchRight;
    LimitSwitch bottomLimitSwitchLeft;

    LimitSwitch wrungContactSwitchRight;
    LimitSwitch wrungContactSwitchLeft;

    PIDController climberPid;
    PIDController climberErrorPid;

    private TelescopeArm() {
        requires(motors);
        JoystickHandler.getInstance().bind(this);
    }

    public static TelescopeArmBase getInstance() {
        if (instance == null) {
            if (enabled)
                instance = new TelescopeArm();
            else
                instance = new TelescopeArmBase();
        }
        return instance;
    }

    @Override
    public void periodic() {
        super.periodic();
        if (isInitialized()) {
            maxExceededCheck();
//            zeroEncoders();
        }
    }

    private void zeroEncoders() {
        if (getBottomLimitSwitchRight())
            motors.right.setEncoderPosition(0);

        if (getBottomLimitSwitchLeft())
            motors.left.setEncoderPosition(0);
    }

    private void maxExceededCheck() {
        if (motors.right.getEncoderTicks() >= Constants.MayExceeded.limit) {
            DriverStation.reportError("Max height of right telescope arm reached", false);
            gotoRightSoftMax();
        } else if (motors.right.getEncoderTicks() > Constants.MayExceeded.limit) {
            DriverStation.reportError("Max height of right telescope arm exceeded", false);
            gotoRightSoftMax();
        }

        if (motors.left.getEncoderTicks() >= Constants.MayExceeded.limit) {
            DriverStation.reportError("Max height of left telescope arm reached", false);
            gotoLeftSoftMax();
        } else if (motors.left.getEncoderTicks() > Constants.MayExceeded.limit) {
            DriverStation.reportError("Max height of left telescope arm exceeded", false);
            gotoLeftSoftMax();
        }

    }

    // locks code until motor is in a save position
    private void gotoRightSoftMax() {
        motors.right.stopMotor();
        motors.right.set(Constants.MayExceeded.speed);
        //noinspection StatementWithEmptyBody
        while (motors.right.getEncoderTicks() >= Constants.MayExceeded.softMax) ;
        motors.right.stopMotor();
    }

    // locks code until motor is in a save position
    private void gotoLeftSoftMax() {
        motors.left.stopMotor();
        motors.left.set(Constants.MayExceeded.speed);
        //noinspection StatementWithEmptyBody
        while (motors.left.getEncoderTicks() >= Constants.MayExceeded.softMax) ;
        motors.left.stopMotor();
    }

    private void gotoPos(double pos) {
        motors.right.setPidTarget(pos, FridolinsMotor.PidType.smartMotion);
        motors.left.setPidTarget(pos, FridolinsMotor.PidType.smartMotion);
    }

    GotoPosClimbing gotoPosClimbingCommand = new GotoPosClimbing();
    double currentErrorCorr = 0;
    double outputRight = 0;
    double outputLeft = 0;

    private class GotoPosClimbing extends Command {
        private Optional<Double> target = Optional.empty();

        public void updateTarget(double target) {
            this.target = Optional.of(target);
            climberPid.setSetpoint(target);
        }


        @Override
        public void initialize() {
            if (target.isEmpty())
                return;
            climberPid.setSetpoint(target.get());
            climberErrorPid.setSetpoint(0);
        }

        @Override
        public void execute() {
            System.out.println("target = " + target);
            double out = climberPid.calculate(
                    (motors.left.getEncoderTicks() + motors.right.getEncoderTicks()) / 2);
            double errCorr = climberErrorPid.calculate(motors.right.getEncoderTicks() - motors.left.getEncoderTicks());

            if (abs(out + errCorr) > 1)
                out = Math.signum(out + errCorr);

            if (abs(out - errCorr) > 1)
                out = Math.signum(out - errCorr);

            currentErrorCorr = errCorr;
            outputRight = out + errCorr;
            outputLeft = out - errCorr;
            motors.right.set(out + errCorr);
            motors.left.set(out - errCorr);
//            double out = climberPid.calculate(motors.right.getEncoderTicks());
//            motors.right.set(out);
            target.ifPresent((target) -> {
                if (motors.right.getEncoderTicks() < target)
                    System.out.println("[right] target overshot encoder ticks = " + motors.right.getEncoderTicks());

                if (motors.left.getEncoderTicks() < target)
                    System.out.println("[left] target overshot encoder ticks = " + motors.left.getEncoderTicks());
            });
        }

        @Override
        public void end(boolean interrupted) {
            // DO NOT call stopMotors or you will have infinite recursion
            stopLeftMotor();
            stopRightMotor();
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        //        @Override
//        public boolean isFinished() {
//            return (climberPid.atSetpoint() && climberPid.atSetpoint()) || target.isEmpty();
//        }
    }

    private void gotoPosClimbing(double pos) {
        gotoPosClimbingCommand.updateTarget(pos);
        CommandScheduler.getInstance().schedule(gotoPosClimbingCommand);
    }

    @Override
    public void gotoFirstWrung() {
        gotoPos(Constants.Heights.firstWrung);
    }

    @Override
    public void gotoCheckFristWrung() {
        gotoPos(Constants.Heights.checkFirst);
    }

    @Override
    public void gotoTraversalWrungPreparation() {
        gotoPos(Constants.Heights.traversalPreparationHeight);
    }

    @Override
    public void gotoTraversalWrung() {
        gotoPos(Constants.Heights.traversalHeight);
    }

    @Override
    public void gotoHandover() {
        gotoPos(Constants.Heights.handover);
    }

    @Override
    public void resetEncoders() {
        motors.right.setEncoderPosition(0);
        motors.left.setEncoderPosition(0);
    }

    @Override
    public boolean getBottomLimitSwitchLeft() {
        return bottomLimitSwitchLeft.get();
    }

    @Override
    public boolean getBottomLimitSwitchRight() {
        return bottomLimitSwitchRight.get();
    }

    @Override
    public boolean hasWrungContact() {
        return wrungContactSwitchLeft.get() && wrungContactSwitchRight.get();
    }

    @Override
    public void retract() {
        gotoPos(0.0);
    }

    @Override
    public void startZero() {
        motors.left.set(Constants.zeroSpeed);
        motors.right.set(Constants.zeroSpeed);
    }

    @Override
    public void init() {
        super.init();
        motors.init();

        bottomLimitSwitchRight = motors.right.getReverseLimitSwitch();
        bottomLimitSwitchLeft = motors.left.getReverseLimitSwitch();

        // TODO: use DIO ports
//        wrungContactSwitchRight = motors.right.getReverseLimitSwitch();
//        wrungContactSwitchLeft = motors.left.getReverseLimitSwitch();

        climberPid = new PIDController(Constants.PID.climbingPid.kP, Constants.PID.climbingPid.kI, Constants.PID.climbingPid.kD);
        climberErrorPid = new PIDController(Constants.PID.climbingPid.kP, Constants.PID.climbingPid.kI, Constants.PID.climbingPid.kD);

        setDefaultCommand(new CommandBase() {
            {
                addRequirements(TelescopeArm.this);
            }

            @Override
            public void execute() {
                motors.right.set(MathUtil.applyDeadband(JoystickHandler.getInstance().getJoystick(Joysticks.Drive).getThrottle(), 0.05) * 0.7);
//                motors.left.set(MathUtil.applyDeadband(JoystickHandler.getInstance().getJoystick(Joysticks.Drive).getThrottle(), 0.05) * 0.7);
                motors.left.set(MathUtil.applyDeadband(JoystickHandler.getInstance().getJoystick(Joysticks.Drive).getY(), 0.05) * 0.7);
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        });
    }

    @Override
    public void stopLeftMotor() {
        motors.left.stopMotor();
    }

    @Override
    public void stopRightMotor() {
        motors.right.stopMotor();
    }

    @Override
    public void stopMotors() {
        CommandScheduler.getInstance().cancel(gotoPosClimbingCommand);
        stopLeftMotor();
        stopRightMotor();
    }

    @Override
    public boolean isAtTarget() {
        return motors.left.pidAtTarget() && motors.right.pidAtTarget();
    }

    @Override
    public List<Binding> getMappings() {
        return List.of(
                new Binding(Joysticks.Drive, () -> 3, Button::whenPressed, new InstantCommand(this::resetEncoders)),
                // Climb up
                new Binding(Joysticks.Drive, () -> 9, Button::whileHeld, new CommandBase() {
                    {
                        addRequirements(TelescopeArm.this);
                    }

                    @Override
                    public void initialize() {
                        gotoPosClimbing(0);
                    }

                    @Override
                    public void end(boolean interrupted) {
                        stopMotors();
                    }
                }),

                // Go to Ground
                new Binding(Joysticks.Drive, () -> 10, Button::whileHeld, new CommandBase() {
                    {
                        addRequirements(TelescopeArm.this);
                    }

                    @Override
                    public void initialize() {
                        gotoPosClimbing(127);
                    }

                    @Override
                    public void end(boolean interrupted) {
                        stopMotors();
                    }
                }),

                // Go over first wrung
                new Binding(Joysticks.Drive, () -> 1, Button::whileHeld, new CommandBase() {
                    {
                        addRequirements(TelescopeArm.this);
                    }

                    @Override
                    public void initialize() {
                        gotoPos(145);
                    }

                    @Override
                    public void end(boolean interrupted) {
                        stopMotors();
                    }
                }),

                // Touch wrung
                new Binding(Joysticks.Drive, () -> 5, Button::whileHeld, new CommandBase() {
                    {
                        addRequirements(TelescopeArm.this);
                    }

                    @Override
                    public void initialize() {
                        gotoPos(135);
                    }

                    @Override
                    public void end(boolean interrupted) {
                        stopMotors();
                    }
                }),

                // go slowly down
                new Binding(Joysticks.Drive, () -> 7, Button::whileHeld, new CommandBase() {
                    {
                        addRequirements(TelescopeArm.this);
                    }

                    private double setPoint = 0;
                    private Timer timer = new Timer();

                    @Override
                    public void initialize() {
                        setPoint = (motors.right.getEncoderTicks() + motors.left.getEncoderTicks()) / 2;
                        gotoPosClimbing(setPoint);
                        timer.reset();
                        timer.start();
                    }

                    @Override
                    public void execute() {
                        setPoint -= timer.get() * 3;
                        gotoPosClimbingCommand.updateTarget(setPoint);
                        timer.reset();
                        timer.start();
                    }

                    @Override
                    public void end(boolean interrupted) {
                        stopMotors();
                    }

                    @Override
                    public boolean isFinished() {
                        return getBottomLimitSwitchLeft() && getBottomLimitSwitchRight();
                    }
                })
        );
    }

    @Override
    public void gotoCheckTraversalWrung() {
        gotoPos(Constants.Heights.traversalCheck);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.addDoubleProperty("encoder pos right", () -> isInitialized() ? motors.right.getEncoderTicks() : 0.0, null);
        builder.addDoubleProperty("encoder pos left", () -> isInitialized() ? motors.left.getEncoderTicks() : 0.0, null);
        builder.addBooleanProperty("bottom limit switch right", () -> isInitialized() ? getBottomLimitSwitchLeft() : false, null);
        builder.addBooleanProperty("bottom limit switch left", () -> isInitialized() ? getBottomLimitSwitchRight() : false, null);
        builder.addDoubleProperty("output right", () -> isInitialized() ? motors.right.get() : 0.0, null);
        builder.addDoubleProperty("output left", () -> isInitialized() ? motors.left.get() : 0.0, null);
        builder.addDoubleProperty("output joystick", () -> isInitialized() ? MathUtil.applyDeadband(JoystickHandler.getInstance().getJoystick(Joysticks.Drive).getY(), 0.05) * 0.7 : 0.0, null);
        builder.addDoubleProperty("error", () -> isInitialized() ? motors.right.getEncoderTicks() - motors.left.getEncoderTicks() : 0.0, null);
        builder.addDoubleProperty("error corr of pid", () -> isInitialized() ? currentErrorCorr : 0.0, null);
    }
}