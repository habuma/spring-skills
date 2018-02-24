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

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

import spring.skills.handler.IntentRequestHandler;
import spring.skills.handler.LaunchRequestHandler;

@SpringBootApplication
public class ConferenceSkill {

	Logger log = LoggerFactory.getLogger(ConferenceSkill.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ConferenceSkill.class, args);
	}
	
	@Autowired
	private ConferenceSkillProps props;
	
	@Bean
	public IntentRequestHandler sayHello() {
		return (requestEnv) -> {
			SpeechletResponseEnvelope responseEnv = new SpeechletResponseEnvelope();
			SpeechletResponse response = new SpeechletResponse();
			SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
			outputSpeech.setId("hello");
			outputSpeech.setSsml(wrapAsSSML(props.getMessage()));
			response.setOutputSpeech(outputSpeech);
			SimpleCard card = new SimpleCard();
			card.setTitle("Hello");
			card.setContent(props.getMessage());
			response.setCard(card);
			responseEnv.setResponse(response);
			return responseEnv;
		};
	}
	
	@Bean
	public IntentRequestHandler hokeyPokey() {
		return (requestEnv) -> {
			IntentRequest request = (IntentRequest) requestEnv.getRequest();
			Intent intent = request.getIntent();
			Slot slot = intent.getSlot("Part");
			String part = slot.getValue();
			SpeechletResponseEnvelope responseEnv = new SpeechletResponseEnvelope();
			SpeechletResponse response = new SpeechletResponse();
			SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
			outputSpeech.setId("hokey_pokey");
			
			String hokeyPokey = props.getHokey().replaceAll("\\{part\\}", part);
			
			outputSpeech.setSsml(wrapAsSSML(hokeyPokey));
			response.setOutputSpeech(outputSpeech);
			SimpleCard card = new SimpleCard();
			card.setTitle("Hokey Pokey");
			card.setContent(hokeyPokey);
			response.setCard(card);
			responseEnv.setResponse(response);
			return responseEnv;
		};
	}
	
	@Bean
	public IntentRequestHandler help() {
		return (requestEnv) -> {
			SpeechletResponseEnvelope responseEnv = new SpeechletResponseEnvelope();
			SpeechletResponse response = new SpeechletResponse();
			SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
			outputSpeech.setId("hello_help");
			outputSpeech.setSsml(wrapAsSSML("Just ask me to say hello"));
			response.setOutputSpeech(outputSpeech);
			SimpleCard card = new SimpleCard();
			card.setTitle("Hello Help");
			card.setContent("Just ask me to say hello");
			response.setCard(card);
			responseEnv.setResponse(response);
			return responseEnv;
		};
	}
	
	@Bean
	public LaunchRequestHandler launchRequestHandler() {
		return new LaunchRequestHandler() {
			
			@Override
			public SpeechletResponseEnvelope handleLaunchRequest(SpeechletRequestEnvelope<SpeechletRequest> requestEnvelope) {
				SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
				SpeechletResponse response = new SpeechletResponse();
				SimpleCard card = new SimpleCard();
				card.setTitle("Welcome to DevNexus");
				card.setContent(props.getMessage());
				response.setCard(card);
				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
				outputSpeech.setId("devnexus_welcome");
				outputSpeech.setText(props.getMessage());
				response.setOutputSpeech(outputSpeech);
				responseEnvelope.setResponse(response);
				return responseEnvelope;
			}
		};
	}

	private static String wrapAsSSML(String text) {
		return "<speak>" + text + "</speak>";
	}
}
