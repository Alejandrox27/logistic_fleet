package org.example.Exceptions.DriverExceptions;

import org.example.models.dto.DriverFatigueDTO;

public class DriverFatigueException extends DriverException {
    public DriverFatigueException(DriverFatigueDTO driverFatigue) {
        super("The driver " + driverFatigue.getName() + " with the id " + driverFatigue.getIdDriver() + " can't " +
                "drive for a while, we need to prevent driver fatigue");
    }
}
