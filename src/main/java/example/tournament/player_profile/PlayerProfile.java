package example.tournament.player_profile;

import jakarta.persistence.*;

@Entity
public class PlayerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    @Column(unique = true, nullable = false, length = 100)
    private String twitter;

    public PlayerProfile() {
    }

    public PlayerProfile(String twitter) {
        this.twitter = twitter;
    }

    public long getId() {
        return Id;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
