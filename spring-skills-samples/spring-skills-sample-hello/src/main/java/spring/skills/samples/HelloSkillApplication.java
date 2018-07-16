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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import spring.skills.alexa.AlexaWebConfig;
import spring.skills.core.Speech;
import spring.skills.core.SpeechCard;
import spring.skills.core.SpeechRequest.Source;
import spring.skills.core.SpeechRequestHandler;
import spring.skills.core.SpeechResponse;

@SpringBootApplication
@Import(AlexaWebConfig.class)
public class HelloSkillApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSkillApplication.class, args);
	}
	
	/*
	 * This SpeechRequestHandler bean will handle requests for intents named
	 * "sayHello", "say-hello", "say_hello", or some variant thereof. 
	 */
	@Bean
	public SpeechRequestHandler sayHello() {
		return request -> {
			Source source = request.getSource();
			String textToSay = "Well, hello there. I'm handling a request on behalf of " + source + ".";
			Speech speech = new Speech();
			speech.setSsml("<speak>" + textToSay + "</speak>");
			SpeechCard card = new SpeechCard("Hello!", textToSay);
			SpeechResponse response = new SpeechResponse(speech, card);
			response.setEndSession(false);
			return response;
		};
	}
	
}
