(ns utils.types)

(defmacro defenum [name val]
  "This is a macro that defines a constant enumeration. You can use these names as globals after defining them."
  `(def ^:const ~name val))
