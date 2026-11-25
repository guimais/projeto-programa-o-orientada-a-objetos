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
import javafx.scene.layout.StackPane;
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

    // guarda o lance de promoção enquanto o jogador escolhe a peça
    private Posicao promocaoOrigem;
    private Posicao promocaoDestino;

    private Group camadaTabuleiro;
    private Group camadaPainel;

    // true enquanto o menu inicial está aparecendo
    private boolean noMenu = true;

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
        novaPartida();
    }

    // Cria uma partida do zero (usado no início e no "jogar novamente")
    private void novaPartida() {
        partida = new GerenciadorDePartida();
        tabuleiro = partida.getTabuleiro();
        turnos = partida.getTurnos();
        origemSelecionada = null;
        movimentosPossiveis = new ArrayList<>();
        promocaoOrigem = null;
        promocaoDestino = null;
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

    // Desenha o menu inicial com as opções Jogar e Sair
    private void desenharMenuInicial() {
        // imagem de fundo (3:2) escalada para cobrir a janela sem distorcer
        Texture imagemFundo = FXGL.texture("fundoinicio.jpg", 960, 640);
        imagemFundo.setTranslateX(-40);
        camadaPainel.getChildren().add(imagemFundo);

        // camada escura por cima da imagem para o título e os botões ficarem legíveis
        Rectangle fundo = new Rectangle(880, 640);
        fundo.setFill(Color.rgb(0, 0, 0, 0.45));
        camadaPainel.getChildren().add(fundo);

        Text titulo = new Text("XADREZ");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 64));
        titulo.setFill(Color.WHITE);
        titulo.setTranslateX(440 - titulo.getLayoutBounds().getWidth() / 2);
        titulo.setTranslateY(200);
        camadaPainel.getChildren().add(titulo);

        camadaPainel.getChildren().add(criarBotao("Jogar", 340, 280, 200, () -> {
            noMenu = false;
            novaPartida();
            atualizarTela();
        }));
        camadaPainel.getChildren().add(criarBotao("Sair", 340, 360, 200, () -> sairDoJogo()));
    }

    // Desenha a janela de fim de jogo perguntando se quer jogar novamente
    private void desenharFimDeJogo() {
        String resultado;
        if (partida.getEstado().equals("EMPATE")) {
            resultado = "Empate!";
        } else if (turnos.getVez().isBranca()) {
            resultado = "Xeque-mate! Pretas vencem";
        } else {
            resultado = "Xeque-mate! Brancas vencem";
        }

        // escurece o tabuleiro e bloqueia cliques nele
        Rectangle escurecer = new Rectangle(640, 640);
        escurecer.setFill(Color.rgb(0, 0, 0, 0.55));
        camadaTabuleiro.getChildren().add(escurecer);

        double caixaX = 120;
        double caixaY = 220;
        Rectangle caixa = new Rectangle(400, 200);
        caixa.setTranslateX(caixaX);
        caixa.setTranslateY(caixaY);
        caixa.setArcWidth(16);
        caixa.setArcHeight(16);
        caixa.setFill(Color.web("#302E2B"));
        camadaTabuleiro.getChildren().add(caixa);

        Text textoResultado = new Text(resultado);
        textoResultado.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        textoResultado.setFill(Color.ORANGE);
        textoResultado.setTranslateX(320 - textoResultado.getLayoutBounds().getWidth() / 2);
        textoResultado.setTranslateY(caixaY + 50);
        camadaTabuleiro.getChildren().add(textoResultado);

        Text pergunta = new Text("Deseja jogar novamente?");
        pergunta.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        pergunta.setFill(Color.WHITE);
        pergunta.setTranslateX(320 - pergunta.getLayoutBounds().getWidth() / 2);
        pergunta.setTranslateY(caixaY + 90);
        camadaTabuleiro.getChildren().add(pergunta);

        camadaTabuleiro.getChildren().add(criarBotao("Sim", caixaX + 40, caixaY + 120, 140, () -> {
            novaPartida();
            atualizarTela();
        }));
        camadaTabuleiro.getChildren().add(criarBotao("Não", caixaX + 220, caixaY + 120, 140, () -> sairDoJogo()));
    }

    // Cria um botão simples (retângulo + texto) que executa a ação ao ser clicado
    private StackPane criarBotao(String texto, double x, double y, double largura, Runnable acao) {
        Rectangle fundo = new Rectangle(largura, 50);
        fundo.setArcWidth(12);
        fundo.setArcHeight(12);
        fundo.setFill(Color.web("#81B64C"));

        Text rotulo = new Text(texto);
        rotulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        rotulo.setFill(Color.WHITE);

        StackPane botao = new StackPane(fundo, rotulo);
        botao.setTranslateX(x);
        botao.setTranslateY(y);
        botao.setOnMouseEntered(e -> fundo.setFill(Color.web("#A3D160")));
        botao.setOnMouseExited(e -> fundo.setFill(Color.web("#81B64C")));
        botao.setOnMouseClicked(e -> acao.run());
        return botao;
    }

    // Fecha o programa
    private void sairDoJogo() {
        FXGL.getGameController().exit();
    }

    // Limpa e redesenha o tabuleiro, o painel lateral e todas as peças
    private void atualizarTela() {
        camadaTabuleiro.getChildren().clear();
        camadaPainel.getChildren().clear();

        if (noMenu) {
            desenharMenuInicial();
            return;
        }

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
        textoRodada.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textoRodada.setFill(Color.LIGHTGRAY);
        textoRodada.setTranslateX(660);
        textoRodada.setTranslateY(40);

        Text textoVez = new Text("Vez: " + turnos.getVez().getNome());
        textoVez.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textoVez.setFill(Color.WHITE);
        textoVez.setTranslateX(660);
        textoVez.setTranslateY(80);


        String mensagem = "em andamento";

        if(partida.getEstado().equals("XEQUE")){
            mensagem = "Xeque";
        }

        if(partida.getEstado().equals("XEQUE_MATE")){
           if(turnos.getVez().isBranca()){
               mensagem = "Xeque Mate\nPretas vencem";
           }
           else{
               mensagem = "Xeque Mate\nBrancas vencem";
           }

        }

        if(partida.getEstado().equals("EMPATE")){
            mensagem = "Empate";

        }



        Text textoEstado  = new Text("Status: " + mensagem);
        textoEstado.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        textoEstado.setFill(Color.ORANGE);
        textoEstado.setTranslateX(660);
        textoEstado.setTranslateY(120);


        camadaPainel.getChildren().addAll(textoRodada, textoVez, textoEstado);

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

        // se tem promoção pendente, mostra o menu de escolha por cima de tudo
        if (promocaoDestino != null) {
            desenharMenuPromocao();
        }

        // se a partida acabou, pergunta se quer jogar novamente
        if (partida.getEstado().equals("XEQUE_MATE") || partida.getEstado().equals("EMPATE")) {
            desenharFimDeJogo();
        }
    }

    // Desenha a janela com as 4 opções de peça para a promoção do peão
    private void desenharMenuPromocao() {
        String[] simbolos = {"D", "T", "B", "C"};
        String corNome = turnos.getVez().getNome();

        // fundo escuro cobrindo o tabuleiro; clicar fora cancela a promoção
        Rectangle escurecer = new Rectangle(640, 640);
        escurecer.setFill(Color.rgb(0, 0, 0, 0.55));
        escurecer.setOnMouseClicked(e -> {
            promocaoOrigem = null;
            promocaoDestino = null;
            atualizarTela();
        });
        camadaTabuleiro.getChildren().add(escurecer);

        double larguraCaixa = simbolos.length * TAMANHO_CASA + 50;
        double alturaCaixa = 150;
        double caixaX = (640 - larguraCaixa) / 2;
        double caixaY = (640 - alturaCaixa) / 2;

        Rectangle caixa = new Rectangle(larguraCaixa, alturaCaixa);
        caixa.setTranslateX(caixaX);
        caixa.setTranslateY(caixaY);
        caixa.setArcWidth(16);
        caixa.setArcHeight(16);
        caixa.setFill(Color.web("#302E2B"));
        camadaTabuleiro.getChildren().add(caixa);

        Text titulo = new Text("Escolha a peça da promoção");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titulo.setFill(Color.WHITE);
        titulo.setTranslateX(caixaX + 20);
        titulo.setTranslateY(caixaY + 30);
        titulo.setMouseTransparent(true);
        camadaTabuleiro.getChildren().add(titulo);

        for (int i = 0; i < simbolos.length; i++) {
            String simbolo = simbolos[i];
            double x = caixaX + 25 + i * TAMANHO_CASA;
            double y = caixaY + 50;

            // quadrado clicável de cada opção
            Rectangle botao = new Rectangle(TAMANHO_CASA - 6, TAMANHO_CASA - 6);
            botao.setTranslateX(x + 3);
            botao.setTranslateY(y + 3);
            botao.setArcWidth(10);
            botao.setArcHeight(10);
            botao.setFill(Color.web("#EEEED2"));
            botao.setOnMouseEntered(e -> botao.setFill(Color.web("#F6F669")));
            botao.setOnMouseExited(e -> botao.setFill(Color.web("#EEEED2")));
            botao.setOnMouseClicked(e -> escolherPromocao(simbolo));
            camadaTabuleiro.getChildren().add(botao);

            Texture textura = FXGL.texture(simbolo + "_" + corNome + ".png", 64, 64);
            textura.setTranslateX(x + 8);
            textura.setTranslateY(y + 8);
            textura.setMouseTransparent(true);
            camadaTabuleiro.getChildren().add(textura);
        }
    }

    // Executa o lance de promoção com a peça escolhida no menu
    private void escolherPromocao(String simbolo) {
        try {
            partida.tentarJogada(promocaoOrigem, promocaoDestino, simbolo);
        } catch (JogadaInvalidaException erro) {
            System.out.println(erro.getMessage());
        }
        promocaoOrigem = null;
        promocaoDestino = null;
        atualizarTela();
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

        //testa as situacoes de xeque mate e empate p acabar o jogo
        if(partida.getEstado().equals("XEQUE_MATE") || partida.getEstado().equals("EMPATE")){
            return;
        }

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
                if (partida.ehPromocao(origemSelecionada, posicaoClicada)) {
                    // não move ainda: abre o menu para escolher a peça
                    promocaoOrigem = origemSelecionada;
                    promocaoDestino = posicaoClicada;
                } else {
                    try {
                        partida.tentarJogada(origemSelecionada, posicaoClicada);
                    } catch (JogadaInvalidaException erro) {
                        System.out.println(erro.getMessage());
                    }
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
