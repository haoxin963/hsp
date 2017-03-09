package net.hsp.web.util;

public class ViewModel {
	private String url;
	private String type;

	public ViewModel(String url, String type) {
		super();
		this.url = url;
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
