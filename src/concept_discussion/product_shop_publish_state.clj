(ns concept-discussion.product-shop-publish-state
  (:require [concept-discussion.basic-resource :as r]))

(defn create-product-shop-publish-state! []
  (r/create-or-update-product-shop-publish-state! {:state :unpublished}))
