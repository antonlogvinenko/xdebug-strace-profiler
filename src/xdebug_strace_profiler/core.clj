(ns xdebug-strace-profiler.core
  (:use [clojure.java.io :only (file reader)])
  (:use [clojure.core :only (read-line)])
  (:import [java.io File]))


(defn get-traces [file-path]
  (->> file-path file reader line-seq (drop 1) (drop-last 2)))


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
     :index index}))

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



(defn family-reunion [parent children]
  )

(defn create-xml-node-from-entry [entry]
  )

(defn render-xml-for-level [profile]
  )

(defn xml-profile [profile]
  (let [profile-levels (group-by :level profile)
        max-level (->> profile-levels keys (apply min))]
    
    profile))


(defn dump [x]
  (let [file "cake.txt"]
    (do
      (spit file "")
      (doseq [e x]
        (spit file e :append true)
        (spit file "\n\n" :append true)))))

  
(defn -main [file-path]
  (-> file-path get-traces parse-traces calculate-time xml-profile dump))




