import com.google.gson.*;
import com.google.gson.reflect.*;
import org.apache.http.client.fluent.Request;
import java.util.*;

/*
 * must have apache http components and google gson on CLASSPATH
 * https://hc.apache.org/httpcomponents-client-ga/
 * http://search.maven.org/#artifactdetails%7Ccom.google.code.gson%7Cgson%7C2.6.1%7Cjar
 */
class demo {
  private String accessToken;
  // format from : http://searchupc.com/developers/welcome.aspx
  private static final String BASE_URL = "http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3";

  demo () {
    // received by signing up at http://www.searchupc.com
    accessToken = "C5E04049-C5F4-429A-9CEB-FA3A1AF74BFF";
  }

  public void getName(String upc) {
    // build the URL for the query: http://www.searchupc.com/handlers/upcsearch.ashx?request_type=3&access_token=C5E04049-C5F4-429A-9CEB-FA3A1AF74BFF&upc=49000024692

    String url = BASE_URL + "&access_token=" + accessToken + "&upc=" + upc;
    String response = "unknown";  // will hold the json response from the query

    try {
      // fluent API to Apache httpclient
      // https://hc.apache.org/httpcomponents-client-ga/tutorial/html/fluent.html
      response = Request.Get(url)
                 .connectTimeout(10000)
                 .socketTimeout(10000)
                 .execute()
                 .returnContent()
                 .asString();
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
    }

    if (!response.equals("unknown")) {
      // use gson to parse the server response and create a Product object
      // http://google.github.io/gson/apidocs/
      Gson gson = new Gson();
      JsonParser parser = new JsonParser();
      JsonElement jElem = (JsonElement) parser.parse(response).getAsJsonObject().get("0");

      // if there were no problems, we can now print the product name
      Product prod = gson.fromJson(jElem, Product.class);
      System.out.println(prod.productname);

      return;
    }

    System.out.println("Response was " + response);
  }

  public static void main(String[] args) {
    String upc = "49000024692";  // Diet Coke 6pk bottles

    new demo().getName(upc);
  }
}

