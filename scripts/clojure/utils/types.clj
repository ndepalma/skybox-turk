(ns utils.types)

(defmacro defenum [name val]
  `(def ^:const ~name val))
