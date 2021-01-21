import requests
import json
import time 
from datetime import datetime, timedelta

class SmqCliente:
    __slots__ = ['_url' , '_tempo_consumo']

    def __init__(self , url="http://localhost:8080" , tempo_consumo = 604800 ):
        self._url = url
        self._tempo_consumo = (datetime.now()- timedelta( minutes=tempo_consumo)).isoformat()

    def envia(self , nome_fila="padrao" , lista_elementos_grupo = list() , mensagem = "" ):
        lista_consumidores = self.__envia_gera_lista_consumidores(lista_elementos_grupo)
        body = self.__envia_monta_body(nome_fila, lista_consumidores, mensagem)
        path = self.__envia_monta_path()
        result = self.__envia_( path , body )
        return result.status_code == 200

    def __envia_(self , path , body ):
        count = 5
        while count > 0:
            result = requests.post(path,json=body)       
            if result.status_code == 500:
                time.sleep(1)
                count -= 1
            else:
                count = 0
        return result 

    def recebe_bloqueante(self, nome_fila="padrao" , elemento_grupo = "padrao"):
        retorno = self.recebe_nao_bloqueante(nome_fila, elemento_grupo)
        while not retorno.existe():
            retorno = self.recebe_nao_bloqueante(nome_fila, elemento_grupo)
        return retorno 
        

    def recebe_nao_bloqueante(self, nome_fila="padrao" , elemento_grupo = "padrao"):
        retorno = self.__recebe(nome_fila,elemento_grupo)
        if not retorno.existe():
            time.sleep(0.001)
        return retorno

    def recebe_bloqueante_commit(self, nome_fila="padrao" , elemento_grupo = "padrao"):
        retorno = self.recebe_bloqueante(nome_fila, elemento_grupo)
        if retorno.existe():
            if not retorno.commit():
                raise Exception("Processo de commitar a mensagem não funcionou")
        return retorno

    def recebe_nao_bloqueante_commit(self, nome_fila="padrao" , elemento_grupo = "padrao"):
        retorno = self.recebe_nao_bloqueante(nome_fila, elemento_grupo)
        if retorno.existe():
            if not retorno.commit():
                raise Exception("Processo de commitar a mensagem não funcionou")
        return retorno
        
    def __recebe(self, nome_fila , elemento_grupo):
        path = self.__recebe_monta_path(nome_fila , elemento_grupo )
        result = self.__recebe_(path)
        return SmqCliente.SmqClienteRetornoConsumo( self._url , result.text if self.__recebe_status_valido(result) else None)
    
    def __recebe_(self, path ):
        count = 5
        while count > 0:
            result = requests.get(path)   
            if result.status_code == 500:
                time.sleep(1)
                count -= 1
            else:
                count = 0
        return result 

    def __recebe_status_valido(self, result ):
            return result.status_code == 200

    def __recebe_monta_path(self, nome_fila, elemento_grupo):
        return f"{self._url}/mensagem?fila={nome_fila}&grupo={elemento_grupo}"

    def __envia_monta_path(self):
        return f"{self._url}/mensagem"

    def __envia_monta_body(self , nome_fila, lista_consumidores, mensagem):
        body = dict()
        body['fila'] = {"nome":nome_fila}
        body['consumidores']  = lista_consumidores
        body['mensagem'] = mensagem
        body['data_expiração'] = self._tempo_consumo
        return body

    def __envia_gera_lista_consumidores(self, lista_elementos_grupo):
        retorno = list()
        if len( lista_elementos_grupo ) == 0:
            retorno.append({"nome":"padrao"})
        else:
            if type( lista_elementos_grupo )== str:
                retorno.append({"nome":lista_elementos_grupo})
            else:
                for elemento in lista_elementos_grupo:
                    retorno.append( {"nome":elemento})
        return retorno

    def fila_limpar( self , nome_fila ):
        path = self.__fila_limpar_monta_path(nome_fila)
        result = requests.delete(path)
        return result.status_code == 200
        

    def __fila_limpar_monta_path( self, nome_fila ):
        return f"{self._url}/fila/{nome_fila}"

    def __fila_limpar( self , path  ):
        count = 2
        while count > 0:
            result = requests.get(path)   
            if result.status_code == 500:
                time.sleep(1)
                count -= 1
            else:
                count = 0
        return result 
        
    class SmqClienteRetornoConsumo:
        __slots__=['_nome_grupo', '_id_mensagem', '_mensagem' , '_url' , '_ja_comitada']
        def __init__(self, url ,  str_json):
            self._url = url
            self._ja_comitada = False
            if str_json != None:
                obj_json = json.loads(str_json)
                self._nome_grupo = obj_json['nome_no_grupo']
                self._id_mensagem = obj_json['identificacao_mensagem']
                self._mensagem = obj_json['mensagem']
            else:
                self._nome_grupo = None
                self._id_mensagem = None
                self._mensagem = None
                        
        def existe(self):
            return self._mensagem != None
                
        def mensagem(self):
            return self._mensagem

        def commit(self):
            if self._mensagem != None and self._ja_comitada == False:
                path = self.__commit_monta_path()
                result = requests.post(path)
                if result.status_code == 200:
                    self._ja_comitada = True
                    return True
            return False

        def __commit_monta_path(self):
            return f"{self._url}/mensagem/{self._id_mensagem}/consumidor/{self._nome_grupo}"