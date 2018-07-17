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

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import spring.skills.core.IntentSpeechRequest;
import spring.skills.core.Parameter;
import spring.skills.core.Speech;
import spring.skills.core.SpeechCard;
import spring.skills.core.SpeechRequestHandler;
import spring.skills.core.SpeechResponse;

@SpringBootApplication
public class SpringSkillsSampleHokeyPokeyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSkillsSampleHokeyPokeyApplication.class, args);
	}
	
	@Autowired
	private HokeyPokeyProps props;
	
	@Bean
	public SpeechRequestHandler hokeyPokey() {
		return (request) -> {
			IntentSpeechRequest intentRequest = (IntentSpeechRequest) request;
			HashMap<String, Parameter> parameters = intentRequest.getParameters();
			String bodyPart = parameters.get("bodyPart").getValue();
			String textToSay = props.getLyrics().replaceAll("\\{bodyPart\\}", bodyPart).trim();
			Speech speech = new Speech();
			speech.setSsml("<speak>" + textToSay + "</speak>");
			SpeechCard card = new SpeechCard("Hokey Pokey", textToSay);
			SpeechResponse response = new SpeechResponse(speech, card);
			response.setEndSession(false);
			return response;
		};
	}
}
