import org.testng.annotations.Test;
import io.restassured.RestAssured;
import pojo.AddPlace;
import pojo.Location;

import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

public class SerializeTest {
	@Test
	public void serialize() {
		RestAssured.baseURI = "https://rahulshettyacademy.com/";
		
		AddPlace p = new AddPlace();
		p.setAccuracy(50);
		p.setAddress("29, side layout, cohen 09");
		p.setLanguage("French-IN");
		p.setName("Tushar Rathore Academy");
		p.setPhone_number("(+91) 983 893 3937");
		p.setWebsite("http://google.com");
		
		List<String> types = new ArrayList<String>();
		types.add("shoe park");
		types.add("shop");
		p.setTypes(types);
		
		Location lc = new Location();
		lc.setLat(-38.383494);
		lc.setLng(33.427362);
		p.setLocation(lc);
		
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
							.body(p)
							.when().post("maps/api/place/add/json")
							.then().log().all().statusCode(200)
							.extract().response().asString();
		
		System.out.println(response);
	}
}
