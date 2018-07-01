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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Spring Skills.
 * 
 * @author Craig Walls
 */
@ConfigurationProperties(prefix="spring.skills")
public class SpringSkillsProperties {

	private String helpIntentBeanName = "help";
	
	private String cancelIntentBeanName = "cancel";
	
	private String stopIntentBeanName = "stop";

	public String getHelpIntentBeanName() {
		return helpIntentBeanName;
	}

	public void setHelpIntentBeanName(String helpIntentBeanName) {
		this.helpIntentBeanName = helpIntentBeanName;
	}

	public String getCancelIntentBeanName() {
		return cancelIntentBeanName;
	}

	public void setCancelIntentBeanName(String cancelIntentBeanName) {
		this.cancelIntentBeanName = cancelIntentBeanName;
	}

	public String getStopIntentBeanName() {
		return stopIntentBeanName;
	}

	public void setStopIntentBeanName(String stopIntentBeanName) {
		this.stopIntentBeanName = stopIntentBeanName;
	}
	
}
