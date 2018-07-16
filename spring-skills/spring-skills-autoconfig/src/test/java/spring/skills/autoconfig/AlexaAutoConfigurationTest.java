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
package spring.skills.autoconfig;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazon.ask.servlet.verifiers.SkillServletVerifier;

import spring.skills.alexa.AlexaIntentController;
import spring.skills.core.SpeechRequestDispatcher;

public class AlexaAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(
					SpringSkillsAutoConfiguration.class, AlexaAutoConfiguration.class));
	
	@Test
	public void shouldAutoConfigureAlexaBeans() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
			.run(
				context -> {
					SpeechRequestDispatcher dispatcher = context.getBean(SpeechRequestDispatcher.class);

					assertThat(context).hasSingleBean(AlexaIntentController.class);
					assertThat(context.getBean(AlexaIntentController.class))
						.isSameAs(context.getBean(AlexaAutoConfiguration.class).alexaController(dispatcher));

					
					assertThat(context).hasSingleBean(SkillServletVerifier.class);
					assertThat(context.getBean(SkillServletVerifier.class))
						.isSameAs(context.getBean(AlexaAutoConfiguration.class).noopVerifier());
				});
	}
	
	@Test
	public void shouldBackOffOnAutoConfiguredAlexaBeans() {
		this.contextRunner.withUserConfiguration(AlexaConfiguration.class)
			.run(
				context -> {
					SpeechRequestDispatcher dispatcher = context.getBean(SpeechRequestDispatcher.class);
					
					assertThat(context).hasSingleBean(AlexaIntentController.class);
					assertThat(context.getBean(AlexaIntentController.class))
						.isSameAs(context.getBean(AlexaConfiguration.class).alexaController(dispatcher));

					assertThat(context).hasSingleBean(SkillServletVerifier.class);
					assertThat(context.getBean(SkillServletVerifier.class))
						.isSameAs(context.getBean(AlexaConfiguration.class).noopVerifier());
				});
	}

	@Configuration
	public static class AlexaConfiguration {

		@Bean
		public AlexaIntentController alexaController(SpeechRequestDispatcher dispatcher) {
			return new AlexaIntentController(dispatcher);
		}
		
		@Bean
		public SkillServletVerifier noopVerifier() {
				return (request, serializedEnvelope, deserializedEnvelope) -> {};
		}
		
	}
}
