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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=Include.NON_NULL)
public class BasicCard {

	private String title;
	
	private String subtitle;
	
	private String formattedText;
	
	private Image image;
	
	private List<Button> buttons;
	
	private ImageDisplayOptions imageDisplayOptions;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getFormattedText() {
		return formattedText;
	}

	public void setFormattedText(String formattedText) {
		this.formattedText = formattedText;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}

	public ImageDisplayOptions getImageDisplayOptions() {
		return imageDisplayOptions;
	}

	public void setImageDisplayOptions(ImageDisplayOptions imageDisplayOptions) {
		this.imageDisplayOptions = imageDisplayOptions;
	}

	public static enum ImageDisplayOptions {
		DEFAULT, WHITE, CROPPED
	}
	
}
