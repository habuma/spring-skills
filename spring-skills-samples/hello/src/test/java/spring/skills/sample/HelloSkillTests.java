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
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.User;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT, classes=HelloSkill.class)
public class HelloSkillTests {

	@Autowired
	TestRestTemplate testRest;
	
	@Test
	public void shouldSayHelloThroughRequestDispatcher() throws Exception {
		SpeechletRequestEnvelope<SpeechletRequest> requestEnv = buildHelloIntentRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SpeechletRequestEnvelope<SpeechletRequest>> requestEntity = new HttpEntity<SpeechletRequestEnvelope<SpeechletRequest>>(requestEnv, headers);
		SpeechletResponseEnvelope responseEnv = testRest.postForObject("/_requestDispatcher", requestEntity, SpeechletResponseEnvelope.class);
		SimpleCard card = (SimpleCard) responseEnv.getResponse().getCard();
		assertEquals("Hello", card.getTitle());
		assertEquals("Hello Spring World!", card.getContent());
		PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
		assertEquals("Hello Spring World!", outputSpeech.getText());
	}

	@Test
	public void shouldHandleHelpIntentThroughRequestDispatcher() throws Exception {
		SpeechletRequestEnvelope<SpeechletRequest> requestEnv = buildHelpIntentRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SpeechletRequestEnvelope<SpeechletRequest>> requestEntity = new HttpEntity<SpeechletRequestEnvelope<SpeechletRequest>>(requestEnv, headers);
		SpeechletResponseEnvelope responseEnv = testRest.postForObject("/_requestDispatcher", requestEntity, SpeechletResponseEnvelope.class);
		SimpleCard card = (SimpleCard) responseEnv.getResponse().getCard();
		assertEquals("Hello Help", card.getTitle());
		assertEquals("Just ask me to say hello", card.getContent());
		PlainTextOutputSpeech outputSpeech = (PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
		assertEquals("Just ask me to say hello", outputSpeech.getText());
	}

	private SpeechletRequestEnvelope<SpeechletRequest> buildHelloIntentRequest() {
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
		
		return SpeechletRequestEnvelope.builder()
			.withRequest(intentRequest)
			.withSession(session)
			.withVersion("1.0")
			.build();
	}
	
	private SpeechletRequestEnvelope<SpeechletRequest> buildHelpIntentRequest() {
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
		
		return SpeechletRequestEnvelope.builder()
			.withRequest(intentRequest)
			.withSession(session)
			.withVersion("1.0")
			.build();
	}
}
