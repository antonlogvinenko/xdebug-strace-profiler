(ns xdebug-strace-profiler.core
  (:use [clojure.java.io :only (file reader)]
        [clojure.core :only (read-line)]
        [clojure.contrib.prxml :only (prxml)]
        [clojure.contrib.duck-streams :only (with-out-writer)]
        [clojure.contrib.math :only (round)])
  (:import [java.io File]
           [java.text DecimalFormat]))


(defn get-traces [file-path]
  (->> file-path file reader line-seq (drop 1) (drop-last 3)))


(defn get-trace-position [trace] (nth trace 4 "xdebug_stop_trace()"))
(defn get-trace-time [trace] (-> trace (nth 0) Double/parseDouble))
(defn get-trace-level [trace]
  (let [part-1 (-> trace (.split "->") first)
        part-1-clean (.trim part-1)]
    (- (count part-1) (count part-1-clean))))
(defn get-trace-call [trace] (nth trace 3 "end"))

(defn split-trace [trace]
  (-> trace (.replaceAll " +" " ") .trim (.split " ") seq))

(defn parse-trace [index trace]
  (let [trace-split (split-trace trace)]
    {:time-start (get-trace-time trace-split)
     :call (get-trace-call trace-split)
     :level (get-trace-level trace)
     :position (get-trace-position trace-split)
     :index index
     :children []}))

(defn parse-traces [traces]
  (map-indexed parse-trace traces))



(defn time-search [profile]
  (fn [index element]
    (let [my-level (:level element)
          my-index (:index element)
          start-time (:time-start element)
          filtered (->> profile
                        (drop (inc index))
                        (filter #(-> % :level (<= my-level)))
                        (take 1))
          real-filtered (nth filtered 0 {:time-start start-time :index my-index})
          time-new (real-filtered :time-start)
          next-index (real-filtered :index)]
    (assoc element :time-spent (- time-new start-time) :next-index next-index))))

(defn calculate-time [profile]
  (map-indexed (time-search profile) profile))



(defn find-nodes [profile level start end]
  (->> level
       profile
       (filter (fn [x] (let [l (:index x)] (and (< l end) (> l start)))))))

(defn render-node-with-children [node children]
  (let [f (DecimalFormat. "#.####")]
    (assoc node :children children)
    [:call
     {:name (node :call)
      :time (->> :time-spent node (.format f))
      :where (node :position)}
     children]))

(defn render-node [profile]
  (fn [node]
    (let [level (node :level)
          children-level (+ level 2)
          start-index (node :index)
          end-index (node :next-index)
          children (find-nodes profile children-level start-index end-index)
          rendered-children (map (render-node profile) children)]
      (render-node-with-children node rendered-children))))

(defn put-to-document [top-level]
  [:profile top-level])

(defn xml-profile [profile]
  (let [profile-levels (group-by :level profile)
        min-level (->> profile-levels keys (apply min))
        render-level (get profile-levels min-level)
        rendered-level (map (render-node profile-levels) render-level)]
    (put-to-document rendered-level)))



(defn to-xml [profile]
  (let [wr (java.io.StringWriter.)]
    (-> wr
        (with-out-writer (prxml profile) wr)
        .toString)))



(defn -main [file-path]
  (->> file-path get-traces parse-traces calculate-time xml-profile to-xml (spit "cake.xml")))

