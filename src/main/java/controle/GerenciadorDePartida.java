package controle;

import java.util.ArrayList;

import modelo.Cor;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;
import modelo.pecas.Rainha;

public class GerenciadorDePartida {

    // Tabuleiro já montado com as 32 peças
    private Tabuleiro tabuleiro = new Tabuleiro();
    // Controla de quem é a vez
    private ControladorDeTurnos turnos = new ControladorDeTurnos();
    // Soma os pontos das capturas
    private Placar placar = new Placar();
    // Guarda os lances da partida
    private HistoricoDeJogadas historico = new HistoricoDeJogadas();
    // Situação da partida: EM_ANDAMENTO, XEQUE, XEQUE_MATE ou EMPATE
    private String estado = "EM_ANDAMENTO";
    // Conta os lances seguidos sem captura e sem mover peão
    private int lancesSemCapturaOuPeao = 0;

    // Devolve o tabuleiro para a tela desenhar
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    // Devolve o controle de turnos para a tela mostrar a vez
    public ControladorDeTurnos getTurnos() {
        return turnos;
    }

    // Devolve o placar para a tela mostrar os pontos
    public Placar getPlacar() {
        return placar;
    }

    // Devolve o histórico para a tela mostrar os lances
    public HistoricoDeJogadas getHistorico() {
        return historico;
    }

    // Devolve a situação atual da partida
    public String getEstado() {
        return estado;
    }

    // Devolve quantos lances seguidos foram feitos sem captura e sem peão
    public int getLancesSemCapturaOuPeao() {
        return lancesSemCapturaOuPeao;
    }

    // Faz a jogada se ela for permitida; senão, lança a exceção
    public void tentarJogada(Posicao origem, Posicao destino) throws JogadaInvalidaException {
        // Depois do xeque-mate ou do empate ninguém joga mais
        if (estado.equals("XEQUE_MATE") || estado.equals("EMPATE")) {
            throw new JogadaInvalidaException("A partida terminou");
        }

        // Pega a peça que o jogador quer mover
        Peca peca = tabuleiro.getPeca(origem);
        // A peça precisa existir e ser da cor da vez
        if (!turnos.podeMover(peca)) {
            throw new JogadaInvalidaException("Não é a vez dessa peça");
        }

        // Lista de destinos permitidos para essa peça
        ArrayList<Posicao> legais = movimentosLegais(origem);
        // Começa supondo que o destino não está na lista
        boolean permitida = false;
        // Procura o destino escolhido na lista de destinos permitidos
        for (int i = 0; i < legais.size(); i++) {
            if (legais.get(i).mesmaPosicao(destino)) {
                permitida = true;
            }
        }
        // Destino fora da lista é uma jogada proibida
        if (!permitida) {
            throw new JogadaInvalidaException("Movimento inválido");
        }

        // Move a peça e guarda a peça capturada, se houver
        Peca capturada = tabuleiro.moverPeca(origem, destino);
        // Lance de peão ou com captura zera a contagem dos 50 lances
        if (peca.getSimbolo().equals("P") || capturada != null) {
            lancesSemCapturaOuPeao = 0;
        } else {
            // Qualquer outro lance soma 1 na contagem
            lancesSemCapturaOuPeao = lancesSemCapturaOuPeao + 1;
        }
        // Soma os pontos da captura
        placar.registrarCaptura(capturada);
        // Anota o lance no histórico
        historico.registrar(origem, destino);
        // Transforma o peão em Rainha se ele chegou ao fim do tabuleiro
        promover(peca, destino);
        // Passa a vez para o adversário
        turnos.passarVez();
        // Verifica xeque, xeque-mate e empate para o adversário
        atualizarEstado();
    }

    // Devolve as casas para onde a peça pode ir sem deixar o próprio Rei em xeque
    public ArrayList<Posicao> movimentosLegais(Posicao origem) {
        // Lista que vai guardar os movimentos permitidos
        ArrayList<Posicao> legais = new ArrayList<>();
        // Pega a peça da casa escolhida
        Peca peca = tabuleiro.getPeca(origem);
        // Casa vazia ou peça do adversário não tem movimentos
        if (!turnos.podeMover(peca)) {
            return legais;
        }

        // Pede à própria peça os movimentos dela (polimorfismo)
        ArrayList<Posicao> possiveis = peca.calcularMovimentosPossiveis(tabuleiro);
        // Guarda só os movimentos que não deixam o próprio Rei em xeque
        for (int i = 0; i < possiveis.size(); i++) {
            if (!deixaReiEmXeque(origem, possiveis.get(i))) {
                legais.add(possiveis.get(i));
            }
        }
        return legais;
    }

