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

/*
 * Normalizes various potential intent names into a predictable bean name.
 * 
 * TODO: Although this currently works as expected, it's messy and likely not ideally optimized. 
 */
class IntentToBeanNameUtils {

	public static String beanifyIntentName(String intentName) {
		if (intentName.endsWith("Intent")) {
			intentName = intentName.substring(0, intentName.length() - 6);
		}
		String[] intentParts = intentName.split("[\\s\\-()\\_]");
		if (intentParts.length > 1) {
			intentName = "";
			for (String part : intentParts) {
				if (part.length() > 0) {
					intentName += Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase();
				}
			}
		}
		return Character.toLowerCase(intentName.charAt(0)) + intentName.substring(1);
	}
	
}
