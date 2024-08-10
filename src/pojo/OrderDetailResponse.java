package pojo;

public class OrderDetailResponse {
	private OrderData data;
	private String message;

	public OrderData getData() {
		return data;
	}

	public void setData(OrderData data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
