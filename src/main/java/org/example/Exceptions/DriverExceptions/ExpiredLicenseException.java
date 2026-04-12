package org.example.Exceptions.DriverExceptions;

import org.example.models.DriverLicense;

public class ExpiredLicenseException extends DriverException {
    public ExpiredLicenseException(DriverLicense license) {
        super("The driver's license is expired: " + license.getExpiryDate());
    }
}
