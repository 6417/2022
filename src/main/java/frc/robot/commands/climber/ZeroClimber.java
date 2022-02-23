package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.ParallelCommandGroup;
import frc.robot.commands.telescopeArm.ZeroTelescopeArm;
import frc.robot.commands.tilter.ZeroTilter;

public class ZeroClimber extends ParallelCommandGroup {
    public ZeroClimber() {
        addCommands(new ZeroTilter(), new ZeroTelescopeArm());
    }
}
