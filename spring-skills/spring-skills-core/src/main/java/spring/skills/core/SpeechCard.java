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

public class SpeechCard {

	private final String title;
	
	private final String text;
	
	private String smallImageUrl;
	
	private String largeImageUrl;
	
	//
	// TODO: Google Action cards also have
	//
	
	public SpeechCard(String title, String text) {
		this.title = title;
		this.text = text;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getText() {
		return text;
	}

	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}

	public String getLargeImageUrl() {
		return largeImageUrl;
	}
	
	public void setLargeImageUrl(String largeImageUrl) {
		this.largeImageUrl = largeImageUrl;
	}
	
}
