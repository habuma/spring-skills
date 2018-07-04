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
 * Platform-neutral domain type representing a request that indicates the end of
 * a session.
 * 
 * @author Craig Walls
 */
public class SessionEndedSpeechRequest extends SpeechRequest {

	private final String reason;

	private final String errorType;

	private final String errorMessage;

	public SessionEndedSpeechRequest(String reason, String errorType, String errorMessage, String requestId,
			OffsetDateTime timestamp, Locale locale) {
		super(requestId, timestamp, locale);
		this.reason = reason;
		this.errorType = errorType;
		this.errorMessage = errorMessage;
	}

	public String getReason() {
		return reason;
	}

	public String getErrorType() {
		return errorType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
