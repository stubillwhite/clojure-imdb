-- name: create-person!
-- Creates an person.
INSERT INTO persons (id, name) VALUES (:id, :name)

-- name: get-person
-- Returns an person by ID.
SELECT * FROM persons WHERE id = :id

-- name: create-film!
-- Creates a film.
INSERT INTO films (id, title) VALUES (:id, :title)

-- name: get-film
-- Returns a film by ID.
SELECT * FROM films WHERE id = :id

-- name: create-role!
-- Creates a role.
INSERT INTO roles (id, title) VALUES (:id, :title)

-- name: get-role
-- Returns a role by ID.
SELECT * FROM roles WHERE id = :id

-- name: create-credit!
-- Links a person to a particular role on a particular film.
INSERT INTO credits (film_id, person_id, role_id) VALUES (:film_id, :person_id, :role_id)

-- name: films-to-persons
-- TODO: Testing, should be removed
SELECT f.title, r.title, p.name FROM credits c
  JOIN films f ON c.film_id = f.id
  JOIN persons p ON c.person_id = p.id
  JOIN roles r ON c.role_id = r.id

