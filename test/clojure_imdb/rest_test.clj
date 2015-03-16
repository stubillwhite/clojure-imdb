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

(def person-one { :name "person-one" })
(def person-two { :name "person-two" })

(fact
  "Get person given person exists then person"
  (let [ id       (create-person-and-get-id person-one)
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

(fact
  "Get persons given persons exist then persons"
  (let [ id-one          (create-person-and-get-id person-one)
         id-two          (create-person-and-get-id person-two)
         expected-persons [(with-id person-one id-one) (with-id person-two id-two)]
         response        (app-get "/persons") ]
    (response :status)                         => HttpServletResponse/SC_OK
    (extract-body response)                       => (contains expected-persons :in-any-order)
    (get-in response [:headers :Content-Type]) => "application/json"))

(def film-one { :title "film-one" })
(def film-two { :title "film-two" })

(fact
  "Get film given film exists then film"
  (let [ id       (create-film-and-get-id film-one)
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

(fact
  "FOO"
  (let [ film-id  (create-film-and-get-id film-one)
         person-id (create-person-and-get-id person-one)
         response (app-post (str "/film/" film-id "/persons/") { :id person-id }) ]
    (get-in response [:headers :Content-Type]) => "application/json"))

