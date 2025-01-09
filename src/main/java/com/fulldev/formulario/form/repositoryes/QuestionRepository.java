package com.fulldev.formulario.form.repositoryes;

import com.fulldev.formulario.form.model.entities.Form;
import com.fulldev.formulario.form.model.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByForm(Form form);

    List<Question> findByFormId(Long id);
}
