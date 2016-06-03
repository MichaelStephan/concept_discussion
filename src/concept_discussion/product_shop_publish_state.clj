(ns concept-discussion.product-shop-publish-state
  (:require [concept-discussion.link :as l]
            [concept-discussion.resource :as r]
            [concept-discussion.basic-resource :as br]))
;:product-shop-publish-state
(defn publish-product! [[product-id _ :as product] author]
  (assert product)
  (assert author)
  (let [state (br/create-or-update-product-shop-publish-state! {:state :published
                                                               :author author})]
    ; TODO does it make sense to return the link version for a specific resource operation as it may be useful in further processing
    (l/add-links! nil state :product product :link-version (l/count-links product-id))
    (l/add-links! nil product :product-shop-publish-state state)))
