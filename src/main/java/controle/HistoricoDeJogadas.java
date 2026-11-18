package controle;

import java.util.ArrayList;

import modelo.Posicao;

public class HistoricoDeJogadas {

    // Lista com os lances da partida em texto, como "e2-e4"
    private ArrayList<String> lances = new ArrayList<>();

    // Guarda um lance no fim da lista
    public void registrar(Posicao origem, Posicao destino) {
        lances.add(origem.getNome() + "-" + destino.getNome());
    }

    // Devolve todos os lances feitos até agora
    public ArrayList<String> getLances() {
        return lances;
    }
}