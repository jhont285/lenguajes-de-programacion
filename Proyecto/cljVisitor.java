import java.io.*;

public class cljVisitor extends ClojureBaseVisitor<String>
{
    private int tabs;
    private PrintWriter file;

    public cljVisitor()
    {
        tabs = 0;
        try{
            file = new PrintWriter(new File("magicDone.py"));
        }
        catch( IOException e ){}
    }
	@Override
    public String visitPrincipal(ClojureParser.PrincipalContext ctx)
    {
        visitCuerpo( ctx.cuerpo() );
        file.close();
        return "";
    }

	@Override
    public String visitCuerpo(ClojureParser.CuerpoContext ctx)
    {
        if( ctx.declaracion() != null )
        {
            visitDeclaracion( ctx.declaracion() );
            visitCuerpo( ctx.cuerpo() );
        }
        else if( ctx.condicional() != null )
        {
            visitCondicional( ctx.condicional() );
            visitCuerpo( ctx.cuerpo() );
        }
        else if( ctx.llamarFuncion() != null )
        {
            makeTabs( tabs );
            visitLlamarFuncion( ctx.llamarFuncion() );
            file.write( "\n" );
            visitCuerpo( ctx.cuerpo() );
        }
        else if( ctx.funcion() != null )
        {
            visitFuncion( ctx.funcion() );
            visitCuerpo( ctx.cuerpo() );
        }
        else if( ctx.ciclos() != null )
        {
            visitCiclos( ctx.ciclos() );
            visitCuerpo( ctx.cuerpo() );
        }
        else if( ctx.envolver() != null )
        {
            visitEnvolver( ctx.envolver() );
            visitCuerpo( ctx.cuerpo() );
        }
        return "";
    }

	@Override
    public String visitDeclaracion(ClojureParser.DeclaracionContext ctx)
    {
        makeTabs( tabs );
        file.write( ctx.ID().getText() + " = " );
        visitExpresion( ctx.expresion() );
        file.write( "\n" );
        return "";
    }

	@Override
    public String visitDeclaracionTem(ClojureParser.DeclaracionTemContext ctx) { return ""; }

	@Override
    public String visitExpresion(ClojureParser.ExpresionContext ctx)
    {

        if( ctx.valor() != null )
            visitValor( ctx.valor() );
        else if( ctx.estructura() != null )
            visitEstructura( ctx.estructura() );
        else if( ctx.funcionAnonima() != null )
            visitFuncionAnonima( ctx.funcionAnonima() );
        else if( ctx.llamarFuncion() != null )
            visitLlamarFuncion( ctx.llamarFuncion() );
        else{
            file.write( '(' );
            visitExpresion( ctx.expresion() );
            String oper = ctx.operador().getText();
            if( oper.equals("=") )
                oper = "==";
            else if( oper.equals("str") )
                oper = "+";
            else if( oper.equals("not=") )
                oper = "!=";
            visitOtraExpresion( ctx.otraExpresion(), oper );
            file.write( ')' );
        }
        return "";
    }

	@Override
    public String visitOtraExpresion(ClojureParser.OtraExpresionContext ctx) { return ""; }
    // se sobrecarga la funcion
    public String visitOtraExpresion(ClojureParser.OtraExpresionContext ctx, String operador)
    {
        if( ctx.expresion() != null )
        {
            file.write( " " + operador + " " );
            visitExpresion( ctx.expresion() );
            visitOtraExpresion( ctx.otraExpresion(), operador );
        }
        return "";
    }

	@Override
    public String visitCondicional(ClojureParser.CondicionalContext ctx)
    {
        makeTabs( tabs );
        file.write( "if " );
        visitExpresion( ctx.expresion() );
        file.write( ":\n" );
        tabs++;
        visitCuerpoAux( ctx.cuerpoAux(0) );
        if( ctx.cuerpoAux(1) != null ) // else
        {
            tabs--;
            makeTabs( tabs );
            file.write( "else:\n" );
            tabs++;
            visitCuerpoAux( ctx.cuerpoAux(1) );
        }
        tabs--;
        return "";
    }

	@Override
    public String visitLlamarFuncion(ClojureParser.LlamarFuncionContext ctx)
    {
        if( ctx.funcionLeng().getText().equals( "println" ) || ctx.funcionLeng().getText().equals( "print" ) )
        {
            file.write( "print " );
            if( ctx.expresion() != null )
                visitExpresion( ctx.expresion() );
            visitOtraExpresion( ctx.otraExpresion(), "," );
        }
        else if( ctx.funcionLeng().getText().equals( "get" ) || ctx.funcionLeng().getText().equals( "nth" ) )
        {
            file.write( ctx.expresion().getText() + "[" );
            visitOtraExpresion( ctx.otraExpresion(), "" );
            file.write( "]" );
        }
        else{
            file.write( ctx.funcionLeng().getText() + "(" );
            if( ctx.expresion() != null )
                visitExpresion( ctx.expresion() );
            visitOtraExpresion( ctx.otraExpresion(), "," );
            file.write( ")" );
        }
        return "";
    }

    @Override
    public String visitFuncionAnonima(ClojureParser.FuncionAnonimaContext ctx)
    {
        file.write( "lambda " );
        visitOtraExpresion( ctx.otraExpresion(), "" );
        file.write( ":" );
        visitExpresion( ctx.expresion() );
        return "";
    }

