package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class App extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new App());
  }

  @Override
  public void start() {
    Router router = Router.router(vertx);

    StudentService studentService = new StudentService();

    router.route().handler(BodyHandler.create());
    router.get("/students").handler(studentService::getAll);
    router.post("/students").handler(studentService::addStudent);
    router.get("/students/:id").handler(studentService::getById);
    router.put("/students/:id").handler(studentService::updateStudent);
    router.delete("/students/:id").handler(studentService::deleteStudent);

    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8888, http -> {
        if (http.succeeded()) {
          System.out.println("Server started on http://localhost:8888");
        }
      });
  }
}
