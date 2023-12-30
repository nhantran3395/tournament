package example.tournament.registration;

import java.time.Instant;

/**
 * DTO for {@link Registration}
 */
public record CreateRegistrationRequest(Instant date) {
}