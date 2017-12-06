import org.antlr.v4.runtime.misc.NotNull;
import java.io.*;
import java.util.HashMap;

public class psiVisitor extends psiCoderBaseVisitor<String>
{
    public int tabs;
    public PrintWriter system;
    public String tipo;

    public psiVisitor()
    {
        tabs = 0;
        try{
            system = new PrintWriter(new File("Template.cpp"));
        }
        catch( IOException e ){};
    }
	@Override
    public String visitPrincipal(psiCoderParser.PrincipalContext ctx)
    {
        system.write( "#include<iostream>\n\n#define verdadero true\n#define falso false\n\nusing namespace std;\n\n" );
        if( !ctx.sentencia(0).getText().isEmpty() )
            visitSentencia( ctx.sentencia(0) );
        if( !ctx.sentencia(1).getText().isEmpty() )
            visitSentencia( ctx.sentencia(1) );
        system.write( "\nint main()\n" );
        system.write( "{\n" );
        tabs++;
        if( !ctx.cuerpo().getText().isEmpty() )
            visitCuerpo( ctx.cuerpo() );
        makeTabs( 1 );
        system.write( "return 0;\n}\n" );
        system.close();
        return "";
    }

	@Override
    public String visitSentencia(psiCoderParser.SentenciaContext ctx)
    {
        if( ctx.sentencia() != null )
        {
            if( ctx.declaracion() != null ){
                makeTabs( tabs );
                visitDeclaracion( ctx.declaracion() );
            }
            else if( ctx.funcion() != null ){
                makeTabs( tabs );
                visitFuncion( ctx.funcion() );
            }
            else if( ctx.estructura() != null ){
                makeTabs( tabs );
                system.write( "struct " );
                visitEstructura( ctx.estructura() );
            }
            visitSentencia( ctx.sentencia() );
        }
        return "";
    }

	@Override public String visitCuerpo(psiCoderParser.CuerpoContext ctx)
    {
        if( ctx.cuerpo() != null )
        {
            if( ctx.declaracion() != null ) {
                makeTabs( tabs );
                visitDeclaracion( ctx.declaracion() );
            }
            else if( ctx.asignacionVarDecla() != null ){
                makeTabs( tabs );
                visitAsignacionVarDecla( ctx.asignacionVarDecla() );
            }
            else if( ctx.ciclos() != null ){
                makeTabs( tabs );
                visitCiclos( ctx.ciclos() );
            }
            else if( ctx.condicional() != null ) {
                makeTabs( tabs );
                visitCondicional( ctx.condicional() );
            }
            else if( ctx.seleccionMultiple() != null ){
                makeTabs( tabs );
                visitSeleccionMultiple( ctx.seleccionMultiple() );
            }
            else if( ctx.impresion() != null )  {
                makeTabs( tabs );
                visitImpresion( ctx.impresion() );
            }
            else if( ctx.llamarFuncion() != null )  {
                makeTabs( tabs );
                visitLlamarFuncion( ctx.llamarFuncion() );
            }
            else if( ctx.captura() != null )  {
                makeTabs( tabs );
                visitCaptura( ctx.captura() );
            }
            visitCuerpo( ctx.cuerpo() );

        }
        return "";
    }

	@Override
    public String visitDeclaracion(psiCoderParser.DeclaracionContext ctx)
    {
        visitTipoDeDato( ctx.tipoDeDato() );
        system.write( " "+ ctx.ID().getText() + " " );
        tipo = ctx.tipoDeDato().getText();
        visitAsignacion( ctx.asignacion() );
        system.write( " " );
        visitOtraDeclarcion( ctx.otraDeclarcion() );
        system.write( ";\n" );
        tipo = "";
        return "";
    }

