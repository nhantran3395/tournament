package example.tournament.tournament;

/**
 * DTO for {@link Tournament}
 */
public record CreateTournamentRequest(String name, String location) {
}