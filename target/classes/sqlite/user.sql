CREATE TABLE user (
      id INTEGER PRIMARY KEY,
      ballotId INTEGER,
      userType TEXT,
      username TEXT,
      password TEXT,
      token TEXT,
      expired BOOLEAN NOT NULL CHECK (expired IN (0, 1))
);