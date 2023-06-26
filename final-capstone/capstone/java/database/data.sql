BEGIN
TRANSACTION;

INSERT INTO users (username, password_hash, role)
VALUES ('user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('admin', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_ADMIN'),
       ('organizer', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_ORGANIZER'),
       ('thunderstrike', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('shadowblade', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('radiant', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('blazeheart', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('tempest', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('astral', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('thunderclap', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('lunar', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('thunderstorm', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('golden', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('soulstealer', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('crimson', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('rising', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER'),
       ('eternal', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER');

INSERT INTO tournament (tournament_name, status, game, prize, date_and_time, location, format, rules, description, entry_fee, tournament_owner)
-- empty tournaments
VALUES ('Summer Clash', 'Open', 'League of Legends', '5000', '2023-08-01 14:00:00', 'Los Angeles', 'Best of 5', '5v5', 'Join the epic battle for glory!', '25', 'organizer'),
       ('Overwatch Mayhem', 'Open', 'Overwatch 2', '1800', '2023-08-08 14:00:00', 'Los Angeles', 'Best of 5', '6v6', 'Prepare for mayhem as teams battle for supremacy.', '10', 'organizer'),
-- tournaments with 7 teams
       ('Legends Unleashed', 'Open', 'League of Legends', '4000', '2023-07-10 15:30:00', 'Online', 'Best of 3', '5v5', 'Unleash your inner legend and dominate the competition.', '25', 'organizer'),
       ('Divine Showdown', 'Open', 'Smite', '1500', '2023-07-20 13:00:00', 'Dallas', 'Best of 3', '5v5', 'Battle for divine supremacy and win amazing rewards!', '20', 'organizer'),
-- tournaments with 8 teams
       ('Tactical Showdown', 'Upcoming', 'Valorant', '2000', '2023-07-07 21:00:00', 'Online', 'Best of 3', '5v5', 'Engage in tactical warfare and outsmart your opponents.', '30', 'organizer'),
       ('Summer Slam', 'Upcoming', 'League of Legends', '4500', '2023-07-09 16:00:00', 'Miami', 'Best of 5', '5v5', 'Experience the heat of the summer in this intense tournament.', '25', 'organizer'),
-- finished tournament
       ('Champion''s Arena', 'Upcoming', 'Dota 2', '3000', '2023-06-20 16:30:00', 'Seattle', 'Best of 3', '5v5', 'Prove your skills and claim the title.', '15', 'organizer'),
       ('Mayhem Madness', 'Upcoming', 'Overwatch 2', '2000', '2023-06-24 19:00:00', 'Online', 'Best of 5', '6v6', 'Experience intense action in this thrilling tournament.', '10', 'organizer');

INSERT INTO team (team_name, open_to_public, created_by)
VALUES ('Lunar Eclipse', true, 'lunar'), ('Storm Fusion', true, 'thunderstorm'), ('Aureate Guard', true, 'golden'), ('Soul Stealers', true, 'soulstealer'),
       ('Crimson Chaos', true, 'crimson'), ('Rising Phoenix', true, 'rising'), ('Eternal Legends', true, 'eternal'),
       ('Immortal Titans', true, 'thunderstrike'), ('Shadow Strikers', true, 'shadowblade'), ('Holy Protectors', true, 'radiant'), ('Flame Revenge', true, 'blazeheart'),
       ('Storm Surge', true, 'tempest'), ('Astral Watchers', true, 'astral'), ('Bolt Warriors', true, 'thunderclap'),
       ('Ignition Rising', true, NULL), ('Phantom Reapers', true, NULL), ('Vanguard Elite', true, NULL), ('Valor Enigma', true, NULL),
       ('Foe Guardians', true, NULL), ('Monarch Strike', true, NULL), ('Shadow Guard', true, NULL), ('Fusion Assault', true, NULL),
       ('Inferno Reavers', true, NULL), ('Eclipse Rising', true, NULL), ('Sacred Rise', true, NULL), ('Shadow Serpents', true, NULL),
       ('Astral Watchers', true, NULL), ('Thunder Titans', true, NULL), ('Frost Falcons', true, NULL), ('Phoenix Embers', true, NULL),
       ('Thunderbolts', true, NULL), ('Radiant Kings', true, NULL), ('Dire Shadows', true, NULL), ('Mystic Fury', true, NULL),
       ('Astral Guard', true, NULL), ('Chaos Reborn', true, NULL), ('Ever Victors', true, NULL), ('Storm Surge', true, NULL),
       ('Valor Squad', true, NULL), ('Nexus Force', true, NULL), ('Omega Guardians', true, NULL), ('Vortex Titans', true, NULL),
       ('Eclipse Blitz', true, NULL), ('Nova Sentinels', true, NULL), ('Shadow Raiders', true, NULL), ('Astral Reapers', true, NULL);

UPDATE users SET team_id = CASE
    WHEN username = 'lunar' THEN 1 WHEN username = 'thunderstorm' THEN 2 WHEN username = 'golden' THEN 3 WHEN username = 'soulstealer' THEN 4
    WHEN username = 'crimson' THEN 5 WHEN username = 'rising' THEN 6 WHEN username = 'eternal' THEN 7
    WHEN username = 'thunderstrike' THEN 8 WHEN username = 'shadowblade' THEN 9 WHEN username = 'radiant' THEN 10 WHEN username = 'blazeheart' THEN 11
    WHEN username = 'tempest' THEN 12 WHEN username = 'astral' THEN 13 WHEN username = 'thunderclap' THEN 14
    ELSE team_id
END
WHERE username IN ('lunar', 'thunderstorm', 'golden', 'soulstealer', 'crimson', 'rising', 'eternal',
    'thunderstrike', 'shadowblade', 'radiant', 'blazeheart', 'tempest', 'astral', 'thunderclap');

INSERT INTO tournament_team(tournament_id, team_id)
VALUES (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (3, 6), (3, 7),
       (4, 8), (4, 9), (4, 10), (4, 11), (4, 12), (4, 13), (4, 14),
       (5, 15), (5, 16), (5, 17), (5, 18), (5, 19), (5, 20), (5, 21), (5, 22),
       (6, 23), (6, 24), (6, 25), (6, 26), (6, 27), (6, 28), (6, 29), (6, 30),
       (7, 31), (7, 32), (7, 33), (7, 34), (7, 35), (7, 36), (7, 37), (7, 38),
       (8, 39), (8, 40), (8, 41), (8, 42), (8, 43), (8, 44), (8, 45), (8, 46);

COMMIT TRANSACTION;
