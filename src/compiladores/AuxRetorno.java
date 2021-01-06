package compiladores;


public class AuxRetorno {
       public int tipo;
        public String nomeDeVariavel;// Lexema: String que forma o token.

  
     public AuxRetorno(int t, String l) {    
        tipo = t;
        this.nomeDeVariavel = l;
    }
      public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
        public String getName() {
        return nomeDeVariavel;
    }

    public void setName(String n) {
        this.nomeDeVariavel = n;
    }

    @Override
    public  String toString() {
           return this.nomeDeVariavel;
    }

  @Override
    public boolean equals(Object o) {
        if (o instanceof AuxRetorno) {
            AuxRetorno qualquer = (AuxRetorno) o;
           return this.tipo==qualquer.tipo
                    && this.nomeDeVariavel.equals(qualquer.nomeDeVariavel);
        } else {
            return false;
        }
    }
 
}


