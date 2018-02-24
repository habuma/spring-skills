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
package spring.skills.handler;

import java.util.HashMap;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;

public class DefaultUnknownIntentHandler implements UnknownIntentHandler {

	@Override
	public SpeechletResponseEnvelope apply(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText("There was an error: Unknown intent.");
		SimpleCard card = new SimpleCard();
		card.setTitle("Spring Skills Unknown Intent");
		card.setContent("There was an error: Unknown intent.");
		SpeechletResponse response = SpeechletResponse.newTellResponse(outputSpeech, card);
		SpeechletResponseEnvelope envelope = new SpeechletResponseEnvelope();
		envelope.setResponse(response);
		envelope.setSessionAttributes(new HashMap<>());
		envelope.setVersion("1.0");
		return envelope;
	}
	
}
