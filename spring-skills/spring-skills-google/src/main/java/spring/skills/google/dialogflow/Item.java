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
package spring.skills.google.dialogflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=Include.NON_NULL)
public class Item {

	private SimpleResponse simpleResponse;
	
	private BasicCard basicCard;

	public SimpleResponse getSimpleResponse() {
		return simpleResponse;
	}

	public void setSimpleResponse(SimpleResponse simpleResponse) {
		this.simpleResponse = simpleResponse;
	}

	public BasicCard getBasicCard() {
		return basicCard;
	}

	public void setBasicCard(BasicCard basicCard) {
		this.basicCard = basicCard;
	}
	
}
