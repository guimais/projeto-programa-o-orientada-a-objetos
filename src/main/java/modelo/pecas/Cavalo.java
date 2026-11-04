package modelo.pecas;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;

import java.util.ArrayList;

public class Cavalo extends Peca {

    public Cavalo(Cor cor, Posicao posicao) {
        super(cor, posicao, 3);
    }

    @Override
    public ArrayList<Posicao> calcularMovimentosPossiveis(Tabuleiro tabuleiro) {
        ArrayList<Posicao> movimentos = new ArrayList<>();
        Posicao atual = getPosicao();

        int[][] saltos = {
                {-2, -1}, {-2, 1},
                {-1, -2}, {-1, 2},
                {1, -2},  {1, 2},
                {2, -1},  {2, 1}
        };

        for (int i = 0; i < saltos.length; i++) {
            int novaLinha = atual.getLinha() + saltos[i][0];
            int novaColuna = atual.getColuna() + saltos[i][1];
            Posicao destino = new Posicao(novaLinha, novaColuna);

            if (tabuleiro.estaDentro(destino)) {
                Peca pecaNoDestino = tabuleiro.getPeca(destino);

                if (pecaNoDestino == null) {
                    movimentos.add(destino);
                } else {
                    if (!pecaNoDestino.getCor().mesmaCor(getCor())) {
                        movimentos.add(destino);
                    }
                }
            }
        }

        return movimentos;
    }

    @Override
    public String getSimbolo() {
        return "C";
    }
}