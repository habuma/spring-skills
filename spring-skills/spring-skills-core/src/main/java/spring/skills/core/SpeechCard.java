package spring.skills.core;

public class SpeechCard {

	private final String title;
	
	private final String text;
	
	private String smallImageUrl;
	
	private String largeImageUrl;
	
	//
	// TODO: Google Action cards also have
	//
	
	public SpeechCard(String title, String text) {
		this.title = title;
		this.text = text;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getText() {
		return text;
	}

	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}

	public String getLargeImageUrl() {
		return largeImageUrl;
	}
	
	public void setLargeImageUrl(String largeImageUrl) {
		this.largeImageUrl = largeImageUrl;
	}
	
}
