package spring.skills.alexa;
import java.io.IOException;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StreamUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class AlexaControllerTests {
	
	@Autowired
	TestRestTemplate rest;
	
	@Test
	public void testStuff() throws IOException, JSONException {
		String intentRequestJSON = readToString("/simple-intent-request.json");
		String expectedResponseJSON = readToString("/simple-intent-response.json");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(intentRequestJSON, headers);
		String response = rest.postForObject("/alexa", requestEntity, String.class);		
		JSONAssert.assertEquals(expectedResponseJSON, response, false);
	}

	private String readToString(String path) throws IOException {
		ClassPathResource resource = new ClassPathResource(path);
		String intentRequestBody = StreamUtils.copyToString(resource.getInputStream(), Charset.defaultCharset());
		return intentRequestBody;
	}

}
