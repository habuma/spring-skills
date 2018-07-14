package spring.skills;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

@RestController
public class HelloSpeechletController {
	
	@PostMapping("/hello")
	public SpeechletResponseEnvelope sayHello(SpeechletRequestEnvelope requestEnv) {
		SpeechletResponseEnvelope responseEnv = new SpeechletResponseEnvelope();
		
		SpeechletResponse response = new SpeechletResponse();
		
		SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		outputSpeech.setId("hello");
		outputSpeech.setSsml(asSSML("Greetings from a Spring Boot web application."));
		response.setOutputSpeech(outputSpeech);
		
		SimpleCard card = new SimpleCard();
		card.setTitle("Hello");
		card.setContent("Greetings from a Spring Boot web application.");
		response.setCard(card);
		
		responseEnv.setResponse(response);
		return responseEnv;
	}
	
	private static String asSSML(String text) {
		return "<speak>" + text + "</speak>";
	}	
	
}
