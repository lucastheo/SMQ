import smq_cliente

if __name__ == "__main__":
    mensageria = smq_cliente.SmqCliente()
    print("Enviando mensagem")
    mensageria.envia("teste_smq", ["um", "dois" ], "teste 321")

    print("recebendo mensagem de forma bloqueante")
    resposta = mensageria.recebe_bloqueante("teste_smq", "um")
    if resposta.existe():
        print( "resposta da mensagem de forma bloqueante" , resposta.mensagem())
        if not resposta.commit():
            print("Erro em comitar mensagem bloqueante")
    else:
        print("Erro em receber mensagem bloqueante")
    
    print( "recebe mensagem de forma bloqueante com commit" )
    resposta = mensageria.recebe_bloqueante_commit("teste_smq", "dois")
    print( "resposta da mensagem de forma bloqueante com commit" , resposta.mensagem())
    
    print( "enviando outra mensagem para outra fila" )
    mensageria.envia("teste_smq_1", ["tres", "quatro" ], "teste 123")
    
    print( "recebe mensagem de forma não bloqueante" )
    resposta = mensageria.recebe_nao_bloqueante("teste_smq_1", "tres")
    while not resposta.existe():
        resposta = mensageria.recebe_nao_bloqueante("teste_smq_1", "tres")
    if not resposta.commit():
        print("Erro em comitar mensagem não bloqueante")
    print( "resposta da mensagem de forma não bloqueante" , resposta.mensagem())
    
    print( "recebe mensagem de forma não bloqueante com commit" )
    resposta = mensageria.recebe_nao_bloqueante_commit("teste_smq_1", "quatro")
    print( "resposta da mensagem de forma não bloqueante com commit" , resposta.mensagem())
    

    mensageria = smq_cliente.SmqCliente()
    print("Enviando mensagem")
    mensageria.envia("teste_smq", ["um", "dois" ], "teste 321")
    mensageria.envia("teste_smq", ["um", "dois" ], "teste 321")
    mensageria.envia("teste_smq", ["um", "dois" ], "teste 321")

    mensageria.fila_limpar("teste_smq")
    print("recebendo mensagem de forma bloqueante")
    resposta = mensageria.recebe_nao_bloqueante_commit("teste_smq", "um")
    if resposta.existe():
        print("Mensagem encontrada, isso não está certo")
    else:
        print("Mensagem não encotrada, sucesso")