package example.tournament.player_profile;

import example.tournament.shared.exception.NoSuchResourceException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerProfileService {
    private final PlayerProfileRepository playerProfileRepository;

    public PlayerProfileService(PlayerProfileRepository playerProfileRepository) {
        this.playerProfileRepository = playerProfileRepository;
    }

    public PlayerProfile addProfile(CreatePlayerProfileRequest playerProfileRequest) {
        PlayerProfile newPlayerProfile = new PlayerProfile(playerProfileRequest.twitter());
        return playerProfileRepository.save(newPlayerProfile);
    }

    public PlayerProfile get(long profileId) {
        Optional<PlayerProfile> playerProfileOptional = playerProfileRepository.findById(profileId);

        if (playerProfileOptional.isEmpty()) {
            throw new NoSuchResourceException();
        }

        return playerProfileOptional.get();
    }
}
