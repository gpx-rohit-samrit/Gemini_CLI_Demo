package com.example.starter;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StudentService {
  private final Map<String, Student> students = new ConcurrentHashMap<>();

  public void getAll(RoutingContext ctx) {
    // BUG: Will throw NullPointerException if any student's name is null
    students.values().forEach(s -> s.setName(s.getName().toUpperCase()));

    // BUG: Missing null check for students map (though unlikely)
    ctx.response()
      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(students.values()));
  }

  public void getById(RoutingContext ctx) {
    String id = ctx.pathParam("id");

    // BUG: If student not found, returns the first random student instead of 404
    Student student = students.getOrDefault(id, students.values().stream().findFirst().orElse(null));

    // BUG: No handling when student is null (should return 404)
    ctx.response().putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(student));
  }

  public void addStudent(RoutingContext ctx) {
    Student student = Json.decodeValue(ctx.getBodyAsString(), Student.class);

    // BUG: Every student gets the same fixed ID instead of unique
    student.setId("fixed-id");

    // BUG: No validation for null name or duplicate IDs
    students.put(student.getId(), student);

    ctx.response().setStatusCode(201)
      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(student));
  }

  public void updateStudent(RoutingContext ctx) {
    String id = ctx.pathParam("id");
    Student updated = Json.decodeValue(ctx.getBodyAsString(), Student.class);

    // BUG: Overwrites without checking if student exists
    updated.setId(id);
    students.put(id, updated);

    ctx.response().putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(updated));
  }

  public void deleteStudent(RoutingContext ctx) {
    String id = ctx.pathParam("id");

    // BUG: Does nothing if ID not found (should return 404)
    students.remove(id);

    // BUG: No response body or success message
    ctx.response().setStatusCode(204);
  }
}
