(ns ngom-product.core
  (:gen-class))

; product repository
(def products_ (atom {}))
(defn create-or-update-product! [{:keys [id] :or {id (keyword (gensym "product_")) :create? true} :as product}]
  ; fields can only be added and changed, not deleted
  (swap! products_ update-in [id] conj product)
  (assoc product :id id))

(defn get-product [id version]
  (apply merge (take version (reverse (id @products_)))))

; media repository
(def shop-media_ (atom {}))
(defn create-shop-media! [shop-media]
  (let [id (keyword (gensym "shop-media_"))]
    (swap! shop-media_ assoc id shop-media)
    (assoc shop-media :id id)))

; rate-plan repository
(def rate-plans_ (atom {}))
(defn create-or-update-rate-plan! [{:keys [id] :or {id (keyword (gensym "rateplan_")) :create? true} :as rate-plan}]
  (swap! rate-plans_ assoc id (assoc rate-plan :id id))
  (assoc rate-plan :id id))

; link repository
(def links_ (atom #{}))
(defn add-links! [& links]
  (swap! links_ conj (partition 3 links)))

; create rate plan lifecycle service
(defn create-rate-plan [{:keys [terms-and-conditions] :as rate-plan}]
  (assert terms-and-conditions) ; do business checks 
  (create-or-update-rate-plan! rate-plan))

; create subscription product lifecycle service
(defn create-subscription-product [{:keys [product rate-plan shop-media]}]
  (assert product)
  (assert (:name product))
  (assert rate-plan)
  (let [{product-id :id :as product} (create-or-update-product! product)
        {rate-plan-id :id :as rate-plan} (create-rate-plan rate-plan)
        {shop-media-id :id :as shop-media} (create-shop-media! shop-media)
        links (add-links! product-id :rate-plan rate-plan-id
                          product-id :shop-media shop-media-id)]
    ))

; order repository

; create order lifecycle service



(comment
  (create-or-update-product! {:name "test2"})
  (create-or-update-product! {:id :product6118 :name "test2"})
  (println @products_))

(comment
  (create-or-update-rate-plan! {:name "test"})
  (create-or-update-rate-plan! {:id :rateplan6507 :name "test2"})
  (println @rate-plans_))

(comment
  (create-subscription-product {:product {:name "persistence package"}
                                :rate-plan {:terms-and-conditions "Lorem ipsum dolor sit amet ..."}
                                :shop-media {:thumbnail "http://blabla.png"}}) 
  (println @links_))

(comment
  (create-or-update-product! {:id :abc :name "aname"})
  (create-or-update-product! {:id :abc :description "adescription"})
  (create-or-update-product! {:id :abc :name "aname2"}))
