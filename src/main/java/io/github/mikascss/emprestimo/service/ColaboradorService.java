package io.github.mikascss.emprestimo.service;

import io.github.mikascss.emprestimo.exception.ConflitoException;
import io.github.mikascss.emprestimo.exception.NaoEncontradoException;
import io.github.mikascss.emprestimo.model.Colaborador;
import io.github.mikascss.emprestimo.model.ResultModel;
import io.github.mikascss.emprestimo.repository.ColaboradorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorService(ColaboradorRepository colaboradorRepository){
        this.colaboradorRepository = colaboradorRepository;
    }

    public ResponseEntity<ResultModel<List<Colaborador>>> listar(){
        return ResultModel.resultSucesso(HttpStatus.OK, colaboradorRepository.findAll());
    }

    public ResponseEntity<ResultModel<Colaborador>> buscar(Long id){
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Colaborador não encontrado"));

        return ResultModel.resultSucesso(HttpStatus.OK, colaborador);
    }

    public ResponseEntity<ResultModel<Colaborador>> cadastrar(Colaborador colaborador){
        if(colaboradorRepository.existsByEmail(colaborador.getEmail())){
            throw new ConflitoException("E-mail já cadastrado");
        }

        return ResultModel.resultSucesso(HttpStatus.CREATED, colaboradorRepository.save(colaborador));
    }
}
