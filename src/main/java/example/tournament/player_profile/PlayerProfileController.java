package example.tournament.player_profile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "/profiles")
public class PlayerProfileController {
    private final PlayerProfileService playerProfileService;

    public PlayerProfileController(PlayerProfileService playerProfileService) {
        this.playerProfileService = playerProfileService;
    }

    @PostMapping(path = "")
    private ResponseEntity<Void> addProfile(@RequestBody CreatePlayerProfileRequest newProfileRequest) throws URISyntaxException {
        PlayerProfile savedProfile = playerProfileService.addProfile(newProfileRequest);
        return ResponseEntity.created(new URI("/profiles/" + savedProfile.getId())).build();
    }

    @GetMapping(path = "/{requestedProfileId}")
    private ResponseEntity<PlayerProfile> get(@PathVariable long requestedProfileId) {
        PlayerProfile playerProfile = playerProfileService.get(requestedProfileId);
        return ResponseEntity.ok(playerProfile);
    }
}
