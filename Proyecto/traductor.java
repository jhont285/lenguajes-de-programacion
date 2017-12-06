import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.File;

public class traductor extends ClojureBaseListener{


    public static void main(String [] args) throws Exception{
        try{
            ClojureLexer lexer;
            if(args.length > 0)
                lexer = new ClojureLexer(new ANTLRFileStream(args[0]));
            else
                lexer = new ClojureLexer(new ANTLRInputStream(System.in));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ClojureParser parser = new ClojureParser(tokens);
            ParseTree tree = parser.principal();
            cljVisitor visitor = new cljVisitor();
			visitor.visit(tree);

        } catch( Exception e){
            System.out.println("Error (Test):   " + e);
        }
    }
}
