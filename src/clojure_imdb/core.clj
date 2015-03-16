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

(defn get-persons
  ([]
    (seq (db/get-persons db/yesql-config))))

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

(defn link-film-to-person
  ([film-id person-id]
    (debug (format "Linking film %s with person %s" film-id person-id))
    (db/link-film-to-person! db/yesql-config film-id person-id)
    { :film-id film-id :person-id person-id }))
