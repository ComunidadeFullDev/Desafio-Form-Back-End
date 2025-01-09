package com.fulldev.formulario.form.model.entities;

import com.fulldev.formulario.security.domain.model.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private String createdBy;

    private String link;

    private boolean sendEmailResponsesCount = true;

    private String idPublic;

    @Column(nullable = true)
    private int responsesCount = 0;

    @Column(nullable = true)
    private int views = 0;

    @Enumerated(EnumType.STRING)
    private FormHasLogin formHasLogin = FormHasLogin.PUBLIC;

    @Column(nullable = true)
    private List<String> accessUsername;

    @Column(nullable = true)
    private String accessPassword;

    @Column(nullable = false)
    private Boolean isPublished = false;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
