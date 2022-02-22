package frc.robot;

import java.util.List;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.pneumatics.PneumaticHandler;
import ch.fridolins.fridowpi.sensors.Navx;
import ch.fridolins.fridowpi.utils.CSVLogger;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.climber.AngleFilter;
import frc.robot.subsystems.climber.Tilter;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
        PneumaticHandler.getInstance().configureCompressor(30, PneumaticsModuleType.CTREPCM);
        JoystickHandler.getInstance().setupJoysticks(List.of(Joysticks.Drive));
        Navx.getInstance();
        Tilter.getInstance();
        Initializer.getInstance().init();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    CSVLogger filterTest = new CSVLogger("/tmp/filterTest.csv", "time stamp", "current angle", "filtered angle");
    AngleFilter filter = new AngleFilter(100);
    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
        double angle = Navx.getInstance().getPitch();
        filter.update(angle);
        filterTest.put("time stamp", System.currentTimeMillis());
        filterTest.put("current angle", angle);
        filterTest.put("filtered angle", filter.get());

        Tilter.getInstance().setVelocity(JoystickHandler.getInstance().getJoystick(Joysticks.Drive).getY());
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }
}
