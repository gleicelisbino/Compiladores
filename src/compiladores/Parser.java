package compiladores;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Stack;
import java.util.*;

public class Parser {
    //  public static String l;
    //public static String ll;

    public static SCAN s;//Mudei para static
    public String variavelTemp;
    public static AuxTable a;
    public int valueLab;
    public static Token la;
    public FileInputStream fonte;
    public static int escopo;
    public String tempp;
    public int valueT;
    public static Stack<AuxTable> t;
    public static AuxRetorno retorno;
    Collection<AuxTable> ax = new ArrayList();
    public static Collection<AuxTable> x = new ArrayList();

    public Parser(FileInputStream fonte) {
        retorno = new AuxRetorno(0, "null");
        valueLab = 0;
        valueT = 0;
        escopo = 0;
        this.fonte = fonte;
        s = new SCAN(fonte);
        t = new Stack<>();
        tempp = null;
    }

    public void icrEscopo() {
        escopo++;
    }

    public void dcrEscopo() {
        escopo--;
    }

    public AuxRetorno intTOfloat(AuxRetorno Aux1, String later) {
        System.out.println("float " + Aux1.nomeDeVariavel);
        Aux1.setName(variavelTemp);
        Aux1.setTipo(37);
        return Aux1;
    }

    public AuxRetorno addClone(String gamb) {

        if (gamb != null) {
            AuxTable d;
            for (AuxTable v : ax) {
                int tip;
                if (v.nomeDeVariavel.equals(gamb)) {

                    if (v.nomeDeVariavel.equals(gamb) && v.escopo == escopo) {

                        tip = t.lastIndexOf(v);
                        d = t.set(tip, v);
                        retorno = new AuxRetorno(d.tipo, d.nomeDeVariavel);
                        retorno.setName(d.nomeDeVariavel);
                        retorno.setTipo(d.tipo);
                        // System.out.println(d.tipo);

                        return retorno;
                    }
                    if (v.nomeDeVariavel.equals(gamb)) {

                        tip = t.lastIndexOf(v);
                        d = t.set(tip, v);
                        retorno = new AuxRetorno(d.tipo, d.nomeDeVariavel);
                        retorno.setName(d.nomeDeVariavel);
                        retorno.setTipo(d.tipo);
                        // System.out.println(d.tipo);

                        return retorno;
                    }
                }

            }

        }
        retorno = new AuxRetorno(0, "null");
        return retorno;

    }

    public void remov() {
        for (AuxTable vog : ax) {
            if (vog.escopo == escopo) {
                x.add(vog);

            }
        }

    }

    public String label() {
        String lab = "_L" + this.valueLab;
        this.valueLab++;
        return lab;
    }

    public void add(int g) {
        a = new AuxTable(g, tempp, escopo);
        if (t.empty() == true) {
            t.addElement(a);
            ax.add(a);

        } else if (t.empty() == false) {
            if (t.contains(a) == false) {
                t.addElement(a);
                ax.add(a);
            } else {
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Variável já declarada. ");
                System.exit(0);
            }
        }
    }

    public boolean NullPointerBegin() throws IOException {
        if (Token.getTipo() == 000) {
            System.exit(0);
            return true;
        } else {
            return false;
        }
    }

    public String newVarTemporaria() {
        String tt = "_t" + valueT;
        this.valueT++;
        return tt;
    }

