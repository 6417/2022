package frc.robot;

import java.util.List;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.pneumatics.PneumaticHandler;
import ch.fridolins.fridowpi.sensors.Navx;
import ch.fridolins.fridowpi.utils.CSVLogger;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.statemachines.ClimberStatemachine;
import frc.robot.statemachines.Events;
import frc.robot.subsystems.climber.AngleFilter;
import frc.robot.subsystems.climber.Tilter;

public class Robot extends TimedRobot {

    @Override
    public void robotInit() {
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

    Joystick joystick;
    JoystickButton startButton;
    JoystickButton simulateMoveupFinishedButton;
    JoystickButton simulateCheckFinishedButton;
    JoystickButton simulateCheckSuccessButton;
    JoystickButton simulatePullfinishedButton;
    JoystickButton simulateHandoverFinishedButton;
    JoystickButton simulateHandoverSucceededButton;
    JoystickButton simulateTilterResettedButton;

    @Override
    public void teleopInit() {
        // joystick = new Joystick(0);
        // startButton = new JoystickButton(joystick, 1);
        // simulateMoveupFinishedButton = new JoystickButton(joystick, 2);
        // simulateCheckFinishedButton = new JoystickButton(joystick, 3);
        // simulateCheckSuccessButton = new JoystickButton(joystick, 4);
        // simulatePullfinishedButton = new JoystickButton(joystick, 5);
        // simulateHandoverFinishedButton = new JoystickButton(joystick, 6);
        // simulateHandoverSucceededButton = new JoystickButton(joystick, 7);
        // simulateTilterResettedButton = new JoystickButton(joystick, 8);

        // startButton.whenPressed(() -> ClimberStatemachine.getInstance().fireEvent(new Events.PressedStart()));
        // simulateMoveupFinishedButton.whenPressed(() -> ClimberStatemachine.getInstance().fireEvent(new Events.MoveUpFinished()));
        // simulateCheckFinishedButton.whenPressed(() -> ClimberStatemachine.getInstance().fireEvent(new Events.CheckFinished()));
        // simulateCheckSuccessButton.whenPressed(() -> ClimberStatemachine.getInstance().fireEvent(new Events.CheckPassed()));
        // simulatePullfinishedButton.whenPressed(() -> ClimberStatemachine.getInstance().fireEvent(new Events.PullFinished()));
        // simulateHandoverFinishedButton.whenPressed(() -> ClimberStatemachine.getInstance().fireEvent(new Events.HandoverFinished()));
        // simulateHandoverSucceededButton.whenPressed(() -> ClimberStatemachine.getInstance().fireEvent(new Events.HandoverCheckSuccess()));
        // simulateTilterResettedButton.whenPressed(() -> ClimberStatemachine.getInstance().fireEvent(new Events.TilterResetted()));
    }

    @Override
    public void teleopPeriodic() {
        // System.out.println(ClimberStatemachine.getInstance().getCurrentState().getName());
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
    }
}