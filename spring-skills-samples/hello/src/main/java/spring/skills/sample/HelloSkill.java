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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import spring.skills.SpeechletResponseEnvelopeBuilder;
import spring.skills.handler.IntentRequestHandler;

@SpringBootApplication
public class HelloSkill {

	public static void main(String[] args) {
		SpringApplication.run(HelloSkill.class, args);
	}
	
	@Bean
	public IntentRequestHandler sayHello() {
		return (requestEnv) -> {
			return SpeechletResponseEnvelopeBuilder
						.withPlainText("Hello Spring World!")
						.cardTitle("Hello")
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

}
