package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.ball.Thrower;
import frc.robot.subsystems.ball.base.ThrowerBase;

public class Robot extends TimedRobot {

  Joystick jst;
  FridolinsMotor throwerTest;
  @Override
  public void robotInit() {
    Thrower.getInstance();
    Initializer.getInstance().init();
    jst = new Joystick(0);
    // throwerTest = new FridoCanSparkMax(31, MotorType.kBrushless);
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {}

//   ThrowerBase thrower;

  @Override
  public void teleopInit() {
    //   thrower = Thrower.getInstance();
  }

  @Override
  public void teleopPeriodic() {
    Thrower.getInstance().setPercentage(jst.getY());
    // throwerTest.set(jst.getY());
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}
}