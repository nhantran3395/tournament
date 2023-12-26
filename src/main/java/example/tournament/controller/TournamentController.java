package example.tournament.controller;

import example.tournament.dto.CreateTournamentDto;
import example.tournament.model.Tournament;
import example.tournament.service.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/tournaments")
public class TournamentController {
    private final TournamentService tournamentService;

    private final Logger logger = LoggerFactory.getLogger(TournamentController.class);

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping(path = "")
    private ResponseEntity<List<Tournament>> getAll() {
        List<Tournament> tournaments = tournamentService.getAll();
        logger.info("List of tournaments: {}", tournaments);
        return ResponseEntity.ok(tournaments);
    }

    @PostMapping(path = "")
    private ResponseEntity<Void> add(@RequestBody CreateTournamentDto newTournamentDto) throws URISyntaxException {
        Tournament savedTournament = tournamentService.add(newTournamentDto);
        return ResponseEntity.created(new URI("/tournaments/" + savedTournament.getId())).build();
    }

    @GetMapping(path = "/{tournamentId}")
    private ResponseEntity<Tournament> get(@PathVariable long tournamentId) {
        Tournament tournament = tournamentService.get(tournamentId);
        logger.info("Matching tournament: {}", tournament);
        return ResponseEntity.ok(tournament);
    }

    @PutMapping(path = "/{tournamentId}/registrations/{registrationId}")
    private ResponseEntity<Void> attachRegistration(@PathVariable long tournamentId, @PathVariable long registrationId) {
        tournamentService.attachRegistration(tournamentId, registrationId);
        return ResponseEntity.noContent().build();
    }
}
