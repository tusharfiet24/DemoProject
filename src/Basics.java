import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import files.Payload;

public class Basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Validate if Add Place API is working as expected

		// Given- all input details
		// When- submit the API- resource, http method
		// Then- validate the response
		// Add Place -> Update Place with new address -> Get Place to validate if new
		// address is present in response

		RestAssured.baseURI = "https://rahulshettyacademy.com/";

		System.out.println("============Post Call==============");
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(Payload.addPlace())
				.when().post("maps/api/place/add/json")
				.then().log().all().assertThat()
				.statusCode(200).body("scope", equalTo("APP")).header("server", "Apache/2.4.52 (Ubuntu)")
				.extract().response().asString();
		
		JsonPath js = new JsonPath(response); // For parsing Json
		String placeId = js.getString("place_id");
		
		System.out.println("============Put Call==============");
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body("{\r\n"
						+ "    \"place_id\": \""+placeId+"\",\r\n"
						+ "    \"address\": \"70 Summer walk, UK\",\r\n"
						+ "    \"key\": \"qaclick123\"\r\n"
						+ "}")
				.when().put("maps/api/place/update/json")
				.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		System.out.println("============Get Call==============");
		given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
				.when().get("maps/api/place/get/json")
				.then().log().all().assertThat().statusCode(200).body("address", equalTo("70 Summer walk, UK"));
	}
}
