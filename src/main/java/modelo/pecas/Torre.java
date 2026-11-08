package modelo.pecas;

import java.util.ArrayList;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;

public class Torre extends Peca {

    public Torre(Cor cor, Posicao posicao) {
        super(cor, posicao, 5);
    }

    @Override
    public ArrayList<Posicao> calcularMovimentosPossiveis(Tabuleiro tabuleiro) {
        ArrayList<Posicao> movimentos = new ArrayList<>();
        Posicao atual = getPosicao();
        int[][] direcoes = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}
        };

        for (int i = 0; i < direcoes.length; i++) {
            int linha = atual.getLinha() + direcoes[i][0];
            int coluna = atual.getColuna() + direcoes[i][1];
            Posicao destino = new Posicao(linha, coluna);

            while (tabuleiro.estaDentro(destino)) {
                Peca peca = tabuleiro.getPeca(destino);

                if (peca == null) {
                    movimentos.add(destino);
                } else {
                    if (!peca.getCor().mesmaCor(getCor())) {
                        movimentos.add(destino);
                    }
                    break;
                }

                linha = linha + direcoes[i][0];
                coluna = coluna + direcoes[i][1];
                destino = new Posicao(linha, coluna);
            }
        }

        return movimentos;
    }

    @Override
    public String getSimbolo() {
        return "T";
    }
}