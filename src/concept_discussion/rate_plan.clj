(ns concept-discussion.rate-plan
  (:require [concept-discussion.resource :as r]))

(defn create! [rate-plan]
  (r/create! :rate-plan rate-plan))
