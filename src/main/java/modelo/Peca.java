package modelo;

import java.util.ArrayList;

public abstract class Peca {

    private Cor cor;
    private Posicao posicao;
    private int valor;

    public Peca(Cor cor, Posicao posicao, int valor) {
        this.cor = cor;
        this.posicao = posicao;
        this.valor = valor;
    }

    public Cor getCor() {
        return cor;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public void setPosicao(Posicao posicao) {
        this.posicao = posicao;
    }

    public int getValor() {
        return valor;
    }

    public abstract ArrayList<Posicao> calcularMovimentosPossiveis(Tabuleiro tabuleiro);

    public abstract String getSimbolo();
}
