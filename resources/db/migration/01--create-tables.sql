CREATE TABLE persons
(
  id   text NOT NULL,
  name text NOT NULL,
  CONSTRAINT persons_pk PRIMARY KEY (id)
);

CREATE TABLE films
(
  id    text NOT NULL,
  title text NOT NULL,
  CONSTRAINT films_pk PRIMARY KEY (id)
);

CREATE TABLE roles
(
  id    text NOT NULL,
  title text NOT NULL,
  CONSTRAINT roles_pk PRIMARY KEY (id)
);

CREATE TABLE credits
(
  film_id   text NOT NULL REFERENCES films (id),
  role_id   text NOT NULL REFERENCES roles (id),
  person_id text NOT NULL REFERENCES persons (id)
);
