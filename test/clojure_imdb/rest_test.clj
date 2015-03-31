(ns clojure-imdb.rest-test
  (:use
    [clojure.test]
    [midje.sweet]
    [cheshire.core :refer [parse-string generate-string]]
    [ring.mock.request :refer [request body content-type header]]
    [clojure.walk :refer [keywordize-keys]]
    [clojure-imdb.infrastructure :refer [clean datasource]]
    [clojure-imdb.common]
    [clojure-imdb.rest])
  (:import
    [javax.servlet.http HttpServletResponse]))

(clean datasource)

(fact
  "Non-existent resource then not found"
  (let [ response (app-get "/non-existent-resource") ]
    (response :status) => HttpServletResponse/SC_NOT_FOUND))

(def person-one { :name "person-one" })

(fact
  "Get person given person exists then person"
  (let [ id       (get-id (create-person person-one))
         location (str "/person/" id)
         response (app-get location) ]
    (response :status)                         => HttpServletResponse/SC_OK
    (extract-body response)                    => (with-id person-one id)
    (get-in response [:headers :Content-Type]) => "application/json"))

(fact
  "Get person given person does not exist then not found"
  (let [ response (app-get "/person/12345") ]
    (response :status)                         => HttpServletResponse/SC_NOT_FOUND
    (get-in response [:headers :Content-Type]) => "application/json"))

(def film-one { :title "film-one" })

(fact
  "Get film given film exists then film"
  (let [ id       (get-id (create-film film-one))
         location (str "/film/" id)
         response (app-get location) ]
    (response :status)                         => HttpServletResponse/SC_OK
    (extract-body response)                    => (with-id film-one id)
    (get-in response [:headers :Content-Type]) => "application/json"))

(fact
  "Get film given film does not exist then not found"
  (let [ response (app-get "/film/12345") ]
    (response :status)                         => HttpServletResponse/SC_NOT_FOUND
    (get-in response [:headers :Content-Type]) => "application/json"))

(def role-one { :title "role-one" })

(fact
  "Get role given role exists then role"
  (let [ id       (get-id (create-role role-one))
         location (str "/role/" id)
         response (app-get location) ]
    (response :status)                         => HttpServletResponse/SC_OK
    (extract-body response)                    => (with-id role-one id)
    (get-in response [:headers :Content-Type]) => "application/json"))

(fact
  "Get role given role does not exist then not found"
  (let [ response (app-get "/role/12345") ]
    (response :status)                         => HttpServletResponse/SC_NOT_FOUND
    (get-in response [:headers :Content-Type]) => "application/json"))




(defn acc
  ([]
    { :films   []
      :roles   []
      :persons [] }))

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


(comment fact
  "Get credits given credits exist then credits"
  (let [ acc     (-> (acc)
                   (add "film-one" "role-one" "person-one")
                   (add "film-one" "role-one" "person-two")
                   (add "film-one" "role-two" "person-three"))
         film-id (get-in acc [:films "film-one"])
         response (app-get (format "/films/%s/credits" film-id)) ]
    (response :status)                         => HttpServletResponse/SC_OK
    (get-in response [:headers :Content-Type]) => "application/json"))


(comment -> (acc)
  (add "film-one" "role-one" "person-one")
  (add "film-one" "role-one" "person-two")
  (add "film-one" "role-two" "person-three"))

