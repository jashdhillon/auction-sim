package jdbc;

public class Bid {
	
	private int bid_id;
	private int post_id;
	private String user_id;
	private double amount;
	
	public Bid(int bid_id, int post_id, String user_id, double amount) {
		this.bid_id = bid_id;
		this.post_id = post_id;
		this.user_id = user_id;
		this.amount = amount;
	}

	public int getBid_id() {
		return bid_id;
	}

	public void setBid_id(int bid_id) {
		this.bid_id = bid_id;
	}

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String toJSON() {
		return "{ " 
				+ "\"bid_id\": "
				+ this.bid_id + ", "
				+ "\"post_id\": "
				+ "\"" + this.post_id + "\", "
				+ "\"user_id\": "
				+ "\"" + this.user_id + "\", "
				+ "\"amount\": "
				+ "\"" + this.amount + "\" "
				+ " }";
	}

}
