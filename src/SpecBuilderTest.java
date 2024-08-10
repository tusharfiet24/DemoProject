import org.testng.annotations.Test;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import static io.restassured.RestAssured.*;
import java.util.ArrayList;
import java.util.List;

public class SpecBuilderTest {
	@Test
	public void serializeTest() {
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
		
		RequestSpecification reqspec = new RequestSpecBuilder()
											.setBaseUri("https://rahulshettyacademy.com/")
											.addQueryParam("key", "qaclick123")
											.setContentType(ContentType.JSON)
											.build();
		
		RequestSpecification req = given().spec(reqspec).body(p);
		
		ResponseSpecification res = new ResponseSpecBuilder()
										.expectStatusCode(200)
										.expectContentType(ContentType.JSON)
										.build();
						
		String response = req.when().post("maps/api/place/add/json")
							.then().spec(res)
							.extract().response().asString();
		
		System.out.println(response);
	}
}
