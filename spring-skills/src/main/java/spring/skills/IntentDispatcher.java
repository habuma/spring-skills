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

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.SpeechletRequest;

import spring.skills.handler.UnknownIntentHandler;

public class IntentDispatcher implements ApplicationContextAware {
	
	private static Logger logger = Logger.getLogger(IntentDispatcher.class.getName());
	
	private ApplicationContext spring;

	private SpringSkillsProperties props;

	private UnknownIntentHandler unknownIntentHandler;
	
	@Autowired
	public void setUnknownIntentHandler(UnknownIntentHandler unknownIntentHandler) {
		this.unknownIntentHandler = unknownIntentHandler;
	}
	
	@Autowired
	public void setSpringSkillsProperties(SpringSkillsProperties props) {
		this.props = props;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext spring) throws BeansException {
		this.spring = spring;
	}

	public SpeechletResponseEnvelope handleIntentRequest(SpeechletRequestEnvelope<SpeechletRequest> requestEnvelope) {
		String requestId = requestEnvelope.getRequest().getRequestId();
		String sessionId = requestEnvelope.getSession().getSessionId();
		String userId = requestEnvelope.getSession().getUser().getUserId();
		logger.info("Handling speechlet request ID=" + requestId);
		logger.info("                   session ID=" + sessionId);
		logger.info("                      user ID=" + userId);
		
		SpeechletRequest request = requestEnvelope.getRequest();
		String intentName = ((IntentRequest) request).getIntent().getName();
		String intentBeanName = getIntentBeanName(intentName);
		logger.info("Handling intent request for intent: " + intentName);
		try {
			@SuppressWarnings("unchecked")
			Function<SpeechletRequestEnvelope<SpeechletRequest>, SpeechletResponseEnvelope> intentFunction = spring.getBean(intentBeanName, Function.class);
			return intentFunction.apply(requestEnvelope);
		} catch (NoSuchBeanDefinitionException nsbe) {
			String errorMessage = "No bean " + intentBeanName + " found for intent " + intentName + ".";
			logger.log(Level.WARNING, errorMessage);
			return unknownIntentHandler.apply(requestEnvelope);
		}			
	}
	
	private String getIntentBeanName(String intentName) {
		if (intentName.equals("AMAZON.HelpIntent")) {
			return props.getHelpIntentBeanName();
		} else if (intentName.equals("AMAZON.StopIntent")) {
			return props.getStopIntentBeanName();
		} else if (intentName.equals("AMAZON.CancelIntent")) {
			return props.getCancelIntentBeanName();
		}
		return beanifyIntentName(intentName);
	}

	private String beanifyIntentName(String intentName) {
		if (intentName.endsWith("Intent")) {
			intentName = intentName.substring(0, intentName.length() - 6);
		}
		return Character.toLowerCase(intentName.charAt(0)) + intentName.substring(1);
	}

}
