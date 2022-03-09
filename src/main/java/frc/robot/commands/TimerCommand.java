package frc.robot.commands;

import ch.fridolins.fridowpi.command.Command;
import edu.wpi.first.wpilibj.Timer;

public class TimerCommand extends Command {
    private Timer timer;
    private double timerTarget;

    public TimerCommand(double timerTargetSeconds) {
        this.timerTarget = timerTargetSeconds;
    }

    @Override
    public void initialize() {
        super.initialize();
        timer = new Timer();
        timer.start();
    }     

    @Override
    public boolean isFinished() {
        return timer.get() >= timerTarget;
    }
}