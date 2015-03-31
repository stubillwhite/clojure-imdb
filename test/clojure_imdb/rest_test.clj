(ns clojure-imdb.rest-test
  (:use
    [expectations]
    [cheshire.core :refer [parse-string generate-string]]
    [ring.mock.request :refer [request body content-type header]]
    [clojure.walk :refer [keywordize-keys]]
    [clojure-imdb.infrastructure :refer [clean datasource]]
    [clojure-imdb.common]
    [clojure-imdb.rest])
  (:import
    [javax.servlet.http HttpServletResponse]))

(clean datasource)

(defn resource-not-found
  ([]
    (more->
      HttpServletResponse/SC_NOT_FOUND (:status))))

(defn resource
  ([body]
    (more-> HttpServletResponse/SC_OK (:status)
            "application/json"        (get-in [:headers :Content-Type])
            body                      (extract-body))))

;; Non-existent resource then not found
(expect (resource-not-found) (app-get "/non-existent-resource"))

;; Get person given person exists then person
(expect-let [ id (get-id (create-person { :name "person-one" })) ]
  (resource { :name "person-one" :id id })
  (app-get (str "/person/" id)))

;; Get person given person does not exist then not found
(expect (resource-not-found) (app-get "/person/non-existent-id"))

;; Get film given film exists then film
(expect-let [ id (get-id (create-film { :title "film-one" })) ]
  (resource { :title "film-one" :id id })
  (app-get (str "/film/" id)))

;; Get film given film does not exist then not found
(expect (resource-not-found) (app-get "/film/non-existent-id"))

;; Get role given role exists then role
(expect-let [ id (get-id (create-role { :title "role-one" })) ]
  (resource { :title "role-one" :id id })
  (app-get (str "/role/" id)))

;; Get role given role does not exist then not found
(expect (resource-not-found) (app-get "/role/non-existent-id"))







