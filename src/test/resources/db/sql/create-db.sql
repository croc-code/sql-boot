CREATE TABLE users (
  id         INTEGER PRIMARY KEY,
  name VARCHAR(30),
  email  VARCHAR(50)
);

CREATE ALIAS getVersion FOR "org.h2.engine.Constants.getVersion";