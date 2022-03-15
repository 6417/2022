package frc.robot;

import java.util.List;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.JoystickHandler;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.pneumatics.PneumaticHandler;
import ch.fridolins.fridowpi.sensors.Navx;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.autonomous.PathviewerLoader;
import frc.robot.autonomous.RamseteCommandGenerator;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.ball.BallSubsystem;
import frc.robot.subsystems.climber.TelescopeArm;
import frc.robot.subsystems.climber.Tilter;

public class Robot extends TimedRobot {
    private Command m_autonomousCommand;

    static {
    }

    PowerDistribution pdp;

    @Override
    public void robotInit() {
        pdp = new PowerDistribution(0, PowerDistribution.ModuleType.kCTRE);

        JoystickHandler.getInstance().setupJoysticks(List.of(Joysticks.values()));
        Navx.setup(SPI.Port.kMXP);
        BallSubsystem.getInstance();
        // Navx.setYawOffset(-180);
        Navx.getInstance();
        Drive.getInstance();
        Navx.getInstance().resetDisplacement();
        Navx.getInstance().reset();

        PneumaticHandler.getInstance().configureCompressor(30, PneumaticsModuleType.CTREPCM);
        PneumaticHandler.getInstance().init();
        TelescopeArm.getInstance().init();
        Tilter.getInstance().init();
        JoystickHandler.getInstance().init();
        Initializer.getInstance().init();

        System.out.println("Initializer init");
    }

    double[] maxCurrents = new double[16];
    double totalMax = 0;

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
//        for (int i = 0; i < 16; i++) {
//            SmartDashboard.putNumber("PDP Chanel " + i + " current", pdp.getCurrent(i));
//            if (pdp.getCurrent(i) > maxCurrents[i]) {
//                SmartDashboard.putNumber("PDP Chanel " + i + " max current", pdp.getCurrent(i));
//                maxCurrents[i] = pdp.getCurrent(i);
//            }
//        }
//
//            SmartDashboard.putNumber("PDP Total current", pdp.getTotalCurrent());
//
//        if (pdp.getTotalCurrent() > totalMax) {
//            SmartDashboard.putNumber("PDP Total max current max current", pdp.getTotalCurrent());
//            totalMax = pdp.getTotalCurrent();
//        }

    }

    @Override
    public void disabledInit() {
        for (FridoCanSparkMax buggyMotor : FridoCanSparkMax.allMotorsWithShitySoftware) {
            // Fuck REV they don't disable there motors on robot disable
            buggyMotor.set(0);
            buggyMotor.stopMotor();
        }
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
        Navx.getInstance().reset();

        String trajectoryJSON = "paths/output/GeradeAus3m.wpilib.json";

        Trajectory pathWeavertest = PathviewerLoader.loadTrajectory(trajectoryJSON);

        m_autonomousCommand = RamseteCommandGenerator.generateRamseteCommand(pathWeavertest);

        // Drive.getInstance().setDirectionToReverse();
        Drive.getInstance().setDirectionToForward();

        if (m_autonomousCommand != null) {
            Drive.getInstance().setDirectionToReverse();
            m_autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        Navx.getInstance().reset();
        Drive.getInstance().resetOdometry(new Pose2d(0, 0, new Rotation2d(0)));

        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
    }
}