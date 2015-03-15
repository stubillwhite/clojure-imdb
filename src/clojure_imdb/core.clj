(ns clojure-imdb.core
  (:require
    [clojure-imdb.persistence :as db]
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)
(timbre/set-level! :warn)

(defn- generate-id
  ([]
    (str (java.util.UUID/randomUUID))))

(defn create-actor
  ([actor]
    (let [ id (generate-id)
           {:keys [:name]} actor ]
      (debug (format "Creating actor %s with content %s" id actor))
      (db/create-actor! db/yesql-config id name)
      id)))

(defn get-actor
  ([id]
    (first (db/get-actor db/yesql-config id))))

(defn get-actors
  ([]
    (seq (db/get-actors db/yesql-config))))

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

(defn link-film-to-actor
  ([film-id actor-id]
    (debug (format "Linking film %s with actor %s" film-id actor-id))
    (db/link-film-to-actor! db/yesql-config film-id actor-id)
    { :film-id film-id :actor-id actor-id }))
