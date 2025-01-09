package com.fulldev.formulario.form.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fulldev.formulario.security.domain.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    @JsonIgnore
    private Form form;

    @ManyToOne
    @JoinColumn(name = "answered_by_id")
    private User answeredBy;

    @ElementCollection
    @CollectionTable(name = "answer_details", joinColumns = @JoinColumn(name = "answer_id"))
    @MapKeyColumn(name = "question_id")
    @Column(name = "response")
    private Map<Long, String> answers = new HashMap<>();
}
