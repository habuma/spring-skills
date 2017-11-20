package habuma.speech;

import java.util.HashMap;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.amazon.speech.json.SpeechletResponseEnvelope;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

@SpringBootApplication
public class LyricsApplication {
	
	@Autowired
	private LyricsService songService;

	@Bean
	public Supplier<SpeechletResponseEnvelope> lyrics() {
		return () -> {
			String lyric = songService.pickALyric();
			SsmlOutputSpeech ssmlOS = new SsmlOutputSpeech();
			String message = lyric;
			ssmlOS.setSsml("<speak> " + message + " </speak>");

			SimpleCard card = new SimpleCard();
			card.setTitle("Lyrically");
			card.setContent(lyric);

			SpeechletResponse response = SpeechletResponse.newTellResponse(ssmlOS, card);
			SpeechletResponseEnvelope envelope = new SpeechletResponseEnvelope();
			envelope.setResponse(response);
			envelope.setSessionAttributes(new HashMap<>());
			envelope.setVersion("1.0");

			return envelope;
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(LyricsApplication.class, args);
	}
}
