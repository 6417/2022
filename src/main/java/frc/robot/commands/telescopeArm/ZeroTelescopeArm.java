package frc.robot.commands.telescopeArm;

import ch.fridolins.fridowpi.command.Command;
import ch.fridolins.fridowpi.command.ParallelCommandGroup;
import ch.fridolins.fridowpi.command.SequentialCommandGroup;
import ch.fridolins.fridowpi.motors.LimitSwitch;
import ch.fridolins.fridowpi.utils.LatchedBoolean;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.climber.TelescopeArm;

import java.util.function.Consumer;

public class ZeroTelescopeArm extends SequentialCommandGroup {
    public class GoUpUntilLimitSwitchNotPressed extends CommandBase {
        Consumer<Double> setMotor;
        Runnable stopMotor;
        LimitSwitch limitSwitch;

        public GoUpUntilLimitSwitchNotPressed(Consumer<Double> setMotor, Runnable stopMotor, LimitSwitch limitSwitch) {
            this.setMotor = setMotor;
            this.limitSwitch = limitSwitch;
            this.stopMotor = stopMotor;
        }

        @Override
        public void initialize() {
            setMotor.accept(TelescopeArm.Constants.zeroMotorSpeed);
        }

        @Override
        public void end(boolean interrupted) {
            stopMotor.run();
        }

        @Override
        public boolean isFinished() {
            return !limitSwitch.get();
        }
    }

    public class FinalGoDown extends CommandBase {
        Consumer<Double> setMotor;
        Runnable stopMotor;
        LimitSwitch limitSwitch;

        public FinalGoDown(Consumer<Double> setMotor, Runnable stopMotor, LimitSwitch limitSwitch) {
            this.setMotor = setMotor;
            this.limitSwitch = limitSwitch;
            this.stopMotor = stopMotor;
        }

        @Override
        public void initialize() {
            setMotor.accept(TelescopeArm.Constants.zeroMotorSpeed / 2);
        }

        @Override
        public void end(boolean interrupted) {
            stopMotor.run();
        }

        @Override
        public boolean isFinished() {
            return limitSwitch.get();
        }
    }

    public ZeroTelescopeArm() {
        requires(TelescopeArm.getInstance());
        addCommands(new CommandBase() {
                        @Override
                        public void initialize() {
                            TelescopeArm.getInstance().startZero();
                        }

                        @Override
                        public void end(boolean interrupted) {
                            TelescopeArm.getInstance().stopMotors();
                        }

                        @Override
                        public boolean isFinished() {
                            return TelescopeArm.getInstance().getBottomLimitSwitchLeft() && TelescopeArm.getInstance().getBottomLimitSwitchRight();
                        }
                    }, new ParallelCommandGroup(new GoUpUntilLimitSwitchNotPressed(TelescopeArm.getInstance()::setMotorLeft, TelescopeArm.getInstance()::stopLeftMotor, TelescopeArm.getInstance()::getBottomLimitSwitchLeft), new GoUpUntilLimitSwitchNotPressed(TelescopeArm.getInstance()::setMotorRight, TelescopeArm.getInstance()::stopRightMotor, TelescopeArm.getInstance()::getBottomLimitSwitchRight)),
                new ParallelCommandGroup(new FinalGoDown(TelescopeArm.getInstance()::setMotorLeft, TelescopeArm.getInstance()::stopLeftMotor, TelescopeArm.getInstance()::getBottomLimitSwitchLeft), new FinalGoDown(TelescopeArm.getInstance()::setMotorRight, TelescopeArm.getInstance()::stopRightMotor, TelescopeArm.getInstance()::getBottomLimitSwitchRight)));
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        TelescopeArm.getInstance().resetEncoders();
        TelescopeArm.getInstance().stopMotors();
    }
}
