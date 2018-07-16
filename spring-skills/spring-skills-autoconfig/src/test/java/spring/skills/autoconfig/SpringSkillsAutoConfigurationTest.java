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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import spring.skills.core.BeanNameSpeechRequestDispatcher;
import spring.skills.core.SpeechRequestDispatcher;

public class SpringSkillsAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(SpringSkillsAutoConfiguration.class));
	
	@Test
	public void shouldAutoConfigureSpringSkills() {
		this.contextRunner.withUserConfiguration(EmptyConfiguration.class)
			.run(
				context -> {
					assertThat(context).hasSingleBean(SpeechRequestDispatcher.class);
					assertThat(context.getBean(SpeechRequestDispatcher.class))
						.isSameAs(context.getBean(SpringSkillsAutoConfiguration.class).dispatcher());
				});
	}
	
	@Test
	public void shouldBackoffOnSpeechRequestDispatcherConfiguration() {
		this.contextRunner.withUserConfiguration(SpeechRequestDispatcherConfiguration.class)
			.run(
				context -> {
					assertThat(context).hasSingleBean(SpeechRequestDispatcher.class);
					assertThat(context.getBean(SpeechRequestDispatcher.class))
						.isSameAs(context.getBean(SpeechRequestDispatcherConfiguration.class).dispatcher());
				});
	}
	
	@Configuration
	public static class SpeechRequestDispatcherConfiguration {

		@Bean
		@ConditionalOnMissingBean(value=SpeechRequestDispatcher.class)
		public SpeechRequestDispatcher dispatcher() {
			return new BeanNameSpeechRequestDispatcher(null, null, null);
		}
		
	}
}
