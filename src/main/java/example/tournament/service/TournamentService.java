package example.tournament.service;

import example.tournament.dto.CreateTournamentDto;
import example.tournament.exception.NoSuchResourceException;
import example.tournament.model.Registration;
import example.tournament.model.Tournament;
import example.tournament.repository.RegistrationRepository;
import example.tournament.repository.TournamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TournamentService {
    private final static Logger logger = LoggerFactory.getLogger(TournamentService.class);
    private final TournamentRepository tournamentRepository;
    private final RegistrationRepository registrationRepository;

    public TournamentService(TournamentRepository tournamentRepository, RegistrationRepository registrationRepository) {
        this.tournamentRepository = tournamentRepository;
        this.registrationRepository = registrationRepository;
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

    public void attachRegistration(long tournamentId, long registrationId) {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(tournamentId);
        Optional<Registration> registrationOptional = registrationRepository.findById(registrationId);

        if (tournamentOptional.isEmpty() || registrationOptional.isEmpty()) {
            throw new NoSuchResourceException();
        }

        Tournament tournament = tournamentOptional.get();
        Registration registration = registrationOptional.get();
        List<Registration> registrationList = tournament.getRegistrationList();
        registrationList.add(registration);
        tournament.setRegistrationList(registrationList);

        tournamentRepository.save(tournament);
    }

    public void delete(long tournamentId) {
        Tournament tournament = get(tournamentId);
        tournamentRepository.delete(tournament);
    }
}
