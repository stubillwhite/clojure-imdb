-- name: create-person!
-- Creates an person.
INSERT INTO persons (id, name) VALUES (:id, :name)

-- name: get-person
-- Returns an person by ID.
SELECT * FROM persons WHERE id = :id

-- name: get-persons
-- TODO: Testing, should be removed
SELECT * FROM persons

-- name: create-film!
-- Creates a film.
INSERT INTO films (id, title) VALUES (:id, :title)

-- name: get-film
-- Returns a film by ID.
SELECT * FROM films WHERE id = :id

-- name: link-film-to-person!
-- Links film to person.
INSERT INTO films_persons (film_id, person_id) VALUES (:film_id, :person_id)

-- name: films-to-persons
-- TODO: Testing, should be removed
SELECT f.title, a.name FROM films f
  JOIN films_persons fa ON f.id = fa.film_id
  JOIN persons a ON a.id = fa.person_id
