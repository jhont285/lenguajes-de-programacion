Para la compilacion del programa debe ejecutar en termial ( unix ) el comando:

Primeros debemos crear un archivo con el nombre de template.clj que es donde vamos a traducir nuestro codigo

Construir el proyecto
$ make
Ejecutamos la siguientes instrucciones
antlr4 Clojure.g4
javac *.java


Para traducir un código clojure a python debe tener ese archivo en el mismo directorio y ejecutar:
$ make translate
Con este comando utilizamos los siguientes instrucciones
java traductor template.clj
cat magicDone.py
python magicDone.py


Para comparar los problemas con el de clojure usamos
$ make clojure
Ejecuta las siguientes contrucciones
java -cp clojure-1.8.0.jar clojure.main template.clj

Si deseamos ver el arbol de la construccion de la gramatica
$ make clojure
java -cp clojure-1.8.0.jar clojure.main template.clj 
