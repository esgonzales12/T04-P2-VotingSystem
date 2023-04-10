CREATE TABLE ballot (
    id INTEGER PRIMARY KEY,
    ballotHash TEXT,
    location TEXT,
    date TEXT
);

CREATE TABLE ballotSection(
    id INTEGER PRIMARY KEY,
    ballotId INTEGER,
    name TEXT
);

CREATE TABLE ballotItem (
    id INTEGER PRIMARY KEY,
    ballotId INTEGER,
    ballotSectionId INTEGER,
    description TEXT,
    type TEXT,
    name TEXT,
    allowedSelections INTEGER
);

CREATE TABLE itemCandidate (
    id INTEGER PRIMARY KEY,
    itemId INTEGER,
    name TEXT,
    party TEXT
);

CREATE TABLE itemOption (
    id INTEGER PRIMARY KEY,
    itemId INTEGER,
    option TEXT
);

CREATE TABLE vote (
    id INTEGER PRIMARY KEY,
    ballotId INTEGER,
    ballotSectionId INTEGER,
    itemId INTEGER,
    selectionId INTEGER,
    date TEXT,
    finalized BOOLEAN NOT NULL CHECK (finalized IN (0, 1))
);