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
  ; TODO improve test case with loop
  (testing "update existing resource 2 times and get values"
           (let [data1 {:name "some-name1"}
                 data2 {:name "some-name2"}
                 data3 {:name "some-name3"}
                 [ref-type ref-id ref-version :as ref]
                 (->
                   (r/create! :product data1)
                   (r/update! data2)
                   (r/update! data3))]
             (is (= ref-version 3))
             (is (= (r/versions ref) 3))
             (is (= (select-keys (r/get [ref-type ref-id 2]) [:name]) data2))
             (let [ref [ref-type ref-id 1]]
               (is (= (assoc data1 :_ref ref) (r/get ref))))
             (let [ref [ref-type ref-id 2]]
               (is (= (assoc data2 :_ref ref) (r/get ref))))
             (let [ref [ref-type ref-id 3]]
               (is (= (assoc data3 :_ref ref) (r/get ref))))))
  (testing "delete fields from existing resource"
           (let [data {:name "some-name" :another-name "some-other-name"}
                 [ref-type ref-id ref-version :as ref] (r/create! :product data)]
             (r/delete! ref :name)
             (is (= data (dissoc (r/get [ref-type ref-id 1]) :_ref)))
             (is (= (merge data {:name nil}) (dissoc (r/get [ref-type ref-id 2]) :_ref))))))