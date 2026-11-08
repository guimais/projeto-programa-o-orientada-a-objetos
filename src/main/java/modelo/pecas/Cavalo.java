package modelo.pecas;

import java.util.ArrayList;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;

public class Cavalo extends Peca {

    public Cavalo(Cor cor, Posicao posicao) {
        super(cor, posicao, 3);
    }

    @Override
    public ArrayList<Posicao> calcularMovimentosPossiveis(Tabuleiro tabuleiro) {
        ArrayList<Posicao> movimentos = new ArrayList<>();
        Posicao atual = getPosicao();
        int[][] saltos = {
                {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
                {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int i = 0; i < saltos.length; i++) {
            int linha = atual.getLinha() + saltos[i][0];
            int coluna = atual.getColuna() + saltos[i][1];
            Posicao destino = new Posicao(linha, coluna);

            if (tabuleiro.estaDentro(destino)) {
                Peca peca = tabuleiro.getPeca(destino);
                if (peca == null || !peca.getCor().mesmaCor(getCor())) {
                    movimentos.add(destino);
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