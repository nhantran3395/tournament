package example.tournament.dto;

import java.time.Instant;

/**
 * DTO for {@link example.tournament.model.Registration}
 */
public record CreateRegistrationDto(Instant date) {
}