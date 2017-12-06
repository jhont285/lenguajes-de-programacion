all:
	java -Xmx500M -cp "/usr/local/lib/antlr-4.5-complete.jar:$CLASSPATH" org.antlr.v4.Tool Clojure.g4 -visitor
	javac *.java
run:
	java org.antlr.v4.runtime.misc.TestRig Clojure principal -gui < template.clj
translate:
	java traductor template.clj
	printf '\n\n\n'
	cat magicDone.py
	printf '\n\n\n'
	python magicDone.py
	printf '\n\n\n'

clojure:
	java -cp clojure-1.8.0.jar clojure.main template.clj
