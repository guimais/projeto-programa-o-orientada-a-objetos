package modelo;

import modelo.pecas.Bispo;
import modelo.pecas.Cavalo;
import modelo.pecas.Peao;
import modelo.pecas.Rainha;
import modelo.pecas.Rei;
import modelo.pecas.Torre;

public class Tabuleiro {

    private Casa[][] casas = new Casa[8][8];

    public Tabuleiro() {
        montarPosicaoInicial();
    }

    public Casa getCasa(Posicao posicao) {
        if (!estaDentro(posicao)) {
            return null;
        }
        return casas[posicao.getLinha()][posicao.getColuna()];
    }

    public Casa[][] getCasas() {
        return casas;
    }

    public Peca getPeca(Posicao posicao) {
        Casa casa = getCasa(posicao);
        if (casa == null) {
            return null;
        }
        return casa.getPeca();
    }

    public void colocarPeca(Posicao posicao, Peca peca) {
        Casa casa = getCasa(posicao);
        if (casa != null) {
            casa.setPeca(peca);
            if (peca != null) {
                peca.setPosicao(posicao);
            }
        }
    }

    public Peca moverPeca(Posicao origem, Posicao destino) {
        if (!estaDentro(origem) || !estaDentro(destino)) {
            return null;
        }

        Peca peca = getPeca(origem);
        if (peca == null) {
            return null;
        }

        Peca capturada = getPeca(destino);
        colocarPeca(origem, null);
        colocarPeca(destino, peca);
        return capturada;
    }

    public boolean estaDentro(Posicao posicao) {
        if (posicao == null) {
            return false;
        }
        return posicao.getLinha() >= 0 && posicao.getLinha() < 8
                && posicao.getColuna() >= 0 && posicao.getColuna() < 8;
    }

    public void montarPosicaoInicial() {
        for (int linha = 0; linha < 8; linha++) {
            for (int coluna = 0; coluna < 8; coluna++) {
                casas[linha][coluna] = new Casa(new Posicao(linha, coluna));
            }
        }

        Cor preta = new Cor(false);
        Cor branca = new Cor(true);

        montarFileiraPrincipal(0, preta);
        montarFileiraDePeoes(1, preta);
        montarFileiraDePeoes(6, branca);
        montarFileiraPrincipal(7, branca);
    }

    private void montarFileiraDePeoes(int linha, Cor cor) {
        for (int coluna = 0; coluna < 8; coluna++) {
            Posicao posicao = new Posicao(linha, coluna);
            colocarPeca(posicao, new Peao(cor, posicao));
        }
    }

    private void montarFileiraPrincipal(int linha, Cor cor) {
        colocarPeca(new Posicao(linha, 0), new Torre(cor, new Posicao(linha, 0)));
        colocarPeca(new Posicao(linha, 1), new Cavalo(cor, new Posicao(linha, 1)));
        colocarPeca(new Posicao(linha, 2), new Bispo(cor, new Posicao(linha, 2)));
        colocarPeca(new Posicao(linha, 3), new Rainha(cor, new Posicao(linha, 3)));
        colocarPeca(new Posicao(linha, 4), new Rei(cor, new Posicao(linha, 4)));
        colocarPeca(new Posicao(linha, 5), new Bispo(cor, new Posicao(linha, 5)));
        colocarPeca(new Posicao(linha, 6), new Cavalo(cor, new Posicao(linha, 6)));
        colocarPeca(new Posicao(linha, 7), new Torre(cor, new Posicao(linha, 7)));
    }
}