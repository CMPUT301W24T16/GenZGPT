package com.example.genzgpt;

import static org.junit.jupiter.api.Assertions.*;

import com.example.genzgpt.Model.Role;
import org.junit.jupiter.api.Test;

public class RoleTest {
    @Test
    void testRole() {
        Role role = new Role("TestRole") {
        };
        assertEquals("TestRole", role.getName());
    }
}
