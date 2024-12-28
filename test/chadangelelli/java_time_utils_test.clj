(ns chadangelelli.java-time-utils-test
  (:require [chadangelelli.java-time-utils :as tu]
            [clojure.test :as t :refer [is]])
  (:import [java.time LocalDateTime Instant LocalDate ZoneOffset]
           [java.time.temporal ChronoUnit]
           [java.util Date]))

;; TODO/COVERAGE:
;; =======================================================
;;
;; "x" = implemented, tested
;; "!" = has special condition (usually noted)
;;
;; 1.  [x] add-inst
;; 2.  [x] add-local-date-time
;; 3.  [x] chrono-unit
;; 4.  [x] date->inst
;; 5.  [x] first-day-of-month
;; 6.  [x] day-of-month
;; 7.  [x] day-of-week
;; 8.  [x] day-of-year
;; 9.  [x] hour
;; 17. [x] inst->date
;; 18. [x] inst->local-date-time
;; 19. [x] inst->timestamp
;; 20. [x] last-day-of-month
;; 20. [x] local-date-time->inst
;; 21. [x] local-date-time->timestamp
;; 10. [x] minute
;; 11. [x] month
;; 12. [x] month-value
;; 22. [x] now
;; 15. [x] second
;; 23. [x] str->inst
;; 24. [x] str->local-date-time
;; 25. [x] sub-inst
;; 26. [x] sub-local-date-time
;; 16. [x] year

(t/deftest add-inst
  (t/testing "java-time-utils.core/add-isnt"
    (let [i (tu/str->inst "2012-12-12T00:00:00Z")
          i+6years (tu/add-inst i 6 :years)
          i+6years+6months (tu/add-inst i+6years 6 :months)]
      (is (= 2012 (tu/year i)))
      (is (= 12 (tu/month-value i)))
      (is (= 12 (tu/day-of-month i)))
      (is (= 2018 (tu/year i+6years)))
      (is (= 6 (tu/month-value i+6years+6months)))
      (is (= 12 (tu/day-of-month i+6years)))
      (is (= 12 (tu/day-of-month i+6years+6months))))))

(t/deftest add-local-date-time
  (t/testing "java-time-utils.core/add-local-date-time"
    (let [d (tu/str->local-date-time "2001-01-01T01:01:01Z")
          d+1year (tu/add-local-date-time d 1 :year)
          d+1year+1month (tu/add-local-date-time d+1year 1 :month)
          d+1year+1month+1hour (tu/add-local-date-time d+1year+1month 1 :hour)]

      (is (= 2001 (tu/year d)))
      (is (= 1 (tu/month-value d)))
      (is (= 1 (tu/hour d)))

      (is (= 2002 (tu/year d+1year)))
      (is (= 1 (tu/month-value d+1year)))
      (is (= 1 (tu/hour d+1year)))

      (is (= 2002 (tu/year d+1year+1month)))
      (is (= 2 (tu/month-value d+1year+1month)))
      (is (= 1 (tu/hour d+1year+1month)))

      (is (= 2002 (tu/year d+1year+1month+1hour)))
      (is (= 2 (tu/month-value d+1year+1month+1hour)))
      (is (= 2 (tu/hour d+1year+1month+1hour))))))

(t/deftest chrono-unit
  (t/testing "java-time-utils.core/chrono-unit"
    (let [possibilites [[:days    ChronoUnit/DAYS]
                        [:day     ChronoUnit/DAYS]
                        [:decades ChronoUnit/DECADES]
                        [:decade  ChronoUnit/DECADES]
                        [:hours   ChronoUnit/HOURS]
                        [:hour    ChronoUnit/HOURS]
                        [:micros  ChronoUnit/MICROS]
                        [:micro   ChronoUnit/MICROS]
                        [:millis  ChronoUnit/MILLIS]
                        [:milli   ChronoUnit/MILLIS]
                        [:minutes ChronoUnit/MINUTES]
                        [:minute  ChronoUnit/MINUTES]
                        [:months  ChronoUnit/MONTHS]
                        [:month   ChronoUnit/MONTHS]
                        [:seconds ChronoUnit/SECONDS]
                        [:second  ChronoUnit/SECONDS]
                        [:weeks   ChronoUnit/WEEKS]
                        [:week    ChronoUnit/WEEKS]
                        [:years   ChronoUnit/YEARS]
                        [:year    ChronoUnit/YEARS]]]
      (doseq [[unit correct-response] possibilites]
        (is (= correct-response (tu/chrono-unit unit)))))))


