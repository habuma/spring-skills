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
package spring.skills.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import spring.skills.SpeechletResponseEnvelopeBuilder;
import spring.skills.handler.IntentRequestHandler;
import spring.skills.handler.LaunchRequestHandler;

@SpringBootApplication
public class ConferenceSkill {

	private static Logger log = LoggerFactory.getLogger(ConferenceSkill.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ConferenceSkill.class, args);
	}
	
	@Autowired
	private ConferenceSkillProps props;
	
	@Bean
	public IntentRequestHandler sayHello() {
		return (requestEnv) -> {
			return SpeechletResponseEnvelopeBuilder
				.withPlainText(props.getMessage())
				.cardTitle("Welcome")
				.build();
		};
	}
	
	@Bean
	public IntentRequestHandler hokeyPokey() {
		return (requestEnv) -> {
			String part = requestEnv.getRequest().getIntent().getSlot("Part").getValue();
			String hokeyPokey = props.getHokey().replaceAll("\\{part\\}", part);
			return SpeechletResponseEnvelopeBuilder
					.withPlainText(hokeyPokey)
					.cardTitle("Hokey Pokey")
					.build();
		};
	}
	
	@Bean
	public IntentRequestHandler help() {
		return (requestEnv) -> {
			return SpeechletResponseEnvelopeBuilder
					.withPlainText("Just ask me to say hello")
					.cardTitle("Hello Help")
					.build();
		};
	}
	
	@Bean
	public LaunchRequestHandler launchRequestHandler() {
		return requestEnv -> {
			return SpeechletResponseEnvelopeBuilder
					.withPlainText(props.getMessage())
					.cardTitle("Welcome to DevNexus")
					.build();
		};
	}

}
