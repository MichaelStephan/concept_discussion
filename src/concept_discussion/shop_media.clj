(ns concept-discussion.shop-media
  (:require [concept-discussion.resource :as r]))

(defn create! [{:keys [thumbnail] :as shop-media}]
  (assert thumbnail)
  (r/create! :shop-media shop-media))
