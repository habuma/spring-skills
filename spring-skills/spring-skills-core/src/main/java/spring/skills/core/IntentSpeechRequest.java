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
package spring.skills.core;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Locale;

public class IntentSpeechRequest extends SpeechRequest {

	private final String intentName;
	
	private HashMap<String, Parameter> parameters = new HashMap<>();

	public IntentSpeechRequest(Source source, String intentName, String requestId, OffsetDateTime timestamp, Locale locale) {
		super(source, requestId, timestamp, locale);
		this.intentName = intentName;
	}
	
	public String getIntentName() {
		return intentName;
	}
		
	public HashMap<String, Parameter> getParameters() {
		return parameters;
	}
	
}
