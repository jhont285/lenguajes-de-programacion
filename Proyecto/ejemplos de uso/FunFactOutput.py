def factor (  n):
    """Se hace el calculo iterativamente"""
    answer = 1
    i = 1
    while(i < (n + 1)):
        answer = (i * answer)
        i = (i + 1)
    return answer
print "FACTORIAL DE LOS 10 PRIMEROS NUMEROS\n"
n = 1
while(n < 11):
    print n , "! =" , factor(n)
    n = (1 + n)
