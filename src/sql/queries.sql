-- name: create-person!
-- Creates an person.
INSERT INTO persons (id, name, ft) VALUES (:id, :name, to_tsvector('english', :name))

-- name: get-person
-- Returns an person by ID.
SELECT p.name, p.id FROM persons p WHERE id = :id

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

-- name: get-film-credits
-- Returns the credits for a paticular film.
SELECT 
  f.title, 
  p.name, 
  r.title
FROM 
  credits c, 
  films f, 
  persons p, 
  roles r
WHERE 
  c.film_id = f.id AND
  c.person_id = p.id AND
  c.role_id = r.id AND
  c.film_id = :id

-- name: find-persons-by-name
-- Returns the persons whose name matches a particular string
SELECT p.id, p.name FROM persons p WHERE p.ft @@ plainto_tsquery(:query)
