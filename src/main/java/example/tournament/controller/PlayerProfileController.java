package example.tournament.controller;

import example.tournament.dto.CreatePlayerProfileDto;
import example.tournament.model.PlayerProfile;
import example.tournament.service.PlayerProfileService;
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
    private ResponseEntity<Void> addProfile(@RequestBody CreatePlayerProfileDto profileDto) throws URISyntaxException {
        PlayerProfile savedProfile = playerProfileService.addProfile(profileDto);
        return ResponseEntity.created(new URI("/profiles/" + savedProfile.getId())).build();
    }

    @GetMapping(path = "/{requestedProfileId}")
    private ResponseEntity<PlayerProfile> get(@PathVariable long requestedProfileId) {
        PlayerProfile playerProfile = playerProfileService.get(requestedProfileId);
        return ResponseEntity.ok(playerProfile);
    }
}
