import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.testng.Assert;
import files.ReUsableMethods;

public class Basics {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Validate if Add Place API is working as expected

		// Given- all input details
		// When- submit the API- resource, http method
		// Then- validate the response
		// Add Place -> Update Place with new address -> Get Place to validate if new
		// address is present in response
		// content of the file to String -> content of file can convert into byte -> byte data to String

		RestAssured.baseURI = "https://rahulshettyacademy.com/";
		String path = "E:\\Tushar\\API Testing Practise\\TusharLearning\\DemoProject";

		System.out.println("============Post Call==============");
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get(path + "\\resources\\AddPlace.json"))))
				.when().post("maps/api/place/add/json")
				.then().log().all().assertThat()
				.statusCode(200).body("scope", equalTo("APP")).header("server", "Apache/2.4.52 (Ubuntu)")
				.extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(response); // For parsing Json
		String placeId = js.getString("place_id");
		
		System.out.println("============Put Call==============");
		String newAddress = "Summer walk, India";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body("{\r\n"
						+ "    \"place_id\": \""+placeId+"\",\r\n"
						+ "    \"address\": \""+newAddress+"\",\r\n"
						+ "    \"key\": \"qaclick123\"\r\n"
						+ "}")
				.when().put("maps/api/place/update/json")
				.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		System.out.println("============Get Call==============");
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
				.when().get("maps/api/place/get/json")
				.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = ReUsableMethods.rawToJson(getPlaceResponse);
		String actualAddress = js1.getString("address");
		
		Assert.assertEquals(actualAddress, newAddress);
	}
}
