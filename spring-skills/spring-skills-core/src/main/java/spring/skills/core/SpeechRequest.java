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
import java.util.Locale;

/**
 * A platform-agnostic speech request. 
 * 
 * @author Craig Walls
 */
public class SpeechRequest {

	private final String requestId;
	
	private final OffsetDateTime timestamp;
	
	private final Locale locale;

	public SpeechRequest(String requestId, OffsetDateTime timestamp, Locale locale) {
		this.requestId = requestId;
		this.timestamp = timestamp;
		this.locale = locale;
	}
	
	public String getRequestId() {
		return requestId;
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public Locale getLocale() {
		return locale;
	}
	
	
}
