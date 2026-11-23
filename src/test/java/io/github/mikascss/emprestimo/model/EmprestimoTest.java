package io.github.mikascss.emprestimo.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class EmprestimoTest {
    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "R8: aberto após o prazo está atrasado, 2026-10-27, 2026-11-10, , true",
            "Aberto antes do prazo não está atrasado, 2026-11-06, 2026-11-22, , false",
            "Devolvido não está atrasado mesmo após o prazo, 2026-10-27, 2026-11-10, 2026-11-12, false",
            "Aberto no dia do prazo não está atrasado, 2026-10-31, 2026-11-14, , false"
    })
    void verificarAtrasoEmQuatorzeDeNovembro(String caso, LocalDate dataEmprestimo,
                                             LocalDate dataPrevista, LocalDate dataDevolucao,
                                             boolean atrasoEsperado) {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setDataEmprestimo(dataEmprestimo);
        emprestimo.setDataPrevistaDevolucao(dataPrevista);
        emprestimo.setDataDevolucao(dataDevolucao);

        LocalDate dataConsulta = LocalDate.of(2026, 11, 14);

        try (MockedStatic<LocalDate> mockedDate =
                     Mockito.mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS)) {

            mockedDate.when(LocalDate::now)
                    .thenReturn(dataConsulta);

            boolean isAtrasado = emprestimo.isAtrasado();

            assertEquals(atrasoEsperado, isAtrasado, caso);
        }

    }

}
