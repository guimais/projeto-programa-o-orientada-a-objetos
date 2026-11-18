package controle;

import java.util.ArrayList;

import modelo.Posicao;

public class HistoricoDeJogadas {

    private ArrayList<String> lances = new ArrayList<>();

    // salva o lance como texto no formato "e2-e4"
    public void registrar(Posicao origem, Posicao destino) {
        lances.add(origem.getNome() + "-" + destino.getNome());
    }

    public ArrayList<String> getLances() {
        return lances;
    }
}
