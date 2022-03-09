package frc.robot;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.pneumatics.PneumaticHandler;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.TelescopeArm;
import frc.robot.subsystems.climber.Tilter;

import java.util.List;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
        System.out.println("############################################ RobotInit");
        PneumaticHandler.getInstance().configureCompressor(30, PneumaticsModuleType.CTREPCM);
        PneumaticHandler.getInstance().init();
        JoystickHandler.getInstance().setupJoysticks(List.of(Joysticks.values()));
        TelescopeArm.getInstance();
        TelescopeArm.getInstance().init();
        Tilter.getInstance().init();
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


    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }
}