(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]
            [deps-deploy.deps-deploy :as dd]))

(def lib 'com.github.chadangelelli/java-time-utils)
(def class-dir "target/classes")
(def major-minor-version (System/getenv "JAVA_TIME_UTILS_BUILD_VERSION"))
(def version (format "%s.%s" major-minor-version (b/git-count-revs nil)))

(def RED  "\033[0;31m")
(def NC   "\033[0m")

(defn check-version
  [& _]
  (when-not (seq major-minor-version)
    (throw (Exception. (str RED
                            "ERROR: Could not find "
                            "JAVA_TIME_UTILS_BUILD_VERSION in environment"
                            NC)))))

(defn test "Run all the tests." [opts]
  (let [basis    (b/create-basis {:aliases [:test]})
        cmds     (b/java-command
                  {:basis      basis
                    :main      'clojure.main
                    :main-args ["-m" "cognitect.test-runner"]})
        {:keys [exit]} (b/process cmds)]
    (when-not (zero? exit) (throw (ex-info "Tests failed" {}))))
  opts)

(defn- pom-template [version]
  (check-version)
  [[:description "Java Time utils for Clojure"]
   [:url "https://github.com/chadangelelli/java-time-utils"]
   [:licenses
    [:license
     [:name "Eclipse Public License"]
     [:url "http://www.eclipse.org/legal/epl-v10.html"]]]
   [:developers
    [:developer
     [:name "chadangelelli"]]]
   [:scm
    [:url "https://github.com/chadangelelli/java-time-utils"]
    [:connection "scm:git:https://github.com/chadangelelli/java-time-utils.git"]
    [:developerConnection "scm:git:ssh:git@github.com:chadangelelli/java-time-utils.git"]
    [:tag (str "v" version)]]])

(defn- jar-opts [opts]
  (assoc opts
          :lib lib   :version version
          :jar-file  (format "target/%s-%s.jar" lib version)
          :basis     (b/create-basis {})
          :class-dir class-dir
          :target    "target"
          :src-dirs  ["src"]
          :pom-data  (pom-template version)))

(defn ci "Run the CI pipeline of tests (and build the JAR)." [opts]
  (check-version)
  (test opts)
  (b/delete {:path "target"})
  (let [opts (jar-opts opts)]
    (println "\nWriting pom.xml...")
    (b/write-pom opts)
    (println "\nCopying source...")
    (b/copy-dir {:src-dirs ["resources" "src"] :target-dir class-dir})
    (println "\nBuilding JAR..." (:jar-file opts))
    (b/jar opts))
  opts)

(defn install "Install the JAR locally." [opts]
  (check-version)
  (let [opts (jar-opts opts)]
    (b/install opts))
  opts)

(defn deploy "Deploy the JAR to Clojars." [opts]
  (check-version)
  (let [{:keys [jar-file] :as opts} (jar-opts opts)]
    (dd/deploy {:installer :remote :artifact (b/resolve-path jar-file)
                :pom-file (b/pom-path (select-keys opts [:lib :class-dir]))}))
  opts)
