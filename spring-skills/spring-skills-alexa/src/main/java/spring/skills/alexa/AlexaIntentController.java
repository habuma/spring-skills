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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;

import spring.skills.core.SpeechRequest;
import spring.skills.core.SpeechRequestDispatcher;
import spring.skills.core.SpeechRequestHandler;
import spring.skills.core.SpeechResponse;

/**
 * Spring MVC REST Controller that handles Alexa {@link RequestEnvelope}, converts the request
 * to a Spring Skills {@link SpeechRequest}, and delegates to a {@link SpeechRequestHandler}. Converts the
 * {@link SpeechResponse} from the {@link SpeechRequestHandler} to an Alexa {@link ResponseEnvelope}
 * that it returns.
 *
 * By default, handles requests for "/alexa", but path can be configured by setting
 * `spring.skills.alexa.path`.
 *
 * @author Craig Walls
 */
@RestController
@RequestMapping(path="${spring.skills.alexa.path:alexa}")
public class AlexaIntentController {

	private AlexaSpeechRequestConverter converter;
	private SpeechRequestDispatcher dispatcher;

	public AlexaIntentController(SpeechRequestDispatcher dispatcher) {
		this.converter = new AlexaSpeechRequestConverter();
		this.dispatcher = dispatcher;
	}

	@PostMapping
	public ResponseEnvelope handleSpeechletRequest(RequestEnvelope requestEnv) {
		SpeechRequest speechRequest = converter.toSpeechRequest(requestEnv);
		SpeechResponse speechResponse = dispatcher.dispatchRequest(speechRequest);
		return converter.toPlatformResponse(speechResponse);
	}

}
