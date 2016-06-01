(ns concept-discussion.product
  (:require [concept-discussion.basic-resource :as r]))

(defn create-product! [{:keys [type name] :as product}]
  (assert type)
  (assert name)
  (r/create-or-update-product! (dissoc product :_id :_version)))
