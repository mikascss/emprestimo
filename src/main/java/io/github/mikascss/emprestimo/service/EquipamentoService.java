package io.github.mikascss.emprestimo.service;

import io.github.mikascss.emprestimo.exception.ConflitoException;
import io.github.mikascss.emprestimo.exception.NaoEncontradoException;
import io.github.mikascss.emprestimo.model.Emprestimo;
import io.github.mikascss.emprestimo.model.Equipamento;
import io.github.mikascss.emprestimo.model.ResultModel;
import io.github.mikascss.emprestimo.model.TipoEquipamento;
import io.github.mikascss.emprestimo.repository.EmprestimoRepository;
import io.github.mikascss.emprestimo.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    private final EmprestimoRepository emprestimoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository,
                              EmprestimoRepository emprestimoRepository){
        this.equipamentoRepository = equipamentoRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    public ResponseEntity<ResultModel<List<Equipamento>>> listar(TipoEquipamento tipo,
                                                                 Boolean disponivel){
        if(tipo != null && disponivel != null){
            return ResultModel.resultSucesso(HttpStatus.OK, equipamentoRepository
                    .findByTipoAndDisponivel(tipo, disponivel));
        }
        if(tipo != null){
            return ResultModel.resultSucesso(HttpStatus.OK,equipamentoRepository.findByTipo(tipo));
        }
        if(disponivel != null){
            return ResultModel.resultSucesso(HttpStatus.OK,equipamentoRepository.findByDisponivel(disponivel));
        }
        return ResultModel.resultSucesso(HttpStatus.OK,equipamentoRepository.findAll());
    }

    public ResponseEntity<ResultModel<Equipamento>> buscar(Long id){
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Equipamento não encontrado!"));
        return ResultModel.resultSucesso(HttpStatus.OK, equipamento);
    }

    public ResponseEntity<ResultModel<Equipamento>> cadastrar(Equipamento equipamento) {
        if(equipamentoRepository.existsByPatrimonio(equipamento.getPatrimonio())){
            throw new ConflitoException("Patrimônio já cadastrado.");
        }
        equipamento.setDisponivel(true);

        return ResultModel.resultSucesso(HttpStatus.CREATED, equipamentoRepository.save(equipamento));
    }

    public ResponseEntity<ResultModel<Equipamento>> atualizar(Long id, Equipamento dados){
        Equipamento equipamento = buscar(id).getBody().getDados();
        equipamento.setNome(dados.getNome());
        equipamento.setTipo(dados.getTipo());
        return ResultModel.resultSucesso(HttpStatus.OK, equipamentoRepository.save(equipamento));
    }

    public ResponseEntity<ResultModel<Void>> remover(Long id){
        Equipamento equipamento = buscar(id).getBody().getDados();
        if(emprestimoRepository.existsByEquipamentoId(id)){
            throw new ConflitoException("Equipamento tem empréstimos registrados.");
        }

        return ResultModel.resultSucesso(HttpStatus.NO_CONTENT);
    }
}
