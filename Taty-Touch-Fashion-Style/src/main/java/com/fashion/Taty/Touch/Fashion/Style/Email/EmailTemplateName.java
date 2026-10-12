package com.fashion.Taty.Touch.Fashion.Style.Email;


import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVITE_ACCOUNT("activate_account");
    private final String name;

    EmailTemplateName(String name){
        this.name = name;
    }
}
