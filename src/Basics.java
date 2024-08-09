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
		
		System.out.println(placeId);
	}
}
