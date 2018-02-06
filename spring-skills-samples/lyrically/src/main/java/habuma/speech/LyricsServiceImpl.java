package habuma.speech;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="songs")
public class LyricsServiceImpl implements LyricsService {

	private List<String> lyrics = new ArrayList<>();
	
	public List<String> getLyrics() {
		return lyrics;
	}
	
	@Override
	public String pickALyric() {
		Double floor = Math.floor(Math.random() * lyrics.size());
		int index = floor.intValue();
		return lyrics.get(index);
	}
	
}
