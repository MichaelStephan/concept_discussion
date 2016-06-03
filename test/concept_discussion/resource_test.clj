(ns concept-discussion.resource-test
  (:require [clojure.test :refer [deftest testing run-tests is use-fixtures]]
            [concept-discussion.resource :as r]))

(defn init-fixture [f]
  (reset! r/resources_ {})
  (f))

(use-fixtures :each init-fixture)

(deftest test-resource []
  (testing "creating new resource"
           (let [[ref-type ref-id ref-version] (r/create! :product {:name "some-name"})]
                 (is (= ref-type :product))
                 (is (keyword ref-id))
                 (is (= ref-version 1))))
  (testing "create new resource with update, using _ref"
           (let [ref [:product :some-id1 1]
                 new-ref (r/update! {:_ref ref :name "some-name"})]
                 (is (= new-ref ref))))
  (testing "create new resource with update, without _ref"
           (let [ref [:product :some-id2 1]
                 new-ref (r/update! ref {:name "some-name"})]
                 (is (= new-ref ref))))
  (testing "update existing resource 2 times"
           (let [[ref-type ref-id ref-version :as ref] (->
                       (r/create! :product {:name "some-name1"})
                       (r/update! {:name "some-name2"})
                       (r/update! {:name "some-name3"}))]
             (is (= ref-version 3))
             (is (= (r/versions ref) 3))
             (is (= (select-keys (r/get [ref-type ref-id 2]) [:name]) {:name "some-name2"})))))
                                  