	@Override public String visitAsignacion(psiCoderParser.AsignacionContext ctx)
    {
        if( ctx.expresion() == null )
        {
            if( tipo.equals( "booleano" ) )
                system.write( " = false" );
            else if( tipo.equals( "real" ) )
                system.write( " = 0.0" );
            else if( tipo.equals( "entero" ) )
                system.write( " = 0" );
            else if( tipo.equals( "caracter" ) )
                system.write( " = 'a'" );
            else if( tipo.equals( "cadena" ) )
                system.write( " = \"\"" );
        }
        else
            system.write( "=" +ctx.expresion().getText() );
        return "";
    }

	@Override public String visitOtraDeclarcion(psiCoderParser.OtraDeclarcionContext ctx)
    {
        if( ctx.otraDeclarcion() != null )
        {
            visitOtraDeclarcion( ctx.otraDeclarcion() );
            system.write( "," + ctx.ID()  );
            visitAsignacion( ctx.asignacion() );
        }

        return "";
    }

	@Override
    public String visitAsignacionVarDecla(psiCoderParser.AsignacionVarDeclaContext ctx)
    {
        if( ctx.ID() != null )
            system.write( ctx.ID().getText() );
        else
            system.write( ctx.IDESTRUCTURA().getText() );
        system.write( " = " + ctx.expresion().getText() + ";\n" );
        return "";
    }

	@Override public String visitExpresion(psiCoderParser.ExpresionContext ctx) { return ""; }

	@Override public String visitExpresionConPar(psiCoderParser.ExpresionConParContext ctx) { return ""; }

	@Override public String visitOtraExpresion(psiCoderParser.OtraExpresionContext ctx) { return ""; }

	@Override public String visitRomper(psiCoderParser.RomperContext ctx)
    {
        if( !ctx.getText().isEmpty() )
        {
            makeTabs( tabs );
            system.write( "break;\n" );
        }
        return "";
    }

	@Override
    public String visitCondicional(psiCoderParser.CondicionalContext ctx)
    {
        system.write( "if ( " + ctx.expresion().getText() + " ){\n" );
        tabs++;
        visitCuerpo( ctx.cuerpo() );
        visitRomper( ctx.romper() );
        tabs--;
        makeTabs( tabs );
        system.write( "}\n" );
        if( !ctx.enOtroCaso().getText().isEmpty() )
            visitEnOtroCaso( ctx.enOtroCaso() );
        return "";
    }

	@Override
    public String visitEnOtroCaso(psiCoderParser.EnOtroCasoContext ctx)
    {
        makeTabs( tabs );
        system.write( "else {\n" );
        tabs++;
        visitCuerpo( ctx.cuerpo() );
        visitRomper( ctx.romper() );
        tabs--;
        makeTabs( tabs );
        system.write( "}\n" );
        return "";
    }

	@Override public String visitCiclos(psiCoderParser.CiclosContext ctx)
    {
        if( ctx.cicloMientras() != null )
            visitCicloMientras( ctx.cicloMientras() );
        else if( ctx.cicloPara() != null )
            visitCicloPara( ctx.cicloPara() );
        else if( ctx.cicloHacer() != null )
            visitCicloHacer( ctx.cicloHacer() );
        return "";
    }

	@Override public String visitCicloMientras(psiCoderParser.CicloMientrasContext ctx)
    {
        system.write( "while( " + ctx.expresion().getText() + " ){\n" );
        tabs++;
        visitCuerpo( ctx.cuerpo() );
        visitRomper( ctx.romper() );
        tabs--;
        makeTabs( tabs );
        system.write( "}\n" );
        return "";
    }

	@Override public String visitCicloPara(psiCoderParser.CicloParaContext ctx)
    {
        system.write( "for( " );
        if( ctx.tipoDeDato() != null )
            visitTipoDeDato( ctx.tipoDeDato() );
        system.write( ctx.ID(0).getText() +" = " + ctx.expresion(0).getText() + "; " + ctx.expresion(1).getText()
                    + " ; " + ctx.ID(0).getText() + "+=" );
        if( ctx.NUMEROENTERO() != null )
            system.write( ctx.NUMEROENTERO().getText() );
        else
            system.write( ctx.ID(1).getText() );
        system.write( " ){\n" );
        tabs++;
        visitCuerpo( ctx.cuerpo() );
        visitRomper( ctx.romper() );
        tabs--;
        makeTabs( tabs );
        system.write( "}\n" );
        return "";
    }

