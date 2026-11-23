package io.github.mikascss.emprestimo.service;

import io.github.mikascss.emprestimo.dto.equipamento.EquipamentoRegisterRequest;
import io.github.mikascss.emprestimo.dto.equipamento.EquipamentoUpdateRequest;
import io.github.mikascss.emprestimo.exception.ConflitoException;
import io.github.mikascss.emprestimo.exception.NaoEncontradoException;
import io.github.mikascss.emprestimo.model.Equipamento;
import io.github.mikascss.emprestimo.model.ResultModel;
import io.github.mikascss.emprestimo.model.TipoEquipamento;
import io.github.mikascss.emprestimo.repository.EmprestimoRepository;
import io.github.mikascss.emprestimo.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<ResultModel<Equipamento>> cadastrar(EquipamentoRegisterRequest cadastro) {
        if(equipamentoRepository.existsByPatrimonio(cadastro.patrimonio())){
            throw new ConflitoException("Patrimônio já cadastrado.");
        }
        Equipamento equipamento = new Equipamento();
        equipamento.setNome(cadastro.nome());
        equipamento.setPatrimonio(cadastro.patrimonio());
        equipamento.setTipo(cadastro.tipo());

        return ResultModel.resultSucesso(HttpStatus.CREATED, equipamentoRepository.save(equipamento));
    }

    public ResponseEntity<ResultModel<Equipamento>> atualizar(Long id, EquipamentoUpdateRequest dados){
        Equipamento equipamento = buscar(id).getBody().getDados();
        equipamento.setNome(dados.nome());
        equipamento.setTipo(dados.tipo());
        return ResultModel.resultSucesso(HttpStatus.OK, equipamentoRepository.save(equipamento));
    }

    public ResponseEntity<ResultModel<Void>> remover(Long id){
        Equipamento equipamento = buscar(id).getBody().getDados();
        
        if(emprestimoRepository.existsByEquipamentoId(id)){
            throw new ConflitoException("Equipamento tem empréstimos registrados.");
        }

        equipamentoRepository.deleteById(id);

        return ResultModel.resultSucesso(HttpStatus.NO_CONTENT);
    }
}
