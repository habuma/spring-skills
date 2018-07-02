package spring.skills.alexa.handlers;

import spring.skills.core.Speech;
import spring.skills.core.SpeechCard;
import spring.skills.core.SpeechRequest;
import spring.skills.core.SpeechRequestHandler;
import spring.skills.core.SpeechResponse;

public class LaunchRequestHandler implements SpeechRequestHandler {

	@Override
	public SpeechResponse handle(SpeechRequest request) {
		Speech speech = new Speech();
		speech.setSsml("<speak>Welcome</speak>");
		SpeechCard card = new SpeechCard("Welcome", "Welcome");
		return new SpeechResponse(speech, card);		
	}

}
