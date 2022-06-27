package it.matty.duels.utils;

import lombok.Getter;

@Getter
public class Placeholder {
    private final String key;
    private final String replaced;
    
    public Placeholder(String key, String replaced) {
        this.key = "%" + key + "%";
        this.replaced = replaced;
    }
}
