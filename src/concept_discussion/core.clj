(ns concept-discussion.core
  (:gen-class))

(def resources_ {:product (atom {})
                 :shop-media (atom {})
                 :rate-plan (atom {})
                 :links (atom {})})

; fields can only be added and changed, not deleted
(defn create-or-update-resource! [type {:keys [id] :or {id (keyword (gensym (str (name type) "_"))) :create? true} :as resource}]
  (let [resources (type resources_)]
    (swap! resources update-in [id] conj (dissoc resource :id))
    [id (count (id @resources))]))

(defn get-versioned-resource [type id version]
  (assoc (apply merge (take version (reverse (id @(type resources_))))) :id id :version version))

; product repository
(def create-or-update-product! (partial create-or-update-resource! :product)) 
(def get-product (partial get-versioned-resource :product)) 

; media repository
(def create-shop-media! (partial create-or-update-resource! :shop-media))
(def get-shop-media (partial get-versioned-resource :shop-media))

; rate-plan repository
(def create-or-update-rate-plan! (partial create-or-update-resource! :rate-plan))
(def get-rate-plan (partial get-versioned-resource :rate-plan)) 

; link repository
(defn add-link! [src-id src-version type dst-id dst-version]
  (let [src-id (keyword src-id)
        {:keys [links]} resources_]
      (swap! links update-in [src-id] (fn [coll x]
                                        (let [last-src-version (-> coll first first)]
                                          (if (or (nil? last-src-version) (<= last-src-version src-version))
                                            (conj coll x)
                                            (throw (Exception. "version issue")))))
             [src-version type dst-id dst-version])))

(defn add-links! [src-id src-version & links]
  (loop [[type dst-id dst-version & links] links]
    (when type 
      (add-link! src-id src-version type dst-id dst-version)
      (recur links))))

(defn get-links [src-id src-version type ]
  (first (filter (fn [[cur-src-version cur-type]]
                   (and (= cur-type type)
                        (<= cur-src-version src-version)))
                 (src-id @(:links resources_)))))

; create rate plan lifecycle service
(defn create-rate-plan [{:keys [terms-and-conditions] :as rate-plan}]
  (assert terms-and-conditions) ; do business checks 
  (create-or-update-rate-plan! rate-plan))

; create subscription product lifecycle service
(defn create-subscription-product! [{:keys [product rate-plan shop-media]}]
  (assert product)
  (assert (:name product))
  (assert rate-plan)
  (let [[product-id product-version :as product] (create-or-update-product! product)
        [rate-plan-id rate-plan-version :as rate-plan] (create-rate-plan rate-plan)
        [shop-media-id shop-media-version :as shop-media] (create-shop-media! shop-media)
        links (add-links! product-id product-version
                          :rate-plan rate-plan-id rate-plan-version
                          :shop-media shop-media-id shop-media-version)]
    product))

; order repository

; create order lifecycle service



(comment
  (create-or-update-product! {:name "test2"})
  (create-or-update-product! {:id :product6118 :name "test2"}))

(comment
  (create-or-update-rate-plan! {:name "test"})
  (create-or-update-rate-plan! {:id :rateplan6507 :name "test2"}))

(comment
  (let [[product-id product-version :as product] (create-subscription-product! {:product {:id :pabc :name "persistence package"}
                                                                                :rate-plan {:id :rpabc :terms-and-conditions "Lorem ipsum dolor sit amet ..."}
                                                                                :shop-media {:id :smabc :thumbnail "http://blabla.png"}})
        [rate-plan-id rate-plan-version :as rate-plan] (create-or-update-rate-plan! {:id :rpabc :terms-and-conditions "22Lorem ipsum dolor sit amet ..."})]
    (add-links! product-id product-version
                :rate-plan rate-plan-id rate-plan-version)))

(comment
  (get-links :pabc 1 :rate-plan))

(comment
  (create-or-update-product! {:id :abc :name "aname"})
  (create-or-update-product! {:id :abc :description "adescription"})
  (create-or-update-product! {:id :abc :name "aname2"}))
