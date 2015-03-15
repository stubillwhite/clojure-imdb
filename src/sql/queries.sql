-- name: create-actor!
-- Creates an actor.
INSERT INTO actors (id, name) VALUES (:id, :name)

-- name: get-actor
-- Returns an actor by ID.
SELECT * FROM actors WHERE id = :id

-- name: get-actors
-- TODO: Testing, should be removed
SELECT * FROM actors

-- name: create-film!
-- Creates a film.
INSERT INTO films (id, title) VALUES (:id, :title)

-- name: get-film
-- Returns a film by ID.
SELECT * FROM films WHERE id = :id

-- name: link-film-to-actor!
-- Links film to actor.
INSERT INTO films_actors (film_id, actor_id) VALUES (:film_id, :actor_id)

-- name: films-to-actors
-- TODO: Testing, should be removed
SELECT f.title, a.name FROM films f
  JOIN films_actors fa ON f.id = fa.film_id
  JOIN actors a ON a.id = fa.actor_id
