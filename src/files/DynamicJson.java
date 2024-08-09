package files;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class DynamicJson {
	@Test(dataProvider="BooksData")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166/";

		String response = given().log().all().header("Content-Type", "application/json")
				.body(Payload.addBook(isbn, aisle))
				.when().post("Library/Addbook.php")
				.then().log().all().assertThat().statusCode(200)
				.extract().response().asString();

		JsonPath js = ReUsableMethods.rawToJson(response);
		String bookId = js.getString("ID");
		System.out.println(bookId);
		
		String actualMsg = js.getString("Msg");
		Assert.assertEquals(actualMsg, "successfully added");
		
		//Delete book
		given().log().all().header("Content-Type", "application/json")
		.body("{\r\n"
				+ "    \"ID\": \""+bookId+"\"\r\n"
				+ "}")
		.when().post("Library/DeleteBook.php")
		.then().log().all().assertThat().statusCode(200)
		.body("msg",equalTo("book is successfully deleted"));
	}
	
	@DataProvider(name="BooksData")
	public Object[][] getData() {
		// Array = Collection of elements
		// MultiDimensional Array = Collection of array
		return new Object[][] {{"asd","356"},{"fdg","789"},{"kjl","890"}};
	}
}