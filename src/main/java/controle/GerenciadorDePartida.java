package controle;

import java.util.ArrayList;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;
import modelo.pecas.Peao;
import modelo.pecas.Rainha;
import modelo.pecas.Rei;

public class GerenciadorDePartida {

    private Tabuleiro tabuleiro = new Tabuleiro();
    private ControladorDeTurnos turnos = new ControladorDeTurnos();
    private Placar placar = new Placar();
    private HistoricoDeJogadas historico = new HistoricoDeJogadas();
    private String estado = "EM_ANDAMENTO";

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

    public void tentarJogada(Posicao origem, Posicao destino) throws JogadaInvalidaException {
        if (estado.equals("XEQUE_MATE") || estado.equals("EMPATE")) {
            throw new JogadaInvalidaException("A partida terminou");
        }

        Peca peca = tabuleiro.getPeca(origem);
        if (!turnos.podeMover(peca)) {
            throw new JogadaInvalidaException("Não é a vez dessa peça");
        }

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

        ArrayList<Posicao> possiveis = peca.calcularMovimentosPossiveis(tabuleiro);
        for (int i = 0; i < possiveis.size(); i++) {
            if (!deixaReiEmXeque(origem, possiveis.get(i))) {
                legais.add(possiveis.get(i));
            }
        }
        return legais;
    }

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

    private boolean deixaReiEmXeque(Posicao origem, Posicao destino) {
        Peca peca = tabuleiro.getPeca(origem);
        Peca capturada = tabuleiro.moverPeca(origem, destino);
        boolean xeque = estaEmXeque(peca.getCor());
        tabuleiro.moverPeca(destino, origem);
        tabuleiro.colocarPeca(destino, capturada);
        return xeque;
    }

    private Posicao encontrarRei(Cor cor) {
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = tabuleiro.getPeca(new Posicao(linha, coluna));
                if (peca instanceof Rei && peca.getCor().mesmaCor(cor)) {
                    return peca.getPosicao();
                }
            }
        }
        return null;
    }

    private void promover(Peca peca, Posicao destino) {
        if (peca instanceof Peao && (destino.getLinha() == 0 || destino.getLinha() == 7)) {
            tabuleiro.colocarPeca(destino, new Rainha(peca.getCor(), destino));
        }
    }

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

    private void atualizarEstado() {
        boolean xeque = estaEmXeque(turnos.getVez());
        boolean temLance = temMovimentoLegal();

        int situacao = 0;
        if (xeque) {
            situacao = situacao + 1;
        }
        if (!temLance) {
            situacao = situacao + 2;
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