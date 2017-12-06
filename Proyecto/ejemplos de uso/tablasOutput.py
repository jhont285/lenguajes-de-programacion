print "\n\t TABLAS DE MULTIPLICAR\n\n"
i = 2
while(i < 10):
    j = 2
    while(j <= 10):
        print "\t" , i , "*" , j , "=" , (i * j)
        j = (1 + j)
    print 
    i = (i + 1)
