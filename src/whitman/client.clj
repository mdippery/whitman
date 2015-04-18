(ns whitman.client)

(def source-url [cfg user]
  (format (get cfg "source") user))
