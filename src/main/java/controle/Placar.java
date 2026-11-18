package controle;

import modelo.Cor;
import modelo.Peca;

public class Placar {

    // Pontos das brancas, começa em 0
    private int pontosBrancas;
    // Pontos das pretas, começa em 0
    private int pontosPretas;

    // Soma o valor da peça capturada para quem capturou
    public void registrarCaptura(Peca capturada) {
        // Só soma pontos quando alguma peça foi capturada
        if (capturada != null) {
            // Peça branca capturada vale ponto para as pretas
            if (capturada.getCor().isBranca()) {
                pontosPretas = pontosPretas + capturada.getValor();
            } else {
                // Peça preta capturada vale ponto para as brancas
                pontosBrancas = pontosBrancas + capturada.getValor();
            }
        }
    }

    // Devolve os pontos da cor pedida
    public int getPontos(Cor cor) {
        if (cor.isBranca()) {
            return pontosBrancas;
        }
        return pontosPretas;
    }
}