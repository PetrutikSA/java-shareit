CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512),
  available BOOLEAN NOT NULL,
  user_id BIGINT NOT NULL REFERENCES users (Id),
  CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS ind_user_id ON items(user_id);

CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  user_id BIGINT NOT NULL REFERENCES users (id),
  item_id BIGINT NOT NULL REFERENCES items (id),
  start TIMESTAMP NOT NULL,
  ending TIMESTAMP NOT NULL,
  status  VARCHAR NOT NULL,
  CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS ind_booker_id ON bookings(user_id);
CREATE INDEX IF NOT EXISTS ind_item_id ON bookings(item_id);

CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  text VARCHAR NOT NULL,
  item_id BIGINT NOT NULL REFERENCES items(id),
  author_id BIGINT NOT NULL REFERENCES users(id),
  created TIMESTAMP DEFAULT now(),
  CONSTRAINT pk_comment PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  user_id BIGINT NOT NULL REFERENCES users (id),
  description VARCHAR(512),
  name VARCHAR(255),
  status VARCHAR NOT NULL,
  created TIMESTAMP DEFAULT now(),
  CONSTRAINT pk_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS offered_items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  item_id BIGINT NOT NULL REFERENCES items(id) ON DELETE CASCADE,
  request_id BIGINT NOT NULL REFERENCES requests(id) ON DELETE CASCADE,
  CONSTRAINT pk_offered_items PRIMARY KEY (id)
);