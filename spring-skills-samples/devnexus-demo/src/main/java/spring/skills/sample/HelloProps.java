package spring.skills.sample;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="skills.hello")
public class HelloProps {

	private String message;
	
	private String hokey;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHokey() {
		return hokey;
	}

	public void setHokey(String hokey) {
		this.hokey = hokey;
	}
	
}
