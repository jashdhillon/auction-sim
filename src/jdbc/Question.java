package jdbc;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Question {
	
	private int message_id;
	private String poster_id;
	private String question;
	private Timestamp post_time;
	private ArrayList<Response> responses;
	
	public Question(int message_id, String poster_id, String question, Timestamp post_time) {
		this.message_id = message_id;
		this.poster_id = poster_id;
		this.question = question;
		this.post_time = post_time;
		this.responses = new ArrayList<Response>();
	}

	public int getMessage_id() {
		return message_id;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}

	public String getPoster_id() {
		return poster_id;
	}

	public void setPoster_id(String poster_id) {
		this.poster_id = poster_id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Timestamp getPost_time() {
		return post_time;
	}

	public void setPost_time(Timestamp post_time) {
		this.post_time = post_time;
	}

	public ArrayList<Response> getResponses() {
		return responses;
	}

	public void setResponses(ArrayList<Response> responses) {
		this.responses = responses;
	}
	
	public void addResponse(Response response) {
		this.responses.add(response);
	}
	
	private String responsesToJSON() {
		String result = "[ ";
		
		for(int i = 0; i < this.responses.size() - 1; i++) {
			Response pr = this.responses.get(i);
			
			result += pr.toJSON() + ", ";
		}
		
		if(this.responses.size() > 0) {
			Response pr = this.responses.get(this.responses.size() - 1);
			result += pr.toJSON();
		}
		
		result += " ]";
		
		return result;
	}
	
	public String toJSON() {
		return "{ " 
				+ "\"message_id\": "
				+ this.message_id + ", "
				+ "\"poster_id\": "
				+ "\"" + this.poster_id + "\", "
				+ "\"question\": "
				+ "\"" + this.question.replaceAll("\n", PostUtils.NEW_LINE_REP).replaceAll("\r", PostUtils.CARRIAGE_RETURN_REP) + "\", "
				+ "\"post_time\": "
				+ "\"" + this.post_time + "\", "
				+ "\"responses\": "
				+ responsesToJSON()
				+ " }";
	}
}
