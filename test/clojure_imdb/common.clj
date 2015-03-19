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

(defn create-person [person]
  (get-location (app-post "/persons" person)))

(defn create-film
  ([film]
    (get-location (app-post "/films" film))))

(defn create-role
  ([role]
    (get-location (app-post "/roles" role))))

(defn create-credit
  ([film-id person-id role-id]
    (app-post (format "/film/%s/credits" film-id) { :person-id person-id :role-id role-id })))

(defn with-id
  ([person id]
    (merge {:id id} person)))

