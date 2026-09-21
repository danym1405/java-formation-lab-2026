package com.indra.logistics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TrackingIdGeneratorTest {

    private final TrackingIdGenerator generator = new TrackingIdGenerator();

    @ParameterizedTest
    @DisplayName("Should generate tracking ID for different routes")
    @CsvSource({
        "BOG, MED",
        "CLO, CAR",
        "CTG, BAQ"
    })
    void shouldGenerateTrackingIdForDifferentRoutes(String origin, String destination) {
        String trackingId = generator.generate(new Route(origin, destination));
        assertTrue(trackingId.matches(origin.toUpperCase() + "-" + destination.toUpperCase() + "-[A-Z0-9]{8}"));
    }


    @Test
    @DisplayName("The generated ID must have the format ORIG-DEST-XXXXXXXX")
    void shouldGenerateIdWithCorrectFormatForBogMedLowerCase() {
        String trackingId = generator.generate(new Route("bog", "med"));
        assertTrue(trackingId.matches("BOG-MED-[A-Z0-9]{8}"));
    }

    @Test
    @DisplayName("Should throw an exception if route")
    void shouldThrowWhenRouteIsNull() {
        assertThrows(IllegalArgumentException.class, () -> generator.generate(null));
    }

    @ParameterizedTest 
    @DisplayName("Should throw an exception if origin or destination is empty")
    @CsvSource({
        "'', MED",
        "'', ''",
        "BOG, ' '",
        "' ', MED",
        "BOG, ' '"
    })
    void shouldThrowWhenOriginOrDestinationIsEmpty(String origin, String destination) {
        assertThrows(IllegalArgumentException.class, () -> generator.generate(new Route(origin, destination)));
    }

    @Test
    @DisplayName("Should throw an exception if origin is null")
    void shouldThrowWhenOriginIsNull() {
        assertThrows(IllegalArgumentException.class, () -> generator.generate(new Route(null, "MED")));
    }

    @Test
    @DisplayName("Should throw an exception if destination is null")
    void shouldThrowWhenDestinationIsNull() {
        assertThrows(IllegalArgumentException.class, () -> generator.generate(new Route("BOG", null)));
    }

}