(ns clojure-imdb.rest
  (:require
    [clojure-imdb.core :as core]
    [compojure.core :refer [defroutes context GET POST PUT DELETE]]
    [compojure.route :as route]
    [ring.middleware.content-type :refer [wrap-content-type]]
    [ring.middleware.defaults :refer [wrap-defaults]]
    [ring.middleware.json :refer [wrap-json-body wrap-json-params wrap-json-response]]
    [ring.util.response :refer [response created not-found content-type redirect]]
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

(defn- find-person
  ([{:keys [name]}]
    (with-response-defaults
      (response (core/find-person name)))))

(defn- get-person
  ([id]
    (with-response-defaults
      (if-let [ person (core/get-person (str id)) ]
        (response person)
        (not-found id)))))

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

(defn- get-film-credits
  ([{:keys [:film-id]}]
    (with-response-defaults
      (core/get-film-credits film-id))))

(defn- create-credit
  ([{:keys [:film-id :person-id :role-id]}]
    (with-response-defaults
      (core/create-credit film-id person-id role-id))))

(defn- create-role
  ([role]
    (let [ id (core/create-role role) ]
      (created (str "/role/" id)))))

(defn- get-role
  ([id]
    (with-response-defaults
      (if-let [ role (core/get-role (str id)) ]
        (response role)
        (not-found id)))))

(defroutes app-routes
  (context "/person" []
    (GET  "/"  {params :params} (find-person params)))
  (context "/person/:id" [id]
    (GET  "/" [] (get-person id)))
  (context "/persons" []
    (POST "/" {params :params} (create-person params)))

  (context "/film/:id" [id]
    (GET  "/" [] (get-film id)))
  (context "/film/:film-id/credits" []
    (GET  "/" {params :params} (get-film-credits params))
    (POST "/" {params :params} (create-credit params)))

  (context "/films" []
    (POST "/" {params :params} (create-film params)))

  (context "/roles" []
    (POST "/" {params :params} (create-role params)))
  (context "/role/:id" [id]
    (GET "/" [] (get-role id)))

  (GET "/" [] (redirect "/index.html"))
  (route/resources "/")
  (route/not-found (not-found "Not found")))

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
