(ns whitman.client)

(defn source-url [cfg user]
  (format (get cfg "source") user))
