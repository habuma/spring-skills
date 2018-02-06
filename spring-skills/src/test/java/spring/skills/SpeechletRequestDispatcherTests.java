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

public class SpeechletRequestDispatcherTests {

	//
	// TODO: Context runner stuff not available until Boot 2.0. Meanwhile,
	//       Boot 2.0 makes stuff break WRT Spring Cloud Function, since 
	//       SCF is built with 1.5.9.RELEASE. So...temporarily commenting out
	//       tests. (They do pass if building against Boot 2.0.)
	//

	
//	private final ApplicationContextRunner contextRunner = 
//			new ApplicationContextRunner()
//				.withConfiguration(
//						AutoConfigurations.of(SpringSkillsAutoConfiguration.class));
//
//	@Test
//	public void shouldHandleLaunchRequestWithDefaultResponse() {
//		contextRunner.run(
//			context -> {
//				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
//				SpeechletResponseEnvelope responseEnv = dispatcher.apply(getLaunchRequest());
//				PlainTextOutputSpeech outputSpeech = 
//						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
//				assertEquals("Welcome", outputSpeech.getText());
//			}
//		);
//	}
//	
//	@Test
//	public void shouldHandleLaunchRequestWithOverridingRequestHandler() {
//		contextRunner
//		.withConfiguration(UserConfigurations.of(CustomSpringSkillConfiguration.class))
//		.run(
//			context -> {
//				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
//				SpeechletResponseEnvelope responseEnv = dispatcher.apply(getLaunchRequest());
//				PlainTextOutputSpeech outputSpeech = 
//						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
//				assertEquals("Buenos dias", outputSpeech.getText());
//			}
//		);
//	}
//	
//	@Test
//	public void shouldHandleSessionEndedRequestWithDefaultResponse() {
//		contextRunner.run(
//			context -> {
//				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
//				SpeechletResponseEnvelope responseEnv = dispatcher.apply(getSessionEndedRequest());
//				PlainTextOutputSpeech outputSpeech = 
//						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
//				assertEquals("Goodbye", outputSpeech.getText());
//			}
//		);
//	}
//
//	@Test
//	public void shouldHandleSessionEndedRequestWithOverridingRequestHandler() {
//		contextRunner.run(
//			context -> {
//				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
//				SpeechletResponseEnvelope responseEnv = dispatcher.apply(getSessionEndedRequest());
//				PlainTextOutputSpeech outputSpeech = 
//						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
//				assertEquals("Goodbye", outputSpeech.getText());
//			}
//		);
//	}
//
//	@Test
//	public void shouldHandleUnrecognizedRequestWithDefaultResponse() {
//		contextRunner
//		.run(
//			context -> {
//				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
//				SpeechletResponseEnvelope responseEnv = dispatcher.apply(getBogusRequest());
//				PlainTextOutputSpeech outputSpeech = 
//						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
//				assertEquals("There was an error: Unknown request type."
//						, outputSpeech.getText());			}
//		);
//	}
//	
//	@Test
//	public void shouldHandleUnrecognizedRequestWithOverridingRequestHandler() {
//		contextRunner.withConfiguration(UserConfigurations.of(CustomSpringSkillConfiguration.class))
//		.run(
//			context -> {
//				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
//				SpeechletResponseEnvelope responseEnv = dispatcher.apply(getBogusRequest());
//				PlainTextOutputSpeech outputSpeech = 
//						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
//				assertEquals("I have no idea what you're asking for."
//						, outputSpeech.getText());			
//			}
//		);
//	}
//	
//	@Test
//	public void shouldHandleIntentRequest() {
//		contextRunner.withConfiguration(UserConfigurations.of(CustomSpringSkillConfiguration.class))
//		.run(
//			context -> {
//				SpeechletRequestDispatcher dispatcher = context.getBean(SpeechletRequestDispatcher.class);
//				Intent intent = Intent.builder()
//						.withName("hello")
//						.build();
//				SpeechletResponseEnvelope responseEnv = dispatcher.apply(getIntentRequest(intent));
//				PlainTextOutputSpeech outputSpeech = 
//						(PlainTextOutputSpeech) responseEnv.getResponse().getOutputSpeech();
//				assertEquals("Hello Spring World", outputSpeech.getText());			
//			}
//		);
//	}
//	 
//	@Configuration
//  	public static class CustomSpringSkillConfiguration {
//		
//		@Bean
//		public Function<SpeechletRequestEnvelope, SpeechletResponseEnvelope> hello() {
//			return (requestEnvelope) -> {
//				SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
//				SpeechletResponse response = new SpeechletResponse();
//				SimpleCard card = new SimpleCard();
//				card.setTitle("Hello");
//				card.setContent("Hello Spring World");
//				response.setCard(card);
//				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
//				outputSpeech.setId("hello");
//				outputSpeech.setText("Hello Spring World");
//				response.setOutputSpeech(outputSpeech);
//				responseEnvelope.setResponse(response);
//				return responseEnvelope;
//			};
//		}
//		
//		@Bean
//		public LaunchRequestHandler launchRequestHandler() {
//			return (requestEnvelope) -> {
//				SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
//				SpeechletResponse response = new SpeechletResponse();
//				SimpleCard card = new SimpleCard();
//				card.setTitle("Buenos dias");
//				card.setContent("Buenos dias");
//				response.setCard(card);
//				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
//				outputSpeech.setId("buenos_dias");
//				outputSpeech.setText("Buenos dias");
//				response.setOutputSpeech(outputSpeech);
//				responseEnvelope.setResponse(response);
//				return responseEnvelope;
//			};
//		}
//		
//		@Bean
//		public SessionEndedRequestHandler sessionEndedRequestHandler() {
//			return (requestEnvelope) -> {
//				SpeechletResponseEnvelope responseEnvelope = new SpeechletResponseEnvelope();
//				SpeechletResponse response = new SpeechletResponse();
//				SimpleCard card = new SimpleCard();
//				card.setTitle("Adios");
//				card.setContent("Adios");
//				response.setCard(card);
//				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
//				outputSpeech.setId("adios");
//				outputSpeech.setText("Adios");
//				response.setOutputSpeech(outputSpeech);
//				responseEnvelope.setResponse(response);
//				return responseEnvelope;
//			};
//		}
//		
//		@Bean
//		public UnknownRequestHandler unknownRequestHandler() {
//			return (requestEnvelope) -> {
//				PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
//				outputSpeech.setText("I have no idea what you're asking for.");
//	
//				SimpleCard card = new SimpleCard();
//				card.setTitle("Spring Skills Error");
//				card.setContent("I have no idea what you're asking for.");
//	
//				SpeechletResponse response = SpeechletResponse.newTellResponse(outputSpeech, card);
//				SpeechletResponseEnvelope envelope = new SpeechletResponseEnvelope();
//				envelope.setResponse(response);
//				envelope.setSessionAttributes(new HashMap<>());
//				envelope.setVersion("1.0");
//	
//				return envelope;
//			};
//		}
//		
//	}

}
