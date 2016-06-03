(ns concept-discussion.product
  (:require [concept-discussion.resource :as r]))

(defn create! [{:keys [type name] :as product}]
  (assert type)
  (assert name)
  (r/create! :product product))
