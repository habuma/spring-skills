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
package spring.skills.google.dialogflow;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=Include.NON_NULL)
public class WebhookResponse {
	
	private String fulfillmentText;
	
	private List<Message> fulfillmentMessages;
	
	private String source;
	
	private Map<String, Payload> payload;
	
	private List<Context> outputContexts;
	
	private EventInput followupEventInput;

	public String getFulfillmentText() {
		return fulfillmentText;
	}

	public void setFulfillmentText(String fulfillmentText) {
		this.fulfillmentText = fulfillmentText;
	}

	public List<Message> getFulfillmentMessages() {
		return fulfillmentMessages;
	}

	public void setFulfillmentMessages(List<Message> fulfillmentMessages) {
		this.fulfillmentMessages = fulfillmentMessages;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Map<String, Payload> getPayload() {
		return payload;
	}

	public void setPayload(Map<String, Payload> payload) {
		this.payload = payload;
	}

	public List<Context> getOutputContexts() {
		return outputContexts;
	}

	public void setOutputContexts(List<Context> outputContexts) {
		this.outputContexts = outputContexts;
	}

	public EventInput getFollowupEventInput() {
		return followupEventInput;
	}

	public void setFollowupEventInput(EventInput followupEventInput) {
		this.followupEventInput = followupEventInput;
	}
	
}
