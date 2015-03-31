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




(comment
  
  (defn acc
    ([]
      { :films   []
        :roles   []
        :persons [] }))

  (defn add-film
    ([acc title]
      (update-in acc [:films title] (fn [x] (or x (get-id (create-film { :title title })))))))

  (defn add-role
    ([acc title]
      (update-in acc [:roles title] (fn [x] (or x (get-id (create-role { :title title })))))))

  (defn add-person
    ([acc name]
      (update-in acc [:persons name] (fn [x] (or x (get-id (create-person { :name name })))))))

  (defn add-credit
    ([acc film-id person-id role-id]
      (create-credit film-id person-id role-id)))

  (defn add
    ([acc film-title role-title person-name]
      (let [ make-credit (fn [acc] (let [ film-id   (get-in acc [:films film-title])
                                         role-id   (get-in acc [:roles role-title])
                                         person-id (get-in acc [:persons person-name]) ]
                                    (add-credit acc film-id person-id role-id)
                                    acc))]
        (-> acc
          (add-film film-title)
          (add-role role-title)
          (add-person person-name)
          (make-credit)))))


  (comment fact
    "Get credits given credits exist then credits"
    (let [ acc     (-> (acc)
                     (add "film-one" "role-one" "person-one")
                     (add "film-one" "role-one" "person-two")
                     (add "film-one" "role-two" "person-three"))
           film-id (get-in acc [:films "film-one"])
           response (app-get (format "/films/%s/credits" film-id)) ]
      (response :status)                         => HttpServletResponse/SC_OK
      (get-in response [:headers :Content-Type]) => "application/json"))


  (comment -> (acc)
    (add "film-one" "role-one" "person-one")
    (add "film-one" "role-one" "person-two")
    (add "film-one" "role-two" "person-three"))


  )
