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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.adapter.aws.SpringFunctionInitializer;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import reactor.core.publisher.Flux;

/**
 * AWS adapter, much like SpringBootStreamHandler, but that configures a
 * custom ObjectMapper with a deserializer for ASK's Context.
 * 
 * This is mostly a copy-n-paste of SpringBootStreamHandler, because the
 * ObjectMappper of that class is private and offers no opportunity for
 * modification.
 * 
 * @author Craig Walls
 */
public class SkillsSpringBootStreamHandler extends SpringFunctionInitializer
		implements RequestStreamHandler {

	private static Logger logger = Logger.getLogger(SkillsSpringBootStreamHandler.class.getName());
	
	@Autowired
	private ObjectMapper mapper;

	public SkillsSpringBootStreamHandler() {
		super();
	}

	public SkillsSpringBootStreamHandler(Class<?> configurationClass) {
		super(configurationClass);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(com.amazon.speech.speechlet.Context.class, new ContextDeserializer(com.amazon.speech.speechlet.Context.class));
		mapper.registerModule(module);
	}

	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context)
			throws IOException {
		initialize();
		Object value = convertStream(input);
		Flux<?> flux = apply(extract(value));
		mapper.writeValue(output, result(value, flux));
	}

	private Object result(Object input, Flux<?> flux) {
		List<Object> result = new ArrayList<>();
		for (Object value : flux.toIterable()) {
			result.add(value);
		}
		if (isSingleValue(input) && result.size()==1) {
			return result.get(0);
		}
		return result;
	}

	private boolean isSingleValue(Object input) {
		return !(input instanceof Collection);
	}

	private Flux<?> extract(Object input) {
		if (input instanceof Collection) {
			return Flux.fromIterable((Iterable<?>) input);
		}
		return Flux.just(input);
	}

	private Object convertStream(InputStream input) {
		try {
			return mapper.readValue(input, getInputType());
		}
		catch (Exception e) {
			throw new IllegalStateException("Cannot convert event", e);
		}
	}
	
	private static class ContextDeserializer extends StdDeserializer<com.amazon.speech.speechlet.Context> {
		private static final long serialVersionUID = 1L;

		protected ContextDeserializer(Class<com.amazon.speech.speechlet.Context> vc) {
			super(vc);
		}

		@Override
		public com.amazon.speech.speechlet.Context deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException, JsonProcessingException {
			logger.info("Deserializing in custom Context deserializer");

			jp.getCodec().readTree(jp);
			return com.amazon.speech.speechlet.Context.builder()
					.build();
		}
	}
}
