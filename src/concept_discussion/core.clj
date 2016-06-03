(ns concept-discussion.core
  (:require [concept-discussion.product :as product]
            [concept-discussion.subscription-product :as subscription-product]
            [concept-discussion.rate-plan :as rate-plan]
            [concept-discussion.shop-media :as shop-media])
  (:gen-class))

(comment
  (try
    (let [product-ref (subscription-product/create! {:product {:name "hugo"}
                                                     :rate-plan-ref (rate-plan/create! {:terms-and-conditions "Lorem ipsum dolor sit amet ..."})
                                                     :shop-media-ref (shop-media/create! {:thumbnail "http://blabla.png"})})]
      
      )
    (catch Exception e
      (.printStackTrace e))))

(comment
  (l/get-links :pabc 1 :rate-plan))
