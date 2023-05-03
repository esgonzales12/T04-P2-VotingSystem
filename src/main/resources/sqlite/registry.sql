CREATE TABLE registeredVoter (
     id INTEGER PRIMARY KEY,
     demographicHash TEXT,
     name TEXT,
     address TEXT,
     voteStatus TEXT NOT NULL CHECK (voteStatus IN ('VOTED', 'NOT_VOTED'))
);