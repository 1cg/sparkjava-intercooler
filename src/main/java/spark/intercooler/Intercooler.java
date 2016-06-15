package spark.intercooler;


import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Objects;

public class Intercooler
{
  private static final Intercooler INSTANCE = new Intercooler();
  private static final ThreadLocal<Request> REQ = new ThreadLocal<>();
  private static final ThreadLocal<Response> RESP = new ThreadLocal<>();
  private Intercooler() {}

  //============================================================
  // Public API
  //============================================================
  public static final Intercooler intercooler = INSTANCE;

  public static void initIntercooler()
  {
    Spark.before( INSTANCE::before );
    Spark.after( ( request, response ) -> {
      INSTANCE.after();
    } );
  }

  public boolean isRequest()
  {
    return Objects.equals( REQ.get().queryParams( "ic-request" ), "true" ) ||
           Objects.equals( REQ.get().params( "ic-request" ), "true" );
  }

  public boolean targetIs( String id )
  {
    return Objects.equals( REQ.get().queryParams( "ic-target" ), id ) ||
           Objects.equals( REQ.get().params( "ic-target" ), id );
  }

  public void trigger( String event )
  {
    RESP.get().header( "X-IC-Trigger", event );
  }

  //============================================================
  // Internals
  //============================================================
  private void before( Request request, Response response )
  {
    REQ.set( request );
    RESP.set( response );
  }

  private void after()
  {
    REQ.remove();
    RESP.remove();
  }
}
