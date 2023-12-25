package example.tournament.service;

import example.tournament.dto.CreatePlayerProfileDto;
import example.tournament.exception.NoSuchResourceException;
import example.tournament.model.PlayerProfile;
import example.tournament.repository.PlayerProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerProfileService {
    private final PlayerProfileRepository playerProfileRepository;

    public PlayerProfileService(PlayerProfileRepository playerProfileRepository) {
        this.playerProfileRepository = playerProfileRepository;
    }

    public PlayerProfile addProfile(CreatePlayerProfileDto playerProfileDto) {
        PlayerProfile newPlayerProfile = new PlayerProfile(playerProfileDto.twitter());
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
