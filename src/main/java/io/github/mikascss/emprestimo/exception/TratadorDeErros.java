package io.github.mikascss.emprestimo.exception;

import io.github.mikascss.emprestimo.model.ResultModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(NaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResposta naoEncontrado(NaoEncontradoException e){
        return  new ErroResposta(e.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResposta conflito(ConflitoException e){
        return new ErroResposta(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta validacao(MethodArgumentNotValidException e){
        String mensagem = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return new ErroResposta(mensagem);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta tipoErrado(MethodArgumentTypeMismatchException e){
        String mensagem = String.format(
                "O valor '%s' informado para o parâmetro '%s' é inválido. Tipo esperado: %s.",
                e.getValue(),
                e.getName(),
                e.getRequiredType() != null
                        ? e.getRequiredType().getSimpleName()
                        : "desconhecido"
        );
        return new ErroResposta(mensagem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErroResposta handleJsonInvalido(
            HttpMessageNotReadableException ex) {

        String mensagem = "JSON inválido ou com valores incompatíveis.";

        Throwable causa = ex.getCause();

        if (causa instanceof InvalidFormatException erro) {

            Class<?> tipo = erro.getTargetType();

            if (tipo != null && tipo.isEnum()) {
                mensagem = "Valor '" + erro.getValue()
                        + "' inválido. Valores permitidos: "
                        + Arrays.toString(tipo.getEnumConstants());
            }
        }

        return new ErroResposta(mensagem);
    }

}
