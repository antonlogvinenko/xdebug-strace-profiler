(ns xdebug-strace-profiler.test.core
  (:use [xdebug-strace-profiler.core])
  (:use [clojure.test]))

(deftest get-trace-position-test
  (are [list value] (= (get-trace-position list) value)
       ["a" "b" "c" "d" "e" "f"] "e"
       ["a" "b"] "xdebug_stop_trace()"))

(deftest get-trace-time-test
  (is (= (get-trace-time ["0.002344"]) 0.002344)))

(deftest get-trace-level-test
  (are [line level] (= (get-trace-level line) level)
       "    234234234   -> asdasdasd 34242" 7
       "     23424       -> aaasdasd 234234" 12))

(deftest get-trace-call-test
  (are [trace call] (= (get-trace-call trace) call)
       [1 4 5 6 7] 6
       [1 3] "end"))

(deftest split-trace-test
  (are [line trace] (= (split-trace line) trace)
       "      cake cakes       ->   black   white  green    yellow "
       ["cake" "cakes" "->" "black" "white" "green" "yellow"]))


       

