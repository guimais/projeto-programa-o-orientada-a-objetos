package controle;

import java.util.ArrayList;

import modelo.Posicao;

public class HistoricoDeJogadas {

    private ArrayList<String> lances = new ArrayList<>();

    public void registrar(Posicao origem, Posicao destino) {
        lances.add(origem.getNome() + "-" + destino.getNome());
    }

    public ArrayList<String> getLances() {
        return lances;
    }
}