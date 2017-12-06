;jhon
( defn factor
    "Se hace el calculo iterativamente" [n]
    ( def answer 1 )
    ( def i 1 )
    ( while (< i (+ n 1))
        ( def answer (* i answer) )
        ( def i (+ i 1) )
    )
    answer
)

( println "FACTORIAL DE LOS 10 PRIMEROS NUMEROS\n" )
( def n  1 )
( while (< n 11)
    ( println n "! =" (factor n) )
    ( def n (+ 1 n) )
)
