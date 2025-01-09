package com.fulldev.formulario.form.dto;

import java.util.List;

public record QuestionDTO(String title, String questionDescription, String type, List<String> options, boolean required) {
}
