package frc.robot.subsystems.climber;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ch.fridolins.fridowpi.initializer.Initialisable;
import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.LimitSwitch;
import ch.fridolins.fridowpi.pneumatics.FridoSolenoid;
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
            public static final double checkFirst = 0;
            public static final double traversalHeight = 0;
            public static final double extended = 0;
        }

        public static final class MayExceeded {
            public static final double limit = 0;
            public static final double softMax = 0;
            public static final double speed = -0.1;
        }
    }

    private static class Motors implements Initialisable{
        private boolean initialized = false;
        private FridoCanSparkMax rightFollower;
        private FridoCanSparkMax leftFollower;
        public FridoCanSparkMax left;
        public FridoCanSparkMax right;

        public Motors() {
            Initializer.getInstance().addInitialisable(this);
        }

        @Override
        public void init() {
            initialized = true;
            right = new FridoCanSparkMax(Constants.Ids.right, MotorType.kBrushless);
            rightFollower = new FridoCanSparkMax(Constants.Ids.rightFollower, MotorType.kBrushless);
            left = new FridoCanSparkMax(Constants.Ids.left, MotorType.kBrushless);
            leftFollower = new FridoCanSparkMax(Constants.Ids.leftFollower, MotorType.kBrushless);

            rightFollower.follow(right);
            leftFollower.follow(left);

            left.configEncoder(ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice.kBuildin, 1);
            right.configEncoder(ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice.kBuildin, 1);

            right.enableForwardLimitSwitch(Constants.limitSwitchPolarity, true);
            left.enableForwardLimitSwitch(Constants.limitSwitchPolarity, true);

            right.enableReverseLimitSwitch(Constants.limitSwitchPolarity, false);
            left.enableReverseLimitSwitch(Constants.limitSwitchPolarity, false);
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

    private void gotoRightSoftMax() {
        motors.right.stopMotor();
        motors.right.set(Constants.MayExceeded.speed);
        //noinspection StatementWithEmptyBody
        while (motors.right.getEncoderTicks() >= Constants.MayExceeded.softMax) ;
        motors.right.stopMotor();
    }

    private void gotoLeftSoftMax() {
        motors.left.stopMotor();
        motors.left.set(Constants.MayExceeded.speed);
        //noinspection StatementWithEmptyBody
        while (motors.left.getEncoderTicks() >= Constants.MayExceeded.softMax) ;
        motors.left.stopMotor();
    }

    private void gotoPos(double pos) {
        motors.right.setPosition(pos);
        motors.left.setPosition(pos);
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
    public void gotoTraversalWrung() {
        gotoPos(Constants.Heights.traversalHeight);
    }

    @Override
    public void resetEncoders() {
        motors.right.setEncoderPosition(0);
        motors.left.setEncoderPosition(0);
    }

    @Override
    public boolean getBottomLimitSwitch() {
        return bottomLimitSwitchRight.get() && bottomLimitSwitchLeft.get();
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
    public void extend() {
        gotoPos(Constants.Heights.extended);
    }

    @Override
    public void init() {
        super.init();
        // TODO: Find out which limit switch is used
        bottomLimitSwitchRight = null;
        bottomLimitSwitchLeft = null;

        wrungContactSwitchRight = null;
        wrungContactSwitchLeft = null;
    }

    @Override
    public boolean isAtTarget() {
        return motors.left.pidAtTarget() && motors.right.pidAtTarget();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        super.initSendable(builder);
        builder.addDoubleProperty("encoder pos", motors.right::getEncoderTicks, null);
        builder.addBooleanProperty("bottom limit switch", this::getBottomLimitSwitch, null);
    }
}