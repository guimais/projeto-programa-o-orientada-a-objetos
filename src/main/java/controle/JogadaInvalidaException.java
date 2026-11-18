package controle;

// Exceção lançada quando o jogador tenta uma jogada proibida
public class JogadaInvalidaException extends Exception {

    // Recebe a mensagem do erro e entrega para a classe Exception
    public JogadaInvalidaException(String mensagem) {
        super(mensagem);
    }
}