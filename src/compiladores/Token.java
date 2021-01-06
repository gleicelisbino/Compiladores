package  compiladores;

public class Token {

    public  static  int tipo;// TROQUEI OS STATIC. OBSERVAR OCORRêNCIA DE BUGS
    public StringBuffer lexema;// Lexema: String que forma o token.

    public Token(int tipo, StringBuffer lexema) {
        tipo = 0;
        lexema = null;
    }

    public static int getTipo() {
        return tipo;
    }

    public   StringBuffer getLexema() {
        return lexema;
    }

    public   void setTipo(int tipo) {
        Token.tipo = tipo;
    }

    public void setLexema(StringBuffer lexema) {
        this.lexema = lexema;
    }

    @Override
    public String toString() {
        return "Lexema: " + this.lexema + " Tipo (" + tipo + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Token) {
          //  Token qualquer = (Token) o;
      //      return Token.lexema.equals(Token.getLexema());
        return true;
        } else {
            return false;
        }

    }
}
