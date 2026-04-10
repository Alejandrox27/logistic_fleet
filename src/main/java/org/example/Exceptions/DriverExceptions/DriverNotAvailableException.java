package org.example.Exceptions.DriverExceptions;

import org.example.models.DriverStatus;

public class DriverNotAvailableException extends DriverException {
    public DriverNotAvailableException(DriverStatus statusDriver) {
        super("The driver is not available at this moment. CURRENT STATUS: " + statusDriver);
    }
}
