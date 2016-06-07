package domain;

public class RemoveFilePayload {
	
	private String filename;
	private String host;	
	
	public RemoveFilePayload() {
	}
	
	public RemoveFilePayload(String filename, String host) {
		this.filename = filename;
		this.host = host;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	
}
