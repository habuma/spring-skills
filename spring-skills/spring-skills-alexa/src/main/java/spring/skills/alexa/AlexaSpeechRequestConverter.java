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

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.SessionEndedError;
import com.amazon.ask.model.SessionEndedReason;
import com.amazon.ask.model.SessionEndedRequest;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.ui.Image;
import com.amazon.ask.model.ui.OutputSpeech;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.amazon.ask.model.ui.StandardCard;
import com.amazon.ask.model.ui.StandardCard.Builder;

import spring.skills.core.BuiltInIntents;
import spring.skills.core.IntentSpeechRequest;
import spring.skills.core.Parameter;
import spring.skills.core.SessionEndedSpeechRequest;
import spring.skills.core.SpeechCard;
import spring.skills.core.SpeechRequest;
import spring.skills.core.SpeechRequestConverter;
import spring.skills.core.SpeechResponse;

/**
 * An Alexa-specific implementation of {@link SpeechRequestConverter} that converts {@link RequestEnvelope}
 * to {@link SpeechRequest} and {@link SpeechResponse} to {@link ResponseEnvelope}.
 * 
 * @author Craig Walls
 */
public class AlexaSpeechRequestConverter 
		implements SpeechRequestConverter<RequestEnvelope, ResponseEnvelope> {
	
	private static Logger logger = Logger.getLogger(AlexaSpeechRequestConverter.class.getName());

	@Override
	public SpeechRequest toSpeechRequest(RequestEnvelope alexaRequest) {
		
		Request request = alexaRequest.getRequest();
		String requestId = request.getRequestId();
		OffsetDateTime timestamp = request.getTimestamp();
		Locale locale = new Locale(request.getLocale());
		
		if ("IntentRequest".equals(request.getType())) {
			IntentRequest intentRequest = (IntentRequest) request;
			
			// TODO: Decide how to handle dialog state
			// DialogState dialogState = intentRequest.getDialogState();
			
			Intent intent = intentRequest.getIntent();
			String intentName = neutralizeBuiltInIntentNames(intent.getName());
			IntentSpeechRequest intentSpeechRequest = new IntentSpeechRequest(intentName, requestId, timestamp, locale);
			
			// TODO: Decide how to handle confirmation status
			// IntentConfirmationStatus confirmationStatus = intent.getConfirmationStatus();
			
			Map<String, Slot> slots = intent.getSlots();
			if (slots != null) {
				for (Slot slot : slots.values()) {
					// TODO: Decide how to handle confirmation status and resolutions
					// SlotConfirmationStatus confirmationStatus = slot.getConfirmationStatus();
					// Resolutions resolutions = slot.getResolutions();
					
					Parameter parameter = new Parameter(slot.getName(), slot.getValue());
					intentSpeechRequest.getParameters().put(slot.getName(), parameter);
				}
			}
			
			return intentSpeechRequest;
		// TODO: Handle other types of requests, such as session-ended requests
		// } else if (...) {
		} else if ("SessionEndedRequest".equals(request.getType())) {
			SessionEndedRequest sessionEndedRequest = (SessionEndedRequest) request;
			SessionEndedReason reason = sessionEndedRequest.getReason();
			SessionEndedError error = sessionEndedRequest.getError();
			String errorType = error != null ? error.getType().toString() : null;
			String errorMessage = error != null ? error.getMessage() : null;
			logger.info("Session ended request: " + reason);
			
			// TODO: Consider creating a platform-neutral reason and error type enums to avoid
			//       leaking Alexa-specific enum values and the be able to reuse them for other
			//       speech platforms.
			return new SessionEndedSpeechRequest(reason.toString(), errorType, errorMessage, requestId, timestamp, locale);
		} else {
			logger.info("Unknown request type: " + request.getType());
			return new SpeechRequest(requestId, timestamp, locale);
		}
		
	}
	
	@Override
	public ResponseEnvelope toPlatformResponse(SpeechResponse speechResponse) {
		OutputSpeech outputSpeech = SsmlOutputSpeech.builder()
				.withSsml(speechResponse.getSpeech().getSsml())
				.build();
		
		StandardCard card = buildCard(speechResponse);
		
		// TODO: with...directives, reprompt, should end session?
		Response response = Response.builder()
				.withOutputSpeech(outputSpeech)
				.withCard(card)
				.withShouldEndSession(speechResponse.shouldEndSession())
				.build();
		
		Map<String, Object> sessionAttributes = new HashMap<>(); // TODO: For now...populate with real data later
		
		// TODO: with...session attribute and user agent?
		return ResponseEnvelope.builder()
				.withVersion(speechResponse.getVersion())
				.withResponse(response)
				.withSessionAttributes(sessionAttributes )
				.build();
	}

	private StandardCard buildCard(SpeechResponse speechResponse) {
		SpeechCard speechCard = speechResponse.getCard();
		Builder cardBuilder = StandardCard.builder()
				.withTitle(speechCard.getTitle())
				.withText(speechCard.getText());
		
		if (speechCard.getSmallImageUrl() != null || speechCard.getLargeImageUrl() != null) {
			cardBuilder.withImage(buildImage(speechCard, cardBuilder));
		}
		
		StandardCard card = cardBuilder.build();
		return card;
	}

	private Image buildImage(SpeechCard speechCard, Builder cardBuilder) {
		com.amazon.ask.model.ui.Image.Builder imageBuilder = Image.builder();
		if (speechCard.getLargeImageUrl() != null) {
			imageBuilder.withLargeImageUrl(speechCard.getLargeImageUrl());
		}
		if (speechCard.getSmallImageUrl() != null) {
			imageBuilder.withSmallImageUrl(speechCard.getSmallImageUrl());
		}
		return imageBuilder.build();
	}
	
	private String neutralizeBuiltInIntentNames(String intentName) {
		if (intentName.equals(AlexaBuiltInIntents.HELP_INTENT)) {
			intentName = BuiltInIntents.HELP_INTENT;
		} else if (intentName.equals(AlexaBuiltInIntents.STOP_INTENT)) {
			intentName = BuiltInIntents.STOP_INTENT;
		} else if (intentName.equals(AlexaBuiltInIntents.CANCEL_INTENT)) {
			intentName = BuiltInIntents.CANCEL_INTENT;
		}
		return intentName;
	}
	
}
