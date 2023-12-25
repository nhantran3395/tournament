package example.tournament.service;

import example.tournament.dto.CreatePlayerDto;
import example.tournament.exception.NoSuchResourceException;
import example.tournament.model.Player;
import example.tournament.model.PlayerProfile;
import example.tournament.repository.PlayerProfileRepository;
import example.tournament.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    private final PlayerProfileRepository playerProfileRepository;
    private final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    public PlayerService(PlayerRepository playerRepository, PlayerProfileRepository playerProfileRepository) {
        this.playerRepository = playerRepository;
        this.playerProfileRepository = playerProfileRepository;
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
        Optional<Player> playerOptional = playerRepository.findById(playerId);

        if (profileOptional.isEmpty() || playerOptional.isEmpty()) {
            throw new NoSuchResourceException();
        }

        PlayerProfile profile = profileOptional.get();
        Player player = playerOptional.get();
        player.setProfile(profile);
        playerRepository.save(player);
    }

    public void delete(long playerId) {
        Player player = get(playerId);
        logger.info("Player to delete: " + player.toString());
        playerRepository.deleteById(player.getId());
    }
}
