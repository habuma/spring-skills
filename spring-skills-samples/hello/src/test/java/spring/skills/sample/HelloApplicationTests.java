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
import com.amazon.speech.speechlet.User;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT, classes=HelloApplication.class)
public class HelloApplicationTests {

	@Autowired
	TestRestTemplate testRest;
	
	@Test
	public void shouldSayHello() {
		SpeechletRequestEnvelope requestEnv = buildHelloIntentRequest();
		SpeechletResponseEnvelope responseEnv = testRest.postForObject("/sayHello", requestEnv, SpeechletResponseEnvelope.class);
		SimpleCard card = (SimpleCard) responseEnv.getResponse().getCard();
		assertEquals("Hello", card.getTitle());
		assertEquals("Hello Spring World!", card.getContent());
		SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) responseEnv.getResponse().getOutputSpeech();
		assertEquals("hello", outputSpeech.getId());
		assertEquals("<speak>Hello Spring World!</speak>", outputSpeech.getSsml());
	}
	
	@Test
	public void shouldSayHelloThroughRequestDispatcher() throws Exception {
		SpeechletRequestEnvelope requestEnv = buildHelloIntentRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SpeechletRequestEnvelope> requestEntity = new HttpEntity<SpeechletRequestEnvelope>(requestEnv, headers);
		SpeechletResponseEnvelope responseEnv = testRest.postForObject("/requestDispatcher", requestEntity, SpeechletResponseEnvelope.class);
		SimpleCard card = (SimpleCard) responseEnv.getResponse().getCard();
		assertEquals("Hello", card.getTitle());
		assertEquals("Hello Spring World!", card.getContent());
		SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) responseEnv.getResponse().getOutputSpeech();
		assertEquals("hello", outputSpeech.getId());
		assertEquals("<speak>Hello Spring World!</speak>", outputSpeech.getSsml());
	}

	@Test
	public void shouldHandleHelpIntentThroughRequestDispatcher() throws Exception {
		SpeechletRequestEnvelope requestEnv = buildHelpIntentRequest();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SpeechletRequestEnvelope> requestEntity = new HttpEntity<SpeechletRequestEnvelope>(requestEnv, headers);
		SpeechletResponseEnvelope responseEnv = testRest.postForObject("/requestDispatcher", requestEntity, SpeechletResponseEnvelope.class);
		SimpleCard card = (SimpleCard) responseEnv.getResponse().getCard();
		assertEquals("Hello Help", card.getTitle());
		assertEquals("Just ask me to say hello", card.getContent());
		SsmlOutputSpeech outputSpeech = (SsmlOutputSpeech) responseEnv.getResponse().getOutputSpeech();
		assertEquals("hello_help", outputSpeech.getId());
		assertEquals("<speak>Just ask me to say hello</speak>", outputSpeech.getSsml());
	}

	private SpeechletRequestEnvelope buildHelloIntentRequest() {
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
		
		SpeechletRequestEnvelope requestEnv = SpeechletRequestEnvelope.builder()
			.withRequest(intentRequest)
			.withSession(session)
			.withVersion("1.0")
			.build();
		return requestEnv;
	}
	
	private SpeechletRequestEnvelope buildHelpIntentRequest() {
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
		
		SpeechletRequestEnvelope requestEnv = SpeechletRequestEnvelope.builder()
			.withRequest(intentRequest)
			.withSession(session)
			.withVersion("1.0")
			.build();
		return requestEnv;
	}
}
