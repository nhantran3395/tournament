package example.tournament.controller;

import example.tournament.dto.CreatePlayerDto;
import example.tournament.model.Player;
import example.tournament.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/players")
public class PlayerController {
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
    private ResponseEntity<Void> addPlayer(@RequestBody CreatePlayerDto newPlayerDto) throws URISyntaxException {
        Player savedPlayer = playerService.addPlayer(newPlayerDto);
        return ResponseEntity.created(new URI("/players/" + savedPlayer.getId())).build();
    }

    @GetMapping(path = "/{playerId}")
    private ResponseEntity<Player> get(@PathVariable long playerId) {
        Player player = playerService.get(playerId);
        return ResponseEntity.ok(player);
    }

    @PutMapping(path = "/{playerId}/profiles/{profileId}")
    private ResponseEntity<Void> attachProfile(@PathVariable long playerId, @PathVariable long profileId) throws URISyntaxException {
        playerService.attachProfile(playerId, profileId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{playerId}")
    private ResponseEntity<Void> delete(@PathVariable long playerId) {
        playerService.delete(playerId);
        return ResponseEntity.noContent().build();
    }
}