	@Override public String visitCicloHacer(psiCoderParser.CicloHacerContext ctx)
    {
        system.write( "do{\n " );
        tabs++;
        visitCuerpo( ctx.cuerpo() );
        visitRomper( ctx.romper() );
        tabs--;
        makeTabs( tabs );
        system.write( "}while( " + ctx.expresion().getText() + " );\n" );
        return "";
    }

	@Override
    public String visitSeleccionMultiple(psiCoderParser.SeleccionMultipleContext ctx)
    {
        system.write( "switch( " );
        if( ctx.ID() != null )
            system.write( ctx.ID().getText() );
        else
            system.write( ctx.IDESTRUCTURA().getText() );
        system.write( " ){\n" );
        tabs++;
        if( !ctx.casos().getText().isEmpty() )
            visitCasos( ctx.casos() );
        if( !ctx.casoDefecto().getText().isEmpty() )
            visitCasoDefecto( ctx.casoDefecto() );
        tabs--;
        makeTabs( tabs );
        system.write( "}\n " );
        return "";
    }

	@Override public String visitCasos(psiCoderParser.CasosContext ctx)
    {
        if( ctx.casos() != null )
        {
            makeTabs( tabs );
            system.write( "case " );
            if( ctx.ID() != null )
                system.write( ctx.ID().getText() );
            else
                system.write( ctx.NUMEROENTERO().getText() );
            system.write( ":\n" );
            tabs++;
            visitCuerpo( ctx.cuerpo() );
            visitRomper( ctx.romper() );
            tabs--;
            visitCasos( ctx.casos() );
        }
        return "";
    }

	@Override
    public String visitCasoDefecto(psiCoderParser.CasoDefectoContext ctx)
    {
        makeTabs( tabs );
        system.write( "default:\n" );
        tabs++;
        visitCuerpo( ctx.cuerpo() );
        visitRomper( ctx.romper() );
        tabs--;
        return "";
    }

	@Override
    public String visitCaptura(psiCoderParser.CapturaContext ctx)
    {
        system.write( "cin >> " );
        if( ctx.ID() != null )
            system.write( ctx.ID().getText() );
        else
            system.write( ctx.IDESTRUCTURA().getText() );
        system.write( ";\n" );
        return "";
    }
	@Override public String visitImpresion(psiCoderParser.ImpresionContext ctx)
    {
        system.write( "cout << " + ctx.expresion().getText() + " ;\n" );
        if( !ctx.otrosParametros().getText().isEmpty() );
            visitOtrosParametros( ctx.otrosParametros() );
        return "";
    }

	@Override public String visitOtrosParametros(psiCoderParser.OtrosParametrosContext ctx)
    {
        if( ctx.otrosParametros() != null )
        {
            makeTabs( tabs );
            system.write( "cout << " + ctx.expresion().getText() + ";\n" );
            visitOtrosParametros( ctx.otrosParametros() );
        }
        return "";
    }

	@Override
    public String visitLlamarFuncion(psiCoderParser.LlamarFuncionContext ctx)
    {
        if( ctx.ID() != null )
            system.write( ctx.ID().getText() );
        else
            system.write( ctx.IDESTRUCTURA().getText() );
        system.write( "( "  );
        if( !ctx.argLLamada().getText().isEmpty() )
            system.write(  ctx.argLLamada().getText() );
        system.write( " )" );
        if( ctx.TK_PYC() != null )
            system.write( ";" );
        system.write( "\n" );
        return "";
    }

	@Override public String visitArgLLamada(psiCoderParser.ArgLLamadaContext ctx) {      return "";    }

	@Override public String visitOtrosargLLamada(psiCoderParser.OtrosargLLamadaContext ctx)
    {
        return "";
    }

