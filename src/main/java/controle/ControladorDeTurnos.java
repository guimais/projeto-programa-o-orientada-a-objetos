package controle;

import modelo.Cor;
import modelo.Peca;

public class ControladorDeTurnos {

    private Cor vez;
    private int rodada;

    public ControladorDeTurnos() {
        vez = new Cor(true);
        rodada = 1;
    }

    public Cor getVez() {
        return vez;
    }

    public int getRodada() {
        return rodada;
    }

    public boolean podeMover(Peca peca) {
        return peca != null && peca.getCor().mesmaCor(vez);
    }

    public void passarVez() {
        if (!vez.isBranca()) {
            rodada++;
        }
        vez = new Cor(!vez.isBranca());
    }
}