(ns concept-discussion.resource
  (:use [slingshot.slingshot :only [throw+]]))

(def resources_ (atom {}))

(defn reset-resources_ []
  (reset! resources_ {}))

(defn create-or-update-resource! "In case _id is set the associated resource is updated. In case _id is nil or no resource identified by _id exists the resource is created. Inserts or updates are only successful in case _version is nil or the version of the existing resource matches _version. In case of success the vector [id version] is returned. On error an exception is thrown"
  [type {:keys [_ref _id _version] :or {_id (keyword (gensym (str (name type) "_")))} :as resource}]
  (if (nil? _ref)
    (let [rs-key [type _id]
          rs (get-in @resources_ rs-key {})
          rs-count (count rs)]
      (if (or (nil? _version)
              (= rs-count _version))
        [_id (count (get-in (swap! resources_ update-in rs-key conj (dissoc resource :_id :_version)) rs-key))]
        (throw+ {:type :concurrency-exception :hint resource})))
    (create-or-update-resource! type (dissoc (assoc resource :_id (first _ref) :_version (second _ref)) :_ref))))

(defn get-versioned-resource [type id version]
  (assoc (apply merge (take version (reverse (get-in @resources_ [type id])))) :_ref [id version]))

(defn get-versions [type id]
  (->
    (get-in @resources_ [type id])
    count))
