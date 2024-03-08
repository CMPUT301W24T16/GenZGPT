package com.example.genzgpt.Model;

/**
 * Abstract role class, just holds the name of the Role and a getter
 */
public abstract class Role {
    protected String name;

    /**
     * Constructs a Role by assigning its name.
     *
     * @param name
     * The name of the Role being constructed.
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * A getter for the name of the Role.
     *
     * @return
     * The name of the Role.
     */
    public String getName() {
        return name;
    }
}