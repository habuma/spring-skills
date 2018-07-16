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
package spring.skills.autoconfig;

import java.util.logging.Logger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.skills.core.BeanNameSpeechRequestDispatcher;
import spring.skills.core.SessionEndedSpeechRequest;
import spring.skills.core.SessionEndedSpeechRequestHandler;
import spring.skills.core.Speech;
import spring.skills.core.SpeechCard;
import spring.skills.core.SpeechRequestDispatcher;
import spring.skills.core.SpeechResponse;

@Configuration
@ConditionalOnClass(SpeechRequestDispatcher.class)
public class SpringSkillsAutoConfiguration {

	private static Logger logger = Logger.getLogger(SpringSkillsAutoConfiguration.class.getName());

	@Bean
	@ConditionalOnMissingBean(value=SpeechRequestDispatcher.class)
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
				(request) -> {
					Speech speech = new Speech();
					speech.setSsml("<speak>Welcome</speak>");
					SpeechCard card = new SpeechCard("Welcome", "Welcome");
					SpeechResponse response = new SpeechResponse(speech, card);
					response.setEndSession(false);
					return response;
				}, // TODO : Consider a better way to specify this
				new SessionEndedSpeechRequestHandler() {
					@Override
					public void handleSessionEndedRequest(SessionEndedSpeechRequest request) {
						logger.info("Session ended. Reason: " + request.getReason());
					}
				});
	}

}
