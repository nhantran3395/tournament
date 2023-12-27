package example.tournament.service;

import example.tournament.dto.CreatePlayerDto;
import example.tournament.exception.NoSuchResourceException;
import example.tournament.model.Player;
import example.tournament.model.PlayerProfile;
import example.tournament.model.Registration;
import example.tournament.repository.PlayerProfileRepository;
import example.tournament.repository.PlayerRepository;
import example.tournament.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    private final PlayerProfileRepository playerProfileRepository;
    private final RegistrationRepository registrationRepository;
    private final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    public PlayerService(PlayerRepository playerRepository, PlayerProfileRepository playerProfileRepository, RegistrationRepository registrationRepository) {
        this.playerRepository = playerRepository;
        this.playerProfileRepository = playerProfileRepository;
        this.registrationRepository = registrationRepository;
    }

    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    public Player addPlayer(CreatePlayerDto playerDto) {
        Player player = new Player(playerDto.name());
        return playerRepository.save(player);
    }

    public Player get(long playerId) {
        Optional<Player> playerOptional = playerRepository.findById(playerId);

        if (playerOptional.isEmpty()) {
            throw new NoSuchResourceException();
        }

        return playerOptional.get();
    }

    public void attachProfile(long playerId, long profileId) {
        Optional<PlayerProfile> profileOptional = playerProfileRepository.findById(profileId);

        if (profileOptional.isEmpty()) {
            throw new NoSuchResourceException();
        }

        PlayerProfile profile = profileOptional.get();
        Player player = get(playerId);
        player.setProfile(profile);
        playerRepository.save(player);
    }

    public void attachRegistration(long playerId, long registrationId) {
        logger.info(String.format("attach registration to player: playerId: %d, registrationId: %d", playerId, registrationId));
        Optional<Registration> registrationOptional = registrationRepository.findById(registrationId);

        if (registrationOptional.isEmpty()) {
            throw new NoSuchResourceException();
        }

        Registration registration = registrationOptional.get();
        Player player = get(playerId);

        registration.setPlayer(player);
        List<Registration> registrations = player.getRegistrations();
        registrations.add(registration);
        player.setRegistrations(registrations);

        playerRepository.save(player);
    }

    public void delete(long playerId) {
        Player player = get(playerId);
        logger.info("Player to delete: " + player.toString());
        playerRepository.deleteById(player.getId());
    }
}
