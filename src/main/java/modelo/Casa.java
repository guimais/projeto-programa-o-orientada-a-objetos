package modelo;

public class Casa {

    private Posicao posicao;
    private Peca peca;

    public Casa(Posicao posicao) {
        this.posicao = posicao;
        this.peca = null;
    }

    public Posicao getPosicao() {
        return posicao;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public boolean estaVazia() {
        return peca == null;
    }
}