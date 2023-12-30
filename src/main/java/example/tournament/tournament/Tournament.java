package example.tournament.tournament;

import example.tournament.registration.Registration;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @Column(nullable = false)
    private String location;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tournament_id", referencedColumnName = "id")
    private List<Registration> registrations = new ArrayList<>();

    public Tournament() {
    }

    public Tournament(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Registration> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(List<Registration> registrations) {
        this.registrations = registrations;
    }

    @Override
    public String toString() {
        return "Tournament{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", registrations=" + registrations +
                '}';
    }
}