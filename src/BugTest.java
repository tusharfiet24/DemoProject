import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import java.io.File;
import files.Payload;
import files.ReUsableMethods;

public class BugTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RestAssured.baseURI = "https://gabhi8687.atlassian.net";
		String encodedToken = "Z2FiaGk4Njg3QGdtYWlsLmNvbTpBVEFUVDN4RmZHRjBRQWdYc2dYcU53QVR4YjdVV21HVy1kMXZDSjVsSHVBMjNEYWVVY3J0a0tYaldiSHBrcXVNNjk3LXhmN2dMVDRqMTU4U0J2d2s2UVR1OE5tTG5Tc3RlU1FmcjRFUkp1Wmx6RmZyblVoQU1TRFJ6SU84bVZmc3lJeVdWczdTaEZuU0xFZnV2T2RQLVg5V3RCazBnVTlIVlJnNkx3YkVNVWRGVUlHTVJ0RDczNEk9QzMzODQ1Nzg=";
		String accessToken = "Basic " + encodedToken; 
		
		String createIssueResponse = given().log().all().header("Content-Type", "application/json").header("Authorization", accessToken)
		.body(Payload.createIssue("Sticky search doesn't retain after page reload."))
		.when().post("/rest/api/3/issue")
		.then().log().all().assertThat().statusCode(201).contentType("application/json")
		.extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(createIssueResponse);
		String issueId = js.getString("id");
		System.out.println(issueId);
		
		// Add Attachment
		String path = "E:\\Tushar\\API Testing Practise\\TusharLearning\\DemoProject";
		
		given().log().all().pathParam("key", issueId)
				.header("Authorization", accessToken)
				.header("X-Atlassian-Token", "no-check")
				.multiPart("file", new File(path + "\\resources\\Capture.PNG"))
				.when().post("/rest/api/3/issue/{key}/attachments")
				.then().log().all().assertThat().statusCode(200);
	}
}
