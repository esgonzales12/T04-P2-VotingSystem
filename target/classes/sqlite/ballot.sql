CREATE TABLE ballot (
    id INTEGER PRIMARY KEY,
    hash TEXT,
    name TEXT,
    instructions text,
    location TEXT,
    date TEXT
);

CREATE TABLE section
(
    id INTEGER PRIMARY KEY,
    ballotId INTEGER,
    name TEXT
);

CREATE TABLE item (
    id INTEGER PRIMARY KEY,
    sectionId INTEGER,
    description TEXT,
    type TEXT,
    name TEXT,
    allowedSelections INTEGER
);

CREATE TABLE option (
    id INTEGER PRIMARY KEY,
    itemId INTEGER,
    name TEXT,
    party TEXT,
    choice TEXT
);


CREATE TABLE vote (
    id INTEGER PRIMARY KEY,
    itemId INTEGER,
    selectionId INTEGER,
    value TEXT,
    date TEXT,
    finalized BOOLEAN NOT NULL CHECK (finalized IN (0, 1))
);