package modelo.pecas;

import java.util.ArrayList;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;

public class Peao extends Peca {

    public Peao(Cor cor, Posicao posicao) {
        super(cor, posicao, 1);
    }

    @Override
    public ArrayList<Posicao> calcularMovimentosPossiveis(Tabuleiro tabuleiro) {
        ArrayList<Posicao> movimentos = new ArrayList<>();
        Posicao atual = getPosicao();

        int direcao = 1;
        int linhaInicial = 1;
        if (getCor().isBranca()) {
            direcao = -1;
            linhaInicial = 6;
        }

        Posicao umaCasa = new Posicao(atual.getLinha() + direcao, atual.getColuna());
        if (tabuleiro.estaDentro(umaCasa) && tabuleiro.getPeca(umaCasa) == null) {
            movimentos.add(umaCasa);

            Posicao duasCasas = new Posicao(atual.getLinha() + 2 * direcao, atual.getColuna());
            if (atual.getLinha() == linhaInicial && tabuleiro.estaDentro(duasCasas) && tabuleiro.getPeca(duasCasas) == null) {
                movimentos.add(duasCasas);
            }
        }

        int[] diagonais = {-1, 1};
        for (int i = 0; i < diagonais.length; i++) {
            Posicao diagonal = new Posicao(atual.getLinha() + direcao, atual.getColuna() + diagonais[i]);
            Peca peca = tabuleiro.getPeca(diagonal);
            if (peca != null && !peca.getCor().mesmaCor(getCor())) {
                movimentos.add(diagonal);
            }
        }

        return movimentos;
    }

    @Override
    public String getSimbolo() {
        return "P";
    }
}