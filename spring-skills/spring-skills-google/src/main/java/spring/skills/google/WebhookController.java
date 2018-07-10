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

import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.skills.core.SpeechRequest;
import spring.skills.core.SpeechRequestDispatcher;
import spring.skills.core.SpeechRequestHandler;
import spring.skills.core.SpeechResponse;
import spring.skills.google.dialogflow.WebhookRequest;
import spring.skills.google.dialogflow.WebhookResponse;

/**
 * Spring MVC REST Controller that handles Google Action Requests, translating them into
 * platform-neutral {@link SpeechRequest} for implementations of {@link SpeechRequestHandler}
 * to handle.
 *
 * By default, handles requests for "/google", but path can be configured by setting
 * `spring.skills.google.path`.
 *
 * @author Craig Walls
 */
@RestController
@RequestMapping(path="${spring.skills.google.path:/google}")
public class WebhookController {

	private static Logger logger = Logger.getLogger(WebhookController.class.getName());

	private SpeechRequestDispatcher dispatcher;
	
	private WebhookSpeechRequestConverter converter;

	public WebhookController(SpeechRequestDispatcher dispatcher) {
		this.converter = new WebhookSpeechRequestConverter();
		this.dispatcher = dispatcher;
	}

	@PostMapping
	public ResponseEntity<WebhookResponse> handleWebhookRequest(@RequestBody WebhookRequest webhookRequest) {
		logger.info("Handling webhook request. Session ID: " + webhookRequest.getSession());
		SpeechRequest speechRequest = converter.toSpeechRequest(webhookRequest);
		SpeechResponse speechResponse = dispatcher.dispatchRequest(speechRequest);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return new ResponseEntity<WebhookResponse>(
				converter.toPlatformResponse(speechResponse),
				headers, HttpStatus.OK);
	}

}
