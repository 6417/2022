package frc.robot.subsystems.climber;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.LimitSwitch;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.subsystems.climber.base.TelescopeArmBase;

public class TelescopeArm extends TelescopeArmBase {
    private static TelescopeArmBase instance = null;
    private static final boolean enabled = true;

    public static final class Constants {
        public static final FridolinsMotor.LimitSwitchPolarity limitSwitchPolarity = FridolinsMotor.LimitSwitchPolarity.kDisabled;
        public static final double zeroSpeed = -0.1;

        public static final class Ids {
            public static final int right = 0;
            public static final int rightFollower = 0;
            public static final int left = 0;
            public static final int leftFollower = 0;
        }

        public static final class Heights {
            public static final double firstWrung = 0;
            public static final double traversalHeight = 0;
            public static final double extended = 0;
        }

        public static final class MayExceeded {
            public static final double limit = 0;
            public static final double softMax = 0;
            public static final double speed = -0.1;
        }
    }

    private static class Motors extends FridoCanSparkMax {
        private boolean initialized = false;

        private FridoCanSparkMax rightFollower;
        private FridoCanSparkMax leftFollower;
        private FridoCanSparkMax left;


        public Motors() {
            super(Constants.Ids.right, MotorType.kBrushless);
            Initializer.getInstance().addInitialisable(this);
        }

        @Override
        public void init() {
            initialized = true;
            rightFollower = new FridoCanSparkMax(Constants.Ids.rightFollower, MotorType.kBrushless);
            left = new FridoCanSparkMax(Constants.Ids.left, MotorType.kBrushless);
            leftFollower = new FridoCanSparkMax(Constants.Ids.leftFollower, MotorType.kBrushless);

            rightFollower.follow(this);
            left.follow(this);
            leftFollower.follow(this);

            configEncoder(FridoFeedBackDevice.kBuildin, 1);
            enableForwardLimitSwitch(Constants.limitSwitchPolarity, true);
        }

        @Override
        public boolean isInitialized() {
            return initialized;
        }
    }

    Motors motors = new Motors();
    LimitSwitch bottomLimitSwitch;

    private TelescopeArm() {
        requires(motors);
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
        maxExceededCheck();
    }

    private void maxExceededCheck() {
        if (motors.getEncoderTicks() == Constants.MayExceeded.limit) {
            DriverStation.reportError("Max height of telescope arm reached", false);
            gotoSoftMax();
        } else if (motors.getEncoderTicks() > Constants.MayExceeded.limit) {
            DriverStation.reportError("Max height of telescope arm exceeded", false);
            gotoSoftMax();
        }
    }

    private void gotoSoftMax() {
        motors.stopMotor();
        motors.set(Constants.MayExceeded.speed);
        //noinspection StatementWithEmptyBody
        while (motors.getEncoderTicks() >= Constants.MayExceeded.softMax) ;
        motors.stopMotor();
    }

    private void gotoPos(double pos) {
        motors.setPosition(pos);
    }

    @Override
    public void gotoFirstWrung() {
        gotoPos(Constants.Heights.firstWrung);
    }

    @Override
    public void gotoTraversalWrung() {
        gotoPos(Constants.Heights.traversalHeight);
    }

    @Override
    public void resetEncoders() {
        motors.setEncoderPosition(0);
    }

    @Override
    public boolean getBottomLimitSwitch() {
        return bottomLimitSwitch.get();
    }

    @Override
    public void retract() {
        gotoPos(0.0);
    }

    @Override
    public void startZero() {
        motors.set(Constants.zeroSpeed);
    }

    @Override
    public void extend() {
        gotoPos(Constants.Heights.extended);
    }

    @Override
    public void init() {
        super.init();
        // TODO: Find out which limit switch is used
        bottomLimitSwitch = null;
    }

    @Override
    public boolean isAtTarget() {
        return motors.pidAtTarget();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.addDoubleProperty("encoder pos", motors::getEncoderTicks, null);
        builder.addBooleanProperty("bottom limit switch", this::getBottomLimitSwitch, null);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("EncoderPosition", () -> motor.getEncoderTicks(), null);
        super.initSendable(builder);
    }
}