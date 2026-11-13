package io.github.mikascss.emprestimo.service;

import io.github.mikascss.emprestimo.model.Equipamento;
import io.github.mikascss.emprestimo.model.TipoEquipamento;
import io.github.mikascss.emprestimo.repository.EquipamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository){
        this.equipamentoRepository = equipamentoRepository;
    }

    public List<Equipamento> listar(TipoEquipamento tipo){
        if(tipo == null){
            return equipamentoRepository.findAll();
        }
        return equipamentoRepository.findByTipo(tipo);
    }

    public Equipamento buscar(Long id){
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Equipamento não encontrado"));
    }

    public Equipamento cadastrar(Equipamento equipamento) {
        if(equipamentoRepository.existsByPatrimonio(equipamento.getPatrimonio())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Patrimônio já cadastrado.");
        }
        equipamento.setDisponivel(true);

        return equipamentoRepository.save(equipamento);
    }
}
