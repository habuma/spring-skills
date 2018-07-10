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
package spring.skills.core;

import static org.junit.Assert.assertEquals;

import java.time.OffsetDateTime;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import spring.skills.core.SpeechRequest.Source;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=BeanNameSpeechRequestDispatcherTests.TestConfig.class)
public class BeanNameSpeechRequestDispatcherTests {

	@Autowired
	SpeechRequestDispatcher dispatcher;
	
	@Test
	public void shouldDispatchToBeanMatchingIntentName() {
		SpeechRequest request = buildIntentRequest("sayHello");
		SpeechResponse response = dispatcher.dispatchRequest(request);
		assertEquals(response.getSpeech().getSsml(), TestConfig.HELLO_SSML);
	}

	@Test
	public void shouldDispatchToBeanMatchingCapitalizedIntentName() {
		SpeechRequest request = buildIntentRequest("SayHello");
		SpeechResponse response = dispatcher.dispatchRequest(request);
		assertEquals(response.getSpeech().getSsml(), TestConfig.HELLO_SSML);
	}
	
	@Test
	public void shouldDispatchToBeanMatchingSuffixedIntentName() {
		SpeechRequest request = buildIntentRequest("sayHelloIntent");
		SpeechResponse response = dispatcher.dispatchRequest(request);
		assertEquals(response.getSpeech().getSsml(), TestConfig.HELLO_SSML);
	}
	
	@Test
	public void shouldDispatchToBeanMatchingHyphenatedIntentName() {
		SpeechRequest request = buildIntentRequest("say-hello");
		SpeechResponse response = dispatcher.dispatchRequest(request);
		assertEquals(response.getSpeech().getSsml(), TestConfig.HELLO_SSML);
	}
	
	@Test
	public void shouldDispatchUnknownIntentToDefaultUnknownRequestHandler() {
		SpeechRequest request = buildIntentRequest("bogusIntent");
		SpeechResponse response = dispatcher.dispatchRequest(request);
		assertEquals(response.getSpeech().getSsml(), TestConfig.UNKNOWN_INTENT_SSML);		
	}
	
	private IntentSpeechRequest buildIntentRequest(String intentName) {
		return new IntentSpeechRequest(Source.ALEXA, intentName, "request_1", OffsetDateTime.now(), Locale.US);
	}

	@Configuration
	public static class TestConfig {
		
		public static final String HELLO_SSML = "<speak>Hello</speak>";

		public static final String UNKNOWN_INTENT_SSML = "<speak>I'm not sure what you're asking</speak>";

		@Bean
		public SpeechRequestHandler sayHello() {
			return (request) -> {
				Speech speech = new Speech();
				speech.setSsml(HELLO_SSML);
				SpeechCard card = new SpeechCard("Hello", "Hello, world!");
				SpeechResponse response = new SpeechResponse(speech, card);
				return response;
			};
		}
		
		
		@Bean
		public SpeechRequestDispatcher speechRequestDispatcher() {
			return new BeanNameSpeechRequestDispatcher( 
					(request) -> {
						Speech speech = new Speech();
						speech.setSsml(UNKNOWN_INTENT_SSML);
						SpeechCard card = new SpeechCard("Huh", "Huh?");
						SpeechResponse response = new SpeechResponse(speech, card);
						return response;
					}, null, null);
		}
		
	}
	
}
