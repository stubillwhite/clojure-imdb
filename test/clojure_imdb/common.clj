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

(defn create-person-and-get-id
  ([person]
    (get-id (create-person person))))

(defn create-film
  ([film]
    (get-location (app-post "/films" film))))

(defn create-film-and-get-id
  ([film]
    (get-id (create-film film))))

(defn link-film-to-person
  ([film-id person-id]
    (app-post (str "/film/" film-id "/persons/") { :id person-id })))

(defn with-id
  ([person id]
    (merge {:id id} person)))

