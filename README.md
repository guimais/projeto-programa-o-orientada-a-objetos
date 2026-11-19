# Xadrez FXGL

Jogo de xadrez para dois jogadores no mesmo computador, desenvolvido em Java com o framework FXGL para a disciplina de Desenvolvimento de Sistemas Orientados a Objetos (PUC-Campinas, 2026).

## Requisitos

- **Java 21 ou mais novo** (JDK)
- **IntelliJ IDEA** (recomendado) ou **Maven** instalado
- **Internet na primeira execução**, para o Maven baixar o FXGL

O FXGL não precisa ser instalado: o arquivo `pom.xml` baixa a versão 21.1 automaticamente.

## Como executar

### Pelo IntelliJ IDEA

1. Descompacte o `.zip` do projeto.
2. No IntelliJ, abra a pasta do projeto em **File > Open**.
3. Se aparecer o aviso **Load Maven Project**, clique nele. Se não aparecer, clique com o botão direito no `pom.xml` e escolha **Add as Maven Project**.
4. Espere o Maven terminar de baixar as dependências (barra de progresso no rodapé).
5. Confira o Java em **File > Project Structure > SDK** e selecione uma versão 21 ou mais nova.
6. Abra `src/main/java/tela/XadrezApp.java` e clique no triângulo verde ao lado do método `main`.

### Pelo terminal

Com o Maven instalado, dentro da pasta do projeto:

```
mvn clean compile exec:java
```

Mensagens de aviso no terminal, como `sun.misc.Unsafe` ou `unnamed module`, são normais e não impedem o jogo de rodar.

## Como jogar

- As brancas começam.
- Clique em uma peça da sua cor para selecioná-la. A casa fica destacada e os destinos possíveis aparecem marcados com uma bolinha.
- Clique em uma casa marcada para mover a peça.
- Clique em outra peça sua para trocar a seleção, ou em uma casa vazia para cancelar.
- O painel lateral mostra a rodada e de quem é a vez.

## Regras implementadas

- Movimento de todas as peças: Rei, Rainha, Torre, Bispo, Cavalo e Peão
- Captura de peças adversárias
- Avanço de duas casas no primeiro movimento do Peão
- Promoção automática do Peão a Rainha
- Xeque, xeque-mate e empate por afogamento
- Empate por material insuficiente e pela regra dos 50 lances
- Placar com o valor das peças capturadas e histórico de lances

Fora do escopo: roque, en passant e escolha da peça na promoção.

## Estrutura do projeto

```
src/main/java
├── modelo           Cor, Posicao, Casa, Peca (abstrata) e Tabuleiro
│   └── pecas        Rei, Rainha, Torre, Bispo, Cavalo e Peao
├── controle         GerenciadorDePartida, ControladorDeTurnos, Placar,
│                    HistoricoDeJogadas e JogadaInvalidaException
└── tela             XadrezApp (interface gráfica em FXGL)
src/main/resources
└── assets/textures  Imagens do tabuleiro e das peças
```

## Grupo

- Gabriel Duarte Frias
- Guilherme Carvalho Mais
- João Gabriel Breganon Ferreira
- Laura Suzuki
- Luigi de Lima Miranda
