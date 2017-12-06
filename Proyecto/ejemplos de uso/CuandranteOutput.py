def cuadrante (x , y):
    if ((x > 0) and (y > 0)):
        print "El punto se encuentra en el primer Cuadrante"
    if ((x < 0) and (y > 0)):
        print "El punto se encuentra en el segundo Cuadrante"
    if ((x < 0) and (y < 0)):
        print "El punto se encuentra en el tercer Cuadrante"
    if ((x < 0) and (y < 0)):
        print "El punto se encuentra en el cuarto Cuadrante"
x = 4
y = 8
print "Determinar en que cuadrante esta el punto\t" , "x =" , x , " y =" , y
cuadrante(x , y)
