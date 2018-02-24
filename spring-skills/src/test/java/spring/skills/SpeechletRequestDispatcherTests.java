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
import static spring.skills.SpringSkillsTestHelpers.getBogusRequest;
import static spring.skills.SpringSkillsTestHelpers.getIntentRequest;
import static spring.skills.SpringSkillsTestHelpers.getLaunchRequest;
import static spring.skills.SpringSkillsTestHelpers.getSessionEndedRequest;

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
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

import spring.skills.autoconfig.SpringSkillsAutoConfiguration;
import spring.skills.handler.LaunchRequestHandler;
import spring.skills.handler.SessionEndedRequestHandler;
import spring.skills.handler.UnknownRequestHandler;

public class SpeechletRequestDispatcherTests {

	private final ApplicationContextRunner contextRunner = 
			new ApplicationContextRunner()
				.withConfiguration(
						AutoConfigurations.of(SpringSkillsAutoConfiguration.class));

	@Test
	public void shouldHandleLaunchRequestWithDefaultResponse() {
		contextRunner.run(
			context -> {
				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
				SpeechletResponseEnvelope responseEnv = dispatcher.handle(getLaunchRequest());
				PlainTextOutputSpeech outputSpeech = 
						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
				assertEquals("Welcome", outputSpeech.getText());
			}
		);
	}
	
	@Test
	public void shouldHandleLaunchRequestWithOverridingRequestHandler() {
		contextRunner
		.withConfiguration(UserConfigurations.of(CustomSpringSkillConfiguration.class))
		.run(
			context -> {
				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
				SpeechletResponseEnvelope responseEnv = dispatcher.handle(getLaunchRequest());
				PlainTextOutputSpeech outputSpeech = 
						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
				assertEquals("Buenos dias", outputSpeech.getText());
			}
		);
	}
	
	@Test
	public void shouldHandleSessionEndedRequestWithDefaultResponse() {
		contextRunner.run(
			context -> {
				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
				SpeechletResponseEnvelope responseEnv = dispatcher.handle(getSessionEndedRequest());
				PlainTextOutputSpeech outputSpeech = 
						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
				assertEquals("Goodbye", outputSpeech.getText());
			}
		);
	}

	@Test
	public void shouldHandleSessionEndedRequestWithOverridingRequestHandler() {
		contextRunner.run(
			context -> {
				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
				SpeechletResponseEnvelope responseEnv = dispatcher.handle(getSessionEndedRequest());
				PlainTextOutputSpeech outputSpeech = 
						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
				assertEquals("Goodbye", outputSpeech.getText());
			}
		);
	}

	@Test
	public void shouldHandleUnrecognizedRequestWithDefaultResponse() {
		contextRunner
		.run(
			context -> {
				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
				SpeechletResponseEnvelope responseEnv = dispatcher.handle(getBogusRequest());
				PlainTextOutputSpeech outputSpeech = 
						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
				assertEquals("There was an error: Unknown request type."
						, outputSpeech.getText());			}
		);
	}
	
	@Test
	public void shouldHandleUnrecognizedRequestWithOverridingRequestHandler() {
		contextRunner.withConfiguration(UserConfigurations.of(CustomSpringSkillConfiguration.class))
		.run(
			context -> {
				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
				SpeechletResponseEnvelope responseEnv = dispatcher.handle(getBogusRequest());
				PlainTextOutputSpeech outputSpeech = 
						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
				assertEquals("I have no idea what you're asking for."
						, outputSpeech.getText());			
			}
		);
	}
	
	@Test
	public void shouldHandleIntentRequest() {
		contextRunner.withConfiguration(UserConfigurations.of(CustomSpringSkillConfiguration.class))
		.run(
			context -> {
				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
				Intent intent = Intent.builder()
						.withName("hello")
						.build();
				SpeechletResponseEnvelope responseEnv = dispatcher.handle(getIntentRequest(intent));
				PlainTextOutputSpeech outputSpeech = 
						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
				assertEquals("Hello Spring World", outputSpeech.getText());			
			}
		);
	}
	 
	@Configuration
  	public static class CustomSpringSkillConfiguration {
		
		@Bean
		public Function<SpeechletRequestEnvelope<SpeechletRequest>, SpeechletResponseEnvelope> hello() {
			return (requestEnvelope) -> {
				SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
				SpeechletResponse response = new SpeechletResponse();
				SimpleCard card = new SimpleCard();
				card.setTitle("Hello");
				card.setContent("Hello Spring World");
				response.setCard(card);
				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
				outputSpeech.setId("hello");
				outputSpeech.setText("Hello Spring World");
				response.setOutputSpeech(outputSpeech);
				responseEnvelope.setResponse(response);
				return responseEnvelope;
			};
		}
		
		@Bean
		public LaunchRequestHandler launchRequestHandler() {
			return (requestEnvelope) -> {
				SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
				SpeechletResponse response = new SpeechletResponse();
				SimpleCard card = new SimpleCard();
				card.setTitle("Buenos dias");
				card.setContent("Buenos dias");
				response.setCard(card);
				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
				outputSpeech.setId("buenos_dias");
				outputSpeech.setText("Buenos dias");
				response.setOutputSpeech(outputSpeech);
				responseEnvelope.setResponse(response);
				return responseEnvelope;
			};
		}
		
		@Bean
		public SessionEndedRequestHandler sessionEndedRequestHandler() {
			return (requestEnvelope) -> {
				SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
				SpeechletResponse response = new SpeechletResponse();
				SimpleCard card = new SimpleCard();
				card.setTitle("Adios");
				card.setContent("Adios");
				response.setCard(card);
				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
				outputSpeech.setId("adios");
				outputSpeech.setText("Adios");
				response.setOutputSpeech(outputSpeech);
				responseEnvelope.setResponse(response);
				return responseEnvelope;
			};
		}
		
		@Bean
		public UnknownRequestHandler unknownRequestHandler() {
			return (requestEnvelope) -> {
				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
				outputSpeech.setText("I have no idea what you're asking for.");
	
				SimpleCard card = new SimpleCard();
				card.setTitle("Spring Skills Error");
				card.setContent("I have no idea what you're asking for.");
	
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
