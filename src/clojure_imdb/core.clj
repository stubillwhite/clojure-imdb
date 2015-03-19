(ns clojure-imdb.core
  (:require
    [clojure-imdb.persistence :as db]
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)
(timbre/set-level! :warn)

(defn- generate-id
  ([]
    (str (java.util.UUID/randomUUID))))

(defn create-person
  ([person]
    (let [ id (generate-id)
           {:keys [:name]} person ]
      (debug (format "Creating person %s with content %s" id person))
      (db/create-person! db/yesql-config id name)
      id)))

(defn get-person
  ([id]
    (first (db/get-person db/yesql-config id))))

(defn create-film
  ([film]
    (let [ id (generate-id)
           {:keys [:title]} film ]
      (debug (format "Creating film %s with content %s" id film))
      (db/create-film! db/yesql-config id title)
      id)))

(defn get-film
  ([id]
    (first (db/get-film db/yesql-config id))))

(defn create-role
  ([role]
    (let [ id (generate-id)
           {:keys [:title]} role ]
      (debug (format "Creating role %s with content %s" id role))
      (db/create-role! db/yesql-config id title)
      id)))

(defn get-role
  ([id]
    (first (db/get-role db/yesql-config id))))

(defn create-credit
  ([film-id person-id role-id]
    (debug (format "Creating credit for person %s as %s in film %s" person-id role-id film-id))
    (db/create-credit! db/yesql-config film-id person-id role-id)
    { :film-id film-id :person-id person-id :role-id role-id}))
