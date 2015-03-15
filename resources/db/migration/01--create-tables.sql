CREATE TABLE actors
(
  id text NOT NULL,
  name text,
  CONSTRAINT actors_pk PRIMARY KEY (id)
);

CREATE TABLE films
(
  id text NOT NULL,
  title text,
  CONSTRAINT films_pk PRIMARY KEY (id)
);

CREATE TABLE films_actors
(
  film_id text NOT NULL REFERENCES films (id),
  actor_id text NOT NULL REFERENCES actors (id)
);
