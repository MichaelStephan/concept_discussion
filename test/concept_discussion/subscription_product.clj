(ns concept-discussion.subscription-product
  (:require [clojure.test :refer [deftest testing run-tests is use-fixtures]]
            [concept-discussion.subscription-product :as subscription-product]
            [concept-discussion.rate-plan :as rate-plan]
            [concept-discussion.shop-media :as shop-media]))

(defn init-fixture [f]
  (reset! r/resources_ {})
  (f))

(use-fixtures :each init-fixture)

(deftest test-resource []
  (testing "creating new subscription product"
           (let [[ref-type ref-id ref-version] (subscription-product/create! {:product {:name "some-name"}
                                                                              :rate-plan-ref (rate-plan/create! {:terms-and-conditions "some-terms-and-conditions"})
                                                                              :shop-media-ref (shop-media/create! {:thumbnail "some-shop-media"})})]
             
             )))
                                  
