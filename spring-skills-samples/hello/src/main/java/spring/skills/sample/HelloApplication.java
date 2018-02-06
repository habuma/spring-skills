package spring.skills.sample;

import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

@SpringBootApplication
public class HelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloApplication.class, args);
	}
	
	@Bean
	public Function<SpeechletRequestEnvelope, SpeechletResponseEnvelope> sayHello() {
		return (requestEnv) -> {
			SpeechletResponseEnvelope responseEnv = new SpeechletResponseEnvelope();
			SpeechletResponse response = new SpeechletResponse();
			SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
			outputSpeech.setId("hello");
			outputSpeech.setSsml("<speak>Hello Spring World!</speak>");
			response.setOutputSpeech(outputSpeech);
			SimpleCard card = new SimpleCard();
			card.setTitle("Hello");
			card.setContent("Hello Spring World!");
			response.setCard(card);
			responseEnv.setResponse(response);
			return responseEnv;
		};
	}
	
	@Bean
	public Function<SpeechletRequestEnvelope, SpeechletResponseEnvelope> help() {
		return (requestEnv) -> {
			SpeechletResponseEnvelope responseEnv = new SpeechletResponseEnvelope();
			SpeechletResponse response = new SpeechletResponse();
			SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
			outputSpeech.setId("hello_help");
			outputSpeech.setSsml("<speak>Just ask me to say hello</speak>");
			response.setOutputSpeech(outputSpeech);
			SimpleCard card = new SimpleCard();
			card.setTitle("Hello Help");
			card.setContent("Just ask me to say hello");
			response.setCard(card);
			responseEnv.setResponse(response);
			return responseEnv;
		};
	}

}
