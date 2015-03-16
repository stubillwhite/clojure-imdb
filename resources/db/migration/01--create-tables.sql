CREATE TABLE persons
(
  id text NOT NULL,
  name text,
  CONSTRAINT persons_pk PRIMARY KEY (id)
);

CREATE TABLE films
(
  id text NOT NULL,
  title text,
  CONSTRAINT films_pk PRIMARY KEY (id)
);

CREATE TABLE films_persons
(
  film_id text NOT NULL REFERENCES films (id),
  person_id text NOT NULL REFERENCES persons (id)
);
