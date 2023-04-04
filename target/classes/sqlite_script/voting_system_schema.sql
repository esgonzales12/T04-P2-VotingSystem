CREATE TABLE election (
    id INTEGER PRIMARY KEY,
    location TEXT,
    date TEXT,
    end_date TEXT
);

CREATE TABLE election_office (
    id INTEGER PRIMARY KEY,
    election_id INTEGER,
    office_name TEXT,
    allowed_selections INTEGER
);

CREATE TABLE election_item (
    id INTEGER PRIMARY KEY,
    election_id INTEGER,
    item_name TEXT,
    allowed_selections INTEGER
);

CREATE TABLE office_candidate (
    id INTEGER PRIMARY KEY,
    election_id INTEGER,
    office_id INTEGER,
    firstname TEXT,
    lastname TEXT,
    party TEXT
);

CREATE TABLE item_candidate (
    id INTEGER PRIMARY KEY,
    election_id INTEGER,
    office_id INTEGER,
    item_name TEXT
);


CREATE TABLE vote (
    id INTEGER PRIMARY KEY,
    election_id INTEGER,
    vote_type TEXT,
    date TEXT,
    item_id INTEGER,
    office_id INTEGER,
    office_selection_id INTEGER,
    item_selection_id INTEGER
);


CREATE TABLE user (
    id INTEGER PRIMARY KEY,
    election_id INTEGER,
    user_type TEXT,
    username TEXT,
    password TEXT,
    valid_pass BOOLEAN NOT NULL CHECK (valid_pass IN (0, 1)),
    expired BOOLEAN NOT NULL CHECK (valid_pass IN (0, 1))
);