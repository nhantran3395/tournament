package example.tournament.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Player player;
    @Column(nullable = false)
    private Instant date;

    public Registration() {
    }

    public Registration(Instant date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", player=" + player +
                ", date=" + date +
                '}';
    }
}