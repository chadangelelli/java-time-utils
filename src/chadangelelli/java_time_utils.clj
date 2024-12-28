(ns chadangelelli.java-time-utils
  "Deals with dates and time by wrapping common `java.time` libraries."
  {:added "5.0"
   :author "Chad Angelelli"}
  (:refer-clojure :exclude [second])
  (:import [java.time Instant LocalDateTime ZoneOffset]
           [java.time.temporal ChronoUnit TemporalAdjusters ]
           [java.util Date]))

(defn now
  "Returns a `java.time.Instant` at present in UTC.

  Examples:

  ```clojure
  (require '[chadangelelli.java-time-utils :as tu])

  (tu/now)
  ;= #object[java.time.Instant 0x776e1ae5 \"2024-11-29T03:32:48.798592Z\"]
  ```"
  []
  (Instant/now))

(defn inst->local-date-time
  "Returns a `java.time.LocalDateTime` for instant. `zone-offset` defaults to
  `java.time.ZoneOffset/UTC`.

  Examples:

  ```clojure
  (require '[chadangelelli.java-time-utils :as tu])

  (tu/inst->local-date-time (tu/now))
  ;= #object[java.time.LocalDateTime 0x933275d \"2024-11-29T03:45:02.302190\"]
  ```"
  [instant & [zone-offset]]
  (let [zone-offset (or zone-offset ZoneOffset/UTC)]
    (LocalDateTime/ofInstant instant zone-offset)))

(defn local-date-time->inst
  [local-date-time & [zone-offset]]
  (let [zone-offset (or zone-offset ZoneOffset/UTC)]
    (.toInstant local-date-time zone-offset)))

(defn chrono-unit
  "Returns `java.time.temporal.ChronoUnit/UNIT` for shorthand `unit`. All units
  except `:millennia` and `:forever` can be provided in singular or plural form
  - this is just for readability and has no effect on the value returned.

  | Unit                    |
  | ----------------------- |
  | `:day, :days`           |
  | `:decade, :decades`     |
  | `:hour, :hours`         |
  | `:micro, :micros`       |
  | `:milli, :millis`       |
  | `:minute, :minutes`     |
  | `:month, :months`       |
  | `:second, :seconds`     |
  | `:week, :weeks`         |
  | `:year, :years`         |

  Examples:

  ```clojure
  (require '[chadangelelli.java-time-utils :as tu])

  (:TODO :add-example)

  ```"
  [unit]
  (case unit
   (:days    :day)    ChronoUnit/DAYS
   (:decades :decade) ChronoUnit/DECADES
   (:hours   :hour)   ChronoUnit/HOURS
   (:micros  :micro)  ChronoUnit/MICROS
   (:millis  :milli)  ChronoUnit/MILLIS
   (:minutes :minute) ChronoUnit/MINUTES
   (:months  :month)  ChronoUnit/MONTHS
   (:seconds :second) ChronoUnit/SECONDS
   (:weeks   :week)   ChronoUnit/WEEKS
   (:years   :year)   ChronoUnit/YEARS))

(defn add-local-date-time
  "Returns `java.time.LocalDateTime` after adding `n` `unit`'s to it.

  Examples:

  ```clojure
  ```

  See also:

  - `java-time-utils.core/chrono-unit`"
  [ldt n unit]
  (.plus ldt n (chrono-unit unit)))

(defn sub-local-date-time
  "Returns `java.time.LocalDateTime` after subtracting `n` `unit`'s to it.

  Examples:

  ```clojure
  ```

  See also:

  - `java-time-utils.core/chrono-unit`"
  [ldt n unit]
  (.minus ldt n (chrono-unit unit)))

(defn add-inst
  "Returns `java.time.Instant` after adding `n` `unit`'s to it.

  Examples:

  ```clojure
  ```

  See also:

  - `java-time-utils.core/chrono-unit`"
  [instant n unit]
  (-> (inst->local-date-time instant)
      (add-local-date-time n unit)
      (.toInstant ZoneOffset/UTC)))

(defn sub-inst
  "Returns `java.time.Instant` after subtracting `n` `unit`'s to it.

  Examples:

  ```clojure
  ```

  See also:

  - `java-time-utils.core/chrono-unit`"
  [instant n unit]
  (-> (inst->local-date-time instant)
      (sub-local-date-time n unit)
      (.toInstant ZoneOffset/UTC)))

(defn- -get-temporal-field
  [field x]
  (let [ldt (if (inst? x)
              (inst->local-date-time x)
              x)]
    (case field
      :year         (.getYear ldt)
      :month        (.getMonth ldt)
      :month-value  (.getMonthValue ldt)
      :day-of-month (.getDayOfMonth ldt)
      :day-of-week  (.getDayOfWeek ldt)
      :day-of-year  (.getDayOfYear ldt)
      :hour         (.getHour ldt)
      :minute       (.getMinute ldt)
      :second       (.getSecond ldt))))

