;raul 
( defn cuadrante [ x y ]
    ( if (and (> x 0) (> y 0) )
        ( println "El punto se encuentra en el primer Cuadrante" )
    )
    ( if (and (< x 0) (> y 0) )
        ( println "El punto se encuentra en el segundo Cuadrante" )
    )
    ( if (and (< x 0) (< y 0))
        ( println "El punto se encuentra en el tercer Cuadrante" )
    )
    ( if (and (< x 0) (< y 0))
        ( println "El punto se encuentra en el cuarto Cuadrante" )
    )
)

( def x 4 )
( def y 8 )
( println "Determinar en que cuadrante esta el punto\t" "x =" x " y =" y )
( cuadrante x y )