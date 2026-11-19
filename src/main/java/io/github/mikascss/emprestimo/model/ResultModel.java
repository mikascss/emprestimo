package io.github.mikascss.emprestimo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultModel<T> {
    private int status;
    private String mensagem;
    private T dados;

    public static <T> ResponseEntity<ResultModel<T>> resultSucesso(
            HttpStatus status, T dados) {

        ResultModel<T> result = new ResultModel<>(
                status.value(),
                "Operação realizada com sucesso!",
                dados
        );

        return ResponseEntity.status(status).body(result);
    }

    public static ResponseEntity<ResultModel<Void>> resultSucesso(
            HttpStatus status) {

        ResultModel<Void> result = new ResultModel<>(
                status.value(),
                "Operação realizada com sucesso!",
                null
        );

        return ResponseEntity.status(status).body(result);
    }

    public static <T> ResponseEntity<ResultModel<T>> resultErro(
            HttpStatus status, String mensagem) {

        ResultModel<T> result = new ResultModel<>(
                status.value(),
                mensagem,
                null
        );

        return ResponseEntity.status(status).body(result);
    }
}
