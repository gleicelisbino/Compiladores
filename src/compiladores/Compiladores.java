package compiladores;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiladores {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        FileInputStream fonte;
  fonte = new FileInputStream("\\Users\\gracc\\OneDrive\\Documentos\\NetBeansProjects\\Compiladores\\src\\compiladores\\fonte.txt");

 //fonte = new FileInputStream(args[0]);
        Parser p = new Parser(fonte);
        p.programa();

        //Pilha
        //  TabelaDeSimbolos pi = new TabelaDeSimbolos();
        // StringBuffer word = new StringBuffer("carro");
        // int x = 10;
        //   Token teste = new Token(x,word);
        //  teste.setTipo(x);
        // teste.setLexema(word);
        /// pi.empilhar(teste);
        //     while (pi.pilhaVazia() == false) {
        //      System.out.println(pi.desempilhar());
        //    }
    }

}
