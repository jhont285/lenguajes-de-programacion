print "Programa para hacer si una cadena es palindroma"
palabra = "RECONOCER"
palindroma = True
j = 8
i = 0
while(i < (9 / 2)):
    if (palabra[  i] != palabra[  j]):
        palindroma = False
    i = (i + 1)
    j = (j - 1)
respuesta = ""
if (palindroma == True):
    respuesta = "SI"
else:
    respuesta = "NO"
print "Es palindroma " , palabra , " :" , respuesta
