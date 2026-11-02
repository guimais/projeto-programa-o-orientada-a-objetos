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
        // As peças específicas serão instanciadas e posicionadas no Passo 4
    }
}
