package simplesmq.domain.enuns;

public enum StatusElementoEmAgrupamentoEnum {
    ENCONTADO("Encontrou o arquivo"),
    NAO_ENCONTRADO("Não encontrou o arquvivo"),
    FALHA("Falha em buscar o arquivo"),
    ADICIONADO("Elemento adicionado"),
    REMOVIDO("Elemento removido");

    String descricao;
    StatusElementoEmAgrupamentoEnum(String descricao){
        this.descricao = descricao;
    }
}
