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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=Include.NON_NULL)
public class Intent {

	private String name;
	
	private String displayName;
	
	private WebhookState webhookState;
	
	private Number priority;
	
	private boolean isFallback;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public WebhookState getWebhookState() {
		return webhookState;
	}

	public void setWebhookState(WebhookState webhookState) {
		this.webhookState = webhookState;
	}

	public Number getPriority() {
		return priority;
	}

	public void setPriority(Number priority) {
		this.priority = priority;
	}

	public boolean isFallback() {
		return isFallback;
	}

	public void setFallback(boolean isFallback) {
		this.isFallback = isFallback;
	}

	private static enum WebhookState {
		WEBHOOK_STATE_UNSPECIFIED, WEBHOOK_STATE_ENABLED, WEBHOOK_STATE_ENABLED_FOR_SLOT_FILLING
	}
}
