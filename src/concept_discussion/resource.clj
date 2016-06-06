(ns concept-discussion.resource
  (:refer-clojure :exclude [get])
  (:use [slingshot.slingshot :only [throw+]]))

(def resources_ (atom {}))

(defn update!
  ([[ref-type ref-id ref-version] new-resource & other-resources]
    (when (or (nil? ref-type) (nil? ref-id))
      (throw+ {:type :invalid-input :msg "missing _ref, type or id"}))
    (when (not (coll? new-resource))
      (throw+ {:type :invalid-input :msg "new-resource is not a map"}))
    (let [new-resources (concat [new-resource] other-resources)]
      (when (or (nil? new-resources) (not (coll? new-resources)))
        (throw+ {:type :invalid-input :msg "missing new-resources or it is no collection" :hint new-resources}))
      [ref-type ref-id (->
                         (swap! resources_ update-in [ref-type ref-id] (fn [x y]
                                                                         (if (or (nil? ref-version) (>= ref-version (count x)))
                                                                           (concat x  y)
                                                                           (throw+ {:type :concurrency-exception :hint new-resources}))) new-resources)
                         (get-in [ref-type ref-id])
                         (count))]))
    ([{:keys [_ref] :as new-resource}]
      (update! _ref (dissoc new-resource :_ref))))
  
(defn create! [ref-type new-resource]
  (when (nil? ref-type)
    (throw+ {:type :invalid-input :msg "missing ref-type"}))
  (when (not (map? new-resource))
    (throw+ {:type :invalid-input :msg "new-resource is not a map"}))
  (update! (assoc new-resource :_ref [ref-type (keyword (gensym (str (name ref-type) "_")))])))

(defn delete! [ref & fields]
  (when (nil? ref)
    (throw+ {:type :invalid-input :msg "missing ref"}))
  (when-not (reduce #(and %1 %2) (map #(or (keyword? %1) (coll? %1)) fields))
     (throw+ {:type :invalid-input :msg "invalid fields"}))
  (->>
    (loop [[field & fields] fields data {}]
      (if field  
        (let [field (if (keyword? field) [field] field)]
          (recur fields (assoc-in data field nil)))
        data))
    (update! ref)))

(defn get [[_ _ ref-version :as ref]]
  (when (or (nil? ref) (nil? ref-version))
    (throw+ {:type :invalid-input :msg "missing ref or ref-version"}))
  (let [ref (take 2 ref)
        versions (take ref-version (get-in @resources_ ref))]
    (assoc (apply merge versions) :_ref (concat ref [(count versions)]))))

(defn versions [ref]
 (when (nil? ref)
   (throw+ {:type :invalid-input :msg "missing ref"}))
  (->
    (get-in @resources_ (take 2 ref))
    (count)))