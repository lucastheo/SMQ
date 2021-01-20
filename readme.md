# Bem vindo ao SMQ

## O que te levou a vir aqui? Caso for uma forma de troca de mensagens fácil encontrou o lugar
O foco desse projeto é desenvolver uma maneira de trafegar mensagens de forma muito simple o qual da instação do servidor até a utilização do cliente seja muito agradável. 

## Como instalar o servidor?
Os detalhes da instação estão presente [aqui.](https://github.com/lucastheo/SMQ/tree/main/server )

## Como utilizar o cliente?
Está nesse outro link [aqui.](https://github.com/lucastheo/SMQ/tree/main/cliente/)

## Quer ajudar? Precisa de ajuda? 
Entre para conversa sobre o projeto no [discord.](https://discord.gg/rAEK3XtR)

## Funcionamento do servidor 
Todo o servidor funciona por protolo rest, os principais processos que podem ser feitos é a gerar uma mensagem, reserva e confirmação de consumo.
### A estrutura para utilização
Como estrutura de utilização temos 4 entidades, fila, mensagem, grupo e consumidor.
Temos algumas definições iniciais

* Todas as filas e consumidores estão presentes no servidor.
* Uma mensagem está contida dentro de uma fila.
* Grupo associa um consumidor a uma mensagem.

Dessa maneira a relação entre um consumidor e uma fila é pelo caminho fila->mensagem->grupo->consumidor, assim na utilização não precisamos pensar em realizar os registros de filas e consumidores, pois todos já estão presentes, assim o foco é na mensagem.

O processo de ciclo de vida da mensagem é dividido em 3 etapas.

1. O produtor gera a mensagem definindo a fila, para quem, a mensagem é e até que momento ela pode existir;
2. O Consumidor verifica se existe alguma mensagem disponível para aquela fila e grupo.

    * Caso sim, reserva esse registro (fila mais grupo).
    * Caso não, espera mais um tempo e verifica novamente

3. Caso o processamento acontecer com sucesso ele confirma o consumo;
    
    * Obs: Caso falhar não precisa realizar nada e esperar o timeout da reselva

---
## Mais detalhes

### Crinado uma mensagem 
A forma de gerar a mensagem é atravez de um método POST na rota /mensagem e ele precisa de um body com o seguinte formato:
```
{
    "fila": { "nome": "nome_da_fila" },
    "consumidores": [ 
        { "nome": "consumidor_1" },
        { "nome": "consumidor_2" }
    ],
    "mensagem": "mensagem de teste",
    "data_expiração": "2021-01-01T30:00:00.000"
}
```
Os códigos de resposta são 200 para sucesso, 500 para erro no servidor e 400 caso tenha algum campo invalido no body.

### Reservar uma mensagem
A maneira de consumir uma mensagem é atravez de um método GET /mensagem, porém é necessario passar dois query paramentos (fila e grupo).

* Exemplo: /mensagem?fila=teste&grupo=consumidor_1

Os possiveis retornos são:
* 500 para erro interno no servidor
* 400 para quando fila ou grupo estiver fora do padrão
* 204 sem body o que siginifica que não existem mensagens para ser consumidas para essa fila e grupo.
* 200 com body no caso que existir mensagem para ser consumida (exemplo de resposta abaixo), com a informação da mensagem e as informações necessaias para confirmar o consumo (nome no grupo e identificação mensagem).

```
{
    "nome_no_grupo": "consumidor_1",
    "identificacao_mensagem": "eff31a5d-ac06-48c3-b0f6-855c12e45821",
    "mensagem": "mensagem de teste"
}
```

### Confirmar consumo
Diferente dos dois outros a maneira de consumir é pelo POST /mensagem/{identificacao_mensagem}/consumidor/{nome_no_grupo}, um exemplo de URL:
* Exemplo: /mensagem/eff31a5d-ac06-48c3-b0f6-855c12e45821/consumidor/consumidor_1.

Os possíveis retornos para esse caso são:
 * 500 no caso de erro interno no servidor
 * 400 para caso falhar na validação
 * 404 quando não é identificado esse registro na base
 * 200 quando confirmação ocorre com sucesso.

### Limpando uma Fila
Para realizar a operação de limpeza é necessário realizar um DELETE sem body na rota /fila/{nome_da_fila}

* Exemplo: /fila/fila_de_teste

Os retornor esperados são 
* 200 para sucesso
* 500 quando ocorre um erro no servidor
* 400 caso o nome da fila for invalido