    // Diz se o Rei dessa cor está sendo atacado por alguma peça inimiga
    public boolean estaEmXeque(Cor cor) {
        // Descobre onde está o Rei dessa cor
        Posicao rei = encontrarRei(cor);

        // Percorre as 64 casas do tabuleiro
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                // Pega a peça da casa atual
                Peca peca = tabuleiro.getPeca(new Posicao(linha, coluna));
                // Só interessam as peças inimigas
                if (peca != null && !peca.getCor().mesmaCor(cor)) {
                    // Casas que essa peça inimiga alcança
                    ArrayList<Posicao> ataques = peca.calcularMovimentosPossiveis(tabuleiro);
                    // Se alguma dessas casas for a do Rei, é xeque
                    for (int i = 0; i < ataques.size(); i++) {
                        if (ataques.get(i).mesmaPosicao(rei)) {
                            return true;
                        }
                    }
                }
            }
        }
        // Nenhuma peça inimiga alcança o Rei
        return false;
    }

    // Testa a jogada, vê se o próprio Rei ficou em xeque e desfaz a jogada
    private boolean deixaReiEmXeque(Posicao origem, Posicao destino) {
        // Pega a peça que vai ser testada
        Peca peca = tabuleiro.getPeca(origem);
        // Faz a jogada de teste e guarda a peça capturada
        Peca capturada = tabuleiro.moverPeca(origem, destino);
        // Confere se o Rei dessa cor ficou em xeque
        boolean xeque = estaEmXeque(peca.getCor());
        // Volta a peça para a casa de origem
        tabuleiro.moverPeca(destino, origem);
        // Devolve a peça capturada para o lugar dela
        tabuleiro.colocarPeca(destino, capturada);
        return xeque;
    }

    // Procura a casa onde está o Rei dessa cor
    private Posicao encontrarRei(Cor cor) {
        // Percorre as 64 casas do tabuleiro
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                // Pega a peça da casa atual
                Peca peca = tabuleiro.getPeca(new Posicao(linha, coluna));
                // O Rei é a peça com símbolo "R" e da cor pedida
                if (peca != null && peca.getSimbolo().equals("R") && peca.getCor().mesmaCor(cor)) {
                    return peca.getPosicao();
                }
            }
        }
        // Só acontece se não houver Rei no tabuleiro
        return null;
    }

    // Transforma o peão em Rainha quando ele chega à última linha
    private void promover(Peca peca, Posicao destino) {
        // Linha 0 é o fim para o peão branco e linha 7 para o peão preto
        if (peca.getSimbolo().equals("P") && (destino.getLinha() == 0 || destino.getLinha() == 7)) {
            tabuleiro.colocarPeca(destino, new Rainha(peca.getCor(), destino));
        }
    }

    // Diz se o jogador da vez tem pelo menos um movimento permitido
    private boolean temMovimentoLegal() {
        // Percorre as 64 casas do tabuleiro
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                // Basta uma peça com algum movimento para a resposta ser sim
                if (movimentosLegais(new Posicao(linha, coluna)).size() > 0) {
                    return true;
                }
            }
        }
        // Nenhuma peça do jogador da vez pode se mover
        return false;
    }

    // Diz se sobraram peças de menos para alguém conseguir dar xeque-mate
    private boolean temMaterialInsuficiente() {
        // Lista com todas as peças do tabuleiro, menos os Reis
        ArrayList<Peca> pecas = new ArrayList<>();

        // Percorre as 64 casas e guarda as peças que não são Rei
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Peca peca = tabuleiro.getPeca(new Posicao(linha, coluna));
                if (peca != null && !peca.getSimbolo().equals("R")) {
                    pecas.add(peca);
                }
            }
        }

        // Só os dois Reis no tabuleiro
        if (pecas.size() == 0) {
            return true;
        }

        // Rei contra Rei com um único Bispo ou um único Cavalo
        if (pecas.size() == 1) {
            Peca unica = pecas.get(0);
            return unica.getSimbolo().equals("B") || unica.getSimbolo().equals("C");
        }

        // Um Bispo de cada lado, os dois em casas da mesma cor
        if (pecas.size() == 2) {
            Peca primeira = pecas.get(0);
            Peca segunda = pecas.get(1);
            // As duas peças precisam ser Bispos
            if (primeira.getSimbolo().equals("B") && segunda.getSimbolo().equals("B")) {
                // Os Bispos precisam ser de cores diferentes
                if (!primeira.getCor().mesmaCor(segunda.getCor())) {
                    // Linha mais coluna par é casa clara e ímpar é casa escura
                    int corCasa1 = (primeira.getPosicao().getLinha() + primeira.getPosicao().getColuna()) % 2;
                    int corCasa2 = (segunda.getPosicao().getLinha() + segunda.getPosicao().getColuna()) % 2;
                    return corCasa1 == corCasa2;
                }
            }
        }

        // Qualquer outra combinação ainda permite xeque-mate
        return false;
    }

    // Atualiza a situação da partida para o jogador que vai jogar agora
    private void atualizarEstado() {
        // O jogador da vez está em xeque?
        boolean xeque = estaEmXeque(turnos.getVez());
        // O jogador da vez tem algum movimento?
        boolean temLance = temMovimentoLegal();

        // Junta as respostas em um número: 0 normal, 1 xeque, 2 empate, 3 xeque-mate
        int situacao = 0;
        // Estar em xeque soma 1
        if (xeque) {
            situacao = situacao + 1;
        }
        // Não ter lance soma 2
        if (!temLance) {
            situacao = situacao + 2;
        }
        // Material insuficiente é empate
        if (temMaterialInsuficiente()) {
            situacao = 2;
        }
        // 50 lances de cada lado sem captura e sem peão é empate, se não for xeque-mate
        if (lancesSemCapturaOuPeao >= 100 && situacao != 3) {
            situacao = 2;
        }

        // Escolhe o estado pelo número da situação
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