    public void programa() throws IOException {
        la = s.proxToken();
        NullPointerBegin();
        if (Token.getTipo() == 36) {//testando int.
            la = s.proxToken();
            if (Token.getTipo() == 30) {//testando main
                la = s.proxToken();
                if (Token.getTipo() == 8) {//testando (.
                    la = s.proxToken();
                    if (Token.getTipo() == 9) {//testando ) 
                        la = s.proxToken();
                        if (Token.getTipo() == 10) {// testando {
                            bloco();

                        } else {
                            System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation {");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation )");
                        System.exit(0);
                    }
                } else {
                    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation (");
                    System.exit(0);
                }
            } else {
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation MAIN");
                System.exit(0);
            }
        } else {
            System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation INT");
            System.exit(0);
        }
    }

    public void bloco() throws IOException {
        if (Token.getTipo() == 10) { // testando {
            icrEscopo();
            la = s.proxToken();
            while (Token.getTipo() == 36 || Token.getTipo() == 37 || Token.getTipo() == 38) { // testando 1 float|| 2 int|| 26 char       
                declararVariavel();
            }
            while (Token.getTipo() == 10 || Token.getTipo() == 33 || Token.getTipo() == 34 || Token.getTipo() == 31 || Token.getTipo() == 29) {//  if || while||do||<id>
                comando();
            }
            if (Token.getTipo() == 11) {
                remov();
                t.removeAll(x);
                ax.removeAll(x);
                x.clear();
                dcrEscopo();
                la = s.proxToken();
            } else {
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation Caracter }");
                System.exit(0);
            }
        } else {
            System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation {");
            System.exit(0);
        }
    }

    public void comando() throws IOException {
        switch (Token.getTipo()) {
            case 29:
            case 10:
                comandoBasico();// Não há print.
                break;
            case 33:
            case 34:
                interacao();// Não há print.
                break;
            case 31:
                ifelse();
                break;
            default:
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation cB||inter||if");
                System.exit(0);
        }
    }

    public void ifelse() throws IOException {
        if (Token.getTipo() == 31) {// IF
            la = s.proxToken();
            if (Token.getTipo() == 8) {
                la = s.proxToken();
                expRelacional();
                String l = label();
                System.out.println("If " + variavelTemp + " == 0 Goto " + l);
                if (Token.getTipo() == 9) {
                    la = s.proxToken();
                    comando();
                    String ll = label();
                    System.out.println("Goto " + ll);
                    System.out.println(l + ":");
                    if (Token.getTipo() == 32) {//else
                        s.proxToken();
                        comando();
                    }
                    System.out.println(ll + ":");
                }
            } else {
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation ");
                System.exit(0);
            }
        } else {
            System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation ");
            System.exit(0);
        }
    }

    public void comandoBasico() throws IOException {
        switch (Token.getTipo()) {
            case 29:// testando ID
                atribuicao();
                break;
            case 10:// incrmenta
                bloco();
                break;
            default:
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation bloco|| atr");
                System.exit(0);
        }
    }

    private void interacao() throws IOException {
        switch (Token.getTipo()) {
            case 33:// testando while
                la = s.proxToken();
                if (Token.getTipo() == 8) {// testando (
                    la = s.proxToken();
                    String l = label();
                    System.out.println(l + ":");
                    expRelacional();
                    String ll = label();
                    System.out.println("If " + variavelTemp + " == 0 Goto " + ll);// verificar se o print é exp relacionald
                    if (Token.getTipo() == 9) {// testando )
                        la = s.proxToken();
                        comando();
                        System.out.println("Goto " + l);
                        System.out.println(ll + ":");
                    } else {
                        System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation { ");
                        System.exit(0);
                    }
                } else {
                    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation )");
                    System.exit(0);
                }
                break;
            case 34://do
                la = s.proxToken();
                String l = label();
                System.out.println(l + ":");
                comando();
                if (Token.getTipo() == 33) {//while
                    la = s.proxToken();
                    if (Token.getTipo() == 8) {//testando (
                        la = s.proxToken();
                        expRelacional();
                        String ll = label();
                        System.out.println("If " + variavelTemp + " == 0 Goto " + ll);// verificar se lexema é exp relacional
                        System.out.println("Goto " + l);
                        System.out.println(ll + ":");
                        if (Token.getTipo() == 9) {//testando )
                            la = s.proxToken();
                            if (Token.getTipo() == 13) {//Equivale a ;
                                la = s.proxToken();
                            } else {
                                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation ; ");
                            }
                        } else {
                            System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation )");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation (");
                        System.exit(0);
                    }
                } else {
                    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation ");
                    System.exit(0);
                }
                break;
            default:
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation WHILE -> ( ");
                System.exit(0);
        }
    }

    public AuxRetorno F() throws IOException {
        AuxRetorno aux1;
        String g;

        aux1 = new AuxRetorno(0, "null");
        switch (Token.tipo) {
            case 29:
                g = la.lexema.toString();
                la = s.proxToken();
                return addClone(g);
            case 1:
            case 2:
            case 26:
            case 25://testando id||float||char||
                la = s.proxToken();
                break;
            case 8: //testando (.
                la = s.proxToken();
                aux1 = e();
                if (Token.getTipo() == 9) {// testando )
                    la = s.proxToken();
                    return aux1;
                } else {
                    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation )");
                    System.exit(0);
                }
                break;
            default:
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation int, id, float or CHARuu");
                System.exit(0);
        }

        return aux1;
    }

    public AuxRetorno t() throws IOException {// Termo
        AuxRetorno aux1, aux2;
        String sb = null, sbb = null;
        int type = Token.tipo;
        if (la.lexema != null) {
            sb = la.lexema.toString();
        }

        aux1 = F();

        if (aux1.tipo == 0) {
            aux1.setTipo(type);
            aux1.setName(sb);
        }
        while (Token.tipo == 14 || Token.tipo == 23) {// testando * || /
            int steste = Token.tipo;
            la = s.proxToken();
            int typeAfter = Token.tipo;
            if (la.lexema != null) {
                sbb = la.lexema.toString();
            }

            aux2 = F();

            if (aux2.tipo == 0) {
                aux2.setTipo(typeAfter);
                aux2.setName(sbb);
            }
            if (aux1.tipo == 0) {
                if (type == 2 && typeAfter == 1) {
                    aux1.setTipo(36);
                    aux1.setName(sb);
                    aux2.setTipo(37);
                    aux2.setName(sbb);
                } else if (type == 2 && typeAfter == 2) {
                    aux1.setTipo(36);
                    aux1.setName(sb);
                    aux2.setTipo(36);
                    aux2.setName(sbb);
                } else if (type == 1 && typeAfter == 1) {
                    aux1.setTipo(37);
                    aux1.setName(sb);
                    aux2.setTipo(37);
                    aux2.setName(sbb);
                }
            } else if (aux1.tipo == 2 && typeAfter == 1) {
                aux2.setTipo(37);
                aux2.setName(sbb);
            } else if (aux1.tipo == 2 && typeAfter == 2) {
                aux2.setTipo(36);
                aux2.setName(sbb);
            } else if (aux1.tipo == 1 && typeAfter == 1) {
                aux2.setTipo(37);
                aux2.setName(sbb);
            } else if (typeAfter == 1) {
                aux2.setTipo(37);
                aux2.setName(sbb);
            } else if (typeAfter == 2) {
                aux2.setTipo(36);
                aux2.setName(sbb);
            }

            if (aux1.tipo == 37 || aux1.tipo == 1) {//INT VAI VIRAR   || type==2   
                if (aux2.tipo == 36) {//FLOAT || typeAfter==1
                    String later = variavelTemp;
                    variavelTemp = newVarTemporaria();
                    System.out.print(variavelTemp + " = ");
                    aux2 = intTOfloat(aux2, later);
                }
            }
            if (aux2.tipo == 37) {//INT VAI VIRAR   || type==2   
                if (aux1.tipo == 36 || aux1.tipo == 2) {//FLOAT || typeAfter==1
                    String later = variavelTemp;
                    variavelTemp = newVarTemporaria();
                    System.out.print(variavelTemp + " = ");                                                                                  //AN?
                    aux1 = intTOfloat(aux1, later);
                }
            }
            // if (aux1.tipo == 38 && aux2.tipo != 38 && aux2.tipo != 0
            //      || aux2.tipo == 38 && aux1.tipo != 38 && aux1.tipo != 0) {//teste char
            //    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Char só é compatível com ");
            //         System.exit(0);
            ///   } else 

            if (typeAfter == 26 && type == 1
                    || typeAfter == 26 && type == 2
                    || typeAfter == 26 && type == 25
                    || type == 26 && typeAfter == 1
                    || type == 26 && typeAfter == 2
                    || type == 26 && typeAfter == 25) {
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation char ");
                System.exit(0);
            }
            if (aux1.tipo != 0 && aux2.tipo != 0) {
                variavelTemp = newVarTemporaria();
                switch (steste) {
                    case 14:
                        System.out.println(variavelTemp + " = " + aux1 + " * " + aux2);
                        aux2.setName(variavelTemp);
                        break;
                    case 23:
                        System.out.println(variavelTemp + " = " + aux1 + " / " + aux2);
                        aux2.setName(variavelTemp);

                        break;
                }
            }
            if (steste == 23) {

                if (aux1.tipo == 36 && aux2.tipo == 36) {//INT VAI VIRAR   || type==2   

                    String later = variavelTemp;
                    variavelTemp = newVarTemporaria();
                    System.out.print(variavelTemp + " = ");
                    aux2 = intTOfloat(aux2, later);
                }
            }
            aux1 = aux2;

        }
        return aux1;
    }

    public AuxRetorno e() throws IOException {//<expr_arit> 
        AuxRetorno aux1, aux2;

        String sbb = null, sb = null;
        int type = Token.tipo;
        if (la.lexema != null) {
            sb = la.lexema.toString();
        }
        aux1 = t();
        while (Token.tipo == 15 || Token.tipo == 16) {// testando +

            int steste = Token.tipo;
            la = s.proxToken();

            int typeAfter = Token.tipo;
            if (la.lexema != null) {
                sbb = la.lexema.toString();
            }

            aux2 = t();
            if (aux1.tipo == 0 && sb != null && sbb != null) {
                if (type == 2 && typeAfter == 1) {
                    aux1.setTipo(36);
                    aux1.setName(sb);
                    aux2.setTipo(37);
                    aux2.setName(sbb);
                } else if (type == 2 && typeAfter == 2) {
                    aux1.setTipo(36);
                    aux1.setName(sb);
                    aux2.setTipo(36);
                    aux2.setName(sbb);
                } else if (type == 1 && typeAfter == 1) {
                    aux1.setTipo(37);
                    aux1.setName(sb);
                    aux2.setTipo(37);
                    aux2.setName(sbb);
                }
            } else if (type == 2 && typeAfter == 1) {
                aux2.setTipo(37);
                aux2.setName(sbb);
            } else if (type == 2 && typeAfter == 2) {
                aux2.setTipo(36);
                aux2.setName(sbb);
            } else if (type == 1 && typeAfter == 1) {
                aux2.setTipo(37);
                aux2.setName(sbb);
            } else if (typeAfter == 1) {
                aux2.setTipo(37);
                aux2.setName(sbb);
            } else if (typeAfter == 2) {
                aux2.setTipo(36);
                aux2.setName(sbb);
            }
            if (aux1.tipo == 37 || aux1.tipo == 1) {
                if (aux2.tipo == 36 || aux2.tipo == 2) {
                    String later = variavelTemp;
                    variavelTemp = newVarTemporaria();
                    System.out.print(variavelTemp + " = ");
                    aux2 = intTOfloat(aux2, later);
                }
            }
            if (aux2.tipo == 37 || aux2.tipo == 1) {//INT VAI VIRAR   || type==2   
                if (aux1.tipo == 36 || aux2.tipo == 2) {//FLOAT || typeAfter==1
                    String later = variavelTemp;
                    variavelTemp = newVarTemporaria();
                    System.out.print(variavelTemp + " = ");
                    aux1.setName(later);                                                                                      //AN?
                    aux1 = intTOfloat(aux1, later);
                }

            }
            if (aux1.tipo == 2 && aux2.tipo == 37) {//FLOAT || typeAfter==1
                String later = variavelTemp;
                variavelTemp = newVarTemporaria();
                System.out.print(variavelTemp + " = ");                                                                                   //AN?
                aux1 = intTOfloat(aux1, later);
            }
            if (aux1.tipo == 38 && aux2.tipo != 26 && aux2.tipo != 38 && aux2.tipo != 0
                    || aux2.tipo == 38 && aux1.tipo != 26 && aux1.tipo != 38 && aux1.tipo != 0) {//teste char

                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Char só é compatível com char..");
                System.exit(0);
            } else if (typeAfter == 26 && type == 1
                    || typeAfter == 26 && type == 2
                    || typeAfter == 26 && type == 25
                    || type == 26 && typeAfter == 1
                    || type == 26 && typeAfter == 2
                    || type == 26 && typeAfter == 25) {
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation char ");
                System.exit(0);
            }
            if (aux1.tipo != 0 && aux2.tipo != 0) {
                variavelTemp = newVarTemporaria();
                switch (steste) {
                    case 15:
                        System.out.println(variavelTemp + " = " + aux1 + " + " + aux2);//print em lexema exp relacional
                        aux2.setName(variavelTemp);
                        break;
                    case 16:
                        System.out.println(variavelTemp + " = " + aux1 + " - " + aux2);//print em lexema exp relacional
                        aux2.setName(variavelTemp);
                        break;

                }
            }
            aux1 = aux2;

        }
        return aux1;
    }

    public AuxRetorno atribuicao() throws IOException {
        AuxRetorno aux1 = null, aux2 = null;
        String sbb, sb;
        if (Token.getTipo() == 29) {// testando ID
            sbb = la.lexema.toString();
            la = s.proxToken();
            if (Token.getTipo() == 21) {// testando =
                la = s.proxToken();//pega um int tipo2
                int type = Token.tipo;
                sb = la.lexema.toString();

                aux1 = e();
                if (aux1.tipo == 1) {
                    aux1.setTipo(37);
                } else if (aux1.tipo == 2) {
                    aux1.setTipo(36);

                }
                aux2 = addClone(sbb);
                if (aux2.tipo == 37 && type == 2 && aux1.tipo == 36) {
                    String later = variavelTemp;
                    variavelTemp = newVarTemporaria();
                    String dont = aux2.getName();
                    aux2.setName(later);
                    System.out.print(variavelTemp + " = ");
                    aux2 = intTOfloat(aux2, later);
                    System.out.println(dont + " = " + aux2);
                    if (Token.getTipo() == 13) {
                        la = s.proxToken();
                        aux2.setName(later);
                        return aux2;
                    }
                }
                if (aux1.tipo != 0) {
                    System.out.println(aux2 + " = " + aux1);

                } else if (aux1.tipo == 0) {
                    System.out.println(aux2 + " = " + sb);
                }       if (aux1.tipo == 38 && aux2.tipo != 38 && aux2.tipo != 0
                              || aux2.tipo == 38 && aux1.tipo != 38 && aux1.tipo != 0) {//teste char
                         System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : x");
                        System.exit(0);
                  }
                else if (aux1.tipo != 38 && type == 26 && aux2.tipo != 38 && aux1.tipo != 0 && aux2.tipo != 0) {
                    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation char ");
                    System.exit(0);
                } else if (type == 2 && aux1.tipo == 38 || type == 2 && aux2.tipo == 38
                        || type == 1 && aux1.tipo == 38 || type == 1 && aux2.tipo == 38
                        || type == 25 && aux1.tipo == 38 || type == 25 && aux2.tipo == 38) {
                    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation int ");
                    System.exit(0);
                } else if (aux2.tipo == 36 && aux1.tipo != 36 && aux1.tipo != 0) {
                    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Não pode atribuir float a int ");
                    System.exit(0);
                }
                if (Token.getTipo() == 13) {
                    la = s.proxToken();
                } else {
                    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation ; ");
                    System.exit(0);
                }
            } else {
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Formação incompleta  ");
                System.exit(0);
            }
        } else {
            System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation ID");
            System.exit(0);
        }

        aux1 = aux2;
        //  aux1.setName(variavelTemp);    
        return aux1;
    }

    public AuxRetorno expRelacional() throws IOException {
        AuxRetorno aux1, aux2;
        int type = Token.tipo;
        int tt = 0;
        aux1 = e();
        if (Token.getTipo() == 20 || Token.getTipo() == 3 || Token.getTipo() == 4 || Token.getTipo() == 5 || Token.getTipo() == 6 || Token.getTipo() == 7) {
            tt = Token.getTipo();
            la = s.proxToken();

        }
        aux2 = e();
        int typeAfter = Token.tipo;
        if (typeAfter == 26 && type == 1
                || typeAfter == 26 && type == 2
                || typeAfter == 26 && type == 25
                || type == 26 && typeAfter == 1
                || type == 26 && typeAfter == 2
                || type == 26 && typeAfter == 25) {
            System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation char ");
            System.exit(0);
        }
        if (aux1.tipo == 37 || aux1.tipo == 1) {//INT VAI VIRAR   || type==2   
            if (aux2.tipo == 36 || aux2.tipo == 2) {//FLOAT || typeAfter==1

                String later = variavelTemp;
                variavelTemp = newVarTemporaria();
                System.out.print(variavelTemp + " = ");
                aux2 = intTOfloat(aux2, later);
            }
        }
        if (aux2.tipo == 37 || aux2.tipo == 1) {//INT VAI VIRAR   || type==2   
            if (aux1.tipo == 36 || aux1.tipo == 2) {//FLOAT || typeAfter==1
                String later = variavelTemp;
                variavelTemp = newVarTemporaria();
                System.out.print(variavelTemp + " = ");
                //AN?
                aux1 = intTOfloat(aux1, later);
            }
        }

        if (aux1.tipo != 0 && aux2.tipo != 0) {
            variavelTemp = newVarTemporaria();
            switch (tt) {
                case 3:
                    System.out.println(variavelTemp + " = " + aux1 + " >= " + aux2);//print em lexema exp relacional
                    aux2.setName(variavelTemp);
                    break;
                case 4:
                    System.out.println(variavelTemp + " = " + aux1 + " > " + aux2);//print em lexema exp relacional
                    aux2.setName(variavelTemp);
                    break;
                case 5:
                    System.out.println(variavelTemp + " = " + aux1 + " <= " + aux2);//print em lexema exp relacional
                    aux2.setName(variavelTemp);
                    break;
                case 6:
                    System.out.println(variavelTemp + " = " + aux1 + " < " + aux2);//print em lexema exp relacional
                    aux2.setName(variavelTemp);
                    break;
                case 7:
                    System.out.println(variavelTemp + " = " + aux1 + " != " + aux2);//print em lexema exp relacional
                    aux2.setName(variavelTemp);
                    break;
                case 20://testando id||float||char||
                    System.out.println(variavelTemp + " = " + aux1 + " == " + aux2);//print em lexema exp relacional
                    aux2.setName(variavelTemp);
                    break;
            }
        }
        aux1 = aux2;
        return aux1;
    }

    public void declararVariavel() throws IOException {//  int gambiarraa=0;
        if (Token.tipo == 36 || Token.getTipo() == 37 || Token.getTipo() == 38) {// int, float ou char
            int gambiarraa = Token.tipo;
            tipo();
            if (Token.tipo == 29) {
                tempp = la.getLexema().toString();
                add(gambiarraa);
                la = s.proxToken();
                while (Token.tipo == 29 || Token.tipo == 12) {// id ou ,
                    if (Token.tipo == 12) {
                        la = s.proxToken();
                        tempp = la.getLexema().toString();
                        add(gambiarraa);
                    } else {
                        la = s.proxToken();
                    }
                }
                if (Token.tipo == 13) {
                    la = s.proxToken();
                } else {
                    System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation ;" + la);
                    System.exit(0);
                }
            } else {
                System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation id||,");
                System.exit(0);
            }
        } else {
            System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + " : Expectation int||float||char");
            System.exit(0);
        }
    }

    public void tipo() throws IOException {
        if (Token.tipo == 36 || Token.getTipo() == 37 || Token.getTipo() == 38) {// int float ou char
            la = s.proxToken();
        } else {
            System.out.println("ERRO na linha " + s.getLinha() + " , coluna " + s.getColuna() + "Não é int||float||char.");
            System.exit(0);
        }
    }

}
