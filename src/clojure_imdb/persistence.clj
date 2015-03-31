(ns clojure-imdb.persistence
  (:require
    [yesql.core :refer [defqueries]]))

(defqueries "sql/queries.sql")

(def yesql-config
  { :classname   "org.postgresql.Driver"
    :subprotocol "postgresql"
    :subname     "//localhost:5432/clojure-imdb-dev"
    :user        "stubillwhite"
    :password    "testpassword" })
