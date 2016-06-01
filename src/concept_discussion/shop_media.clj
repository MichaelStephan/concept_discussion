(ns concept-discussion.shop-media
  (:require [concept-discussion.basic-resource :as r]))

(defn create-shop-media! [{:keys [thumbnail] :as shop-media}]
  (assert thumbnail)
  (r/create-or-update-shop-media! (dissoc shop-media :_id :_version)))
