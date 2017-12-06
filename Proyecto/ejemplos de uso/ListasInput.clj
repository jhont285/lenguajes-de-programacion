;raul
( defn sum[ number len ]
    ( def r 0 )
    ( def acum 0 )
    ( while (< r len)
        ( def acum (+ r acum) )
        (def r (+ r 1))
    )
    acum
)

( def number (list 9 8 7 6 5 4 3 2 1 0 ) )
( println "\n OPERACION SUMA\n" )
( def r 0 )
( while (< r 10)
    ( println "  " (nth number r) )
    (def r (+ r 1))
)
( println "  --" )
( println " " (sum number 10) )
