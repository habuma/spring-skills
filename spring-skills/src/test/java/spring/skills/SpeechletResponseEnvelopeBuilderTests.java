package spring.skills;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

public class SpeechletResponseEnvelopeBuilderTests {

	@Test
	public void shouldFailIfPlainTextIsNull() {
		try {
			SpeechletResponseEnvelopeBuilder.withPlainText(null).build();
			fail();
		} catch (IllegalStateException e) {
			assertEquals("The factory cannot be created if plain text or SSML is null.", e.getMessage());
		}
	}
	
	@Test
	public void shouldFailIfSSMLIsNull() {
		try {
			SpeechletResponseEnvelopeBuilder.withSSML(null).build();
			fail();
		} catch (IllegalStateException e) {
			assertEquals("The factory cannot be created if plain text or SSML is null.", e.getMessage());
		}
	}
	
	@Test
	public void shouldFailCardContentIsNullForSSMLBuilder() {
		try {
			SpeechletResponseEnvelopeBuilder.withSSML("<speak>Hello</speak>").build();
			fail();
		} catch (IllegalStateException e) {
			assertEquals("For SSML responses, the card content must be explicitly set.", e.getMessage());
		}
	}
	
	@Test
	public void shouldCreateSimplePlainTextResponseEnvelope() {
		SpeechletResponseEnvelope envelope = SpeechletResponseEnvelopeBuilder.withPlainText("Hello world").build();
		assertEquals("Hello world", ((PlainTextOutputSpeech) envelope.getResponse().getOutputSpeech()).getText());
		assertEquals("Hello world", ((SimpleCard) envelope.getResponse().getCard()).getContent());
		assertEquals("1.0", envelope.getVersion());
	}

	@Test
	public void shouldCreatePlainTextResponseEnvelopeWithCustomCardContent() {
		SpeechletResponseEnvelope envelope = 
				SpeechletResponseEnvelopeBuilder
						.withPlainText("Hello world")
						.cardContent("Greetings all")
						.build();
		assertEquals("Hello world", ((PlainTextOutputSpeech) envelope.getResponse().getOutputSpeech()).getText());
		assertEquals("Greetings all", ((SimpleCard) envelope.getResponse().getCard()).getContent());
		assertEquals("1.0", envelope.getVersion());
	}

}
