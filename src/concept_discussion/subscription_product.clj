(ns concept-discussion.subscription-product
  (:require [concept-discussion.link :as l]
            [concept-discussion.product-shop-publish-state :as ps]
            [concept-discussion.rate-plan :as rp]
            ))

(defn create-subscription-product! [{:keys [product rate-plan shop-media]}]
  (assert product)
  (assert rate-plan)
  (assert shop-media)
  (let [links (l/add-links! 0 product 
                            :rate-plan rate-plan
                            :shop-media shop-media)]
    product))

(defn update-rate-plan! [product rate-plan]
  (->>
    (rp/update-rate-plan! rate-plan)
    (l/add-links! nil product :rate-plan)))
