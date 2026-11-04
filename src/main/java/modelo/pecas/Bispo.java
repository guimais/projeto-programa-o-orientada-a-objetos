package modelo.pecas;
import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;

import java.util.ArrayList;

public class Bispo extends Peca {

    public Bispo(Cor cor, Posicao posicao) {
        super(cor, posicao, 3);
    }

    @Override
    public ArrayList<Posicao> calcularMovimentosPossiveis(Tabuleiro tabuleiro) {
        ArrayList<Posicao> movimentos = new ArrayList<>();
        Posicao atual = getPosicao();

        int[][] direcoes = {
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int i = 0; i < direcoes.length; i++) {
            int novaLinha = atual.getLinha() + direcoes[i][0];
            int novaColuna = atual.getColuna() + direcoes[i][1];
            Posicao destino = new Posicao(novaLinha, novaColuna);

            while (tabuleiro.estaDentro(destino)) {
                Peca pecaNoDestino = tabuleiro.getPeca(destino);

                if (pecaNoDestino == null) {
                    movimentos.add(destino);
                } else {
                    if (!pecaNoDestino.getCor().mesmaCor(getCor())) {
                        movimentos.add(destino);
                    }
                    break;
                }

                novaLinha = novaLinha + direcoes[i][0];
                novaColuna = novaColuna + direcoes[i][1];
                destino = new Posicao(novaLinha, novaColuna);
            }
        }

        return movimentos;
    }

    @Override
    public String getSimbolo() {
        return "B";
    }
}