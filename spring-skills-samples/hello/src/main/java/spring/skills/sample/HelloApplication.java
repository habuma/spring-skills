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
