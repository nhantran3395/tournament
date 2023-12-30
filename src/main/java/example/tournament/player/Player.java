package example.tournament.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import example.tournament.player_profile.PlayerProfile;
import example.tournament.registration.Registration;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Player {
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("player")
    private List<Registration> registrations;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    @Column(nullable = false, length = 100)
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private PlayerProfile profile;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return Id;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }

    @Override
    public String toString() {
        return "Player{" +
                "registrations=" + registrations +
                ", Id=" + Id +
                ", name='" + name + '\'' +
                ", profile=" + profile +
                '}';
    }
}
