package example.tournament;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import example.tournament.dto.CreatePlayerDto;
import example.tournament.dto.CreatePlayerProfileDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "insert-profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "insert-players.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "delete-players.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "delete-profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TournamentApplicationTests {
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
        CreatePlayerDto newPlayerDto = new CreatePlayerDto("Rafael Nadal");
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
    void shouldGetRequestedPlayer() {
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
        CreatePlayerProfileDto newProfileDto = new CreatePlayerProfileDto("@rafael1");
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
    void shouldDeletePlayerAndAssociatedProfile() {
        ResponseEntity<Void> deleteRes = restTemplate.exchange("/players/100", HttpMethod.DELETE, null, Void.class);
        assertThat(deleteRes.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        ResponseEntity<String> getPlayerRes = restTemplate.getForEntity("/players/100", String.class);
        assertThat(getPlayerRes.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        ResponseEntity<String> getProfileRes = restTemplate.getForEntity("/profiles/100", String.class);
        assertThat(getProfileRes.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
