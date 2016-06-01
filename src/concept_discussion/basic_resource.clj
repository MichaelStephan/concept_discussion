(ns concept-discussion.basic-resource
  (:require [concept-discussion.resource :as r]))

(def create-or-update-product! (partial r/create-or-update-resource! :product)) 
(def get-product (partial r/get-versioned-resource :product)) 

(def create-or-update-shop-media! (partial r/create-or-update-resource! :shop-media))
(def get-shop-media (partial r/get-versioned-resource :shop-media))

(def create-or-update-rate-plan! (partial r/create-or-update-resource! :rate-plan))
(def get-rate-plan (partial r/get-versioned-resource :rate-plan))

(def create-or-update-product-shop-publish-state! (partial r/create-or-update-resource! :product-shop-publish-state))
(def get-product-shop-publish-state (partial r/get-versioned-resource :product-shop-publish-state)) 
