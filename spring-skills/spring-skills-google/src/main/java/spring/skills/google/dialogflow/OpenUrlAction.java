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
public class OpenUrlAction {

	private String url;
	
	private AndroidApp androidApp;
	
	private UrlTypeHint urlTypeHint;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public AndroidApp getAndroidApp() {
		return androidApp;
	}

	public void setAndroidApp(AndroidApp androidApp) {
		this.androidApp = androidApp;
	}

	public UrlTypeHint getUrlTypeHint() {
		return urlTypeHint;
	}

	public void setUrlTypeHint(UrlTypeHint urlTypeHint) {
		this.urlTypeHint = urlTypeHint;
	}
	
	public static enum UrlTypeHint {
		URL_TYPE_HINT_UNSPECIFIED, AMP_CONTENT
	}
}
