package magpiebridge.core.analysis.configuration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import magpiebridge.core.MagpieServer;
import magpiebridge.util.JsonFormatHandler;
import org.apache.commons.io.IOUtils;

/** The class handles HTTP requests of the SARIF page. */
public class SarifFileUploadHttpHandler implements HttpHandler {

  private MagpieServer magpieServer;
  private String serverAddress;

  public SarifFileUploadHttpHandler(MagpieServer magpieServer, String serverAddress) {
    this.magpieServer = magpieServer;
    this.serverAddress = serverAddress;
  }

  public void handle(HttpExchange exchange) throws IOException {
    OutputStream outputStream = exchange.getResponseBody();
    try {
      if ("GET".equals(exchange.getRequestMethod().toUpperCase())) {
        String htmlPage = SarifFileUploadHtmlGenerator.generateHTML(null, this.serverAddress);

        exchange.sendResponseHeaders(200, htmlPage.length());
        outputStream.write(htmlPage.getBytes());
        outputStream.flush();
        outputStream.close();

      } else if ("POST".equals(exchange.getRequestMethod().toUpperCase())) {

        StringWriter writer = new StringWriter();
        IOUtils.copy(exchange.getRequestBody(), writer, StandardCharsets.UTF_8);
        String theString = JsonFormatHandler.getJsonFromString(writer.toString());
        JsonObject sarifJson = (JsonObject) new JsonParser().parse(theString);
        String finalResult = "";
        try {
          SARIFToAanlysisResultConverter sarifElement =
              new SARIFToAanlysisResultConverter(sarifJson);
          this.magpieServer.consume(sarifElement.getAnalysisResults(), "Sarif File Upload");
          finalResult =
              "SARIF file uploaded successfully. Please check the project to see the result.";
        } catch (Exception e) {
          finalResult = e.toString();
        } finally {
          exchange.sendResponseHeaders(200, finalResult.length());
          outputStream.write(finalResult.getBytes());
          outputStream.flush();
          outputStream.close();
        }
      }
    } finally {
      if (outputStream != null) outputStream.close();
    }
  }
}
