package compiladores;

import java.io.FileInputStream;
import java.io.IOException;

public class SCAN {

    /**
     * Cria-se uma classe SCAN com o intuito de não perder a variável "la", o
     * que certamente aconteceria se a mesma estivesse presente somente dentro
     * de um método/função. Diferente de C, em que posso criar livremente
     * variáveis globais adicionando um "static" antes.
     */
    /**
     * Tem o nome lookahead porque o programa lerá caractere por caracte,
     * mandará para o buffer até chegar no igual. Cujo o qual armazenará o '='.
     */
    public static char la = ' ';
    public int linha;
    public int coluna;
    public static int data;
    public static StringBuffer str;
    public static Token token;
    public FileInputStream fonte;

    /**
     * Utiliza-se o static (variável de classe) com o intuito de não haver
     * necessidade de instanciar um objeto no main para utilizá-la. ' ' força o
     * while (blank). Necessário para que haja a primeira leitura. Dessa forma o
     * Scanner começará testando. E não lendo. Assim, não lerá o '=' novamente
     * se houver. Porém, no fim do arquivo, deverá sempre ler mais um .
     * Corresponde Seta sem nada do automato, alimenta o lookahead para próxima
     * chamada. Nos estados D && L,_ +1 também, porque não sei quando acaba.
     *
     * @param fonte
     */
    public SCAN(FileInputStream fonte) {

        token = new Token(la, str);
        this.linha = 1;
        this.coluna = 0;
        this.fonte = fonte;
    }

