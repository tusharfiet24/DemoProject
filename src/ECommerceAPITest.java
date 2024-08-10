import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.OrderDetailResponse;
import pojo.OrderResponse;
import pojo.Orders;
import static io.restassured.RestAssured.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import files.ReUsableMethods;

public class ECommerceAPITest {

	public static void main(String[] args) {
		System.out.println("============Login==============");
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.setContentType(ContentType.JSON).build();
		
		LoginRequest loginReq = new LoginRequest();
		loginReq.setUserEmail("Arjun@gmail.com");
		loginReq.setUserPassword("Arjun@1998@");
		
		RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginReq);
		
		ResponseSpecification loginRes = new ResponseSpecBuilder().expectStatusCode(200)
				.expectContentType(ContentType.JSON).build();
		 
		LoginResponse loginResponse = reqLogin.when().post("/api/ecom/auth/login")
						.then().log().all().spec(loginRes)
						.extract().response().as(LoginResponse.class);
		
		String token = loginResponse.getToken();
		String userId = loginResponse.getUserId();
		String loginMsg = loginResponse.getMessage();
		
		Assert.assertEquals(loginMsg, "Login Successfully");
		
		System.out.println("============Add Product==============");
		String path = "E:\\Tushar\\API Testing Practise\\TusharLearning\\DemoProject";
		
		RequestSpecification addProdBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).build();
		
		RequestSpecification reqAddProd = given().spec(addProdBaseReq)
				.param("productName", "qwerty").param("productAddedBy", userId)
				.param("productCategory", "fashion").param("productSubCategory", "shirts")
				.param("productPrice", "11500").param("productDescription", "Addias Originals")
				.param("productFor", "women")
				.multiPart("productImage", new File(path + "//resources//Picture.jpg"));
		
		String addProdRes = reqAddProd.when().post("/api/ecom/product/add-product")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js = ReUsableMethods.rawToJson(addProdRes);
		String productId = js.getString("productId");
		String createProdMsg = js.getString("message");
		
		Assert.assertEquals(createProdMsg, "Product Added Successfully");
		
		System.out.println("============Create Order==============");
		RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).setContentType(ContentType.JSON).build();
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCountry("India");
		orderDetail.setProductOrderedId(productId);
		
		List<OrderDetail> orderList = new ArrayList<OrderDetail>();
		orderList.add(orderDetail);
		
		Orders orders = new Orders();
		orders.setOrders(orderList);
		
		RequestSpecification reqCreateOrder = given().log().all().spec(createOrderBaseReq).body(orders);
		
		OrderResponse orderRes = reqCreateOrder.when().post("/api/ecom/order/create-order")
		.then().log().all().assertThat().statusCode(201).extract().response().as(OrderResponse.class);
		
		String orderId = orderRes.getOrders().get(0);
		String actualProdId = orderRes.getProductOrderId().get(0);
		String createOrderMsg = orderRes.getMessage();
		
		Assert.assertEquals(actualProdId, productId);
		Assert.assertEquals(createOrderMsg, "Order Placed Successfully");
		
		System.out.println("============View Order Details==============");
		RequestSpecification viewOrderDetailsBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).build();
		
		RequestSpecification viewOrderDetails = given().log().all().spec(viewOrderDetailsBaseReq)
				.queryParam("id", orderId);
		
		OrderDetailResponse orderDetailsRes = viewOrderDetails.when().get("/api/ecom/order/get-orders-details")
		.then().log().all().assertThat().statusCode(200).extract().response().as(OrderDetailResponse.class);
		
		String actualOrderId = orderDetailsRes.getData().get_id();
		String viewOrderDetailsMsg = orderDetailsRes.getMessage();
		
		Assert.assertEquals(actualOrderId, orderId);
		Assert.assertEquals(viewOrderDetailsMsg, "Orders fetched for customer Successfully");
		
		System.out.println("============Delete Product==============");
		RequestSpecification delProdBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).build();
		
		RequestSpecification delProd = given().log().all().spec(delProdBaseReq).pathParam("productId", productId);
		
		String delProdRes = delProd.when().delete("/api/ecom/product/delete-product/{productId}")
				.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1 = ReUsableMethods.rawToJson(delProdRes);
		String delProdMsg = js1.getString("message");
		
		Assert.assertEquals(delProdMsg, "Product Deleted Successfully");
		
		System.out.println("============Delete Order==============");
		RequestSpecification delOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("Authorization", token).build();
		
		RequestSpecification delOrder = given().log().all().spec(delOrderBaseReq).pathParam("orderId", orderId);
		
		String delOrderRes = delOrder.when().delete("/api/ecom/order/delete-order/{orderId}")
				.then().log().all().extract().response().asString();
		
		JsonPath js2 = ReUsableMethods.rawToJson(delOrderRes);
		String delOrderMsg = js2.getString("message");
		
		Assert.assertEquals(delOrderMsg, "Orders Deleted Successfully");
	}
}
