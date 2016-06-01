(ns concept-discussion.resource-test
  (:require [clojure.test :refer [deftest testing run-tests is use-fixtures]]
            [concept-discussion.resource :as r]))

(defn init-fixture [f]
  (r/reset-resources_)
  (f))

(use-fixtures :each init-fixture)

(deftest test-create-or-update-resource! []
  (let [data-type :product data {:data "test"}]
    (testing "creating new resource"
      (let [[product-id product-version] (r/create-or-update-resource! data-type data)]
        (is (not (nil? product-id))) 
        (is (= product-version 1))
        (is (= (assoc data :_id product-id :_version product-version) (r/get-versioned-resource data-type product-id product-version)))))
    
    (testing "updating existing resource"
      (let [updated-data "test2"
            [product-id product-version] (r/create-or-update-resource! data-type data)
            product (r/get-versioned-resource data-type product-id product-version)]
        (r/create-or-update-resource! data-type (assoc product :data updated-data))
        (is (= {:_id product-id :_version 2 :data updated-data} (r/get-versioned-resource data-type product-id 2)))))
   
    (testing "updating existing resource with optimistic lock failure"
      (let [[product-id product-version] (r/create-or-update-resource! data-type data)]
        (is (thrown? RuntimeException (r/create-or-update-resource! data-type {:_id product-id :_version (inc product-version)})))))))
