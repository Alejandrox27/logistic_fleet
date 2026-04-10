package org.example.Exceptions.RouteException;

public class SameOriginAndDestinationException extends RouteException {
    public SameOriginAndDestinationException(String place) {
        super("The destination of the travel cannot be the same as the origin. -- ORIGIN: " + place + " -- DESTINATION: " + place);
    }
}
