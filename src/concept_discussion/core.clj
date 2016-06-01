(ns concept-discussion.core
  (:require [concept-discussion.product :as cp]
            [concept-discussion.subscription-product :as sp]
            [concept-discussion.rate-plan :as rp]
            [concept-discussion.shop-media :as sm]
            [concept-discussion.link :as l]
            [concept-discussion.product-shop-publish-state :as ps])
  (:gen-class))

(comment
  (try
    (let [rate-plan-ref (rp/create-rate-plan! {:terms-and-conditions "Lorem ipsum dolor sit amet ..."})
          product-ref (sp/create-subscription-product! {:product (cp/create-product! {:type :subscription-product :name "persistence package"})
                                                        :rate-plan rate-plan-ref
                                                        :shop-media (sm/create-shop-media! {:thumbnail "http://blabla.png"})})]
      (sp/update-rate-plan! product-ref {:_ref rate-plan-ref :terms-and-conditions "22Lorem ipsum dolor sit amet ..."}) 
      (ps/publish-product! product-ref "michael stephan"))
    (catch Exception e
      (.printStackTrace e))))

(comment
  (l/get-links :pabc 1 :rate-plan))
