package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.Command;
import ch.fridolins.fridowpi.command.ICommand;
import frc.robot.commands.telescopeArm.CheckWrungContact;
import frc.robot.commands.telescopeArm.MoveArmToFirstwrungPosition;
import frc.robot.commands.telescopeArm.MoveArmToHandover;
import frc.robot.commands.telescopeArm.RetractTelescopearm;
import frc.robot.commands.tilter.MoveTilterToTraversalPosition;
import frc.robot.subsystems.climber.TelescopeArm;
import frc.robot.subsystems.climber.Tilter;

public class FirstWrung extends Command{  
    public FirstWrung() {
        requires(TelescopeArm.getInstance());
        requires(Tilter.getInstance());
    }

    ICommand checkWrung;
    ICommand retractTelescopeArm;
    ICommand handover;

    @Override
    public void initialize() {
        checkWrung = new CheckWrungContact();
        new MoveTilterToTraversalPosition();
    }

    @Override
    public void execute() {
        if (checkWrung.isFinished()) {
            if (TelescopeArm.getInstance().hasWrungContact()) {
                retractTelescopeArm = new RetractTelescopearm();
                if (retractTelescopeArm.isFinished()) {
                    handover = new MoveArmToHandover();
                    if (handover.isFinished()) {
                        if (Tilter.getInstance().hasWrungContact()) {
                            end(false);
                        }
                        else 
                            end(true);
                        }
                }
            }
            else {
                new MoveArmToFirstwrungPosition();
                end(true);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
    }
}
