package pomonitor.analyse.entity;

import java.util.List;

public class HotWordsListResponse 
{
	// ×´Ì¬Âë
	private int status;
	// ½á¹û
	private List<RetHotWord> results;
	// message
	private String message;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public List<RetHotWord> getResults() {
		return results;
	}

	public void setResults(List<RetHotWord> results) {
		this.results = results;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