    public Token proxToken() throws IOException {
//Início do token não pode conter caractere branco. Apenas lê.
			while(data != -1){		


        str = new StringBuffer();
        while (Character.isWhitespace(la)) {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
        }
        if (la == 39) {
             str.append(la);
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            if (Character.isLetterOrDigit​(la)) {
                str.append(la);
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
            } else {
                System.out.println("Erro linha " + getLinha() + ", coluna" + getColuna() + ", último token lido " + token + ": Char mal formado.");
                System.exit(0);
            }
            if (la == 39) {
                 str.append(la);
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
                token.setTipo(26);// Char
                token.setLexema(str);
                return token;
            }
            System.out.println("Erro linha " + getLinha() + ", coluna" + getColuna() + ", último token lido " + token + ": Char mal formado.");
            System.exit(0);

        } else if (Character.isDigit(la)) {
            while (Character.isDigit(data)) {
                str.append(la);
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
            }
            if (la == '.') {
                str.append(la);
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
                if (Character.isDigit(data)) {
                    while (Character.isDigit(data)) {
                        str.append(la);
                        data = fonte.read();
                        la = (char) data;
                        getLinhaColuna();
                    }
                    token.setTipo(1);
                    token.setLexema(str);
                    return token;// Retorna float 2345.1234
                } else {
                    System.out.println("Erro linha " + getLinha() + ", coluna" + getColuna() + ", último token lido " + token + ": Float mal formado.");
                    System.exit(0);
                }
            }
            token.setTipo(2);
            token.setLexema(str);

            return token;//Retorna digito int
        } else if (la == '.') {
            str.append(la);
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            if (Character.isDigit(data)) {
                while (Character.isDigit(data)) {
                    str.append(la);
                    data = fonte.read();
                    la = (char) data;
                    getLinhaColuna();
                }
                token.setTipo(25);
                token.setLexema(str);
                return token;// Retorna somente .1234
            }
            System.out.println("Erro linha " + getLinha() + ", coluna" + getColuna() + ", último token lido " + token + ": Float mal formado.");
            System.exit(0);
        } else if (la == '>') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            if ((char) data == '=') {
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();//>134
                token.setTipo(3);
                token.setLexema(null);
                return token;// TOKEN >=
            } else {
                token.setTipo(4);
                token.setLexema(null);
                return token;// TOKEN >
            }

        } else if (la == '<') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            if ((char) data == '=') {
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
                token.setTipo(5);
                token.setLexema(null);
                return token;// TOKEN <=
            } else {
                token.setTipo(6);
                token.setLexema(null);
                return token;// TOKEN <
            }
        } else if (la == '!') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            if ((char) data == '=') {
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
                token.setTipo(7);
                token.setLexema(null);
            } else {
                System.out.println("Erro linha " + getLinha() + ", coluna" + getColuna() + ", último token lido " + token + ": "
                        + "Exclamação (‘!’) não seguida de ‘=’.");
                System.exit(0);
            }
        } else if (la == '\t') {
            getLinhaColuna();
            getLinhaColuna();
            getLinhaColuna();
            getLinhaColuna();
            data = fonte.read();
            la = (char) data;
            token.setTipo(24);
            token.setLexema(null);
            return token;
        } //Se for uma palavra reservada, operador, caracter especial ou delimitador: classificação apenas
        else if (la == '(') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(8);//Abre parêntese ( 
            token.setLexema(null);
            return token;
        } else if (la == ')') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(9);// )
            token.setLexema(null);
            return token;
        } else if (la == '{') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(10);// {
            token.setLexema(null);
            return token;
        } else if (data == -1) {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(000);// COMPILAR COM FIM DE ARQUIVO 
            token.setLexema(null);
            return token;
        } else if (la == '}') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(11);// }
            token.setLexema(null);
            return token;
        } else if (la == ',') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(12);
            token.setLexema(null);
            return token;
        } else if (la == ';') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(13);// ;
            token.setLexema(null);
            return token;
        } else if (la == '*') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(14);// *
            token.setLexema(null);
            return token;
        } else if (la == '+') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(15);
            token.setLexema(null);
            return token; //  +
        } else if (la == '-') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            token.setTipo(16);// -
            token.setLexema(null);
            return token;
        } else if ((Character.isAlphabetic(la)) || la == '_') {
            while (Character.isAlphabetic((char) data) || Character.isDigit((char) data) || (char) data == '_') {
                str.append(la);
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
            }
            if (palavraReservada()) {
                int temp;
                temp = q();
                if (temp == 30) {
                    token.setTipo(30);
                    token.setLexema(null);
                    return token;
                } else if (temp == 31) {
                    token.setTipo(31);
                    token.setLexema(null);
                    return token;
                } else if (temp == 32) {
                    token.setTipo(32);
                    token.setLexema(null);
                    return token;

                } else if (temp == 33) {
                    token.setTipo(33);
                    token.setLexema(null);
                    return token;

                } else if (temp == 34) {
                    token.setTipo(34);
                    token.setLexema(null);
                    return token;

                } else if (temp == 35) {
                    token.setTipo(35);
                    token.setLexema(null);
                    return token;

                } else if (temp == 36) {

                    token.setTipo(36);
                    token.setLexema(null);
                    return token;

                } else if (temp == 37) {
                    token.setTipo(37);
                    token.setLexema(null);
                    return token;

                } else if (temp == 38) {
                    token.setTipo(38);
                    token.setLexema(null);
                    return token;

                }
            } else {
                token.setTipo(29);//Identificador. 
                token.setLexema(str);
                return token;
            }
        } else if (la == '=') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            if ((char) data == '=') {
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
                token.setTipo(20);
                token.setLexema(null);
                return token;
            } else {
                token.setTipo(21);
                token.setLexema(null);
                return token;
            }
        } else if (la == '/') {
            data = fonte.read();
            la = (char) data;
            getLinhaColuna();
            if (la == '/') {
                while (la != '\n') {
                    data = fonte.read();
                    la = (char) data;
                    getLinhaColuna();
                }
            } else if (la == '*') {
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
                do {
                    while (la != '*') {
                        data = fonte.read();
                        la = (char) data;
                        getLinhaColuna();
                        if (data == -1) {
                            break;
                        }
                    }
                    if (data == -1) {
                        System.out.println("Erro linha " + getLinha() + ", coluna " + getColuna() + " .EOF em comentário multilinha");
                        System.exit(0);
                        break;
                    }
                    data = fonte.read();
                    la = (char) data;
                    getLinhaColuna();
                } while (la != '/');
                data = fonte.read();
                la = (char) data;
                getLinhaColuna();
                if (la == '\n') {
                    System.out.println("Erro linha " + getLinha() + ", coluna " + getColuna() + " .EOF em comentário multilinha");
                    System.exit(0);
                }
                if ((char) data == '/') {
                    data = fonte.read();
                    la = (char) data;
                    getLinhaColuna();
                }
                while ((char) data == '*') {
                    data = fonte.read();
                    la = (char) data;
                    getLinhaColuna();
                }
            } else {
                token.setTipo(23);//Operador de divisão
                token.setLexema(null);
                return token;
            }

        } else {
            System.out.println("Erro linha " + getLinha() + ", coluna" + getColuna() + ", último token lido " + token + ": Caracter inválido.");
            System.exit(0);
        }
                        }
        return token;
    }

    public boolean palavraReservada() {
        String p1, p2, p3, p4, p5, p6, p7, p8, p9;
        p1 = "main";
        p2 = "if";
        p3 = "else";
        p4 = "while";
        p5 = "do";
        p6 = "for";
        p7 = "int";
        p8 = "float";
        p9 = "char";
        if (str.toString().equals(p1)) {
            return true;
        } else if (str.toString().equals(p2)) {
            return true;
        } else if (str.toString().equals(p3)) {
            return true;
        } else if (str.toString().equals(p4)) {
            return true;
        } else if (str.toString().equals(p5)) {
            return true;
        } else if (str.toString().equals(p6)) {
            return true;
        } else if (str.toString().equals(p7)) {
            return true;
        } else if (str.toString().equals(p8)) {
            return true;
        } else if (str.toString().equals(p9)) {
            return true;
        } else {
            return false;
            //       pr.append(p2);
        }
    }

    public int q() {
        String p1, p2, p3, p4, p5, p6, p7, p8, p9;
        p1 = "main";
        p2 = "if";
        p3 = "else";
        p4 = "while";
        p5 = "do";
        p6 = "for";
        p7 = "int";
        p8 = "float";
        p9 = "char";
        if (str.toString().equals(p1)) {
            return 30;
        } else if (str.toString().equals(p2)) {
            return 31;
        } else if (str.toString().equals(p3)) {
            return 32;
        } else if (str.toString().equals(p4)) {
            return 33;
        } else if (str.toString().equals(p5)) {
            return 34;
        } else if (str.toString().equals(p6)) {
            return 35;
        } else if (str.toString().equals(p7)) {
            return 36;
        } else if (str.toString().equals(p8)) {
            return 37;
        } else if (str.toString().equals(p9)) {
            return 38;
        } else {
            return 0;
            //       pr.append(p2);
        }
    }

//Cada digito incrementa coluna em 1
    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void getLinhaColuna() {
        if (la == '\n') {
            resetColuna();
            incrLinha();
            getLinha();
        } else {
            incrColuna();
            getColuna();
        }
    }

    public void resetColuna() {
        coluna = 1;
    }

    public void incrColuna() {
        this.coluna++;
    }

    public void incrLinha() {
        this.linha++;
    }
}
