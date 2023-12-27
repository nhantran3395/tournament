CREATE TABLE player_profile
(
    id      BIGINT       NOT NULL,
    twitter VARCHAR(100) NOT NULL,
    CONSTRAINT pk_playerprofile PRIMARY KEY (id)
);

ALTER TABLE player_profile
    ADD CONSTRAINT uc_playerprofile_twitter UNIQUE (twitter);

CREATE TABLE player
(
    id         BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    profile_id BIGINT,
    CONSTRAINT pk_player PRIMARY KEY (id)
);

ALTER TABLE player
    ADD CONSTRAINT uc_player_profile UNIQUE (profile_id);

ALTER TABLE player
    ADD CONSTRAINT FK_PLAYER_ON_PROFILE FOREIGN KEY (profile_id) REFERENCES player_profile (id);

CREATE TABLE tournament
(
    id       BIGINT       NOT NULL,
    name     VARCHAR(100) NOT NULL,
    location VARCHAR(255) NOT NULL,
    CONSTRAINT pk_tournament PRIMARY KEY (id)
);

ALTER TABLE tournament
    ADD CONSTRAINT uc_tournament_name UNIQUE (name);

CREATE TABLE registration
(
    id            BIGINT NOT NULL,
    player_id     BIGINT,
    date          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    tournament_id BIGINT,
    CONSTRAINT pk_registration PRIMARY KEY (id)
);

ALTER TABLE registration
    ADD CONSTRAINT FK_REGISTRATION_ON_PLAYER FOREIGN KEY (player_id) REFERENCES player (id);

ALTER TABLE registration
    ADD CONSTRAINT FK_REGISTRATION_ON_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournament (id);