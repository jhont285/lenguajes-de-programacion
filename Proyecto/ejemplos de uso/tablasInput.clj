;jhon

( println "\n\t TABLAS DE MULTIPLICAR\n\n" )
( def i 2 )
( while (< i 10)
    ( def j 2 )
    ( while (<= j 10 )
        ( println "\t" i "*" j "=" (* i j) )
        ( def j (+ 1 j) )
    )
    ( println )
    (def i (+ i 1))
)
