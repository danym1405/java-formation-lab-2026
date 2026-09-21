package com.indra.logistics;

import java.util.UUID;

public class TrackingIdGenerator {

   public String generate(Route route) {

        if (route == null) {
            throw new IllegalArgumentException("Route cannot be null");
        }

        String origin = route.origin();
        String destination = route.destination();

        if (origin == null || origin.isBlank()) {
            throw new IllegalArgumentException("Origin code cannot be null or empty");
        }
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination code cannot be null or empty");
        }
        
        origin = origin.toUpperCase();
        destination = destination.toUpperCase();

        String uniquePart = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return origin + "-" + destination + "-" + uniquePart;
        
    }
}