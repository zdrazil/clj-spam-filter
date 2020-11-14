(ns spam-filter.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


(def max-ham-score 0.4)
(def min-spam-score 0.6)

(def word-feature {
                   :word "abc"
                   :spam-count 1
                   :ham-count 4
                   })

(defn make-word-feature
  [word]
  {
    :word word
    :spam-count 0
    :ham-count 0
    })

(make-word-feature "abc")

(-> word-feature :ham-count)

(def feature-db (atom { :abc word-feature}))

(defn clear-db
  []
  (reset! feature-db {}))

(defn inter-feature
  [db word]
  (or (get db word)
      (assoc db word (make-word-feature word)) 
      ))

(inter-feature {} :abc)

(defn extract-words [text]
  (distinct (re-seq #"[a-zA-Z]{3,}" text)))

(->> "abc cde fge" extract-words (reduce inter-feature {}) )  

; (defun train (text type)
;   (dolist (feature (extract-features text))
;     (increment-count feature type))
;   (increment-total-count type))

(defn extract-feature
  [db text]
  (->> text extract-words (reduce inter-feature db)))

(extract-feature {} "foo bar foo bar" )

(defn classification
  [score]
  (cond (<= score max-ham-score) "ham"
        (>= score min-spam-score) "spam"
        :else "unknown"))

(classification 5.0)
