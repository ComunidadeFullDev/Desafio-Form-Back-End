package com.fulldev.formulario.form.dto;

import java.util.List;

public record FormDTO(String title, String description, List<QuestionDTO> questions) {
}
