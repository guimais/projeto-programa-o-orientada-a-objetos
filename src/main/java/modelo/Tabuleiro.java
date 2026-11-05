package modelo;

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
        if (!estaDentro(posicao)) {
            return null;
        }
        return casas[posicao.getLinha()][posicao.getColuna()].getPeca();
    }

    public void colocarPeca(Posicao posicao, Peca peca) {
        if (estaDentro(posicao)) {
            casas[posicao.getLinha()][posicao.getColuna()].setPeca(peca);
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
        return posicao.getLinha() >= 0 && posicao.getLinha() < 8 &&
               posicao.getColuna() >= 0 && posicao.getColuna() < 8;
    }

    public void montarPosicaoInicial() {
        for (int l = 0; l < 8; l++) {
            for (int c = 0; c < 8; c++) {
                casas[l][c] = new Casa(new Posicao(l, c));
            }
        }

        Cor preta = new Cor(false);
        Cor branca = new Cor(true);

        colocarPeca(new Posicao(0, 0), new modelo.pecas.Torre(preta, new Posicao(0, 0)));
        colocarPeca(new Posicao(0, 1), new modelo.pecas.Cavalo(preta, new Posicao(0, 1)));
        colocarPeca(new Posicao(0, 2), new modelo.pecas.Bispo(preta, new Posicao(0, 2)));
        colocarPeca(new Posicao(0, 3), new modelo.pecas.Rainha(preta, new Posicao(0, 3)));
        colocarPeca(new Posicao(0, 4), new modelo.pecas.Rei(preta, new Posicao(0, 4)));
        colocarPeca(new Posicao(0, 5), new modelo.pecas.Bispo(preta, new Posicao(0, 5)));
        colocarPeca(new Posicao(0, 6), new modelo.pecas.Cavalo(preta, new Posicao(0, 6)));
        colocarPeca(new Posicao(0, 7), new modelo.pecas.Torre(preta, new Posicao(0, 7)));

        for (int c = 0; c < 8; c++) {
            colocarPeca(new Posicao(1, c), new modelo.pecas.Peao(preta, new Posicao(1, c)));
        }

        for (int c = 0; c < 8; c++) {
            colocarPeca(new Posicao(6, c), new modelo.pecas.Peao(branca, new Posicao(6, c)));
        }

        colocarPeca(new Posicao(7, 0), new modelo.pecas.Torre(branca, new Posicao(7, 0)));
        colocarPeca(new Posicao(7, 1), new modelo.pecas.Cavalo(branca, new Posicao(7, 1)));
        colocarPeca(new Posicao(7, 2), new modelo.pecas.Bispo(branca, new Posicao(7, 2)));
        colocarPeca(new Posicao(7, 3), new modelo.pecas.Rainha(branca, new Posicao(7, 3)));
        colocarPeca(new Posicao(7, 4), new modelo.pecas.Rei(branca, new Posicao(7, 4)));
        colocarPeca(new Posicao(7, 5), new modelo.pecas.Bispo(branca, new Posicao(7, 5)));
        colocarPeca(new Posicao(7, 6), new modelo.pecas.Cavalo(branca, new Posicao(7, 6)));
        colocarPeca(new Posicao(7, 7), new modelo.pecas.Torre(branca, new Posicao(7, 7)));
    }
}
