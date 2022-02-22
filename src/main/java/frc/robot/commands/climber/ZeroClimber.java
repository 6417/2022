package frc.robot.commands.climber;

import ch.fridolins.fridowpi.command.ParallelCommandGroup;

public class ZeroClimber extends ParallelCommandGroup {
    public ZeroClimber() {
        addCommands(new ZeroTilter(), new ZeroTelescopeArm());
    }
}
