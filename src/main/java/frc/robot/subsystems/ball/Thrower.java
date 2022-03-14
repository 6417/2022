package frc.robot.subsystems.ball;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.motors.FridoCanSparkMax;
import ch.fridolins.fridowpi.motors.FridolinsMotor;
import ch.fridolins.fridowpi.motors.FridolinsMotor.FridoFeedBackDevice;
import ch.fridolins.fridowpi.motors.FridolinsMotor.IdleMode;
import ch.fridolins.fridowpi.motors.utils.PidValues;
import ch.fridolins.fridowpi.sensors.UltrasonicSensor;
import ch.fridolins.fridowpi.sensors.UltrasonicSensorArray;

import java.util.Optional;

import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Ultrasonic;
import frc.robot.subsystems.ball.base.ThrowerBase;

public class Thrower extends ThrowerBase {
    private static ThrowerBase instance = null;
    private static final boolean enabled = false;

    public static final class Constants {
        public static final int motorId = 31;
        public static final PidValues pid = new PidValues(1 * Math.pow(10, -4), 0.0, 0.0);
        // public static final PidValues pid = new PidValues(0.0, 0.0, 0.0);
        public static final int velocityTarget = -4500;

        static {
            pid.setTolerance(1000);
            pid.kF = Optional.of(0.00018);
        }

        public static final class UltraSonicSensors {
            public static final int echoRight = 4;
            public static final int pingRight = 5;
            public static final int echoLeft = 6;
            public static final int pingLeft = 7;
            public static final double sensorSeparation = 360.0;
        }
    }

    FridoCanSparkMax motor;

    // us = ultra sonic
    UltrasonicSensor usLeft;
    UltrasonicSensor usRight;
    UltrasonicSensorArray usSensorArray;

    private Thrower() {
        Initializer.getInstance().addInitialisable(this);

        motor = new FridoCanSparkMax(Constants.motorId, CANSparkMaxLowLevel.MotorType.kBrushless);
        requires(motor);
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
        System.out.println("called thrower init");
        super.init();
        motor.setPID(Constants.pid);
        motor.setIdleMode(IdleMode.kCoast);
        motor.configEncoder(FridoFeedBackDevice.kBuildin, 1);

        usLeft = new UltrasonicSensor(Constants.UltraSonicSensors.pingLeft, Constants.UltraSonicSensors.echoLeft);
        usRight = new UltrasonicSensor(Constants.UltraSonicSensors.pingRight, Constants.UltraSonicSensors.echoRight);

        usSensorArray = new UltrasonicSensorArray(usRight, usLeft, Constants.UltraSonicSensors.sensorSeparation);

        Ultrasonic.setAutomaticMode(true);

        registerSubmodule(motor);

        System.out.println("initialized thrower");
    }

    @Override
    public double getRequiredVelocity() {
        // TODO: implement this function with the usSensorArray
        // return 0.0;
        return -5000;
    }


    @Override
    public void setVelocity(double velocity) {
        motor.setVelocity(velocity);
    }

    @Override
    public void blockMotor() {
        motor.setIdleMode(IdleMode.kBrake);
    }

    @Override
    public void unlockMotor() {
        motor.setIdleMode(IdleMode.kCoast);
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
    
    @Override
    public boolean isAtTarget() {
        return motor.pidAtTarget();
    }

    @Override
    public boolean isDistanceValid() {
        return 600 < getDistanceToTarget() && getDistanceToTarget() < 1200;
    } 

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("Velocity", motor::getEncoderVelocity, null);
        builder.addBooleanProperty("isDistanceValid", this::isDistanceValid, null);
        super.initSendable(builder);
    }
}
