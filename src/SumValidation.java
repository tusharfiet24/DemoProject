import org.testng.Assert;
import org.testng.annotations.Test;
import files.Payload;
import files.ReUsableMethods;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	@Test
	public void sumOfCourses() {
		JsonPath js = ReUsableMethods.rawToJson(Payload.coursePrice());
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		int noOfCourse = js.getInt("courses.size()");
		
		int totalPrice = 0;
		for (int i = 0; i < noOfCourse; i++) {
			int coursePrice = js.getInt("courses[" + i + "].price");
			int noOfCopiesSold = js.getInt("courses[" + i + "].copies");
			totalPrice += (coursePrice * noOfCopiesSold);
		}
		Assert.assertEquals(totalPrice, purchaseAmount);
	}
}