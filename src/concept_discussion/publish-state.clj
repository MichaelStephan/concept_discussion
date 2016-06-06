(ns concept-discussion.publish-state
  (:require [concept-discussion.resource :as r]))

(defn publish! [[ref-type ref-id ref-version :as ref] author]
  (assert ref)
  (assert author)
  (let [publish-state-ref (r/update! [:publish-state [ref-type ref-id] nil] {:ref ref})]
    publish-state-ref))

(defn published-ref [[ref-type ref-id _ :as ref]]
  (:ref (r/get [:publish-state [ref-type ref-id] nil])))