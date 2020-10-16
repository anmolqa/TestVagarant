import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiRunner {

  String endPoint;
  String contentType;
  Map<String, String> queryParams;

  public ApiRunner(String endPoint, String contentType, Map<String, String> queryParams) {
    this.endPoint = endPoint;
    this.contentType = contentType;
    this.queryParams = queryParams;
  }

  public Response submitPost(Object payload) {
    String uri = buildEndpointUrl();
    System.out.println("Endpoint : " + uri);
    Response response =
        given().contentType(contentType).body(payload).when().post(uri).then().extract().response();
    return response;
  }

  public Response submitPut(Object payload) {
    String uri = buildEndpointUrl();
    System.out.println("Endpoint : " + uri);
    Response response =
        given().contentType(contentType).body(payload).when().put(uri).then().extract().response();
    return response;
  }

  public Response submitDelete() {
    String uri = buildEndpointUrl();
    return given().contentType("application/json").when().delete(uri).then().extract().response();
  }

  public Response submitDelete(Object payload) {
    String uri = buildEndpointUrl();
    return given()
        .contentType("application/json")
        .body(payload)
        .when()
        .delete(uri)
        .then()
        .extract()
        .response();
  }

  public Response submitGet() {
    String uri = buildEndpointUrl();
    Response response =
        given().contentType(contentType).when().get(uri).then().extract().response();
    return response;
  }

  private String buildEndpointUrl() {
    StringBuilder uri = new StringBuilder(endPoint);
    if (queryParams != null && queryParams.size() > 0) {
      uri.append("?");
      queryParams.forEach((key, value) -> uri.append(key + "=" + value + "&"));
      uri.deleteCharAt(uri.length() - 1);
    }

    return uri.toString();
  }
}
