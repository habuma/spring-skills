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
package spring.skills.alexa;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;

import spring.skills.core.BeanNameSpeechRequestDispatcher;
import spring.skills.core.IntentSpeechRequest;
import spring.skills.core.Parameter;
import spring.skills.core.Speech;
import spring.skills.core.SpeechCard;
import spring.skills.core.SpeechRequest;
import spring.skills.core.SpeechRequestDispatcher;
import spring.skills.core.SpeechRequestHandler;
import spring.skills.core.SpeechResponse;

@SpringBootApplication
public class TestConfiguration {

	@Bean
	public AlexaSpeechRequestConverter converter() {
		return new AlexaSpeechRequestConverter();
	}
	
	@Bean
	public SpeechRequestDispatcher dispatcher() {
		return new BeanNameSpeechRequestDispatcher(null, null, null);
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
	
	@Bean
	public SpeechRequestHandler myFavorites() {
		return request -> {
			IntentSpeechRequest intentRequest = (IntentSpeechRequest) request;
			HashMap<String, Parameter> parameters = intentRequest.getParameters();
			String color = parameters.get("color").getValue();
			String animal = parameters.get("animal").getValue();
			
			String textToSay = "Your favorites are " + color + " and " + animal + ".";
			Speech speech = new Speech();
			speech.setSsml("<speak>" + textToSay + "</speak>");
			
			SpeechCard card = new SpeechCard("Favorites", textToSay);
			SpeechResponse response = new SpeechResponse(speech, card);
			
			return response;
		};
	}
	
}
