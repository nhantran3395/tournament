package example.tournament.player;

import example.tournament.player_profile.PlayerProfileRepository;
import example.tournament.registration.RegistrationRepository;
import example.tournament.shared.exception.NoSuchResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private PlayerProfileRepository playerProfileRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void shouldReturnPlayerWhenGetExistingPlayer() {
        Player player = new Player(10, "Novak Djokovic");
        when(playerRepository.findById(10L)).thenReturn(Optional.of(player));
        assertThat(playerService.get(10)).isEqualTo(player);
    }

    @Test
    void shouldThrowNoSuchResourceExceptionWhenGetNonExistingPlayer() {
        when(playerRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(NoSuchResourceException.class, () -> playerService.get(10));
    }

    @Test
    void shouldThrowNoSuchResourceExceptionWhenDeleteNonExistingPlayer() {
        when(playerRepository.findById(10L)).thenReturn(Optional.empty());
        assertThrows(NoSuchResourceException.class, () -> playerService.delete(10));
    }
}
