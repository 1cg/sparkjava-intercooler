package spark.intercooler;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;

import static spark.Spark.*;
import static spark.intercooler.Intercooler.*;

public class Demo
{
  public static void main( String[] args )
  {
    Intercooler.initIntercooler();

    get("/", (request, response) -> {
      return new ModelAndView( new HashMap<>(), "index.html.vm" );
    }, new VelocityTemplateEngine());

    get("/intercooler", (request, response) -> {
      if(intercooler.isRequest()){
        intercooler.trigger("MyClientSideEvent");
      }
      return "Welcome to Intercooler!";
    });

  }
}
