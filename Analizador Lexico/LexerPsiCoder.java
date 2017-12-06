import java.util.Scanner;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

public class LexerPsiCoder
{
private HashSet<String> reservedWords;
private HashMap<Character,String> charLexeme;
private LinkedList<Token> tokens;
private String error;

public LexerPsiCoder()
{
        reservedWords = new HashSet<>();
        reservedWords.add( "funcion" );             reservedWords.add( "fin_funcion" );
        reservedWords.add( "funcion_principal" );   reservedWords.add( "fin_principal" );
        reservedWords.add( "funcion" );             reservedWords.add( "booleano" );
        reservedWords.add( "falso" );               reservedWords.add( "caracter" );
        reservedWords.add( "real" );                reservedWords.add( "imprimir" );
        reservedWords.add( "si" );                  reservedWords.add( "si_no" );
        reservedWords.add( "fin_mientras" );        reservedWords.add( "para" );
        reservedWords.add( "seleccionar" );         reservedWords.add( "entre" );
        reservedWords.add( "caso" );                reservedWords.add( "defecto" );
        reservedWords.add( "estructura" );          reservedWords.add( "fin_estructura" );
        reservedWords.add( "retornar" );            reservedWords.add( "entero" );
        reservedWords.add( "verdadero" );           reservedWords.add( "leer" );
        reservedWords.add( "cadena" );              reservedWords.add( "fin_para" );
        reservedWords.add( "mientras" );            reservedWords.add( "romper" );
        reservedWords.add( "fin_seleccionar" );     reservedWords.add( "entonces" );
        reservedWords.add( "fin_si" );              reservedWords.add( "hacer" );

        charLexeme = new HashMap<>();
        charLexeme.put( '+', "tk_mas" );            charLexeme.put( ':', "tk_dosp" );
        charLexeme.put( '-', "tk_menos" );          charLexeme.put( '*', "tk_mult" );
        charLexeme.put( '%', "tk_mod" );            charLexeme.put( ';', "tk_pyc" );
        charLexeme.put( ',', "tk_coma" );           charLexeme.put( '(', "tk_par_izq" );
        charLexeme.put( ')', "tk_par_der");
}

public boolean analyze()
{
        Scanner is = new Scanner( System.in );
        boolean commented = false;
        StringBuilder token = new StringBuilder();
        tokens = new LinkedList<>();
        Token auxTok = null;
        String tmp;
        int row = 0, sumTok = 1;
        while( is.hasNext() )
        {
                row++;
                tmp = is.nextLine() + " ";
                for( int column = 0; column < tmp.length(); column++ )
                {
                        if( !commented )
                        {
                                if( charLexeme.containsKey( tmp.charAt(column) ) )
                                {
                                        auxTok = new Token( charLexeme.get( tmp.charAt(column) ), row, column+1 );
                                        sumTok = 1;
                                }
                                else {
                                        switch( tmp.charAt(column) )
                                        {
                                        case '/':
                                                if( column+1 < tmp.length() && tmp.charAt(column+1) == '*' )
                                                {
                                                        commented = true;
                                                        column++; sumTok = 0;
                                                }
                                                else if( column+1 < tmp.length() && tmp.charAt(column+1) == '/' )
                                                        column = tmp.length();
                                                else
                                                        auxTok = new Token( "tk_div", row, column+1 );
                                                break;

                                        case '=':
                                                if ( column+1 < tmp.length() && tmp.charAt( column+1 ) == '=' )
                                                {
                                                        auxTok = new Token( "tk_igual", row, column+1 );
                                                        column++; sumTok = 0;
                                                }
                                                else
                                                        auxTok = new Token( "tk_asig", row, column+1 );
                                                break;

                                        case '<':
                                                if ( column+1 < tmp.length() && tmp.charAt( column+1 ) == '=' )
                                                {
                                                        auxTok = new Token( "tk_menor_igual", row, column+1 );
                                                        column++; sumTok = 0;
                                                }
                                                else
                                                        auxTok = new Token( "tk_menor", row, column+1 );
                                                break;
                                        case '>':
                                                if ( column+1 < tmp.length() && tmp.charAt( column+1 ) == '=' )
                                                {
                                                        auxTok = new Token( "tk_mayor_igual", row, column+1 );
                                                        column++; sumTok = 0;
                                                }
                                                else
                                                        auxTok = new Token( "tk_mayor", row, column+1 );
                                                break;

                                        case '!':
                                                if ( column+1 < tmp.length() && tmp.charAt( column+1 ) == '=' )
                                                {
                                                        auxTok = new Token( "tk_dif", row, column+1 );
                                                        column++; sumTok = 0;
                                                }
                                                else
                                                        auxTok = new Token( "tk_neg", row, column+1 );
                                                break;
                                        case '&': // error
                                                if ( column+1 < tmp.length() && tmp.charAt( column+1 ) == '&' )
                                                {
                                                        auxTok = new Token( "tk_y", row, column+1 );
                                                        column++; sumTok = 0;
                                                }
                                                else{
                                                        error = ">>> Error lexico(linea: " + row + ", posicion: " + (column+1) + ")";
                                                        return false;
                                                }
                                                break;

                                        case '|':
                                                if ( column+1 < tmp.length() && tmp.charAt( column+1 ) == '|' )
                                                {
                                                        auxTok = new Token( "tk_o", row, column+1 );
                                                        column++; sumTok = 0;
                                                }
                                                else{
                                                        error = ">>> Error lexico(linea: " + row + ", posicion: " + (column+1) + ")";
                                                        return false;
                                                }
                                                break;

                                        case '.':
                                                if( token.length() > 0 && token.indexOf(".") == -1 && isNumber( token.charAt(0) ) )
                                                        token.append( tmp.charAt( column ) );
                                                else
                                                        auxTok = new Token( "tk_punto", row, column+1 );
                                                break;

                                        case '\'':
                                                if( column + 2 < tmp.length() && isCharacter( tmp.charAt(column + 1) )
                                                    && tmp.charAt(column + 2) == '\'' ) {
                                                        auxTok = new Token( "tk_caracter",  "\'" + tmp.charAt(column+1) + "\'", row, column+1 );
                                                        column += 2; sumTok = -1;
                                                }
                                                break;

                                        case '\"':
                                                StringBuilder chain = new StringBuilder();
                                                chain.append( '\"' );
                                                for( int auxColumn = column + 1; auxColumn < tmp.length(); auxColumn++ )
                                                        if( isCharacter( tmp.charAt(auxColumn) ) )
                                                                chain.append( tmp.charAt(auxColumn) );
                                                        else if( tmp.charAt(column) == '\"' )
                                                        {
                                                                chain.append( '\"' );
                                                                auxTok = new Token( "tk_cadena", chain.toString(), row, column+1 );
                                                                column = auxColumn;
                                                                break;
                                                        }
                                                break;

                                        default:
                                                if( tmp.charAt( column ) != ' ' )
                                                {
                                                        if( isCharacter( tmp.charAt( column ) ) )
                                                                token.append( tmp.charAt( column ) );
                                                        else{
                                                                error = ">>> Error lexico(linea: " + row + ", posicion: " + (column - token.length()+1) + ")";
                                                                return false;
                                                        }
                                                }
                                                break;
                                        }
                                }

                                if( token.length() > 0 && (auxTok != null || tmp.charAt( column ) == ' ') )
                                {
                                        if( reservedWords.contains( token.toString() ) )
                                                tokens.add( new Token( token.toString(), row, column - token.length() + 1 ) );
                                        else if( !isToken( token.toString() ).equals("error") )
                                                tokens.add( new Token( isToken( token.toString() ), token.toString(), row, column - token.length() + sumTok ) );
                                        else {
                                                error = ">>> Error lexico(linea: " + row + ", posicion: " + (column - token.length() ) + ")";
                                                return false;
                                        }
                                        token = new StringBuilder();
                                        sumTok = 1;
                                }
                                if( auxTok != null )
                                        tokens.add( auxTok );
                                auxTok = null;
                        }
                        else
                        if( column+1 < tmp.length() && tmp.charAt(column) == '*' && tmp.charAt(column + 1) == '/' )
                        {
                                commented = false;
                                column++;
                        }
                }
        }
        return true;
}

private String isToken( String chain )
{
        int numbers = 0, letterYUnderStripe = 0, point = 0;
        for( int i = 0; i < chain.length(); i++ )
        {
                if( isNumber( chain.charAt(i) ) )
                        numbers++;
                else if( isLetter( chain.charAt(i) ) || chain.charAt(i) == '_' )
                        letterYUnderStripe++;
                else if( chain.charAt(i) == '.' )
                        point++;
        }
        if( numbers == chain.length() )
                return "tk_entero";
        else if( numbers == chain.length()-1 && point == 1 && chain.charAt(0) != '.' && chain.charAt( chain.length()-1 ) != '.' )
                return "tk_real";
        else if( numbers + letterYUnderStripe == chain.length() && !(chain.charAt(0) >= '0' && chain.charAt(0) <= '9') )
                return "id";
        return "error";
}

private boolean isCharacter( char character )
{
        return isLetter( character ) || isNumber( character ) || character == ' ' || character == '_';
}

private boolean isNumber( char character )
{
        return character >= '0' && character <= '9';
}

private boolean isLetter( char character )
{
        return character >= 'a' && character <= 'z' || character >= 'A' && character <= 'Z';
}


public String getError()
{
        return error;
}

public LinkedList<Token> getTokens()
{
        return tokens;
}

public static class Token
{
private String id, value;
private int row, column;

public Token( String id, String value, int row, int column )
{
        this.id = id;
        this.row  = row;
        this.column = column;
        this.value = value;
}

public Token( String id, int row, int column )
{
        this( id, null, row, column );
}

public String getId()
{
        return id;
}

public int getRow()
{
        return row;
}

public int getColumn()
{
        return column;
}

public String getValue()
{
        return value;
}
}

public static void main( String[] args )
{
        LexerPsiCoder lenguage = new LexerPsiCoder();
        boolean correctAnalytze = lenguage.analyze();
        LinkedList<Token> tokens = lenguage.getTokens();

        for( Token o : tokens )
        {
                if( o.getValue() != null )
                        System.out.println( "<" + o.getId() + "," + o.getValue() + "," + o.getRow() + "," + o.getColumn() + ">" );
                else
                        System.out.println( "<" + o.getId() + "," + o.getRow() + "," + o.getColumn() + ">" );
        }
        if( !correctAnalytze )
                System.out.println( lenguage.getError() );
}

}
