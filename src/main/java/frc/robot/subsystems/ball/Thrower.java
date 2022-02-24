package frc.robot.subsystems.ball;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.utils.PidValues;
import ch.fridolins.fridowpi.sensors.UltrasonicSensor;
import ch.fridolins.fridowpi.sensors.UltrasonicSensorArray;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.ball.base.ThrowerBase;

public class Thrower extends ThrowerBase {
    private static ThrowerBase instance = null;
    private static final boolean enabled = true;

    public static final class Constants {
        public static final int motorId = 0;
        public static final PidValues pid = new PidValues(0.0, 0.0, 0.0);

        public static final class UltraSonicSensors {
            public static final int pingRight = 0;
            public static final int echoRight = 0;
            public static final int pingLeft = 0;
            public static final int echoLeft = 0;
            public static final double sensorSeparation = 0.0;
        }
    }

    FridolinsMotor motor;

    // us = ultra sonic
    UltrasonicSensor usLeft;
    UltrasonicSensor usRight;
    UltrasonicSensorArray usSensorArray;

    private Thrower() {
        Initializer.getInstance().addInitialisable(this);
    }

    public static ThrowerBase getInstance() {
        if (instance == null) {
            if (enabled)
                instance = new Thrower();
            else
                instance = new ThrowerBase();
        }
        return instance;
    }

    @Override
    public void init() {
        super.init();
        motor = new FridoCanSparkMax(Constants.motorId, CANSparkMaxLowLevel.MotorType.kBrushed);
        motor.setPID(Constants.pid);

        usLeft = new UltrasonicSensor(Constants.UltraSonicSensors.pingLeft, Constants.UltraSonicSensors.echoLeft);
        usRight = new UltrasonicSensor(Constants.UltraSonicSensors.pingRight, Constants.UltraSonicSensors.echoRight);

        usSensorArray = new UltrasonicSensorArray(usRight, usLeft, Constants.UltraSonicSensors.sensorSeparation);

        registerSubmodule(motor);
    }

    @Override
    public double getRequiredVelocity() {
        // TODO: implement this function with the usSensorArray
        return 0.0;
    }


    @Override
    public void setVelocity(double velocity) {
        motor.setVelocity(velocity);
    }

    @Override
    public void setPercentage(double percentage) {
        motor.set(percentage);
    }

    @Override
    public double getDistanceToTarget() {
        return usSensorArray.getFilteredDistance();
    }

    @Override
    public Rotation2d getAngleToTarget() {
        return usSensorArray.getFilteredAngle();
    }
}
