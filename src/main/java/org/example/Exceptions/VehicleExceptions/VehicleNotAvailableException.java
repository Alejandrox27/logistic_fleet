package org.example.Exceptions.VehicleExceptions;

import org.example.models.VehicleStatus;

public class VehicleNotAvailableException extends VehicleException {
    public VehicleNotAvailableException(VehicleStatus status) {
        super("The vehicle cannot be assigned. Actual state: " + status);
    }
}
