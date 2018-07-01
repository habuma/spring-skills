package spring.skills.core;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Locale;

public class IntentSpeechRequest extends SpeechRequest {

	private final String intentName;
	
	private HashMap<String, Parameter> parameters = new HashMap<>();

	public IntentSpeechRequest(String intentName, String requestId, OffsetDateTime timestamp, Locale locale) {
		super(requestId, timestamp, locale);
		this.intentName = intentName;
	}
	
	public String getIntentName() {
		return intentName;
	}
		
	public HashMap<String, Parameter> getParameters() {
		return parameters;
	}
	
}
