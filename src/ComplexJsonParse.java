import org.testng.Assert;
import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsonPath js = new JsonPath(Payload.coursePrice());

		// 1. Print No of courses returned by API
		int noOfCourse = js.getInt("courses.size()");
		System.out.println("No of courses: " + noOfCourse);

		// 2. Print Purchase Amount
		int purchaseAmount = js.getInt("dashboard.purchaseAmount");
		System.out.println("Purchase Amount: " + purchaseAmount);

		// 3. Print Title of the first course
		String titleOfFirstCourse = js.getString("courses[0].title");
		System.out.println("Title of the first course: " + titleOfFirstCourse);

		// 4. Print All course titles and their respective Prices
		for (int i = 0; i < noOfCourse; i++) {
			String titleOfCourse = js.getString("courses[" + i + "].title");
			int respectivePrice = js.getInt("courses[" + i + "].price");
			System.out.println(
					"Title of the course: " + titleOfCourse + " and course respective price: " + respectivePrice);
		}

		// 5. Print no of copies sold by RPA Course
//		int noOfCopiesSold = js.getInt("courses[2].copies");
//		System.out.println("No of copies sold by RPA Course: " + noOfCopiesSold);

		for (int i = 0; i < noOfCourse; i++) {
			String titleOfCourse = js.getString("courses[" + i + "].title");
			if (titleOfCourse.equals("RPA")) {
				int noOfCopiesSold = js.getInt("courses[" + i + "].copies");
				System.out.println("No of copies sold by RPA Course: " + noOfCopiesSold);
				break;
			}
		}

		// 6. Verify if Sum of all Course prices matches with Purchase Amount
		int totalPrice = 0;
		for (int i = 0; i < noOfCourse; i++) {
			int coursePrice = js.getInt("courses[" + i + "].price");
			int noOfCopiesSold = js.getInt("courses[" + i + "].copies");
			totalPrice += (coursePrice * noOfCopiesSold);
		}
		Assert.assertEquals(totalPrice, purchaseAmount);
	}
}
