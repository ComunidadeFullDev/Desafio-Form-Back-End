package com.fulldev.formulario.form.service;

import com.fulldev.formulario.form.dto.FormDTO;
import com.fulldev.formulario.form.dto.QuestionDTO;
import com.fulldev.formulario.form.model.entities.Form;
import com.fulldev.formulario.form.model.entities.Question;
import com.fulldev.formulario.form.repositoryes.FormRepository;
import com.fulldev.formulario.form.repositoryes.QuestionRepository;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class FormService {

    private final FormRepository formRepository;

    private final QuestionRepository questionRepository;

    public FormService(FormRepository formRepository, QuestionRepository questionRepository) {
        this.formRepository = formRepository;
        this.questionRepository = questionRepository;
    }

    public Form updateForm(Long id, FormDTO formDTO) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi encontrado nenhum formulário com o id: " + id));

        form.setTitle(formDTO.title());
        form.setDescription(formDTO.description());

        form.getQuestions().clear();
        for (QuestionDTO questionDTO : formDTO.questions()) {
            Question question = new Question();
            question.setForm(form);
            question.setTitle(questionDTO.title());
            question.setType(questionDTO.type());
            question.setQuestionDescription(questionDTO.questionDescription());
            question.setRequired(questionDTO.required());
            question.setOptions(questionDTO.options());
            form.getQuestions().add(question);
        }

        return formRepository.save(form);
    }

    public ResponseEntity deleteFormById(long id){
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formulário não encontrado com o id: "+id));
        formRepository.delete(form);
        return ResponseEntity.ok().body("Formulário deletado com sucesso");
    }


}
