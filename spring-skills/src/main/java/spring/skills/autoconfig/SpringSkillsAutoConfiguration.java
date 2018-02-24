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

import java.util.function.Function;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletRequest;

import spring.skills.IntentDispatcher;
import spring.skills.SpeechletRequestDispatcher;
import spring.skills.SpringSkillsProperties;
import spring.skills.handler.DefaultLaunchRequestHandler;
import spring.skills.handler.DefaultSessionEndedRequestHandler;
import spring.skills.handler.DefaultUnknownIntentHandler;
import spring.skills.handler.DefaultUnknownRequestHandler;
import spring.skills.handler.LaunchRequestHandler;
import spring.skills.handler.SessionEndedRequestHandler;
import spring.skills.handler.UnknownIntentHandler;
import spring.skills.handler.UnknownRequestHandler;

@Configuration
@PropertySource("classpath:spring/skills/autoconfig/skills.properties")
public class SpringSkillsAutoConfiguration {
	
	@Bean
	public SpringSkillsProperties springSkillsProperties() {
		return new SpringSkillsProperties();
	}

	@Bean
	public Function<SpeechletRequestEnvelope<SpeechletRequest>, SpeechletResponseEnvelope> _requestDispatcher() {
		return requestEnv -> {
			return _speechletRequestDispatcher().handle(requestEnv);
		};
	}

	@Bean
	@ConditionalOnMissingBean(SpeechletRequestDispatcher.class)
	public SpeechletRequestDispatcher _speechletRequestDispatcher() {
		return new SpeechletRequestDispatcher();
	}

	@Bean
	@ConditionalOnMissingBean
	public IntentDispatcher _intentDispatcher() {
		IntentDispatcher intentDispatcher = new IntentDispatcher();
		return intentDispatcher;
	}

	@Bean
	@ConditionalOnMissingBean
	public LaunchRequestHandler _launchRequestHandler() {
		return new DefaultLaunchRequestHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public SessionEndedRequestHandler _sessionEndedRequestHandler() {
		return new DefaultSessionEndedRequestHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public UnknownRequestHandler _unknownRequestHandler() {
		return new DefaultUnknownRequestHandler();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public UnknownIntentHandler _unknownIntentHandler() {
		return new DefaultUnknownIntentHandler();
	}

}
