package modelo.pecas;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;

import java.util.ArrayList;

public class Rei extends Peca {

    public Rei(Cor cor, Posicao posicao) {
        super(cor, posicao, 1000);
    }

    @Override
    public ArrayList<Posicao> calcularMovimentosPossiveis(Tabuleiro tabuleiro) {
        ArrayList<Posicao> movimentos = new ArrayList<>();
        Posicao atual = getPosicao();

        int[][] passos = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int i = 0; i < passos.length; i++) {
            int novaLinha = atual.getLinha() + passos[i][0];
            int novaColuna = atual.getColuna() + passos[i][1];
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
        return "R";
    }
}