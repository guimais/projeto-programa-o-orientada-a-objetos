package tela;

import java.util.ArrayList;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import modelo.Peca;
import modelo.Posicao;
import modelo.Tabuleiro;

public class XadrezApp extends GameApplication {

    private static final int TAMANHO_CASA = 80;

    private Tabuleiro tabuleiro;
    private Posicao origemSelecionada;
    private ArrayList<Posicao> movimentosDestacados;

    private Group camadaTabuleiro;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Xadrez");
        settings.setWidth(640);
        settings.setHeight(640);
    }

    @Override
    protected void initGame() {
        tabuleiro = new Tabuleiro();
        origemSelecionada = null;
        movimentosDestacados = new ArrayList<>();
    }

    @Override
    protected void initUI() {
        camadaTabuleiro = new Group();
        FXGL.addUINode(camadaTabuleiro);
        atualizarTela();
    }

    private void atualizarTela() {
        camadaTabuleiro.getChildren().clear();

        Texture imagemTabuleiro = FXGL.texture("tabuleiro.png", 640, 640);
        imagemTabuleiro.setOnMouseClicked(e -> {
            int coluna = (int) (e.getX() / TAMANHO_CASA);
            int linha = (int) (e.getY() / TAMANHO_CASA);
            tratarCliqueCasa(new Posicao(linha, coluna));
        });
        camadaTabuleiro.getChildren().add(imagemTabuleiro);

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

                // Desenhar a imagem da peça, se existir nesta casa
                Peca peca = tabuleiro.getPeca(posicaoAtual);
                if (peca != null) {
                    desenharPeca(peca, x, y);
                }

                // Desenhar bolinha se for um movimento possível
                if (estaNosDestaques(posicaoAtual)) {
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

    private void tratarCliqueCasa(Posicao posicaoClicada) {
        Peca pecaClicada = tabuleiro.getPeca(posicaoClicada);

        if (origemSelecionada == null) {
            if (pecaClicada != null) {
                origemSelecionada = posicaoClicada;
                movimentosDestacados = pecaClicada.calcularMovimentosPossiveis(tabuleiro);
            }
        } else {

            if (estaNosDestaques(posicaoClicada)) {

                tabuleiro.moverPeca(origemSelecionada, posicaoClicada);
                origemSelecionada = null;
                movimentosDestacados.clear();
            } else if (pecaClicada != null) {

                origemSelecionada = posicaoClicada;
                movimentosDestacados = pecaClicada.calcularMovimentosPossiveis(tabuleiro);
            } else {

                origemSelecionada = null;
                movimentosDestacados.clear();
            }
        }

        atualizarTela();
    }

    private boolean estaNosDestaques(Posicao posicao) {
        for (int i = 0; i < movimentosDestacados.size(); i++) {
            if (movimentosDestacados.get(i).mesmaPosicao(posicao)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
