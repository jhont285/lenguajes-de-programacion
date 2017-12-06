import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.File;

public class Analizador extends psiVisitor {
public static void main(String [] args) throws Exception {
        try{
                psiCoderLexer lexer;
                if(args.length > 0)
                        lexer = new psiCoderLexer(new ANTLRFileStream(args[0]));
                else
                        lexer = new psiCoderLexer(new ANTLRInputStream(System.in));
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                psiCoderParser parser = new psiCoderParser(tokens);
                ParseTree tree = parser.principal();
                //System.out.println(tree.toStringTree(parser));
                //System.out.println( tree.getChildCount());
                psiVisitor visitor = new psiVisitor();
                visitor.visit( tree );
                String answer = new psiVisitor().visit(tree);
                // String[] auxPro = answer.split(" ");
                // for( String i: auxPro )
                //System.out.println(tree.toStringTree(parser));
        } catch( Exception e) {
                System.out.println("Error (Test):   " + e);
        }
}
}
