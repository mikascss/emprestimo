package io.github.mikascss.emprestimo.service;

import io.github.mikascss.emprestimo.dto.EmprestimoRequest;
import io.github.mikascss.emprestimo.exception.ConflitoException;
import io.github.mikascss.emprestimo.model.Colaborador;
import io.github.mikascss.emprestimo.model.Emprestimo;
import io.github.mikascss.emprestimo.model.Equipamento;
import io.github.mikascss.emprestimo.model.ResultModel;
import io.github.mikascss.emprestimo.model.TipoEquipamento;
import io.github.mikascss.emprestimo.repository.EmprestimoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    private static final LocalDate DATA_EMPRESTIMO = LocalDate.of(2026, 11, 14);
    private static final LocalDate DATA_DEVOLUCAO = LocalDate.of(2026, 11, 20);
    private static final EmprestimoRequest PEDIDO = new EmprestimoRequest(1L, 2L);

    @Mock
    private EmprestimoRepository repository;
    @Mock
    private EquipamentoService equipamentoService;
    @Mock
    private ColaboradorService colaboradorService;

    private EmprestimoService service;
    private Equipamento equipamento;

    @BeforeEach
    void configurar() {
        service = new EmprestimoService(repository, equipamentoService, colaboradorService);
        equipamento = new Equipamento(1L, "MON-001", "Monitor", TipoEquipamento.MONITOR, true);
    }

    @Test
    @DisplayName("Emprestar equipamento disponível o torna indisponível")
    void emprestarEquipamentoDisponivel() {
        prepararPedido();
        prepararPersistencia();

        try (MockedStatic<LocalDate> datas = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            datas.when(LocalDate::now).thenReturn(DATA_EMPRESTIMO);
            service.emprestar(PEDIDO);
        }

        assertFalse(equipamento.isDisponivel());
        verify(repository).save(any(Emprestimo.class));
    }

    @Test
    @DisplayName("R4: emprestar equipamento indisponível lança exceção")
    void rejeitarEquipamentoIndisponivel() {
        equipamento.setDisponivel(false);
        prepararPedido();

        assertThrows(ConflitoException.class, () -> service.emprestar(PEDIDO));

        assertFalse(equipamento.isDisponivel());
        verify(repository, never()).save(any(Emprestimo.class));
    }

    @Test
    @DisplayName("Devolver equipamento o torna disponível")
    void devolverEquipamento() {
        prepararEmprestimoAberto();
        prepararPersistencia();

        try (MockedStatic<LocalDate> datas = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            datas.when(LocalDate::now).thenReturn(DATA_DEVOLUCAO);
            service.devolver(3L);
        }

        assertTrue(equipamento.isDisponivel());
    }

    @Test
    @DisplayName("R5: empréstimo em 14/11 tem devolução prevista em 28/11")
    void criarEmprestimoComPrazoDeQuatorzeDias() {
        prepararPedido();
        prepararPersistencia();
        LocalDate prazoEsperado = LocalDate.of(2026, 11, 28);

        try (MockedStatic<LocalDate> datas = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            datas.when(LocalDate::now).thenReturn(DATA_EMPRESTIMO);
            Emprestimo emprestimo = service.emprestar(PEDIDO).getBody().getDados();

            assertEquals(DATA_EMPRESTIMO, emprestimo.getDataEmprestimo());
            assertEquals(prazoEsperado, emprestimo.getDataPrevistaDevolucao());
            assertNull(emprestimo.getDataDevolucao());
            verify(repository).save(emprestimo);
        }
    }

    @Test
    @DisplayName("R7: devolver empréstimo aberto em 20/11 registra essa data")
    void registrarDataDeDevolucao() {
        Emprestimo emprestimo = prepararEmprestimoAberto();
        prepararPersistencia();

        try (MockedStatic<LocalDate> datas = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            datas.when(LocalDate::now).thenReturn(DATA_DEVOLUCAO);
            service.devolver(3L);
        }

        assertEquals(DATA_DEVOLUCAO, emprestimo.getDataDevolucao());
        verify(repository).save(emprestimo);
    }

    @Test
    @DisplayName("R6: segunda devolução lança exceção e preserva a primeira data")
    void rejeitarSegundaDevolucao() {
        Emprestimo emprestimo = prepararEmprestimoAberto();
        prepararPersistencia();
        LocalDate diaSeguinte = DATA_DEVOLUCAO.plusDays(1);

        try (MockedStatic<LocalDate> datas = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            datas.when(LocalDate::now).thenReturn(DATA_DEVOLUCAO);
            service.devolver(3L);
            datas.when(LocalDate::now).thenReturn(diaSeguinte);

            assertThrows(ConflitoException.class, () -> service.devolver(3L));
        }

        assertEquals(DATA_DEVOLUCAO, emprestimo.getDataDevolucao());
        assertTrue(equipamento.isDisponivel());
        verify(repository, times(1)).save(emprestimo);
    }

    private void prepararPedido() {
        when(equipamentoService.buscar(1L))
                .thenReturn(ResultModel.resultSucesso(HttpStatus.OK, equipamento));
        when(colaboradorService.buscar(2L))
                .thenReturn(ResultModel.resultSucesso(HttpStatus.OK, new Colaborador()));
        when(repository.findByColaboradorId(2L)).thenReturn(List.of());
    }

    private void prepararPersistencia() {
        when(repository.save(any(Emprestimo.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private Emprestimo prepararEmprestimoAberto() {
        equipamento.setDisponivel(false);
        Emprestimo emprestimo = new Emprestimo(3L, equipamento, new Colaborador(),
                DATA_EMPRESTIMO, LocalDate.of(2026, 11, 28), null);
        when(repository.findById(3L)).thenReturn(Optional.of(emprestimo));
        return emprestimo;
    }
}
