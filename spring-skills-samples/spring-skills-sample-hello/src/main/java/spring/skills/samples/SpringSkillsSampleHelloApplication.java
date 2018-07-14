/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spring.skills.samples;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;

import spring.skills.alexa.AlexaIntentController;
import spring.skills.alexa.AlexaSpeechRequestConverter;
import spring.skills.alexa.AlexaWebConfig;
import spring.skills.core.BeanNameSpeechRequestDispatcher;
import spring.skills.core.SessionEndedSpeechRequest;
import spring.skills.core.SessionEndedSpeechRequestHandler;
import spring.skills.core.Speech;
import spring.skills.core.SpeechCard;
import spring.skills.core.SpeechRequest.Source;
import spring.skills.core.SpeechRequestDispatcher;
import spring.skills.core.SpeechRequestHandler;
import spring.skills.core.SpeechResponse;
import spring.skills.google.WebhookController;

@SpringBootApplication
@Import(AlexaWebConfig.class)
public class SpringSkillsSampleHelloApplication {

	private static Logger logger = Logger.getLogger(SpringSkillsSampleHelloApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(SpringSkillsSampleHelloApplication.class, args);
	}
	
	@Bean
	public SpeechRequestHandler sayHello() {
		return request -> {
			Source source = request.getSource();
			logger.info("HANDLING SPEECH REQUEST FROM:  " + source);
			String textToSay = "Well, hello there. I'm handling a request on behalf of " + source + ".";
			Speech speech = new Speech();
			speech.setSsml("<speak>" + textToSay + "</speak>");
			SpeechCard card = new SpeechCard("Hello!", textToSay);
			SpeechResponse response = new SpeechResponse(speech, card);
			response.setEndSession(false);
			return response;
		};
	}

	//
	// TODO :The following beans and the @Import need to be handled by auto-config
	//	
	@Bean
	public AlexaSpeechRequestConverter converter() {
		return new AlexaSpeechRequestConverter();
	}
	
	@Bean
	public SpeechRequestDispatcher dispatcher() {
		return new BeanNameSpeechRequestDispatcher( 
				(request) -> {
					Speech speech = new Speech();
					speech.setSsml("<speak>I don't know what you're doing...</speak>");
					SpeechCard card = new SpeechCard("Unknown", "Unknown intent");
					SpeechResponse response = new SpeechResponse(speech, card);
					response.setEndSession(false);
					return response;
				}, 
				sayHello(), // TODO : Consider a better way to specify this
				new SessionEndedSpeechRequestHandler() {
					@Override
					public void handleSessionEndedRequest(SessionEndedSpeechRequest request) {
						logger.info("Session ended. Reason: " + request.getReason());
					}
				});
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
	public AlexaIntentController alexaController() {
		return new AlexaIntentController(dispatcher());
	}
	
	@Bean
	public WebhookController webhookController() {
		return new WebhookController(dispatcher());
	}
	
}
