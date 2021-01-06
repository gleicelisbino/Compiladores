package compiladores;
public class AuxTable {
    
    public int tipo;
    public  int escopo;
    public String nomeDeVariavel;// Lexema: String que forma o token.

    public AuxTable(int tipo, String lexema, int escopo) {    
        this.tipo = tipo;
        this.nomeDeVariavel = lexema;
         this.escopo=escopo;
    }
    public AuxTable( int escopo) {    
         this.escopo=escopo;
    }
    
      public int getTipoAuxTable() {
        return tipo;
    }
         public int getEscopo(){
          return escopo;
      }

    public String getLexemaAuxTable() {
        return nomeDeVariavel;
    }
    public void setTipoAuxTable(int tipo) {
        this.tipo = tipo;
    }

    public void setLexemaAuxTable(String lexema) {
        this.nomeDeVariavel = lexema;
    }

    @Override
    public  String toString() {
        return "TipoAuxTable ( "+this.tipo+ ") LexemaAuxTable: " + this.nomeDeVariavel + " Escopo:" + this.escopo ;
    }

 
 
}