(t/deftest get-day-of-month
  (t/testing "java-time-utils.core/day-of-month"
    (let [i1 (tu/str->inst "2012-12-12T12:12:12Z")
          i2 (tu/str->inst "2006-06-06T06:06:06Z")
          d1 (tu/str->local-date-time "2012-12-12T12:12:12Z")
          d2 (tu/str->local-date-time "2006-06-06T06:06:06Z")]
      (is (= 12 (tu/day-of-month i1)))
      (is (= 6 (tu/day-of-month i2)))
      (is (= 12 (tu/day-of-month d1)))
      (is (= 6 (tu/day-of-month d2))))))

(t/deftest get-day-of-week
  (t/testing "java-time-utils.core/day-of-week"
    (let [i1 (tu/str->inst "2012-12-12T12:12:12Z")
          i2 (tu/str->inst "2006-06-06T06:06:06Z")
          d1 (tu/str->local-date-time "2012-12-12T12:12:12Z")
          d2 (tu/str->local-date-time "2006-06-06T06:06:06Z")]
      (is (= "WEDNESDAY" (str (tu/day-of-week i1))))
      (is (= "TUESDAY" (str (tu/day-of-week i2))))
      (is (= "WEDNESDAY" (str (tu/day-of-week d1))))
      (is (= "TUESDAY" (str (tu/day-of-week d2)))))))

(t/deftest get-day-of-year
  (t/testing "java-time-utils.core/day-of-year"
    (let [i1 (tu/str->inst "2012-12-12T12:12:12Z")
          i2 (tu/str->inst "2006-06-06T06:06:06Z")
          d1 (tu/str->local-date-time "2012-12-12T12:12:12Z")
          d2 (tu/str->local-date-time "2006-06-06T06:06:06Z")]
      (is (= 347 (tu/day-of-year i1)))
     (is (= 157 (tu/day-of-year i2)))
     (is (= 347 (tu/day-of-year d1)))
     (is (= 157 (tu/day-of-year d2))))))

(t/deftest get-hour
  (t/testing "java-time-utils.core/hour"
    (let [i1 (tu/str->inst "2012-12-12T12:12:12Z")
          i2 (tu/str->inst "2006-06-06T06:06:06Z")
          d1 (tu/str->local-date-time "2012-12-12T12:12:12Z")
          d2 (tu/str->local-date-time "2006-06-06T06:06:06Z")]
      (is (= 12 (tu/hour i1)))
      (is (= 6 (tu/hour i2)))
      (is (= 12 (tu/hour d1)))
      (is (= 6 (tu/hour d2))))))

(t/deftest get-minute
  (t/testing "java-time-utils.core/minute"
    (let [i1 (tu/str->inst "2012-12-12T12:12:12Z")
          i2 (tu/str->inst "2006-06-06T06:06:06Z")
          d1 (tu/str->local-date-time "2012-12-12T12:12:12Z")
          d2 (tu/str->local-date-time "2006-06-06T06:06:06Z")]
      (is (= 12 (tu/minute i1)))
      (is (= 6 (tu/minute i2)))
      (is (= 12 (tu/minute d1)))
      (is (= 6 (tu/minute d2))))))

(t/deftest get-month
  (t/testing "java-time-utils.core/month"
    (let [i1 (tu/str->inst "2012-12-12T12:12:12Z")
          i2 (tu/str->inst "2006-06-06T06:06:06Z")
          d1 (tu/str->local-date-time "2012-12-12T12:12:12Z")
          d2 (tu/str->local-date-time "2006-06-06T06:06:06Z")]
      (is (= "DECEMBER" (str (tu/month i1))))
      (is (= "JUNE" (str (tu/month i2))))
      (is (= "DECEMBER" (str (tu/month d1))))
      (is (= "JUNE" (str (tu/month d2)))))))

