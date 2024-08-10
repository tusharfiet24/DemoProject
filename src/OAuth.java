import org.testng.annotations.Test;

import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class OAuth {
	@Test
	public void clientCredentialsOAuth() {
		RestAssured.baseURI = "https://rahulshettyacademy.com/oauthapi";
		
		String response = given().log().all()
							.formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
							.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
							.formParam("grant_type", "client_credentials")
							.formParam("scope", "trust")
							.when().post("/oauth2/resourceOwner/token")
							.then().log().all().assertThat().statusCode(200)
							.extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(response);
		String accessToken = js.getString("access_token");
		System.out.println(accessToken);
		
		String getCourseDetails = given().log().all().queryParam("access_token", accessToken)
									.when().get("/getCourseDetails")
									.then().log().all().assertThat().statusCode(401)
									.extract().response().asString();
		
		System.out.println(getCourseDetails);
	}
}