(defn year
  [instant-or-local]
  (-get-temporal-field :year instant-or-local))

(defn month
  [instant-or-local]
  (-get-temporal-field :month instant-or-local))

(defn month-value
  [instant-or-local]
  (-get-temporal-field :month-value instant-or-local))

(defn day-of-month
  [instant-or-local]
  (-get-temporal-field :day-of-month instant-or-local))

(defn day-of-week
  [instant-or-local]
  (-get-temporal-field :day-of-week instant-or-local))

(defn day-of-year
  [instant-or-local]
  (-get-temporal-field :day-of-year instant-or-local))

(defn hour
  [instant-or-local]
  (-get-temporal-field :hour instant-or-local))

(defn minute
  [instant-or-local]
  (-get-temporal-field :minute instant-or-local))

(defn second
  [instant-or-local]
  (-get-temporal-field :second instant-or-local))

(defn str->inst
  "Returns a `java.time.Instant` after parsing string `s`.

  Examples:

  ```clojure
  (require '[chadangelelli.java-time-utils :as tu])

  (tu/str->inst \"2024-11-29T03:32:48.798592Z\")
  ;= #object[java.time.Instant 0x41694e7a \"2024-11-29T03:32:48.798592Z\"]
  ```"
  [s]
  (Instant/parse s))

(defn str->local-date-time
  [s]
  (-> s str->inst inst->local-date-time))

(defn inst->timestamp
  [instant]
  (.toEpochMilli instant))

(defn local-date-time->timestamp
  [local-date-time]
  (.toEpochMilli (local-date-time->inst local-date-time)))

(defn first-day-of-month
  "Takes an `Instant` or `LocalDateTime` and returns the date at the first day
  of the month. This function will return the same type it was given (`Instant`
  for `Instant`, `LocalDateTime` for `LocalDateTime`).

  Examples:

  ```clojure
  (require '[chadangelelli.java-time-utils :as tu])

  (tu/first-day-of-month (tu/now))
  ;= #inst \"2024-12-01T18:36:20.345286000-00:00\"

  (tu/first-day-of-month (tu/inst->local-date-time (tu/now)))
  ;= #object[java.time.LocalDateTime 0x4694dfd \"2024-12-01T18:36:37.339018\"]
  ```

  See also:

  - `last-day-of-month`"
  [instant-or-local]
  (let [x instant-or-local
        is-inst? (inst? x)
        ldt (if is-inst?
              (inst->local-date-time x)
              x)
        r (.with ldt (TemporalAdjusters/firstDayOfMonth))]
    (if is-inst?
      (local-date-time->inst r)
      r)))

(defn last-day-of-month
  "Takes an `Instant` or `LocalDateTime` and returns the date at the last day
  of the month. This function will return the same type it was given (`Instant`
  for `Instant`, `LocalDateTime` for `LocalDateTime`).

  Examples:

  ```clojure
  (require '[chadangelelli.java-time-utils :as tu])

  (tu/last-day-of-month (tu/now))
  ;= #inst \"2024-12-31T18:38:19.809356000-00:00\"

  (tu/last-day-of-month (tu/inst->local-date-time (tu/now)))
  ;= #object[java.time.LocalDateTime 0x5a78e7e3 \"2024-12-31T18:38:29.268378\"]
  ```

  See also:

  - `first-day-of-month`"
  [instant-or-local]
  (let [x instant-or-local
        is-inst? (inst? x)
        ldt (if is-inst?
              (inst->local-date-time x)
              x)
        r (.with ldt (TemporalAdjusters/lastDayOfMonth))]
    (if is-inst?
      (local-date-time->inst r)
      r)))

(defn inst->date
  "Returns a `java.util.Date` for an `Instant`.

  Examples:

  ```clojure
  (require '[chadangelelli.java-time-utils :as tu])

  (tu/inst->date (ut/now))
  ;= #inst \"2024-12-28T16:45:48.560-00:00\"
  ```

  See also:

  - `date->inst`"
  [instant]
  (Date/from instant))

(defn date->inst
  "Returns an `Instant` for a `java.util.Date`.

  Examples:

  ```clojure
  (require '[chadangelelli.java-time-utils :as tu])
  (import '[java.util Date])

  (tu/date->inst (Date.))
  ;= #object[java.time.Instant 0x2ab39942 \"2024-12-28T16:46:41.608Z\"]
  ```

  See also:

  - `inst->date`"
  [date]
  (.toInstant date))
