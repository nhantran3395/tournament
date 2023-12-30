package example.tournament.registration;

import example.tournament.shared.exception.NoSuchResourceException;
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

    public Registration add(CreateRegistrationRequest registrationRequest) {
        Registration registration = new Registration(registrationRequest.date());
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
