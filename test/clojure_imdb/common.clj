(ns clojure-imdb.common
  (:use
    [cheshire.core :refer [parse-string generate-string]]
    [ring.mock.request :refer [request body content-type header]]
    [clojure.walk :refer [keywordize-keys]]
    [clojure-imdb.rest]))

(defn app-get
  ([url]
    (keywordize-keys
      (app (request :get url)))))

(defn app-post
  ([url data]
    (keywordize-keys
      (app (-> (request :post url)
             (body (generate-string data))
             (content-type "application/json")
             (header "Accept" "application/json"))))))

(defn extract-body
  ([response]
    (keywordize-keys (parse-string (response :body)))))

(defn get-location
  ([response]
    (get-in response [:headers :Location])))

(defn get-id
  ([location]
    (last (clojure.string/split location #"/"))))

(defn create-actor [actor]
  (get-location (app-post "/actors" actor)))

(defn create-actor-and-get-id
  ([actor]
    (get-id (create-actor actor))))

(defn create-film
  ([film]
    (get-location (app-post "/films" film))))

(defn create-film-and-get-id
  ([film]
    (get-id (create-film film))))

(defn link-film-to-actor
  ([film-id actor-id]
    (app-post (str "/film/" film-id "/actors/") { :id actor-id })))

(defn with-id
  ([actor id]
    (merge {:id id} actor)))

