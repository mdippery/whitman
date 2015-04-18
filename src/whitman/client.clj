(ns whitman.client)

(def url-for-user [cfg user]
  (format (get cfg "source") user))
