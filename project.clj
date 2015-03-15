(defproject clojure-imdb "0.1.0-SNAPSHOT"

  :description "A toy IMDB application in Clojure, mostly an excuse to play around with Clojure REST frameworks."

  :url "TODO"

  :license { :name "Eclipse Public License"
             :url  "http://www.eclipse.org/legal/epl-v10.html" }

  :repl-options { :port 4555 }

  :plugins [ [lein-ring "0.8.11"] ]

  :ring { :handler clojure-imdb.rest/app
          :nrepl   { :start? true
                     :port   4555 } }
  
  :dependencies [ ;; Core
                  [org.clojure/clojure "1.6.0"]

                  ;; REST
                  [cheshire "5.4.0"]
                  [compojure "1.3.2"]
                  [ring/ring-core "1.3.2"]
                  [ring/ring-defaults "0.1.4"]
                  [ring/ring-jetty-adapter "1.3.2"]
                  [ring/ring-json "0.3.1"]

                  ;; Logging
                  [com.taoensso/timbre "3.4.0"]

                  ;; Debugging
                  [org.clojure/tools.trace "0.7.8"]
                  
                  ;; Testing
                  [midje "1.6.3"]
                  [ring/ring-mock "0.2.0"]

                  ;; Persistence
                  [yesql "0.4.0"]
                  [org.clojure/java.jdbc "0.3.6"]
                  [postgresql "9.1-901-1.jdbc4"]
                  [org.flywaydb/flyway-core "3.2"] ]

  :profiles  { :dev  { :dependencies [ [javax.servlet/servlet-api "2.5"]
                                       [org.clojure/tools.namespace "0.2.10"]
                                       [ring-mock "0.1.5"] ]
                       :source-paths ["dev"] } })
