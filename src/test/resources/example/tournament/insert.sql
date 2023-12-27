INSERT INTO player_profile(id, twitter)
VALUES (100, '@roger1');
INSERT INTO player_profile(id, twitter)
VALUES (101, '@djokovic2');
INSERT INTO player(id, name, profile_id)
VALUES (100, 'Roger Federer', 100);
INSERT INTO player(id, name)
VALUES (101, 'Novak Djokovic');
INSERT INTO tournament(id, name, location)
VALUES (100, 'Bank of China Hong Kong Tennis Open', 'Hong Kong, Hong Kong');
INSERT INTO tournament(id, name, location)
VALUES (101, 'Adelaide International', 'Adelaide, Australia');
INSERT INTO tournament(id, name, location)
VALUES (102, 'ASB Classic', 'Auckland, New Zealand');
INSERT INTO registration(id, date, tournament_id)
VALUES (1000, now(), 100);
INSERT INTO registration(id, date, tournament_id)
VALUES (1001, now(), 100);
INSERT INTO registration(id, date)
VALUES (1002, now());
INSERT INTO registration(id, date)
VALUES (1003, now());