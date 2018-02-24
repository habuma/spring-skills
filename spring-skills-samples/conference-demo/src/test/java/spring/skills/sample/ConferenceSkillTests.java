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

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.User;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT, classes=ConferenceSkill.class)
public class ConferenceSkillTests {

	@Autowired
	TestRestTemplate testRest;
	
	private static String HELLO_MESSAGE = 
			"Hello, and welcome to DevNexus! Thank you for attending Craig's session. "
			+ "I hope you enjoy it!\n";
	
	private static String HOKEY_POKEY_MESSAGE =
			"You put your elbow in. You put your elbow out. You put your elbow in, "
			+ "and you shake it all about. You do the hokey-pokey and turn yourself around. "
			+ "That's what it's all about.\n";
	
	@Test
	public void shouldSayHelloThroughRequestDispatcher() throws Exception {
		SpeechletRequestEnvelope<IntentRequest> requestEnv = buildHelloIntentRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SpeechletRequestEnvelope<IntentRequest>> requestEntity = new HttpEntity<SpeechletRequestEnvelope<IntentRequest>>(requestEnv, headers);
		SpeechletResponseEnvelope responseEnv = testRest.postForObject("/_requestDispatcher", requestEntity, SpeechletResponseEnvelope.class);
		SimpleCard card = (SimpleCard) responseEnv.getResponse().getCard();
		assertEquals("Welcome", card.getTitle());
		assertEquals(HELLO_MESSAGE, card.getContent());
		PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
		assertEquals(HELLO_MESSAGE, outputSpeech.getText());
	}
	
	@Test
	public void shouldDoHokeyPokeyThroughRequestDispatcher() throws Exception {
		SpeechletRequestEnvelope<IntentRequest> requestEnv = buildHokeyPokeyIntentRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SpeechletRequestEnvelope<IntentRequest>> requestEntity = new HttpEntity<SpeechletRequestEnvelope<IntentRequest>>(requestEnv, headers);
		SpeechletResponseEnvelope responseEnv = testRest.postForObject("/_requestDispatcher", requestEntity, SpeechletResponseEnvelope.class);
		SimpleCard card = (SimpleCard) responseEnv.getResponse().getCard();
		assertEquals("Hokey Pokey", card.getTitle());
		assertEquals(HOKEY_POKEY_MESSAGE, card.getContent());
		PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
		assertEquals(HOKEY_POKEY_MESSAGE, outputSpeech.getText());
	}


	@Test
	public void shouldHandleHelpIntentThroughRequestDispatcher() throws Exception {
		SpeechletRequestEnvelope<IntentRequest> requestEnv = buildHelpIntentRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SpeechletRequestEnvelope<IntentRequest>> requestEntity = new HttpEntity<SpeechletRequestEnvelope<IntentRequest>>(requestEnv, headers);
		SpeechletResponseEnvelope responseEnv = testRest.postForObject("/_requestDispatcher", requestEntity, SpeechletResponseEnvelope.class);
		SimpleCard card = (SimpleCard) responseEnv.getResponse().getCard();
		assertEquals("Hello Help", card.getTitle());
		assertEquals("Just ask me to say hello", card.getContent());
		PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
		assertEquals("Just ask me to say hello", outputSpeech.getText());
	}
	
	@Test
	public void shouldHandleLaunchRequestThroughRequestDispatcher() throws Exception {
		SpeechletRequestEnvelope<SpeechletRequest> requestEnv = buildLaunchRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SpeechletRequestEnvelope<SpeechletRequest>> requestEntity = new HttpEntity<SpeechletRequestEnvelope<SpeechletRequest>>(requestEnv, headers);
		SpeechletResponseEnvelope responseEnv = testRest.postForObject("/_requestDispatcher", requestEntity, SpeechletResponseEnvelope.class);
		SimpleCard card = (SimpleCard) responseEnv.getResponse().getCard();
		assertEquals("Welcome to DevNexus", card.getTitle());
		assertEquals(HELLO_MESSAGE, card.getContent());
		PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
		assertEquals(HELLO_MESSAGE, outputSpeech.getText());
	}

	private SpeechletRequestEnvelope<IntentRequest> buildHelloIntentRequest() {
		Intent intent = Intent.builder()
			.withName("SayHello")
			.build();
		
		IntentRequest intentRequest = IntentRequest.builder()
			.withIntent(intent)
			.withRequestId("hello_request_1")
			.withTimestamp(new Date())
			.build();
		
		Session session = Session.builder()
			.withSessionId("hello_session_1")
			.withUser(User.builder().withUserId("habuma").build())
			.build();
		
		return SpeechletRequestEnvelope.<IntentRequest>builder()
			.withRequest(intentRequest)
			.withSession(session)
			.withVersion("1.0")
			.build();
	}
	
	private SpeechletRequestEnvelope<IntentRequest> buildHokeyPokeyIntentRequest() {
		Intent intent = Intent.builder()
			.withName("HokeyPokey")
			.withSlot(Slot.builder().withName("Part").withValue("elbow").build())
			.build();
		
		IntentRequest intentRequest = IntentRequest.builder()
			.withIntent(intent)
			.withRequestId("hokey_pokey_request")
			.withTimestamp(new Date())
			.build();
		
		Session session = Session.builder()
			.withSessionId("hokey_pokey_session_1")
			.withUser(User.builder().withUserId("habuma").build())
			.build();
		
		return SpeechletRequestEnvelope.<IntentRequest>builder()
			.withRequest(intentRequest)
			.withSession(session)
			.withVersion("1.0")
			.build();
	}
	
	private SpeechletRequestEnvelope<IntentRequest> buildHelpIntentRequest() {
		Intent intent = Intent.builder()
			.withName("AMAZON.HelpIntent")
			.build();
		
		IntentRequest intentRequest = IntentRequest.builder()
			.withIntent(intent)
			.withRequestId("help_request_1")
			.withTimestamp(new Date())
			.build();
		
		Session session = Session.builder()
			.withSessionId("help_session_1")
			.withUser(User.builder().withUserId("habuma").build())
			.build();
		
		return SpeechletRequestEnvelope.<IntentRequest>builder()
			.withRequest(intentRequest)
			.withSession(session)
			.withVersion("1.0")
			.build();
	}
	
	private SpeechletRequestEnvelope<SpeechletRequest> buildLaunchRequest() {
		LaunchRequest launchRequest = LaunchRequest.builder()
			.withRequestId("launch_1")
			.build();
				
		Session session = Session.builder()
			.withSessionId("launch_session_1")
			.withUser(User.builder().withUserId("habuma").build())
			.build();
		
		return SpeechletRequestEnvelope.builder()
			.withRequest(launchRequest)
			.withSession(session)
			.withVersion("1.0")
			.build();
	}
}
