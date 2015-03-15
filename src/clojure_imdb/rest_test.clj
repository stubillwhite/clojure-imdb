(ns clojure-imdb.rest-test
  (:use
    [clojure.test]
    [midje.sweet]
    [cheshire.core :refer [parse-string generate-string]]
    [ring.mock.request :refer [request body content-type header]]
    [clojure.walk :refer [keywordize-keys]]
    [clojure-imdb.rest])
  (:import
    [javax.servlet.http HttpServletResponse]))

(def actor-one { :name "actor-one" })
(def actor-two { :name "actor-two" })

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

(defn with-id
  ([actor id]
    (merge {:id id} actor)))

(fact
  "Get actor given actor exists then actor"
  (let [ id       (create-actor-and-get-id actor-one)
         location (str "/actor/" id)
         response (app-get location) ]
    (response :status)                         => HttpServletResponse/SC_OK
    (extract-body response)                    => (with-id actor-one id)
    (get-in response [:headers :Content-Type]) => "application/json"))

(fact
  "Get actor given actor does not exist then not found"
  (let [ response (app-get "/actor/12345") ]
    (response :status)                         => HttpServletResponse/SC_NOT_FOUND
    (get-in response [:headers :Content-Type]) => "application/json"))

(fact
  "Get actors given actors exist then actors"
  (let [ id-one          (create-actor-and-get-id actor-one)
         id-two          (create-actor-and-get-id actor-two)
         expected-actors [(with-id actor-one id-one) (with-id actor-two id-two)]
         response        (app-get "/actors") ]
    (response :status)                         => HttpServletResponse/SC_OK
    (extract-body response)                       => (contains expected-actors :in-any-order)
    (get-in response [:headers :Content-Type]) => "application/json"))

(def film-one { :title "film-one" })
(def film-two { :title "film-two" })

(defn create-film
  ([film]
    (get-location (app-post "/films" film))))

(defn create-film-and-get-id
  ([film]
    (get-id (create-film film))))

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
         actor-id (create-actor-and-get-id actor-one)
         response (app-post (str "/film/" film-id "/actors/") { :id actor-id }) ]
    (get-in response [:headers :Content-Type]) => "application/json"))

