package controle;

import java.util.ArrayList;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;
import modelo.pecas.Rainha;

public class GerenciadorDePartida {

    private Tabuleiro tabuleiro = new Tabuleiro();
    private ControladorDeTurnos turnos = new ControladorDeTurnos();
    private Placar placar = new Placar();
    private HistoricoDeJogadas historico = new HistoricoDeJogadas();
    // pode ser EM_ANDAMENTO, XEQUE, XEQUE_MATE ou EMPATE
    private String estado = "EM_ANDAMENTO";
    private int lancesSemCapturaOuPeao = 0;

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public ControladorDeTurnos getTurnos() {
        return turnos;
    }

    public Placar getPlacar() {
        return placar;
    }

    public HistoricoDeJogadas getHistorico() {
        return historico;
    }

    public String getEstado() {
        return estado;
    }

    public int getLancesSemCapturaOuPeao() {
        return lancesSemCapturaOuPeao;
    }

    public void tentarJogada(Posicao origem, Posicao destino) throws JogadaInvalidaException {
        // bloqueia novas jogadas se a partida já acabou
        if (estado.equals("XEQUE_MATE") || estado.equals("EMPATE")) {
            throw new JogadaInvalidaException("A partida terminou");
        }

        Peca peca = tabuleiro.getPeca(origem);
        if (!turnos.podeMover(peca)) {
            throw new JogadaInvalidaException("Não é a vez dessa peça");
        }

        // confere se o destino escolhido está entre os lances válidos da peça
        ArrayList<Posicao> legais = movimentosLegais(origem);
        boolean permitida = false;
        for (int i = 0; i < legais.size(); i++) {
            if (legais.get(i).mesmaPosicao(destino)) {
                permitida = true;
            }
        }
        if (!permitida) {
            throw new JogadaInvalidaException("Movimento inválido");
        }

        Peca capturada = tabuleiro.moverPeca(origem, destino);
        // regra dos 50 lances, zera o contador se moveu peão ou capturou alguma peça
        if (peca.getSimbolo().equals("P") || capturada != null) {
            lancesSemCapturaOuPeao = 0;
        } else {
            lancesSemCapturaOuPeao = lancesSemCapturaOuPeao + 1;
        }
        placar.registrarCaptura(capturada);
        historico.registrar(origem, destino);
        promover(peca, destino);
        turnos.passarVez();
        atualizarEstado();
    }

    public ArrayList<Posicao> movimentosLegais(Posicao origem) {
        ArrayList<Posicao> legais = new ArrayList<>();
        Peca peca = tabuleiro.getPeca(origem);
        if (!turnos.podeMover(peca)) {
            return legais;
        }

        // pega os movimentos da peça e tira os que deixariam o próprio rei em xeque
        ArrayList<Posicao> possiveis = peca.calcularMovimentosPossiveis(tabuleiro);
        for (int i = 0; i < possiveis.size(); i++) {
            if (!deixaReiEmXeque(origem, possiveis.get(i))) {
                legais.add(possiveis.get(i));
            }
        }
        return legais;
    }

    // vê se alguma peça adversária consegue alcançar a casa do rei
    public boolean estaEmXeque(Cor cor) {
        Posicao rei = encontrarRei(cor);

        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = tabuleiro.getPeca(new Posicao(linha, coluna));
                if (peca != null && !peca.getCor().mesmaCor(cor)) {
                    ArrayList<Posicao> ataques = peca.calcularMovimentosPossiveis(tabuleiro);
                    for (int i = 0; i < ataques.size(); i++) {
                        if (ataques.get(i).mesmaPosicao(rei)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // faz o lance de teste no tabuleiro, checa se ficou em xeque e depois desfaz tudo
    private boolean deixaReiEmXeque(Posicao origem, Posicao destino) {
        Peca peca = tabuleiro.getPeca(origem);
        Peca capturada = tabuleiro.moverPeca(origem, destino);
        boolean xeque = estaEmXeque(peca.getCor());
        tabuleiro.moverPeca(destino, origem);
        tabuleiro.colocarPeca(destino, capturada);
        return xeque;
    }

    // percorre o tabuleiro procurando o rei ("R") da cor pedida
    private Posicao encontrarRei(Cor cor) {
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = tabuleiro.getPeca(new Posicao(linha, coluna));
                if (peca != null && peca.getSimbolo().equals("R") && peca.getCor().mesmaCor(cor)) {
                    return peca.getPosicao();
                }
            }
        }
        return null;
    }

    // quando um peão chega na linha 0 ou 7 ele vira rainha automaticamente
    private void promover(Peca peca, Posicao destino) {
        if (peca.getSimbolo().equals("P") && (destino.getLinha() == 0 || destino.getLinha() == 7)) {
            tabuleiro.colocarPeca(destino, new Rainha(peca.getCor(), destino));
        }
    }

    // checa se o jogador da vez tem pelo menos um lance possível no tabuleiro
    private boolean temMovimentoLegal() {
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                if (movimentosLegais(new Posicao(linha, coluna)).size() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean temMaterialInsuficiente() {
        // junta todas as peças que sobraram no tabuleiro tirando os reis
        ArrayList<Peca> pecas = new ArrayList<>();

        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = tabuleiro.getPeca(new Posicao(linha, coluna));
                if (peca != null && !peca.getSimbolo().equals("R")) {
                    pecas.add(peca);
                }
            }
        }

        // só sobraram os dois reis
        if (pecas.size() == 0) {
            return true;
        }

        // rei contra rei com apenas um bispo ou um cavalo não dá mate
        if (pecas.size() == 1) {
            Peca unica = pecas.get(0);
            return unica.getSimbolo().equals("B") || unica.getSimbolo().equals("C");
        }

        // dois bispos de jogadores opostos na mesma cor de casa nunca conseguem dar mate
        if (pecas.size() == 2) {
            Peca primeira = pecas.get(0);
            Peca segunda = pecas.get(1);
            if (primeira.getSimbolo().equals("B") && segunda.getSimbolo().equals("B")) {
                if (!primeira.getCor().mesmaCor(segunda.getCor())) {
                    // soma de linha + coluna par ou ímpar diz a cor da casa no tabuleiro
                    int corCasa1 = (primeira.getPosicao().getLinha() + primeira.getPosicao().getColuna()) % 2;
                    int corCasa2 = (segunda.getPosicao().getLinha() + segunda.getPosicao().getColuna()) % 2;
                    return corCasa1 == corCasa2;
                }
            }
        }

        return false;
    }

    private void atualizarEstado() {
        boolean xeque = estaEmXeque(turnos.getVez());
        boolean temLance = temMovimentoLegal();

        // usa um número pra combinar os casos: 0 normal, 1 xeque, 2 empate, 3 xeque-mate
        int situacao = 0;
        if (xeque) {
            situacao = situacao + 1;
        }
        if (!temLance) {
            situacao = situacao + 2;
        }
        if (temMaterialInsuficiente()) {
            situacao = 2;
        }
        // 50 rodadas (100 jogadas) sem captura nem peão dá empate, mas xeque-mate tem prioridade
        if (lancesSemCapturaOuPeao >= 100 && situacao != 3) {
            situacao = 2;
        }

        switch (situacao) {
            case 1:
                estado = "XEQUE";
                break;
            case 2:
                estado = "EMPATE";
                break;
            case 3:
                estado = "XEQUE_MATE";
                break;
            default:
                estado = "EM_ANDAMENTO";
        }
    }
}
