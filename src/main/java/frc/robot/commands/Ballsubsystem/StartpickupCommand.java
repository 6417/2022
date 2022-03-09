package frc.robot.commands.Ballsubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Expander.ExpandGripper;
import frc.robot.subsystems.ball.PickUp;
import frc.robot.subsystems.ball.Thrower;
import frc.robot.subsystems.ball.Transport;

public class StartpickupCommand extends SequentialCommandGroup{
    public StartpickupCommand() {
        addCommands(
            new ExpandGripper(),
            new CommandBase() {
                @Override
                public void initialize() {
                    PickUp.getInstance().runBrush();
                    Transport.getInstance().run();
                    Thrower.getInstance().setPercentage(0.1);
                }

                @Override
                public boolean isFinished() {
                    return true;
                }
            }
        );
    }
}
