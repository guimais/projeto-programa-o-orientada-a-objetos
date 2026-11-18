package controle;

// exceção usada quando o jogador tenta fazer uma jogada inválida
public class JogadaInvalidaException extends Exception {

    public JogadaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
