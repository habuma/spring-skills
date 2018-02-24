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

import java.util.Date;
import java.util.Locale;

import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionEndedRequest.Reason;
import com.amazon.speech.speechlet.SpeechletRequest;
import com.amazon.speech.speechlet.User;

public class SpringSkillsTestHelpers {
	
	public static SpeechletRequestEnvelope<SpeechletRequest> getLaunchRequest() {
		LaunchRequest request = LaunchRequest.builder()
			.withRequestId("request_1")
			.withTimestamp(new Date())
			.build();
		return buildTestRequestEnvelope(request, buildTestSession());
	}

	public static SpeechletRequestEnvelope<SpeechletRequest> getSessionEndedRequest() {
		SessionEndedRequest request = SessionEndedRequest.builder()
			.withRequestId("request_1")
			.withTimestamp(new Date())
			.withReason(Reason.USER_INITIATED)
			.build();
		return buildTestRequestEnvelope(request, buildTestSession());
	}
	
	public static SpeechletRequestEnvelope<SpeechletRequest> getIntentRequest(Intent intent) {
		IntentRequest request = IntentRequest.builder()
			.withRequestId("request_1")
			.withTimestamp(new Date())
			.withIntent(intent)
			.build();
		
		return buildTestRequestEnvelope(request, buildTestSession());
	}

	public static SpeechletRequestEnvelope<SpeechletRequest> getBogusRequest() {
		SpeechletRequest request = new BogusRequest();
		return buildTestRequestEnvelope(request, buildTestSession());
	}
	
	public static Session buildTestSession() {
		Session session = 
				Session.builder()
					.withSessionId("session_1")
					.withIsNew(true)
					.withUser(User.builder().withUserId("user_1").build())
					.build();
		return session;
	}
	
	public static SpeechletRequestEnvelope<IntentRequest> buildTestIntentRequestEnvelope(IntentRequest request, Session session) {
		return SpeechletRequestEnvelope.<IntentRequest>builder()
					.withRequest(request)
					.withSession(session)
					.build();
	}

	public static SpeechletRequestEnvelope<SpeechletRequest> buildTestRequestEnvelope(SpeechletRequest request, Session session) {
		return SpeechletRequestEnvelope.builder()
					.withRequest(request)
					.withSession(session)
					.build();
	}

	public static class BogusRequest extends SpeechletRequest {
		public BogusRequest() {
			super("request_X", new Date(), Locale.US);
		}
	}
}