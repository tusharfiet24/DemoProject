import org.testng.Assert;
import org.testng.annotations.Test;
import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;
import static io.restassured.RestAssured.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OAuthTest {
	@Test
	public void clientCredentialsOAuth() {
		String[] courseTitles = {"Selenium Webdriver Java","Cypress","Protractor"};
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
		
//		String getCourseDetails = given().log().all().queryParam("access_token", accessToken)
//									.when().get("/getCourseDetails")
//									.then().log().all().assertThat().statusCode(401)
//									.extract().response().asString();
//		
//		System.out.println(getCourseDetails);
		
		GetCourse gc = given().log().all().queryParam("access_token", accessToken)
						.when().get("/getCourseDetails")
						.then().log().all().assertThat().statusCode(401)
						.extract().response().as(GetCourse.class);
		
		System.out.println(gc.getInstructor());
		System.out.println(gc.getLinkedIn());
		
//		System.out.println(gc.getCourses().getApi().get(1).getPrice());
		
		List<Api> apiCourses = gc.getCourses().getApi();
		for(int i = 0; i < apiCourses.size(); i++) {
			if(apiCourses.get(i).getCourseTitle().equals("SoapUI Webservices testing")) {
				String coursePrice = apiCourses.get(i).getPrice();
				System.out.println(coursePrice);
				break;
			}
		}
		
		List<WebAutomation> webAutomationCourses = gc.getCourses().getWebAutomation();
		ArrayList<String> actualList = new ArrayList<String>();
		for(int i = 0; i < webAutomationCourses.size(); i++) {
			String courseTitle = webAutomationCourses.get(i).getCourseTitle();
//			System.out.println(courseTitle);
			actualList.add(courseTitle);
		}
		
		List<String> expectedList = Arrays.asList(courseTitles);
		
//		Assert.assertEquals(actualList, expectedList);
		Assert.assertTrue(actualList.equals(expectedList));
		
	}
}
