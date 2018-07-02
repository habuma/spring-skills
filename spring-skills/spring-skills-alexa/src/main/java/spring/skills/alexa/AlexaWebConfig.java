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
package spring.skills.alexa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.OutputSpeech;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * {@link WebMvcConfigurer} that adds an argument resolver for
 * {@link RequestEnvelope}. Includes verification of the request
 * signature as required by Amazon for remote/self-hosted skill applications.
 * </p>
 * 
 * <p>
 * Also configures a Jackson {@link ObjectMapper} with a custom mixin for
 * ASK types so that null values aren't included in serialized responses.
 * </p>
 * 
 * <p>
 * See <a href=
 * "https://developer.amazon.com/docs/custom-skills/host-a-custom-skill-as-a-web-service.html#verifying-that-the-request-was-sent-by-alexa">
 * Amazon documentation regarding request verification</a>.
 * </p>
 * 
 * @author Craig Walls
 */
@Configuration
public class AlexaWebConfig implements WebMvcConfigurer {

	@Autowired(required=false)
	private SkillServletVerifier signatureVerifier;
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		RequestEnvelopeArgumentResolver argumentResolver = new RequestEnvelopeArgumentResolver();
		if (signatureVerifier != null) {
			argumentResolver.setSignatureVerifier(signatureVerifier);
		}
		argumentResolvers.add(argumentResolver);
	}
	
	
	//
	// Adds mixins to ignore null values on ASK types without setting null-ignore globally.
	// (Setting null-ignore globally could break host application's requirements.)
	//
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.addMixIn(ResponseEnvelope.class, IgnoreNullPropertiesMixin.class);
		objectMapper.addMixIn(Card.class, IgnoreNullPropertiesMixin.class);
		objectMapper.addMixIn(OutputSpeech.class, IgnoreNullPropertiesMixin.class);
		objectMapper.addMixIn(Response.class, IgnoreNullPropertiesMixin.class);
		return objectMapper;
	}
	
	@JsonInclude(Include.NON_NULL)
	private static class IgnoreNullPropertiesMixin {}

}
