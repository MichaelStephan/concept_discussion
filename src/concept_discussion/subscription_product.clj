(ns concept-discussion.subscription-product
  (:require [concept-discussion.link :as l]
            [concept-discussion.product-shop-publish-state :as ps]))

(defn create-subscription-product! [{:keys [product rate-plan shop-media]}]
  (assert product)
  (assert rate-plan)
  (assert shop-media)
  (let [product-shop-publish-state (ps/create-product-shop-publish-state!) 
        links (l/add-links! 0 product 
                            :product-shop-publish-state product-shop-publish-state
                            :rate-plan rate-plan
                            :shop-media shop-media)]
    product))