	@Override public String visitFuncion(ClojureParser.FuncionContext ctx)
    {
        makeTabs( tabs );
        file.write( "def "+ ctx.ID().getText() + " (" );
        visitExpresion( ctx.expresion(0) );
        visitOtraExpresion( ctx.otraExpresion(), "," );
        file.write( "):\n");
        tabs++;
        if( ctx.CADENA() != null )
        {
            makeTabs(tabs);
            file.write( "\"\"\"" + ctx.CADENA().getText().substring(1) +  "\"\"\n" );
        }
        visitCuerpo( ctx.cuerpo() );
        if( ctx.expresion(1) != null )
        {
            makeTabs( tabs );
            file.write( "return "  );
            visitExpresion( ctx.expresion(1) );
            file.write( "\n" );
        }
        tabs--;
        return "";
    }

	@Override
    public String visitCiclos(ClojureParser.CiclosContext ctx)
    {
        if( ctx.mientras() != null )
            visitMientras( ctx.mientras() );
        return "";
    }

	@Override
    public String visitMientras(ClojureParser.MientrasContext ctx)
    {
        makeTabs( tabs );
        file.write( "while" );
        visitExpresion( ctx.expresion() );
        file.write( ":\n" );
        tabs++;
        visitCuerpo( ctx.cuerpo() );
        tabs--;
        return "";
    }

	@Override public String visitEnvolver(ClojureParser.EnvolverContext ctx)
    {
        visitCuerpo( ctx.cuerpo() );
        return "";
    }

	@Override public String visitEstructura(ClojureParser.EstructuraContext ctx)
    {
        if( ctx.vector() != null )
            visitVector( ctx.vector() );
        else if( ctx.listaEnlazada() != null )
            visitListaEnlazada( ctx.listaEnlazada() );
        else if( ctx.mapa() != null )
            visitMapa( ctx.mapa() );
        else
            visitConjunto( ctx.conjunto() );
        return "";
    }

	@Override
    public String visitVector(ClojureParser.VectorContext ctx)
    {
        file.write( "[" );
        if( ctx.expresion() != null )
        {
            visitExpresion( ctx.expresion() );
            visitOtraExpresion( ctx.otraExpresion(), "," );
        }
        file.write( "]" );
        return "";
    }

	@Override
    public String visitMapa(ClojureParser.MapaContext ctx)
    {
        file.write( "{" );
        if( ctx.llave() != null )
        {
            file.write( "\"" + ctx.llave().getText().substring(1) + "\":" );
            visitExpresion( ctx.expresion() );
            visitOtraValor( ctx.otraValor() );
        }
        file.write( "}" );
        return "";
    }

	@Override
    public String visitOtraValor(ClojureParser.OtraValorContext ctx)
    {
        if( ctx.otraValor() != null )
        {
            file.write( ",\"" + ctx.llave().getText().substring(1) + "\":" );
            visitExpresion( ctx.expresion() );
            visitOtraValor( ctx.otraValor() );
        }
        return "";
    }

	@Override
    public String visitListaEnlazada(ClojureParser.ListaEnlazadaContext ctx)
    {
        file.write( "list([" );
        if( ctx.expresion() != null )
        {
            visitExpresion( ctx.expresion() );
            visitOtraExpresion( ctx.otraExpresion(), "," );
        }
        file.write( "])" );
        return "";
    }

	@Override public String visitConjunto(ClojureParser.ConjuntoContext ctx)
    {
        file.write( "set([" );
        if( ctx.expresion() != null )
        {
            visitExpresion( ctx.expresion() );
            visitOtraExpresion( ctx.otraExpresion(), "," );
        }
        file.write( "])" );
        return "";
    }

	@Override
    public String visitValor(ClojureParser.ValorContext ctx)
    {
        if( ctx.getText().equals( "true" ) )
            file.write( "True" );
        else if( ctx.getText().equals( "false" ) )
            file.write( "False" );
        else if( ctx.getText().equals( "nil" ) )
            file.write( "None" );
        else
            file.write( ctx.getText() );
        return "";
    }

	@Override public String visitLlave(ClojureParser.LlaveContext ctx) { return ""; }

	@Override public String visitOperador(ClojureParser.OperadorContext ctx) { return ""; }

    private void makeTabs( int n )
    {
        while( n-- > 0 )
            file.write( "    " );
    }
    @Override
    public String visitCuerpoAux(ClojureParser.CuerpoAuxContext ctx)
    {
        if( ctx.declaracion() != null )
            visitDeclaracion( ctx.declaracion() );
        else if( ctx.condicional() != null )
            visitCondicional( ctx.condicional() );
        else if( ctx.llamarFuncion() != null )
        {
            makeTabs( tabs );
            visitLlamarFuncion( ctx.llamarFuncion() );
            file.write( "\n" );
        }
        else if( ctx.funcion() != null )
            visitFuncion( ctx.funcion() );
        else if( ctx.ciclos() != null )
            visitCiclos( ctx.ciclos() );
        else if( ctx.envolver() != null )
            visitEnvolver( ctx.envolver() );
        return "";
    }

}
