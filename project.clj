(defproject concept_discussion "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein2-eclipse "2.0.0"]]
            :dependencies [[org.clojure/clojure "1.8.0"]
                 [slingshot "0.12.2"]]
  :main ^:skip-aot concept-discussion.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
