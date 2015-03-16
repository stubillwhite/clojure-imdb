(ns clojure-imdb.rest
  (:require
    [clojure-imdb.core :as core]
    [compojure.core :refer [defroutes context GET POST PUT DELETE]]
    [ring.middleware.content-type :refer [wrap-content-type]]
    [ring.middleware.defaults :refer [wrap-defaults]]
    [ring.middleware.json :refer [wrap-json-body wrap-json-params wrap-json-response]]
    [ring.util.response :refer [response created not-found content-type]]
    [taoensso.timbre :as timbre]))

(timbre/refer-timbre)

(defn- generate-id
  ([]
    (str (java.util.UUID/randomUUID))))

(defn with-response-defaults
  ([response]
    (-> response
      (content-type "application/json"))))

(defn- create-person
  ([person]
    (let [ id (core/create-person person) ]
      (created (str "/person/" id)))))

(defn- get-person
  ([id]
    (with-response-defaults
      (if-let [ person (core/get-person (str id)) ]
        (response person)
        (not-found id)))))

(defn- get-all-persons
  ([]
    (with-response-defaults (response (core/get-persons)))))

(defn- create-film
  ([film]
    (let [ id (core/create-film film) ]
      (created (str "/film/" id)))))

(defn- get-film
  ([id]
    (with-response-defaults
      (if-let [ film (core/get-film (str id)) ]
        (response film)
        (not-found id)))))

(defn- link-film-to-person
  ([{:keys [:film-id :id]}]
    (with-response-defaults
      (core/link-film-to-person film-id id))))

(defroutes app-routes
  (context "/person/:id" [id]
    (GET "/" []
      (get-person id)))

  (context "/persons" []
    (GET  "/" []
      (get-all-persons))
    (POST "/" {params :params}
      (create-person params)))

  (context "/film/:id" [id]
    (GET "/" []
      (get-film id)))
  (context "/film/:film-id/persons" []
    (POST "/" {params :params}
      (link-film-to-person params)))

  (context "/films" []
    (POST "/" {params :params}
      (create-film params))))

(def default-config
  { :params { :urlencoded true
              :keywordize true
              :nested     true
              :multipart  true }

    :responses { :not-modified-responses true
                 :absolute-redirects     true
                 :content-types          true } })

(defn wrap-log-request
  ([handler]
    (fn [req]
      (debug "Handling request" req)
      (handler req))))

(def app
  (-> app-routes
    (wrap-log-request)
    (wrap-defaults default-config)
    (wrap-json-body)
    (wrap-json-params)
    (wrap-json-response)
    (wrap-content-type)))