(t/deftest get-month-value
  (t/testing "java-time-utils.core/month-value"
    (let [i1 (tu/str->inst "2012-12-12T12:12:12Z")
          i2 (tu/str->inst "2006-06-06T06:06:06Z")
          d1 (tu/str->local-date-time "2012-12-12T12:12:12Z")
          d2 (tu/str->local-date-time "2006-06-06T06:06:06Z")]
      (is (= 12 (tu/month-value i1)))
      (is (= 6 (tu/month-value i2)))
      (is (= 12 (tu/month-value d1)))
      (is (= 6 (tu/month-value d2))))))

(t/deftest get-second
  (t/testing "java-time-utils.core/second"
    (let [i1 (tu/str->inst "2012-12-12T12:12:12Z")
          i2 (tu/str->inst "2006-06-06T06:06:06Z")
          d1 (tu/str->local-date-time "2012-12-12T12:12:12Z")
          d2 (tu/str->local-date-time "2006-06-06T06:06:06Z")]
      (is (= 12 (tu/second i1)))
      (is (= 6 (tu/second i2)))
      (is (= 12 (tu/second d1)))
      (is (= 6 (tu/second d2))))))

(t/deftest get-year
  (t/testing "java-time-utils.core/year"
    (let [i1 (tu/str->inst "2012-12-12T12:12:12Z")
          i2 (tu/str->inst "2006-06-06T06:06:06Z")
          d1 (tu/str->local-date-time "2012-12-12T12:12:12Z")
          d2 (tu/str->local-date-time "2006-06-06T06:06:06Z")]
      (is (= 2012 (tu/year i1)))
      (is (= 2006 (tu/year i2)))
      (is (= 2012 (tu/year d1)))
      (is (= 2006 (tu/year d2))))))

(t/deftest inst->local-date-time
  (t/testing "java-time-utils.core/inst->local-date-time"
    (let [;; isntant
          i (tu/now)
          iy (tu/year i)
          im (tu/month-value i)
          id (tu/day-of-month i)
          ih (tu/hour i)
          it (tu/minute i)
          ic (tu/second i)
          ;; datetime
          d (tu/inst->local-date-time i)
          dy (tu/year d)
          dm (tu/month-value d)
          dd (tu/day-of-month d)
          dh (tu/hour d)
          dt (tu/minute d)
          dc (tu/second d)]
      (is (inst? i))
      (is (instance? Instant i))
      (is (instance? LocalDateTime d))
      (is (= iy dy))
      (is (= im dm))
      (is (= id dd))
      (is (= ih dh))
      (is (= it dt))
      (is (= ic dc)))))

(t/deftest local-date-time->inst
  (t/testing "java-time-utils.core/local-date-time->inst"
    (let [;; datetime
          d (tu/str->local-date-time "2012-12-12T12:12:12Z")
          dy (tu/year d)
          dm (tu/month-value d)
          dd (tu/day-of-month d)
          dh (tu/hour d)
          dt (tu/minute d)
          dc (tu/second d)
          ;; isntant
          i (tu/local-date-time->inst d)
          iy (tu/year i)
          im (tu/month-value i)
          id (tu/day-of-month i)
          ih (tu/hour i)
          it (tu/minute i)
          ic (tu/second i)]
      (is (inst? i))
      (is (instance? Instant i))
      (is (instance? LocalDateTime d))
      (is (= iy dy))
      (is (= im dm))
      (is (= id dd))
      (is (= ih dh))
      (is (= it dt))
      (is (= ic dc)))))

(t/deftest now
  (t/testing "java-time-utils.core/now"
    (is (inst? (tu/now)))
    (is (= (LocalDate/ofInstant (Instant/now) ZoneOffset/UTC)
           (LocalDate/ofInstant (tu/now) ZoneOffset/UTC)))))

(t/deftest str->inst
  (t/testing "java-time-utils.core/str->inst"
    (let [s "2012-12-12T12:12:12Z"
          i (tu/str->inst s)
          p (Instant/parse s)]
      (is (= p i)))))

