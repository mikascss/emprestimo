package io.github.mikascss.emprestimo.model;

import lombok.Getter;

@Getter
public enum TipoEquipamento {
    NOTEBOOK("Notebook", 30),
    MONITOR("Monitor", 14),
    MOUSE("Mouse", 7),
    TECLADO("Teclado", 7),
    HEADSET("Headset", 7),
    OUTRO("Outro", 14);

    private final String descricao;
    private final int prazoEmDias;

    TipoEquipamento(String descricao, int prazoEmDias){
        this.descricao = descricao;
        this.prazoEmDias = prazoEmDias;
    }
}
