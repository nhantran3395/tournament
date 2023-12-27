package example.tournament.service;

import example.tournament.dto.CreateRegistrationDto;
import example.tournament.exception.NoSuchResourceException;
import example.tournament.model.Registration;
import example.tournament.repository.RegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;

    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public List<Registration> getAll() {
        return registrationRepository.findAll();
    }

    public Registration add(CreateRegistrationDto registrationDto) {
        Registration registration = new Registration(registrationDto.date());
        return registrationRepository.save(registration);
    }

    public Registration get(long registrationId) {
        Optional<Registration> registrationOptional = registrationRepository.findById(registrationId);

        if (registrationOptional.isEmpty()) {
            throw new NoSuchResourceException();
        }

        return registrationOptional.get();
    }
}
