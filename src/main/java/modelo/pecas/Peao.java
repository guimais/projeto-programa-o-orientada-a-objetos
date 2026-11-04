package modelo.pecas;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;

import java.util.ArrayList;

public class Peao extends Peca {

    public Peao(Cor cor, Posicao posicao) {
        super(cor, posicao, 1);
    }

    @Override
    public ArrayList<Posicao> calcularMovimentosPossiveis(Tabuleiro tabuleiro) {
        ArrayList<Posicao> movimentos = new ArrayList<>();
        Posicao atual = getPosicao();

        int direcao;
        int linhaInicial;

        if (getCor().isBranca()) {
            direcao = -1;
            linhaInicial = 6;
        } else {
            direcao = 1;
            linhaInicial = 1;
        }

        // 1. Avanço de 1 casa para a frente
        Posicao frenteUma = new Posicao(atual.getLinha() + direcao, atual.getColuna());
        if (tabuleiro.estaDentro(frenteUma) && tabuleiro.getPeca(frenteUma) == null) {
            movimentos.add(frenteUma);

            // 2. Avanço de 2 casas se estiver na linha inicial e o caminho estiver livre
            Posicao frenteDuas = new Posicao(atual.getLinha() + (2 * direcao), atual.getColuna());
            if (atual.getLinha() == linhaInicial && tabuleiro.estaDentro(frenteDuas) && tabuleiro.getPeca(frenteDuas) == null) {
                movimentos.add(frenteDuas);
            }
        }

        // 3. Capturas nas duas diagonais frontais
        int[] colunasDiagonais = {-1, 1};
        for (int i = 0; i < colunasDiagonais.length; i++) {
            Posicao diagonal = new Posicao(atual.getLinha() + direcao, atual.getColuna() + colunasDiagonais[i]);

            if (tabuleiro.estaDentro(diagonal)) {
                Peca pecaNaDiagonal = tabuleiro.getPeca(diagonal);
                if (pecaNaDiagonal != null) {
                    if (!pecaNaDiagonal.getCor().mesmaCor(getCor())) {
                        movimentos.add(diagonal);
                    }
                }
            }
        }

        return movimentos;
    }

    @Override
    public String getSimbolo() {
        return "P";
    }
}