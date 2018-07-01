package spring.skills.alexa;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;

import spring.skills.alexa.AlexaSpeechRequestConverter;
import spring.skills.core.BeanNameSpeechRequestDispatcher;
import spring.skills.core.Speech;
import spring.skills.core.SpeechCard;
import spring.skills.core.SpeechRequest;
import spring.skills.core.SpeechRequestDispatcher;
import spring.skills.core.SpeechRequestHandler;
import spring.skills.core.SpeechResponse;
import spring.skills.core.SpringSkillsProperties;

@SpringBootApplication
public class TestConfiguration {

	@Bean
	public AlexaSpeechRequestConverter converter() {
		return new AlexaSpeechRequestConverter();
	}
	
	@Bean
	public SpeechRequestDispatcher dispatcher() {
		return new BeanNameSpeechRequestDispatcher(new SpringSkillsProperties(), null);
	}
	
	@Bean
	public SkillServletVerifier noopVerifier() {
		return new SkillServletVerifier() {
			@Override
			public void verify(HttpServletRequest servletRequest, byte[] serializedRequestEnvelope,
					RequestEnvelope deserializedRequestEnvelope) throws SecurityException {
			}
		};
	}
	
	@Bean
	public SpeechRequestHandler sayHello() {
		return new SpeechRequestHandler() {
			@Override
			public SpeechResponse handle(SpeechRequest request) {
				Speech speech = new Speech();
				speech.setSsml("<speak>Hello</speak>");
				
				SpeechCard card = new SpeechCard("Hello", "Well, hello there");
				SpeechResponse response = new SpeechResponse(speech, card);
				
				return response;
			}
		};
	}
	
}
