package controle;

import modelo.Cor;
import modelo.Peca;

public class Placar {

    private int pontosBrancas;
    private int pontosPretas;

    public void registrarCaptura(Peca capturada) {
        if (capturada == null) {
            return;
        }
        if (capturada.getCor().isBranca()) {
            pontosPretas = pontosPretas + capturada.getValor();
        } else {
            pontosBrancas = pontosBrancas + capturada.getValor();
        }
    }

    public int getPontos(Cor cor) {
        if (cor.isBranca()) {
            return pontosBrancas;
        }
        return pontosPretas;
    }
}