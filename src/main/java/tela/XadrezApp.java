package tela;

import java.util.ArrayList;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import controle.ControladorDeTurnos;
import controle.GerenciadorDePartida;
import controle.JogadaInvalidaException;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;

public class XadrezApp extends GameApplication {

    private static final int TAMANHO_CASA = 80;

    private GerenciadorDePartida partida;
    private Tabuleiro tabuleiro;
    private ControladorDeTurnos turnos;
    private Posicao origemSelecionada;
    private ArrayList<Posicao> movimentosPossiveis;

    private Group camadaTabuleiro;
    private Group camadaPainel;

    // Configura o título e o tamanho da janela
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Xadrez");
        settings.setWidth(880);
        settings.setHeight(640);
    }

    // Inicializa a partida, o tabuleiro, os turnos e limpa a seleção
    @Override
    protected void initGame() {
        partida = new GerenciadorDePartida();
        tabuleiro = partida.getTabuleiro();
        turnos = partida.getTurnos();
        origemSelecionada = null;
        movimentosPossiveis = new ArrayList<>();
    }

    // Cria as camadas visuais e faz o primeiro desenho da tela
    @Override
    protected void initUI() {
        camadaTabuleiro = new Group();
        camadaPainel = new Group();
        FXGL.addUINode(camadaTabuleiro);
        FXGL.addUINode(camadaPainel);
        atualizarTela();
    }

    // Limpa e redesenha o tabuleiro, o painel lateral e todas as peças
    private void atualizarTela() {
        camadaTabuleiro.getChildren().clear();
        camadaPainel.getChildren().clear();

        // Fundo do tabuleiro e leitura do clique
        Texture imagemTabuleiro = FXGL.texture("tabuleiro.png", 640, 640);
        imagemTabuleiro.setOnMouseClicked(e -> {
            int coluna = (int) (e.getX() / TAMANHO_CASA);
            int linha = (int) (e.getY() / TAMANHO_CASA);
            tratarCliqueCasa(new Posicao(linha, coluna));
        });
        camadaTabuleiro.getChildren().add(imagemTabuleiro);

        // Painel lateral de status
        Rectangle fundoPainel = new Rectangle(240, 640);
        fundoPainel.setTranslateX(640);
        fundoPainel.setFill(Color.web("#302E2B"));
        camadaPainel.getChildren().add(fundoPainel);

        Text textoRodada = new Text("Rodada: " + turnos.getRodada());
        textoRodada.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        textoRodada.setFill(Color.LIGHTGRAY);
        textoRodada.setTranslateX(660);
        textoRodada.setTranslateY(40);

        Text textoVez = new Text("Vez: " + turnos.getVez().getNome());
        textoVez.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        textoVez.setFill(Color.WHITE);
        textoVez.setTranslateX(660);
        textoVez.setTranslateY(80);

        camadaPainel.getChildren().addAll(textoRodada, textoVez);

        // Desenha destaques, peças e bolinhas de movimento
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                Posicao posicaoAtual = new Posicao(linha, coluna);
                double x = coluna * TAMANHO_CASA;
                double y = linha * TAMANHO_CASA;

                if (origemSelecionada != null) {
                    if (origemSelecionada.mesmaPosicao(posicaoAtual)) {
                        Rectangle destaque = new Rectangle(TAMANHO_CASA, TAMANHO_CASA);
                        destaque.setTranslateX(x);
                        destaque.setTranslateY(y);
                        destaque.setFill(Color.rgb(246, 246, 105, 0.55));
                        destaque.setMouseTransparent(true);
                        camadaTabuleiro.getChildren().add(destaque);
                    }
                }

                Peca peca = tabuleiro.getPeca(posicaoAtual);
                if (peca != null) {
                    desenharPeca(peca, x, y);
                }

                if (ehMovimentoPossivel(posicaoAtual)) {
                    Circle indicador = new Circle(12);
                    indicador.setTranslateX(x + (TAMANHO_CASA / 2.0));
                    indicador.setTranslateY(y + (TAMANHO_CASA / 2.0));
                    indicador.setFill(Color.rgb(0, 0, 0, 0.30));
                    indicador.setMouseTransparent(true);
                    camadaTabuleiro.getChildren().add(indicador);
                }
            }
        }
    }

    // Carrega e posiciona a imagem de uma peça na casa
    private void desenharPeca(Peca peca, double x, double y) {
        String nomeArquivo = peca.getSimbolo() + "_" + peca.getCor().getNome() + ".png";
        try {
            Texture textura = FXGL.texture(nomeArquivo, 64, 64);
            textura.setTranslateX(x + 8);
            textura.setTranslateY(y + 8);
            textura.setMouseTransparent(true);
            camadaTabuleiro.getChildren().add(textura);
        } catch (Exception erro) {
            System.out.println("Imagem não encontrada: " + nomeArquivo);
        }
    }

    // Trata o clique na casa para selecionar, mover ou trocar a peça
    private void tratarCliqueCasa(Posicao posicaoClicada) {
        Peca pecaClicada = tabuleiro.getPeca(posicaoClicada);

        if (origemSelecionada == null) {
            if (pecaClicada != null) {
                if (turnos.podeMover(pecaClicada)) {
                    origemSelecionada = posicaoClicada;
                    movimentosPossiveis = partida.movimentosLegais(posicaoClicada);
                }
            }
        } else {
            if (ehMovimentoPossivel(posicaoClicada)) {
                try {
                    partida.tentarJogada(origemSelecionada, posicaoClicada);
                } catch (JogadaInvalidaException erro) {
                    System.out.println(erro.getMessage());
                }

                origemSelecionada = null;
                movimentosPossiveis.clear();
            } else if (pecaClicada != null) {
                if (turnos.podeMover(pecaClicada)) {
                    origemSelecionada = posicaoClicada;
                    movimentosPossiveis = partida.movimentosLegais(posicaoClicada);
                } else {
                    origemSelecionada = null;
                    movimentosPossiveis.clear();
                }
            } else {
                origemSelecionada = null;
                movimentosPossiveis.clear();
            }
        }

        atualizarTela();
    }

    // Verifica se a posição está na lista de movimentos possíveis
    private boolean ehMovimentoPossivel(Posicao posicao) {
        for (int i = 0; i < movimentosPossiveis.size(); i++) {
            if (movimentosPossiveis.get(i).mesmaPosicao(posicao)) {
                return true;
            }
        }
        return false;
    }

    // Inicia o jogo
    public static void main(String[] args) {
        launch(args);
    }
}
