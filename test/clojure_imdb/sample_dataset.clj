(ns clojure-imdb.sample-dataset
  (:use
    [clojure-imdb.infrastructure :refer [clean datasource]]
    [clojure-imdb.common]))

(defn- member-of?
  ([x vals]
    (some #{x} vals)))

(defn- load-actors
  ([acc actors]
    (reduce
      (fn [acc x]
        (if (member-of? x (keys (acc :actors)))
          acc
          (assoc-in acc [:actors x] (create-actor-and-get-id {:name x}))))
      acc
      actors)))

(defn- load-films
  ([acc films]
    (reduce
      (fn [acc x]
        (if (member-of? x (keys (acc :films)))
          acc
          (assoc-in acc [:films x] (create-film-and-get-id {:title x}))))
      acc
      films)))

(defn- load-films-to-actors
  ([acc [film actors]]
    (doall (for [actor actors]
             (let [ film-id  (get-in acc [:films film])
                    actor-id (get-in acc [:actors actor]) ]
               (link-film-to-actor film-id actor-id))))
    acc))

(defn- load-films-and-actors
  ([acc [film actors]]
    (-> acc
      (load-films [film])
      (load-actors actors)
      (load-films-to-actors [film actors]))))

(defn- load-dataset
  ([dataset]
    (reduce
      load-films-and-actors
      { :films  {}
        :actors {} }
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

