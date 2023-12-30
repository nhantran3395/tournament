package example.tournament.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/players")
public class PlayerController {
    private final static Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(path = "")
    private ResponseEntity<List<Player>> getAll() {
        List<Player> players = playerService.getAll();
        return ResponseEntity.ok(players);
    }

    @PostMapping(path = "")
    private ResponseEntity<Void> addPlayer(@RequestBody CreatePlayerRequest newPlayerRequest) throws URISyntaxException {
        Player savedPlayer = playerService.addPlayer(newPlayerRequest);
        return ResponseEntity.created(new URI("/players/" + savedPlayer.getId())).build();
    }

    @GetMapping(path = "/{playerId}")
    private ResponseEntity<Player> get(@PathVariable long playerId) {
        Player player = playerService.get(playerId);
        logger.info("Matching player: {}", player);
        return ResponseEntity.ok(player);
    }

    @PutMapping(path = "/{playerId}/profiles/{profileId}")
    private ResponseEntity<Void> attachProfile(@PathVariable long playerId, @PathVariable long profileId) {
        playerService.attachProfile(playerId, profileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{playerId}")
    private ResponseEntity<Void> delete(@PathVariable long playerId) {
        playerService.delete(playerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{playerId}/registrations/{registrationId}")
    private ResponseEntity<Void> attachRegistration(@PathVariable long playerId, @PathVariable long registrationId) {
        playerService.attachRegistration(playerId, registrationId);
        return ResponseEntity.noContent().build();
    }
}
