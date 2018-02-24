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

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionEndedRequest.Reason;
import com.amazon.speech.speechlet.SpeechletRequest;

import spring.skills.handler.LaunchRequestHandler;
import spring.skills.handler.SessionEndedRequestHandler;
import spring.skills.handler.UnknownRequestHandler;

public class SpeechletRequestDispatcher {
	
	private static Logger logger = Logger.getLogger(SpeechletRequestDispatcher.class.getName());
	
	private IntentDispatcher intentDispatcher;

	private LaunchRequestHandler launchRequestHandler;

	private SessionEndedRequestHandler sessionEndedRequestHandler;

	private UnknownRequestHandler unknownRequestHandler;
	
	@Autowired
	public void setIntentDispatcher(IntentDispatcher intentDispatcher) {
		this.intentDispatcher = intentDispatcher;
	}
	
	@Autowired
	public void setLaunchRequestHandler(LaunchRequestHandler launchRequestHandler) {
		this.launchRequestHandler = launchRequestHandler;
	}
	
	@Autowired
	public void setSessionEndedRequestHandler(SessionEndedRequestHandler sessionEndedRequestHandler) {
		this.sessionEndedRequestHandler = sessionEndedRequestHandler;
	}
	
	@Autowired
	public void setUnknownRequestHandler(UnknownRequestHandler unknownRequestHandler) {
		this.unknownRequestHandler = unknownRequestHandler;
	}
	
	public SpeechletResponseEnvelope handle(SpeechletRequestEnvelope<SpeechletRequest> requestEnvelope) {
		String requestId = requestEnvelope.getRequest().getRequestId();
		String sessionId = requestEnvelope.getSession().getSessionId();
		String userId = requestEnvelope.getSession().getUser().getUserId();
		logger.info("Handling speechlet request ID=" + requestId);
		logger.info("                   session ID=" + sessionId);
		logger.info("                      user ID=" + userId);
		
		SpeechletRequest request = requestEnvelope.getRequest();
		if (request instanceof IntentRequest) {
			IntentRequest intentRequest = (IntentRequest) request;
			logger.info("Handling intent request. ID=" + intentRequest.getRequestId());
			return intentDispatcher.handleIntentRequest(requestEnvelope);
		} else if (request instanceof LaunchRequest) {
			LaunchRequest launchRequest = (LaunchRequest) request;
			logger.info("Handling launch request. ID=" + launchRequest.getRequestId());
			return launchRequestHandler.handleLaunchRequest(requestEnvelope);
		} else if (request instanceof SessionEndedRequest) {
			SessionEndedRequest sessionEndRequest = (SessionEndedRequest) request;
			Reason reason = sessionEndRequest.getReason();
			logger.info("Handling session end request. ID=" + sessionEndRequest.getRequestId()
					+ ", REASON: " + reason);
			return sessionEndedRequestHandler.handleSessionEndedRequest(requestEnvelope);
		}
		
		logger.warning("Unknown request type: " + request.getClass().getName());
		return unknownRequestHandler.handleUnknownRequest(requestEnvelope);
	}
}