(t/deftest str->local-date-time
  (t/testing "java-time-utils.core/str->local-date-time"
    (let [d (tu/str->local-date-time "2012-12-12T12:12:12Z")]
      (is (instance? LocalDateTime d))
      (is (= 2012 (tu/year d)))
      (is (= 12 (tu/minute d))))))

(t/deftest sub-inst
  (t/testing "java-time-utils.core/sub-inst"
    (let [i (tu/str->inst "2012-12-12T00:00:00Z")
          i-6years (tu/sub-inst i 6 :years)
          i-6years-6months (tu/sub-inst i-6years 6 :months)]
      (is (= 2012 (tu/year i)))
      (is (= 12 (tu/month-value i)))
      (is (= 12 (tu/day-of-month i)))
      (is (= 2006 (tu/year i-6years)))
      (is (= 6 (tu/month-value i-6years-6months)))
      (is (= 12 (tu/day-of-month i-6years)))
      (is (= 12 (tu/day-of-month i-6years-6months))))))

(t/deftest sub-local-date-time
  (t/testing "java-time-utils.core/sub-local-date-time"
    (let [d (tu/str->local-date-time "2012-12-12T00:00:00Z")
          d-6years (tu/sub-local-date-time d 6 :years)
          d-6years-6months (tu/sub-local-date-time d-6years 6 :months)]
      (is (= 2012 (tu/year d)))
      (is (= 12 (tu/month-value d)))
      (is (= 12 (tu/day-of-month d)))
      (is (= 2006 (tu/year d-6years)))
      (is (= 6 (tu/month-value d-6years-6months)))
      (is (= 12 (tu/day-of-month d-6years)))
      (is (= 12 (tu/day-of-month d-6years-6months))))))

(t/deftest inst->timestamp
  (t/testing "java-time-utils.core/inst->timestamp"
    (let [i (tu/str->inst "2012-12-12T00:00:00Z")
          t (tu/inst->timestamp i)]
      (is (= 1355270400000 t)))))

(t/deftest local-date-time->timestamp
  (t/testing "java-time-utils.core/local-date-time->timestamp"
    (let [d (tu/str->local-date-time "2012-12-12T00:00:00Z")
          t (tu/local-date-time->timestamp d)]
      (is (= 1355270400000 t)))))

(t/deftest first-day-of-month
  (t/testing "java-time-utils.core/first-day-of-month"
    (let [i (tu/str->inst "2012-12-12T00:00:00Z")
          d (tu/str->local-date-time "2012-12-12T00:00:00Z")
          a (tu/first-day-of-month i)
          b (tu/first-day-of-month d)]
      (is (instance? Instant a))
      (is (= "2012-12-01T00:00:00Z" (str a)))
      (is (instance? LocalDateTime b))
      (is (= "2012-12-01T00:00" (str b))))))

(t/deftest last-day-of-month
  (t/testing "java-time-utils.core/last-day-of-month"
    (let [i (tu/str->inst "2012-12-12T00:00:00Z")
          d (tu/str->local-date-time "2012-12-12T00:00:00Z")
          a (tu/first-day-of-month i)
          b (tu/first-day-of-month d)]
      (is (instance? Instant a))
      (is (= "2012-12-01T00:00:00Z" (str a)))
      (is (instance? LocalDateTime b))
      (is (= "2012-12-01T00:00" (str b))))))

(t/deftest inst->date
  (t/testing "java-time-utils.core/inst->date"
    (let [i (tu/str->inst "2012-12-12T00:00:00Z")
          d (tu/inst->date i)]
      (is (instance? Date d))
      (is (= "Tue Dec 11 18:00:00 CST 2012" (str d))))))

(t/deftest date->inst
  (t/testing "java-time-utils.core/date->inst"
    (let [d (tu/inst->date (tu/str->inst "2012-12-12T00:00:00Z"))
          i (tu/date->inst d)]
      (is (inst? i))
      (is (= "2012-12-12T00:00:00Z" (str i))))))

(comment "TEST STUB"
#_:clj-kondo/ignore
(t/deftest xx
  (t/testing "java-time-utils.core/xx"
    (let []
      )))
)
