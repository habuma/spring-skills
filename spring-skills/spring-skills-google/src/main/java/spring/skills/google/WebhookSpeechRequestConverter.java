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
package spring.skills.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import spring.skills.core.IntentSpeechRequest;
import spring.skills.core.SpeechRequest;
import spring.skills.core.SpeechRequest.Source;
import spring.skills.core.SpeechRequestConverter;
import spring.skills.core.SpeechResponse;
import spring.skills.google.dialogflow.BasicCard;
import spring.skills.google.dialogflow.GooglePayload;
import spring.skills.google.dialogflow.Item;
import spring.skills.google.dialogflow.Payload;
import spring.skills.google.dialogflow.RichResponse;
import spring.skills.google.dialogflow.SimpleResponse;
import spring.skills.google.dialogflow.WebhookRequest;
import spring.skills.google.dialogflow.WebhookResponse;

/**
 * An Alexa-specific implementation of {@link SpeechRequestConverter} that converts {@link RequestEnvelope}
 * to {@link SpeechRequest} and {@link SpeechResponse} to {@link ResponseEnvelope}.
 * 
 * @author Craig Walls
 */
public class WebhookSpeechRequestConverter 
		implements SpeechRequestConverter<WebhookRequest, WebhookResponse> {

	@Override
	public SpeechRequest toSpeechRequest(WebhookRequest webhookRequest) {
		String intentDisplayName = webhookRequest.getQueryResult().getIntent().getDisplayName();
		return new IntentSpeechRequest(Source.GOOGLE, 
								intentDisplayName, 
								webhookRequest.getResponseId(), 
								null, null); // TODO: Figure out how to deal with no locale or timestamp
	}
	
	@Override
	public WebhookResponse toPlatformResponse(SpeechResponse speechResponse) {
		WebhookResponse response = new WebhookResponse();
		Map<String, Payload> payload = new HashMap<>();
		GooglePayload googlePayload = new GooglePayload();
		googlePayload.setExpectUserResponse(true);
		RichResponse richResponse = new RichResponse();
		List<Item> items = new ArrayList<>();
		Item item = new Item();
		SimpleResponse simpleResponse = new SimpleResponse();
		simpleResponse.setSsml(speechResponse.getSpeech().getSsml());
		item.setSimpleResponse(simpleResponse);
		items.add(item);

		Item cardItem = new Item();
		BasicCard card = convertCard(speechResponse);
		cardItem.setBasicCard(card);
		items.add(cardItem);
		richResponse.setItems(items);
		googlePayload.setRichResponse(richResponse);
		payload.put("google", googlePayload);
		response.setPayload(payload);
		return response;
	}

	private BasicCard convertCard(SpeechResponse speechResponse) {
		BasicCard card = new BasicCard();
		card.setTitle(speechResponse.getCard().getTitle());
		card.setFormattedText(speechResponse.getCard().getText());
		return card;
	}

}
