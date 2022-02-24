package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.ball.Thrower;
import frc.robot.subsystems.ball.base.ThrowerBase;

public class Robot extends TimedRobot {

  @Override
  public void robotInit() {
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

  ThrowerBase thrower;
  Joystick jst;

  @Override
  public void teleopInit() {
      thrower = Thrower.getInstance();
      jst = new Joystick(0);
  }

  @Override
  public void teleopPeriodic() {
      thrower.setPercentage(jst.getY());
  }

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}
}
