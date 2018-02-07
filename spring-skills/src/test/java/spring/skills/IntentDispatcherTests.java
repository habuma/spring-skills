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
package spring.skills;

import static org.junit.Assert.assertEquals;
import static spring.skills.SpringSkillsTestHelpers.buildTestRequestEnvelope;
import static spring.skills.SpringSkillsTestHelpers.buildTestSession;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.annotation.UserConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

import spring.skills.autoconfig.SpringSkillsAutoConfiguration;
import spring.skills.handler.UnknownIntentHandler;

public class IntentDispatcherTests {
	
	private final ApplicationContextRunner contextRunner = 
			new ApplicationContextRunner()
				.withConfiguration(
						AutoConfigurations.of(SpringSkillsAutoConfiguration.class));
	
	@Test
	public void shouldHandleSimpleIntent() {
		contextRunner
		.withConfiguration(UserConfigurations.of(IntentTestConfiguration.class))
		.run(
				context -> {
					IntentDispatcher dispatcher = context.getBean(IntentDispatcher.class);
					Intent intent = Intent.builder().withName("SayHello").build();
					SpeechletResponseEnvelope responseEnv = dispatcher.handleIntentRequest(getIntentRequestEnvelope(intent));
					
					PlainTextOutputSpeech outputSpeech = 
							(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
					assertEquals("Hello world!", outputSpeech.getText());
				}
			);		
	}
	
	@Test
	public void shouldHandleSimpleIntentWithAlternateNaming() {
		contextRunner
		.withConfiguration(UserConfigurations.of(IntentTestConfiguration.class))
		.run(
				context -> {
					IntentDispatcher dispatcher = context.getBean(IntentDispatcher.class);
					Intent intent = Intent.builder().withName("say_hello").build();
					SpeechletResponseEnvelope responseEnv = dispatcher.handleIntentRequest(getIntentRequestEnvelope(intent));
					
					PlainTextOutputSpeech outputSpeech = 
							(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
					assertEquals("Hello world!", outputSpeech.getText());
				}
			);	
	}
	
	@Test
	public void shouldHandleUnknownIntentWithDefaultHandler() {
		contextRunner.run(
				context -> {
					IntentDispatcher dispatcher = context.getBean(IntentDispatcher.class);
					Intent intent = Intent.builder().withName("BogusIntent").build();
					SpeechletResponseEnvelope responseEnv = dispatcher.handleIntentRequest(getIntentRequestEnvelope(intent));
					
					PlainTextOutputSpeech outputSpeech = 
							(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
					assertEquals("There was an error: Unknown intent.", outputSpeech.getText());
				}
			);	
	}
	
	@Test
	public void shouldHandleUnknownIntentWithCustomHandler() {
		contextRunner
		.withConfiguration(UserConfigurations.of(IntentTestConfiguration.class))
		.run(
				context -> {
					IntentDispatcher dispatcher = context.getBean(IntentDispatcher.class);
					Intent intent = Intent.builder().withName("BogusIntent").build();
					SpeechletResponseEnvelope responseEnv = dispatcher.handleIntentRequest(getIntentRequestEnvelope(intent));
					
					PlainTextOutputSpeech outputSpeech = 
							(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
					assertEquals("I beg your pardon.", outputSpeech.getText());
				}
			);	
	}

	//
	// private test helpers
	//
	private SpeechletRequestEnvelope getIntentRequestEnvelope(Intent intent) {
		SpeechletRequest request = 
				IntentRequest.builder()
					.withIntent(intent)
					.withRequestId("request_1")
					.withTimestamp(new Date())
					.build();
		Session session = buildTestSession();
		return buildTestRequestEnvelope(request, session);
	}	

	//
	// Test configuration
	//
	@Configuration
	public static class IntentTestConfiguration {
		@Bean(name={"say_hello", "sayHello"})
		public Function<SpeechletRequestEnvelope, SpeechletResponseEnvelope> say_hello() {
			return (req) -> {
				SpeechletResponseEnvelope responseEnv = new SpeechletResponseEnvelope();
				SpeechletResponse response = new SpeechletResponse();
				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
				outputSpeech.setId("output_speech_1");
				outputSpeech.setText("Hello world!");
				response.setOutputSpeech(outputSpeech);
				responseEnv.setResponse(response);
				return responseEnv;
			};
		}
		
		@Bean
		public UnknownIntentHandler unknownIntentHandler() {
			return requestEnvelope -> {
				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
				outputSpeech.setText("I beg your pardon.");
				SimpleCard card = new SimpleCard();
				card.setTitle("What?");
				card.setContent("I beg your pardon.");
				SpeechletResponse response = SpeechletResponse.newTellResponse(outputSpeech, card);
				SpeechletResponseEnvelope envelope = new SpeechletResponseEnvelope();
				envelope.setResponse(response);
				envelope.setSessionAttributes(new HashMap<>());
				envelope.setVersion("1.0");
				return envelope;
			};
		}
	}
}
