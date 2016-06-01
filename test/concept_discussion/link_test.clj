(ns concept-discussion.link-test
  (:require [clojure.test :refer [deftest testing run-tests is use-fixtures]]
            [concept-discussion.link :as l]))

(defn init-fixture [f]
  (l/reset-links_)
  (f))

(use-fixtures :each init-fixture)

(deftest test-add-links! []
  (testing "add link"
    (l/add-links! 0 [:src-id 0]
                  :type-a [:dst-id1 0]
                  :type-a [:dst-id1 1]
                  :type-a [:dst-id2 0])
    (let [[links-version links] (l/get-links 3 [:src-id 0] :type-a)]
      (is (= links-version 3))
      (is (= links [0 :type-a [:dst-id2 0]])))))
