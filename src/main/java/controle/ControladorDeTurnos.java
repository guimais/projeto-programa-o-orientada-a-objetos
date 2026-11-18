package controle;

import modelo.Cor;
import modelo.Peca;

public class ControladorDeTurnos {

    private Cor vez;
    private int rodada;

    public ControladorDeTurnos() {
        // brancas sempre começam na rodada 1
        vez = new Cor(true);
        rodada = 1;
    }

    public Cor getVez() {
        return vez;
    }

    public int getRodada() {
        return rodada;
    }

    // verifica se existe peça na casa e se ela é da cor que joga agora
    public boolean podeMover(Peca peca) {
        return peca != null && peca.getCor().mesmaCor(vez);
    }

    public void passarVez() {
        // uma rodada só termina depois que as pretas jogam
        if (!vez.isBranca()) {
            rodada++;
        }
        vez = new Cor(!vez.isBranca());
    }
}
