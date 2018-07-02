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
package spring.skills.alexa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.amazon.ask.model.ui.StandardCard;

import spring.skills.core.BuiltInIntents;
import spring.skills.core.IntentSpeechRequest;
import spring.skills.core.Speech;
import spring.skills.core.SpeechCard;
import spring.skills.core.SpeechRequest;
import spring.skills.core.SpeechResponse;

public class AlexaSpeechRequestConverterTests {
	
	@Test
	public void shouldConvertAlexaBuiltInIntentsToPlatformNeutralIntents() {
		AlexaSpeechRequestConverter converter = new AlexaSpeechRequestConverter();

		RequestEnvelope helpRequestEnvelope = buildIntentRequestEnvelope(AlexaBuiltInIntents.HELP_INTENT);
		IntentSpeechRequest helpSpeechRequest = (IntentSpeechRequest) converter.toSpeechRequest(helpRequestEnvelope);
		assertEquals(BuiltInIntents.HELP_INTENT, helpSpeechRequest.getIntentName());

		RequestEnvelope stopRequestEnvelope = buildIntentRequestEnvelope(AlexaBuiltInIntents.STOP_INTENT);
		IntentSpeechRequest stopSpeechRequest = (IntentSpeechRequest) converter.toSpeechRequest(stopRequestEnvelope);
		assertEquals(BuiltInIntents.STOP_INTENT, stopSpeechRequest.getIntentName());

		RequestEnvelope cancelRequestEnvelope = buildIntentRequestEnvelope(AlexaBuiltInIntents.CANCEL_INTENT);
		IntentSpeechRequest cancelSpeechRequest = (IntentSpeechRequest) converter.toSpeechRequest(cancelRequestEnvelope);
		assertEquals(BuiltInIntents.CANCEL_INTENT, cancelSpeechRequest.getIntentName());
	}

	private RequestEnvelope buildIntentRequestEnvelope(String intentName) {
		Intent intent = Intent.builder()
			.withName(intentName)
			.build();
		
		OffsetDateTime timestamp = OffsetDateTime.of(2018, 7, 15, 12, 0, 0, 0, ZoneOffset.UTC);
		IntentRequest request = IntentRequest.builder()
			.withRequestId("request-1")
			.withTimestamp(timestamp)
			.withLocale("en-US")
			.withIntent(intent)
			.build();
		
		RequestEnvelope envelope = RequestEnvelope.builder()
			.withRequest(request)
			.withVersion("1.0")
			.build();
		return envelope;
	}
	
	@Test
	public void shouldConvertRequestEnvelopeToSpeechRequest() {
		Map<String, Slot> slots = new HashMap<>();
		slots.put("foo", Slot.builder().withName("foo").withValue("abc").build());
		slots.put("bar", Slot.builder().withName("bar").withValue("def").build());
		
		Intent intent = Intent.builder()
			.withName("hello")
			.withSlots(slots)
			.build();
		
		OffsetDateTime timestamp = OffsetDateTime.of(2018, 7, 15, 12, 0, 0, 0, ZoneOffset.UTC);
		IntentRequest request = IntentRequest.builder()
			.withRequestId("request-1")
			.withTimestamp(timestamp)
			.withLocale("en-US")
			.withIntent(intent)
			.build();
		
		RequestEnvelope envelope = RequestEnvelope.builder()
			.withRequest(request)
			.withVersion("1.0")
			.build();
		
		AlexaSpeechRequestConverter converter = new AlexaSpeechRequestConverter();
		SpeechRequest speechRequest = converter.toSpeechRequest(envelope);
		
		assertTrue(speechRequest instanceof IntentSpeechRequest);
		IntentSpeechRequest intentSpeechRequest = (IntentSpeechRequest) speechRequest;
		
		assertEquals("request-1", speechRequest.getRequestId());
		assertEquals(new Locale("en-US"), speechRequest.getLocale());
		assertEquals(timestamp, speechRequest.getTimestamp());
		
		assertEquals("hello", intentSpeechRequest.getIntentName());
		assertEquals("foo", intentSpeechRequest.getParameters().get("foo").getName());
		assertEquals("abc", intentSpeechRequest.getParameters().get("foo").getValue());
		assertEquals("bar", intentSpeechRequest.getParameters().get("bar").getName());
		assertEquals("def", intentSpeechRequest.getParameters().get("bar").getValue());
	}
	
	@Test
	public void shouldConvertSpeechResponseToResponseEnvelope() {
		Speech speech = new Speech();
		speech.setSsml("<speak>Hello World</speak>");
		
		SpeechCard card = new SpeechCard("Hello", "Hello, world!");
		
		SpeechResponse speechResponse = new SpeechResponse(speech, card);
		
		AlexaSpeechRequestConverter converter = new AlexaSpeechRequestConverter();
		ResponseEnvelope responseEnvelope = converter.toPlatformResponse(speechResponse);
		
		assertEquals("1.0", responseEnvelope.getVersion());
		Response response = responseEnvelope.getResponse();
		assertEquals("<speak>Hello World</speak>", ((SsmlOutputSpeech) response.getOutputSpeech()).getSsml());
		assertEquals("Hello", ((StandardCard) response.getCard()).getTitle());
		assertEquals("Hello, world!", ((StandardCard) response.getCard()).getText());
		
		assertTrue(response.getShouldEndSession());
	}
	
}
