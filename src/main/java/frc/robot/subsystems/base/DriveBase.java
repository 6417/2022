package frc.robot.subsystems.base;

import ch.fridolins.fridowpi.module.Module;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.CentripetalAccelerationConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveKinematicsConstraint;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.subsystems.Drive;

public class DriveBase extends Module {
    public void resetSensors() {
    }

    public DifferentialDriveKinematics getDriveKinematics() {
        return new DifferentialDriveKinematics(Drive.Constants.Odometry.trackWidthMeters);
    }

    public DifferentialDriveVoltageConstraint getVoltageConstrain() {
        return new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(0.0, 0.0), getDriveKinematics(), 0.0);
    }

    public DifferentialDriveKinematicsConstraint getKinematicsConstrain() {
        return new DifferentialDriveKinematicsConstraint(getDriveKinematics(), 0.0);
    }

    public CentripetalAccelerationConstraint getCentripetConstraint() {
        return new CentripetalAccelerationConstraint(0.0);
    }

    public TrajectoryConfig getTrajectoryConfig() {
        return new TrajectoryConfig(0.0, 0.0);
    }

    public PIDController getLeftVelocityController() {
        return new PIDController(0.0,0.0,0.0);
    }

    public PIDController getRightVelocityController() {
        return new PIDController(0.0,0.0,0.0);
    }

    private double getLeftWheelDistance() {
        return 0;
    }

    private double getRightWheelDistance() {
        return 0;
    }

    public DifferentialDriveWheelSpeeds getWheelSpeeds() {
        return new DifferentialDriveWheelSpeeds(0, 0);
    }

    public Pose2d getPosition() {
        return new Pose2d(new Translation2d(0.0,0.0), new Rotation2d(0.0));
    }

    public SimpleMotorFeedforward getMotorFeedforward() {
        return new SimpleMotorFeedforward(0.0, 0.0);
    }

    public ChassisSpeeds getChassisSpeeds() {
        return new ChassisSpeeds();
    }

    public void resetOdometry(Pose2d setPoint) {
    }

    private void updateOdometry() {
    }

    public void drive() {
    }

    public void setSpeed(double maxSpeed) {
    }

    public void stop() {
    }

    public void tankDriveVolts(double left, double right) {
    }
}
