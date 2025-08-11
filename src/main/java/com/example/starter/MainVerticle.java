package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    int port = 8888;

    Router router = Router.router(vertx);


    router.get("/hello").handler(routingContext -> {
      String name = routingContext.request().getParam("name");
      HttpServerResponse response = routingContext.response();


      String htmlResponse = "<h1>Hello, " + name + "!</h1>";

      response.putHeader("content-type", "text/html").end(htmlResponse);
    });


    router.get("/info").handler(routingContext -> {
      String server_status = "ok";
      String app_version = "1.0.0";

      String messy_json = "{" +
        "\"status\": \"" + server_status + "\"," +
        "\"version\": \"" + app_version + "\"" +
        "}";
      routingContext.response().putHeader("content-type", "application/json").end(messy_json);
    });

    // Start the server
    vertx.createHttpServer().requestHandler(router)
      .listen(port, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port " + port);
        } else {
          startPromise.fail(http.cause());
        }
      });
  }
  public static void main(String[] args) {
    io.vertx.core.Vertx vertx = io.vertx.core.Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

}
