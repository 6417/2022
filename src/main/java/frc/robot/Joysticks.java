package frc.robot;

import ch.fridolins.fridowpi.joystick.IJoystickId;

public enum Joysticks implements IJoystickId {
    Drive(0);
    private final int port;

    private Joysticks(int id) {
        port = id;
    }

    @Override
    public int getPort() {
        return port;
    }
}
