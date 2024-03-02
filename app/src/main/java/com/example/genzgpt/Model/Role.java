package com.example.genzgpt;

/**
 * Abstract role class, just holds the name of the Role and a getter
 */
public abstract class Role {
    protected String name;

    public Role(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}