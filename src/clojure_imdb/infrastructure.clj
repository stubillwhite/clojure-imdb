(ns clojure-imdb.infrastructure
  (:import
    [org.flywaydb.core Flyway]))

(def datasource
  (doto (org.postgresql.ds.PGSimpleDataSource.)
    (.setServerName "localhost")
    (.setDatabaseName "clojure-imdb-dev")
    (.setUser "stubillwhite")
    (.setPassword "testpassword")))

(defn flyway
  ([datasource]
    (doto (Flyway.)
      (.setDataSource datasource)
      (.setSqlMigrationPrefix "")
      (.setSqlMigrationSeparator "--")
      (.setBaselineOnMigrate true))))

(defn migrate
  ([datasource]
    (.migrate (flyway datasource))))

(defn clean
  ([datasource]
    (.clean (flyway datasource))
    (migrate datasource)))

;(clean datasource)
