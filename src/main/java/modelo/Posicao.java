package modelo;

public class Posicao {

    private int linha;
    private int coluna;

    public Posicao(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public boolean mesmaPosicao(Posicao outra) {
        return linha == outra.getLinha() && coluna == outra.getColuna();
    }

    public String getNome() {
        String letras = "abcdefgh";
        int numero = 8 - linha;
        return letras.charAt(coluna) + "" + numero;
    }
}