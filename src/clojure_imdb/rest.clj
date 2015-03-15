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

(defn- create-actor
  ([actor]
    (let [ id (core/create-actor actor) ]
      (created (str "/actor/" id)))))

(defn- get-actor
  ([id]
    (with-response-defaults
      (if-let [ actor (core/get-actor (str id)) ]
        (response actor)
        (not-found id)))))

(defn- get-all-actors
  ([]
    (with-response-defaults (response (core/get-actors)))))

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

(defn- link-film-to-actor
  ([{:keys [:film-id :id]}]
    (with-response-defaults
      (core/link-film-to-actor film-id id))))

(defroutes app-routes
  (context "/actor/:id" [id]
    (GET "/" []
      (get-actor id)))

  (context "/actors" []
    (GET  "/" []
      (get-all-actors))
    (POST "/" {params :params}
      (create-actor params)))

  (context "/film/:id" [id]
    (GET "/" []
      (get-film id)))
  (context "/film/:film-id/actors" []
    (POST "/" {params :params}
      (link-film-to-actor params)))

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
