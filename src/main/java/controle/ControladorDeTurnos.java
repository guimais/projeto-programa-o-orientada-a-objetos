package controle;

import modelo.Cor;
import modelo.Peca;

public class ControladorDeTurnos {

    // Cor de quem joga agora
    private Cor vez;
    // Número da rodada atual
    private int rodada;

    // A partida começa com as brancas, na rodada 1
    public ControladorDeTurnos() {
        vez = new Cor(true);
        rodada = 1;
    }

    // Devolve a cor de quem joga agora
    public Cor getVez() {
        return vez;
    }

    // Devolve o número da rodada atual
    public int getRodada() {
        return rodada;
    }

    // Só pode mover se existir uma peça e ela for da cor da vez
    public boolean podeMover(Peca peca) {
        return peca != null && peca.getCor().mesmaCor(vez);
    }

    // Troca a vez e soma uma rodada depois que as pretas jogam
    public void passarVez() {
        // Quando as pretas terminam de jogar, a rodada fecha
        if (!vez.isBranca()) {
            rodada++;
        }
        // Inverte a cor: branca vira preta e preta vira branca
        vez = new Cor(!vez.isBranca());
    }
}