;jhon
( println "Funcion y=x^2 con funciones anonimas\n" )
( def cuadrado ( fn [x] (* x x) ) )
( def i 0)
( while (<= i 20)
    ( println "x =" i "  y =" (cuadrado i) )
    (def i (+ i 1))
)
