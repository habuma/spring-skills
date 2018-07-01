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

import java.util.HashMap;
import java.util.Map;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

/**
 * Convenience factory for creating a SpeechletResponseEnvelope.
 *
 * @author Craig Walls
 */
public class SpeechletResponseEnvelopeBuilder {

	private String plainText;
	private String ssml;
	private String version = "1.0";
	private Map<String, Object> sessionAttributes = new HashMap<>();
	private String cardTitle;
	private String cardContent;

	private SpeechletResponseEnvelopeBuilder() {}
	
	/**
	 * Start with a factory that works with SSML content.
	 * @param ssml the SSML content to present in the Speechlet response.
	 * @return The SpeechletResponseEnvelopeBuilder.
	 */
	public static SpeechletResponseEnvelopeBuilder withSSML(String ssml) {
		SpeechletResponseEnvelopeBuilder factory = new SpeechletResponseEnvelopeBuilder();
		factory.ssml = ssml;
		return factory;
	}
	
	/**
	 * Start with a factory that works with plain text content.
	 * 
	 * @param plainText the plain text content to present in the Speechlet response.
	 * @return The SpeechletResponseEnvelopeBuilder.
	 */
	public static SpeechletResponseEnvelopeBuilder withPlainText(String plainText) {
		SpeechletResponseEnvelopeBuilder factory = new SpeechletResponseEnvelopeBuilder();
		factory.plainText = plainText;
		return factory;
	}
	
	/**
	 * Sets the card's title.
	 * 
	 * @param cardTitle The card title.
	 * @return The SpeechletResponseEnvelopeBuilder.
	 */
	public SpeechletResponseEnvelopeBuilder cardTitle(String cardTitle) {
		this.cardTitle = cardTitle;
		return this;
	}
	
	/**
	 * Sets the card's content.
	 * 
	 * This is optional and defaults to the value of plainText, if using a plain-text factory.
	 * 
	 * @param cardContent The content to display on the Speechlet response's card.
	 * @return The SpeechletResponseEnvelopeBuilder.
	 */
	public SpeechletResponseEnvelopeBuilder cardContent(String cardContent) {
		this.cardContent = cardContent;
		return this;
	}
	
	/**
	 * Sets the response's version. Defaults to "1.0".
	 * 
	 * @param version the response version
	 * @return The SpeechletResponseEnvelopeBuilder.
	 */
	public SpeechletResponseEnvelopeBuilder version(String version) {
		this.version = version;
		return this;
	}
	
	/**
	 * Adds a session attribute.
	 * 
	 * @param key The session attribute key
	 * @param value The session attribute value
	 * @return The SpeechletResponseEnvelopeBuilder.
	 */
	public SpeechletResponseEnvelopeBuilder addSessionAttribute(String key, String value) {
		sessionAttributes.put(key, value);
		return this;
	}
	
	/**
	 * Builds a {@link SpeechletResponseEnvelope}
	 * @return the SpeechletResponseEnvelope
	 */
	public SpeechletResponseEnvelope build() {
		if (plainText == null && ssml == null) {
			throw new IllegalStateException("The factory cannot be created if plain text or SSML is null.");
		}
		
		SpeechletResponseEnvelope envelope = new SpeechletResponseEnvelope();
		envelope.setVersion(version);
		envelope.setSessionAttributes(sessionAttributes);
		SpeechletResponse response = new SpeechletResponse();
		
		if (ssml != null) {
			SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
			outputSpeech.setSsml(ssml);
			response.setOutputSpeech(outputSpeech);
		} else if (plainText != null) {
			PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
			outputSpeech.setText(plainText);
			response.setOutputSpeech(outputSpeech);
		}
		
		// TODO: Figure out how to work in LinkedAccountCard
		SimpleCard card = new SimpleCard();
		card.setTitle(cardTitle);
		if (cardContent != null) {
			card.setContent(cardContent);
		} else if (plainText != null) {
			card.setContent(plainText);
		} else {
			throw new IllegalStateException("For SSML responses, the card content must be explicitly set.");
		}
		response.setCard(card);
		
		envelope.setResponse(response);
		return envelope;
	}
	
}
