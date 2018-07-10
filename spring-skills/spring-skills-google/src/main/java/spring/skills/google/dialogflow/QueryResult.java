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
public class QueryResult {

	private String queryText;
	
	private String languageCode;
	
	private Float speechRecognitionConfidence;
	
	private String action;
	
	private Map<String, String> parameters; // TODO: Is this right?
	
	private boolean allRequiredParamsPresent = true;
	
	private String fulfillmentText;
	
	private List<Message> fullfillmentMessages;
	
	private String webhookSource;
	
	/* private Object webhookPayload; // TODO: In docs, but not in test sample */
	
	private List<Context> webhookContexts;
	
	private Intent intent;
	
	private Float intentDetectionConfidence;

	/* private Object diagnosticInfo; // TODO: In docs, but not in test sample */
	
	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public Float getSpeechRecognitionConfidence() {
		return speechRecognitionConfidence;
	}

	public void setSpeechRecognitionConfidence(Float speechRecognitionConfidence) {
		this.speechRecognitionConfidence = speechRecognitionConfidence;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public boolean isAllRequiredParamsPresent() {
		return allRequiredParamsPresent;
	}

	public void setAllRequiredParamsPresent(boolean allRequiredParamsPresent) {
		this.allRequiredParamsPresent = allRequiredParamsPresent;
	}

	public String getFulfillmentText() {
		return fulfillmentText;
	}

	public void setFulfillmentText(String fulfillmentText) {
		this.fulfillmentText = fulfillmentText;
	}

	public List<Message> getFullfillmentMessages() {
		return fullfillmentMessages;
	}

	public void setFullfillmentMessages(List<Message> fullfillmentMessages) {
		this.fullfillmentMessages = fullfillmentMessages;
	}

	public String getWebhookSource() {
		return webhookSource;
	}

	public void setWebhookSource(String webhookSource) {
		this.webhookSource = webhookSource;
	}

	public List<Context> getWebhookContexts() {
		return webhookContexts;
	}

	public void setWebhookContexts(List<Context> webhookContexts) {
		this.webhookContexts = webhookContexts;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}

	public Float getIntentDetectionConfidence() {
		return intentDetectionConfidence;
	}

	public void setIntentDetectionConfidence(Float intentDetectionConfidence) {
		this.intentDetectionConfidence = intentDetectionConfidence;
	}
	
}
