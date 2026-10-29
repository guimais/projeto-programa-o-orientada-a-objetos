package modelo;

public class Cor {

    private boolean branca;

    public Cor(boolean branca) {
        this.branca = branca;
    }

    public boolean isBranca() {
        return branca;
    }

    public String getNome() {
        if (branca) {
            return "Branca";
        } else {
            return "Preta";
        }
    }

    public boolean mesmaCor(Cor outra) {
        return branca == outra.isBranca();
    }
}