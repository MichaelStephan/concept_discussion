(ns concept-discussion.rate-plan
  (:require [concept-discussion.basic-resource :as r]))

(defn update-rate-plan! [{:keys [terms-and-conditions] :as rate-plan}]
  (assert terms-and-conditions)
  (r/create-or-update-rate-plan! rate-plan))

(defn create-rate-plan! [rate-plan]
  (update-rate-plan! (dissoc rate-plan :_id :_version)))
