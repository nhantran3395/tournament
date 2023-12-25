package example.tournament.service;

import example.tournament.dto.CreateTournamentDto;
import example.tournament.exception.NoSuchResourceException;
import example.tournament.model.Tournament;
import example.tournament.repository.TournamentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    public List<Tournament> getAll() {
        return tournamentRepository.findAll();
    }

    public Tournament get(long tournamentId) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);

        if (tournamentOptional.isEmpty()) {
            throw new NoSuchResourceException();
        }

        return tournamentOptional.get();
    }

    public Tournament add(CreateTournamentDto tournamentDto) {
        Tournament newTournament = new Tournament(tournamentDto.name(), tournamentDto.location());
        return tournamentRepository.save(newTournament);
    }
}
