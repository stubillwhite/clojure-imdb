(ns clojure-imdb.sample-dataset
  (:use
    [clojure-imdb.infrastructure :refer [clean datasource]]
    [clojure-imdb.common]))

(defn- member-of?
  ([x vals]
    (some #{x} vals)))

(defn- load-persons
  ([acc persons]
    (reduce
      (fn [acc x]
        (if (member-of? x (keys (acc :persons)))
          acc
          (assoc-in acc [:persons x] (create-person-and-get-id {:name x}))))
      acc
      persons)))

(defn- load-films
  ([acc films]
    (reduce
      (fn [acc x]
        (if (member-of? x (keys (acc :films)))
          acc
          (assoc-in acc [:films x] (create-film-and-get-id {:title x}))))
      acc
      films)))

(defn- load-films-to-persons
  ([acc [film persons]]
    (doall (for [person persons]
             (let [ film-id  (get-in acc [:films film])
                    person-id (get-in acc [:persons person]) ]
               (link-film-to-person film-id person-id))))
    acc))

(defn- load-films-and-persons
  ([acc [film persons]]
    (-> acc
      (load-films [film])
      (load-persons persons)
      (load-films-to-persons [film persons]))))

(defn- load-dataset
  ([dataset]
    (reduce
      load-films-and-persons
      { :films  {}
        :persons {} }
      dataset)))

(defn load-sample-dataset
  ([]
    (clean datasource)
    (load-dataset
      [ [ "Star Wars" [ "Mark Hamill"
                        "Carrie Fisher"
                        "Alec Guinness"
                        "Harrison Ford" ]]

        [ "Raiders of the Lost Ark" [ "Harrison Ford" ]]

        [ "The 'Burbs" [ "Tom Hanks"
                         "Carrie Fisher"
                         "Corey Feldman" ]]

        [ "Dragnet" [ "Tom Hanks"
                      "Dan Ackroyd" ]]

        [ "Ghostbusters" [ "Dan Ackroyd"
                           "Bill Murray"
                           "Harold Ramis" ]]
        ])))

