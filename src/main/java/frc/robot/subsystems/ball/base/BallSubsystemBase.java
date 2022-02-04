package frc.robot.subsystems.ball.base;

import ch.fridolins.fridowpi.command.Command;
import ch.fridolins.fridowpi.command.ICommand;
import ch.fridolins.fridowpi.module.Module;

public class BallSubsystemBase extends Module{
    public ICommand getShootCommand() {return new Command();};
    public ICommand getPickupCommand() {return new Command();};
    public ICommand getRetractCommand() {return new Command();};
    public ICommand getReverseFlowCommand() {return new Command();};

    public void setShooterToVelocity() {};
}
