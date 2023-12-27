DELETE
FROM registration
WHERE id IN (1000, 1001, 1002, 1003);

DELETE
FROM player
WHERE id IN (100, 101);

DELETE
FROM player_profile
WHERE id IN (100, 101);

DELETE
FROM tournament
WHERE id IN (100, 101, 102);