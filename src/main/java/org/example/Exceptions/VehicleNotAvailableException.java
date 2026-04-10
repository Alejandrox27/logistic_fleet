package org.example.Exceptions;

import org.example.models.VehicleStatus;

public class VehicleNotAvailableException extends VehicleExceptions {
    public VehicleNotAvailableException(VehicleStatus status) {
        super("The vehicle cannot be assigned. Actual state: " + status);
    }
}
