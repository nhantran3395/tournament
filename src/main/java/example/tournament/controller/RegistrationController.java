package example.tournament.controller;

import example.tournament.model.Registration;
import example.tournament.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping(path = "")
    private ResponseEntity<List<Registration>> getAll() {
        return ResponseEntity.ok(registrationService.getAll());
    }

    @PostMapping(path = "")
    private ResponseEntity<Void> add() throws URISyntaxException {
        Registration savedRegistration = registrationService.add();
        return ResponseEntity.created(new URI("/registrations/" + savedRegistration.getId())).build();
    }

    @GetMapping(path = "/{registrationId}")
    private ResponseEntity<Registration> get(@PathVariable long registrationId) {
        Registration registration = registrationService.get(registrationId);
        return ResponseEntity.ok(registration);
    }
}
