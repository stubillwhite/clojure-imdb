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

