package io.github.mikascss.emprestimo.service;

import io.github.mikascss.emprestimo.dto.EmprestimoRequest;
import io.github.mikascss.emprestimo.exception.ConflitoException;
import io.github.mikascss.emprestimo.exception.NaoEncontradoException;
import io.github.mikascss.emprestimo.model.Colaborador;
import io.github.mikascss.emprestimo.model.Emprestimo;
import io.github.mikascss.emprestimo.model.Equipamento;
import io.github.mikascss.emprestimo.model.ResultModel;
import io.github.mikascss.emprestimo.repository.EmprestimoRepository;
import io.github.mikascss.emprestimo.repository.EquipamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final EquipamentoService equipamentoService;
    private final ColaboradorService colaboradorService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             EquipamentoService equipamentoService,
                             ColaboradorService colaboradorService){
        this.emprestimoRepository = emprestimoRepository;
        this.equipamentoService = equipamentoService;
        this.colaboradorService = colaboradorService;
    }

    public ResponseEntity<ResultModel<Emprestimo>> buscar(Long id){
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Empréstimo não encontrado"));

        return ResultModel.resultSucesso(HttpStatus.OK, emprestimo);
    }

    public ResponseEntity<ResultModel<List<Emprestimo>>> listarDoColaborador(Long colaboradorId){
        List<Emprestimo> emprestimos = emprestimoRepository.findByColaboradorId(colaboradorId);

        return ResultModel.resultSucesso(HttpStatus.OK, emprestimos);
    }

    @Transactional
    public ResponseEntity<ResultModel<Emprestimo>> emprestar(EmprestimoRequest pedido){
        Equipamento equipamento = equipamentoService.buscar(pedido.equipamentoId()).getBody().getDados();
        Colaborador colaborador = colaboradorService.buscar(pedido.colaboradorId()).getBody().getDados();

        verificarSeColaboradorTemEmprestimoAtrasado(pedido.colaboradorId());

        if(!equipamento.isDisponivel()){
            throw new ConflitoException("Equipamento indisponível.");
        }

        LocalDate dtAtual = LocalDate.now();

        int prazo = equipamento.getTipo().getPrazoEmDias();

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setEquipamento(equipamento);
        emprestimo.setColaborador(colaborador);
        emprestimo.setDataEmprestimo(dtAtual);
        emprestimo.setDataPrevistaDevolucao(dtAtual.plusDays(prazo));
        emprestimo.getEquipamento().setDisponivel(false);

        return ResultModel.resultSucesso(HttpStatus.CREATED, emprestimoRepository.save(emprestimo));
    }

    @Transactional
    public ResponseEntity<ResultModel<Emprestimo>> devolver(Long id){
        Emprestimo emprestimo = buscar(id).getBody().getDados();

        if(emprestimo.getDataDevolucao() != null){
            throw new ConflitoException("Empréstimo já devolvido.");
        }

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.getEquipamento().setDisponivel(true);

        return ResultModel.resultSucesso(HttpStatus.OK, emprestimoRepository.save(emprestimo));
    }

    public ResponseEntity<ResultModel<List<Emprestimo>>> listar(Boolean atrasados){
        if(Boolean.TRUE.equals(atrasados)){
            List<Emprestimo> emprestimosAtrasados = emprestimoRepository.findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate.now());
            return ResultModel.resultSucesso(HttpStatus.OK, emprestimosAtrasados);
        }
        return ResultModel.resultSucesso(HttpStatus.OK, emprestimoRepository.findAll());
    }

    private void verificarSeColaboradorTemEmprestimoAtrasado(Long colaboradorId){
        List<Emprestimo> emprestimosPorColaborador = emprestimoRepository.findByColaboradorId(colaboradorId);
        if(!emprestimosPorColaborador.isEmpty()){
            for(Emprestimo e : emprestimosPorColaborador){
                if(e.isAtrasado()){
                    throw new ConflitoException("Esse colaborador está com empréstimo atrasado.");
                }
            }
        }
    }
}
