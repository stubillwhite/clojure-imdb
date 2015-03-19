(ns clojure-imdb.sample-dataset
  (:use
    [clojure-imdb.infrastructure :refer [clean datasource]]
    [clojure-imdb.common]))

(clean datasource)

(defn add-film
  ([acc title]
    (update-in acc [:films title] (fn [x] (or x (get-id (create-film { :title title })))))))

(defn add-role
  ([acc title]
    (update-in acc [:roles title] (fn [x] (or x (get-id (create-role { :title title })))))))

(defn add-person
  ([acc name]
    (update-in acc [:persons name] (fn [x] (or x (get-id (create-person { :name name })))))))

(defn add-credit
  ([acc film-id person-id role-id]
    (create-credit film-id person-id role-id)))

(defn add
  ([acc film-title role-title person-name]
    (let [ make-credit (fn [acc] (let [ film-id   (get-in acc [:films film-title])
                                       role-id   (get-in acc [:roles role-title])
                                       person-id (get-in acc [:persons person-name]) ]
                                  (add-credit acc film-id person-id role-id)
                                  acc))]
      (-> acc
        (add-film film-title)
        (add-role role-title)
        (add-person person-name)
        (make-credit)))))

(defn accumulator
  ([] { :films   {}
        :persons {}
        :roles   {}} ))

(-> (accumulator)
  (add "Raiders of the Lost Ark" "Director" "Steven Spielberg")
  (add "Raiders of the Lost Ark" "Cast"     "Harrison Ford")
  (add "The 'Burbs"              "Director" "Joe Dante")
  (add "The 'Burbs"              "Cast"     "Tom Hanks")
  (add "The 'Burbs"              "Cast"     "Carrie Fisher")
  (add "The 'Burbs"              "Cast"     "Corey Feldman")
  (add "The 'Burbs"              "Cast"     "Corey Feldman")
  (add "Unforgiven"              "Director" "Clint Eastwood")
  (add "Unforgiven"              "Cast"     "Clint Eastwood")
  (add "Unforgiven"              "Cast"     "Gene Hackman")
  )






