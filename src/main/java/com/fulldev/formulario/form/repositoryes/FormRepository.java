package com.fulldev.formulario.form.repositoryes;

import com.fulldev.formulario.form.model.entities.Form;
import com.fulldev.formulario.security.domain.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findByCreatedBy(String email);
    List<Form> findByCreatedByAndIsPublishedTrue(String createdBy);
    Form findByidPublic(String idPublic);
}
