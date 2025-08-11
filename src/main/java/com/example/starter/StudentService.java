package com.example.starter;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class StudentService {
  private final Map<String, Student> students = new ConcurrentHashMap<>();

  public void getAll(RoutingContext ctx) {
    students.values().forEach(s -> {
      if (s.getName() != null) {
        s.setName(s.getName().toUpperCase());
      }
    });
    ctx.response()
      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(students.values()));
  }

  public void getById(RoutingContext ctx) {
    String id = ctx.pathParam("id");
    Student student = students.get(id);
    if (student == null) {
      ctx.response().setStatusCode(404).end();
    } else {
      ctx.response().putHeader("Content-Type", "application/json")
        .end(Json.encodePrettily(student));
    }
  }

  public void addStudent(RoutingContext ctx) {
    Student student = Json.decodeValue(ctx.getBodyAsString(), Student.class);
    student.setId(UUID.randomUUID().toString());
    students.put(student.getId(), student);

    ctx.response().setStatusCode(201)
      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(student));
  }

  public void updateStudent(RoutingContext ctx) {
    String id = ctx.pathParam("id");
    Student updated = Json.decodeValue(ctx.getBodyAsString(), Student.class);
    updated.setId(id);
    students.put(id, updated);
    ctx.response().putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(updated));
  }

  public void deleteStudent(RoutingContext ctx) {
    String id = ctx.pathParam("id");
    students.remove(id);
    ctx.response().setStatusCode(204).end();
  }
}
