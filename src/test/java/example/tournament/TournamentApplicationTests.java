package example.tournament;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import example.tournament.player.CreatePlayerRequest;
import example.tournament.player_profile.CreatePlayerProfileRequest;
import example.tournament.registration.Registration;
import example.tournament.tournament.CreateTournamentRequest;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainersConfiguration.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TournamentApplicationTests {
    private final static Logger logger = LoggerFactory.getLogger(TournamentApplicationTests.class);
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnListOfPlayers() {
        ResponseEntity<String> res = restTemplate.getForEntity("/players", String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(res.getBody());

        int count = documentContext.read("$.length()");
        assertThat(count).isEqualTo(2);

        String firstPlayerName = documentContext.read("$[0].name");
        assertThat(firstPlayerName).isEqualTo("Roger Federer");
        String secondPlayerName = documentContext.read("$[1].name");
        assertThat(secondPlayerName).isEqualTo("Novak Djokovic");
    }

    @Test
    @DirtiesContext
    void shouldCreateNewPlayer() {
        CreatePlayerRequest newPlayerDto = new CreatePlayerRequest("Rafael Nadal");
        ResponseEntity<Void> postRes = restTemplate.postForEntity("/players", newPlayerDto, Void.class);

        assertThat(postRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI savedPlayerLocation = postRes.getHeaders().getLocation();
        ResponseEntity<String> getRes = restTemplate.getForEntity(savedPlayerLocation, String.class);
        DocumentContext documentContext = JsonPath.parse(getRes.getBody());
        int id = documentContext.read("$.id");
        String name = documentContext.read("$.name");

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(id).isInstanceOf(Number.class).isNotNull();
        assertThat(name).isEqualTo("Rafael Nadal");
    }

    @Test
    void shouldReturnSinglePlayer() {
        ResponseEntity<String> res = restTemplate.getForEntity("/players/100", String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(res.getBody());

        int id = documentContext.read("$.id");
        assertThat(id).isEqualTo(100);
        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo("Roger Federer");
    }

    @Test
    @DirtiesContext
    void shouldCreateNewProfile() {
        CreatePlayerProfileRequest newProfileDto = new CreatePlayerProfileRequest("@rafael1");
        ResponseEntity<Void> postRes = restTemplate.postForEntity("/profiles", newProfileDto, Void.class);

        assertThat(postRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI savedPlayerLocation = postRes.getHeaders().getLocation();
        ResponseEntity<String> getRes = restTemplate.getForEntity(savedPlayerLocation, String.class);
        DocumentContext documentContext = JsonPath.parse(getRes.getBody());
        int id = documentContext.read("$.id");
        String twitter = documentContext.read("$.twitter");

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(id).isInstanceOf(Number.class).isNotNull();
        assertThat(twitter).isEqualTo("@rafael1");
    }

    @Test
    @DirtiesContext
    void shouldAttachProfileToPlayer() {
        ResponseEntity<Void> putRes = restTemplate.exchange("/players/101/profiles/101", HttpMethod.PUT, null, Void.class);
        assertThat(putRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getRes = restTemplate.getForEntity("/players/101", String.class);
        DocumentContext documentContext = JsonPath.parse(getRes.getBody());
        int id = documentContext.read("$.id");
        String name = documentContext.read("$.name");
        int profileId = documentContext.read("$.profile.id");
        String twitter = documentContext.read("$.profile.twitter");

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(id).isEqualTo(101);
        assertThat(name).isEqualTo("Novak Djokovic");
        assertThat(profileId).isEqualTo(101);
        assertThat(twitter).isEqualTo("@djokovic2");
    }

    @Test
    @DirtiesContext
    void shouldAttachRegistrationToPlayer() {
        ResponseEntity<Void> putRes = restTemplate.exchange("/players/101/registrations/1003", HttpMethod.PUT, null, Void.class);
        assertThat(putRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getRes = restTemplate.getForEntity("/players/101", String.class);
        DocumentContext documentContext = JsonPath.parse(getRes.getBody());
        int registrationCount = documentContext.read("$.registrations.length()");
        int registrationId = documentContext.read("$.registrations[0].id");
        Instant registrationDate = Instant.parse(documentContext.read("$.registrations[0].date"));

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(registrationCount).isEqualTo(1);
        assertThat(registrationId).isEqualTo(1003);
        assertThat(registrationDate).isNotNull();
    }

    @Test
    @DirtiesContext
    void shouldDeletePlayerAndAssociatedProfile() {
        ResponseEntity<Void> deleteRes = restTemplate.exchange("/players/100", HttpMethod.DELETE, null, Void.class);
        assertThat(deleteRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getPlayerRes = restTemplate.getForEntity("/players/100", String.class);
        assertThat(getPlayerRes.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ResponseEntity<String> getProfileRes = restTemplate.getForEntity("/profiles/100", String.class);
        assertThat(getProfileRes.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnListOfTournaments() {
        ResponseEntity<String> res = restTemplate.getForEntity("/tournaments", String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(res.getBody());
        int count = documentContext.read("$.length()");
        String tournament1 = documentContext.read("$[0].name");
        String tournament2 = documentContext.read("$[1].name");
        String tournament3 = documentContext.read("$[2].name");

        assertThat(count).isEqualTo(3);
        assertThat(tournament1).isEqualTo("Bank of China Hong Kong Tennis Open");
        assertThat(tournament2).isEqualTo("Adelaide International");
        assertThat(tournament3).isEqualTo("ASB Classic");
    }

    @Test
    @DirtiesContext
    void shouldCreateNewTournament() {
        CreateTournamentRequest newTournamentDto = new CreateTournamentRequest("Australian Open", "Melbourne, Australia");
        ResponseEntity<Void> postRes = restTemplate.postForEntity("/tournaments", newTournamentDto, Void.class);

        assertThat(postRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI savedTournamentLocation = postRes.getHeaders().getLocation();
        ResponseEntity<String> getRes = restTemplate.getForEntity(savedTournamentLocation, String.class);

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getRes.getBody());
        int id = documentContext.read("$.id");
        String name = documentContext.read("$.name");
        String location = documentContext.read("$.location");

        assertThat(id).isInstanceOf(Number.class).isNotNull();
        assertThat(name).isEqualTo("Australian Open");
        assertThat(location).isEqualTo("Melbourne, Australia");
    }

    @Test
    void shouldReturnSingleTournament() {
        ResponseEntity<String> res = restTemplate.getForEntity("/tournaments/102", String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(res.getBody());

        int id = documentContext.read("$.id");
        assertThat(id).isEqualTo(102);
        String name = documentContext.read("$.name");
        assertThat(name).isEqualTo("ASB Classic");
        String location = documentContext.read("$.location");
        assertThat(location).isEqualTo("Auckland, New Zealand");
    }

    @Test
    void shouldReturnListOfRegistrations() {
        ResponseEntity<String> res = restTemplate.getForEntity("/registrations", String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(res.getBody());
        int count = documentContext.read("$.length()");
        JSONArray ids = documentContext.read("$..id");

        assertThat(count).isEqualTo(4);
        assertThat(ids).containsExactlyInAnyOrder(1000, 1001, 1002, 1003);
    }

    @Test
    @DirtiesContext
    void shouldCreateNewRegistration() {
        Registration registration = new Registration(Instant.now());
        logger.info("New registration: {}", registration);
        ResponseEntity<Void> postRes = restTemplate.postForEntity("/registrations", registration, Void.class);

        assertThat(postRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI savedTournamentLocation = postRes.getHeaders().getLocation();
        ResponseEntity<String> getRes = restTemplate.getForEntity(savedTournamentLocation, String.class);

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getRes.getBody());
        int id = documentContext.read("$.id");
        Instant date = Instant.parse(documentContext.read("$.date"));

        assertThat(id).isInstanceOf(Number.class).isNotNull();
        assertThat(date).isInstanceOf(Instant.class).isNotNull();
    }

    @Test
    @DirtiesContext
    void shouldAttachRegistrationToTournament() {
        ResponseEntity<Void> putRes = restTemplate.exchange("/tournaments/102/registrations/1002", HttpMethod.PUT, null, Void.class);
        assertThat(putRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getRes = restTemplate.getForEntity("/tournaments/102", String.class);
        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getRes.getBody());

        int id = documentContext.read("$.id");
        int registrationId = documentContext.read("$.registrations[0].id");

        assertThat(id).isEqualTo(102);
        assertThat(registrationId).isEqualTo(1002);
    }

    @Test
    @DirtiesContext
    void shouldAttachMultipleRegistrationsToOneTournament() {
        ResponseEntity<Void> putRes1 = restTemplate.exchange("/tournaments/102/registrations/1002", HttpMethod.PUT, null, Void.class);
        assertThat(putRes1.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Void> putRes2 = restTemplate.exchange("/tournaments/102/registrations/1003", HttpMethod.PUT, null, Void.class);
        assertThat(putRes2.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getRes = restTemplate.getForEntity("/tournaments/102", String.class);
        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getRes.getBody());

        int id = documentContext.read("$.id");
        JSONArray registrationIds = documentContext.read("$.registrations..id");

        assertThat(id).isEqualTo(102);
        assertThat(registrationIds).containsExactlyInAnyOrder(1002, 1003);
    }

    @Test
    @DirtiesContext
    void shouldDeleteTournamentAndAssociatedRegistrations() {
        ResponseEntity<Void> delRes = restTemplate.exchange("/tournaments/100", HttpMethod.DELETE, null, Void.class);
        assertThat(delRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<Void> getTournamentRes = restTemplate.getForEntity("/tournaments/100", Void.class);
        ResponseEntity<Void> getRegistrationRes1 = restTemplate.getForEntity("/registrations/1000", Void.class);
        ResponseEntity<Void> getRegistrationRes2 = restTemplate.getForEntity("/registrations/1001", Void.class);

        assertThat(getTournamentRes.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getRegistrationRes1.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(getRegistrationRes2.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
