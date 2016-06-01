(ns concept-discussion.link
  (:use [slingshot.slingshot :only [throw+]]))

(def links_ (atom {}))

(defn reset-links_ []
  (reset! links_ {}))

(defn add-links! [version [src-id src-version] & links] ; [[type dst-id dst-version]]
  (let [links (partition 2 links)
        src-id (keyword src-id)]
    (swap! links_ update-in [src-id] (fn [coll x]
                                       (if (= version (count coll))
                                         (let [last-src-version (-> coll first first)]
                                           (if (or (nil? last-src-version) (<= last-src-version src-version))
                                             (concat coll x)
                                             (throw+ {:type :version-exception})))
                                         (throw+ {:type :concurrency-exception})))
           (map (fn [link]
                  (concat [src-version] link)) links)))
  true)

(defn get-links [version [src-id src-version] type]
  (let [links (src-id @links_)]
    [(count links) (last (filter (fn [[cur-src-version cur-type]]
                                   (and (= cur-type type)
                                        (<= cur-src-version src-version)))
                                 (if (nil? version)
                                   links
                                   (take version links))))]))
