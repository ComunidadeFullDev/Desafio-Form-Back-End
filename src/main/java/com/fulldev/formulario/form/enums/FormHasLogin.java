package com.fulldev.formulario.form.enums;

public enum FormHasLogin {
    PRIVATE("private"),
    PASSWORD("password"),
    PUBLIC("public");

    private String formLoginType;

    FormHasLogin(String formLoginType){
        this.formLoginType = formLoginType;
    }

    public String getFormLoginType() {
        return formLoginType;
    }
}
