(ns concept-discussion.subscription-product
  (:require [concept-discussion.resource :as r]
            [concept-discussion.product :as product]))

(defn create! [{:keys [product rate-plan-ref shop-media-ref]}]
  (assert product)
  (assert rate-plan-ref)
  (assert shop-media-ref)
  (let [product-ref (product/create! (merge product {:type :subscription-product}))]
    (r/update! product-ref {:rate-plan rate-plan-ref
                            :shop-media shop-media-ref})))