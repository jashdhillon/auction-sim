package jdbc;

import java.sql.Timestamp;

public class Response {
	
	private int response_id;
	private int message_id;
	private String responder_id;
	private String response;
	private Timestamp post_time;
	
	public Response(int response_id, int message_id, String responder_id, String response, Timestamp post_time) {
		this.response_id = response_id;
		this.message_id = message_id;
		this.responder_id = responder_id;
		this.response = response;
		this.post_time = post_time;
	}

	public int getResponse_id() {
		return response_id;
	}

	public void setResponse_id(int response_id) {
		this.response_id = response_id;
	}

	public int getMessage_id() {
		return message_id;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}

	public String getResponder_id() {
		return responder_id;
	}

	public void setResponder_id(String responder_id) {
		this.responder_id = responder_id;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Timestamp getPost_time() {
		return post_time;
	}

	public void setPost_time(Timestamp post_time) {
		this.post_time = post_time;
	}
	
	public String toJSON() {
		return "{ "
				+ "\"response_id\": "
				+ this.response_id + ", "
				+ "\"message_id\": "
				+ this.message_id + ", "
				+ "\"responder_id\": "
				+ "\"" + this.responder_id + "\", "
				+ "\"response\": "
				+ "\"" + this.response.replaceAll("\n", PostUtils.NEW_LINE_REP).replaceAll("\r", PostUtils.CARRIAGE_RETURN_REP) + "\", "
				+ "\"post_time\": "
				+ "\"" + this.post_time + "\""
				+ " }";
	}
}