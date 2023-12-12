BEGIN TRANSACTION;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS tournament_team CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS tournament CASCADE;
DROP TABLE IF EXISTS team CASCADE;

-- Team Table:
CREATE TABLE team (
	team_id SERIAL,
	team_name varchar(15) NOT NULL,
	open_to_public BOOLEAN,
	CONSTRAINT PK_team PRIMARY KEY (team_id)
);

-- Tournament Table:
CREATE TABLE tournament (
	tournament_id SERIAL,
	tournament_name varchar(100) NOT NULL UNIQUE,
	status varchar(20) DEFAULT 'Upcoming',
	prize varchar(50),
	date_and_time TIMESTAMP NOT NULL,
	location varchar(50) DEFAULT 'Online',
	game varchar(20) NOT NULL,
	format varchar(20) DEFAULT 'Best of 5',
	rules varchar(20) DEFAULT '5v5',
	description varchar(300),
	entry_fee varchar(20) DEFAULT 'Free',
	CONSTRAINT PK_tournament PRIMARY KEY (tournament_id)
);

-- Tournament_team Table:
CREATE TABLE tournament_team (
	tournament_team_id SERIAL PRIMARY KEY,
	tournament_id INTEGER REFERENCES tournament(tournament_id),
	team_id INTEGER REFERENCES team(team_id)
);

-- User Table:
CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	team_id INTEGER REFERENCES team(team_id), -- this can be null for non competitors to browse
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

-- Matches Table:
CREATE TABLE matches (
	match_id SERIAL PRIMARY KEY,
	tournament_id INTEGER REFERENCES tournament(tournament_id) NOT NULL,
	first_team_id INTEGER REFERENCES team(team_id),
	second_team_id INTEGER REFERENCES team(team_id),
	winner_id INTEGER REFERENCES team(team_id) DEFAULT NULL,
	match_format varchar(20) DEFAULT 'best of 3',
	round INTEGER NOT NULL,
	first_score INTEGER DEFAULT 0,
	second_score INTEGER DEFAULT 0,
	date_and_time TIMESTAMP
);

CREATE TABLE team_request (
    team_request_id SERIAL PRIMARY KEY,
    sender_id INTEGER REFERENCES users(user_id),
    receiver_id INTEGER REFERENCES users(user_id),
    team_id INTEGER REFERENCES team(team_id),
    status varchar (20) NOT NULL DEFAULT 'Pending'
);

COMMIT TRANSACTION;

ALTER TABLE tournament
ADD COLUMN tournament_owner varchar(50) REFERENCES users(username);

ALTER TABLE team ADD COLUMN created_by VARCHAR(50) REFERENCES users(username);