	@Override
    public String visitFuncion(psiCoderParser.FuncionContext ctx)
    {
        visitTipoDeDato( ctx.tipoDeDato() );
        system.write( ctx.ID() + " (" );
        if( !ctx.argumentos().getText().isEmpty() )
            visitArgumentos( ctx.argumentos() );
        system.write( " ){\n" );
        tabs++;
        visitCuerpo( ctx.cuerpo() );
        makeTabs( tabs );
        system.write( "return " + ctx.expresion().getText() + ";\n" );
        tabs--;
        makeTabs( tabs );
        system.write( "}\n" );
        return "";
    }

	@Override
    public String visitArgumentos(psiCoderParser.ArgumentosContext ctx)
    {
        visitTipoDeDato( ctx.tipoDeDato() );
        system.write( " " +ctx.ID().getText() + " " );
        if( !ctx.otrosArgumentos().getText().isEmpty() )
            visitOtrosArgumentos( ctx.otrosArgumentos() );
        return "";
    }

	@Override
    public String visitOtrosArgumentos(psiCoderParser.OtrosArgumentosContext ctx)
    {
        if( ctx.otrosArgumentos() != null )
        {
            system.write( ", " );
            visitTipoDeDato( ctx.tipoDeDato() );
            system.write(  " " + ctx.ID().getText() + " " );
            visitOtrosArgumentos( ctx.otrosArgumentos() );
        }
        return "";
    }

	@Override
    public String visitEstructura(psiCoderParser.EstructuraContext ctx)
    {
        system.write( ctx.ID().getText() + "{\n" );
        tabs++;
        visitDecEnEstructura( ctx.decEnEstructura() );
        visitFunEstructura( ctx.funEstructura() );
        tabs--;
        makeTabs( tabs );
        system.write( "};\n " );
        return "";
    }

	@Override public String visitDecEnEstructura(psiCoderParser.DecEnEstructuraContext ctx)
    {
        if( ctx.decEnEstructura() != null )
        {
            makeTabs( tabs );
            visitTipoDeDato( ctx.tipoDeDato()  );
            system.write(" "+ ctx.ID().getText() +  " " );
            if( !ctx.otradecEnEstructura().getText().isEmpty() )
                visitOtradecEnEstructura( ctx.otradecEnEstructura() );
            system.write( ";\n" );
            visitDecEnEstructura( ctx.decEnEstructura() );
        }
        return "";
    }

	@Override public String visitOtradecEnEstructura(psiCoderParser.OtradecEnEstructuraContext ctx)
    {
        if( ctx.otradecEnEstructura() != null )
        {
            system.write( ", " );
            system.write( ctx.ID().getText() +  " " );
            visitOtradecEnEstructura( ctx.otradecEnEstructura() );
        }
        return "";
    }

	@Override public String visitFunEstructura(psiCoderParser.FunEstructuraContext ctx)
    {
        if( ctx.funEstructura() != null )
        {
            makeTabs( tabs );
            if( !ctx.funcion().getText().isEmpty() )
                visitFuncion( ctx.funcion() );
            visitFunEstructura( ctx.funEstructura() );
        }
        return "";
    }

	@Override public String visitTipoDeDato(psiCoderParser.TipoDeDatoContext ctx)
    {
        if( ctx.getText().equals( "entero" ) )
            system.write( "int " );
        else if( ctx.getText().equals( "cadena" ) )
            system.write( "string " );
        else if( ctx.getText().equals( "caracter" ) )
            system.write( "char " );
        else if( ctx.getText().equals( "real" ) )
            system.write( "double " );
        else if( ctx.getText().equals( "booleano" ) )
            system.write( "bool " );
        else
            system.write( ctx.getText() );
        return "";
    }

	@Override
    public String visitValor(psiCoderParser.ValorContext ctx)
    {
         return "";
     }

	@Override public String visitOperador(psiCoderParser.OperadorContext ctx) { return ""; }

    public void makeTabs( int n )
    {
        while( n-- > 0 )
            system.write( "    " );
    }
}
