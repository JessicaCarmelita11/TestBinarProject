package com.example.ProjectBinar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Basic unit test that doesn't require Spring context. */
@DisplayName("ProjectBinar Application Tests")
class ProjectBinarApplicationTests {

  @Test
  @DisplayName("Application class should exist")
  void applicationClassExists() {
    assertDoesNotThrow(() -> Class.forName("com.example.ProjectBinar.ProjectBinarApplication"));
  }

  @Test
  @DisplayName("Main method should exist")
  void mainMethodExists() throws NoSuchMethodException {
    assertNotNull(ProjectBinarApplication.class.getMethod("main", String[].class));
  }
}
