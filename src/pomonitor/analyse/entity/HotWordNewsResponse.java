package pomonitor.analyse.entity;

public class HotWordNewsResponse {
	// ×´Ì¬Âë
	private int status;
	// ½á¹û
	private HotWord results;
	// message
	private String message;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public HotWord getResults() {
		return results;
	}

	public void setResults(HotWord results) {
		this.results = results;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
