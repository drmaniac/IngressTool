package de.pieczewski.ingresstool.entities.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.pieczewski.ingresstool.entities.GameScore;
import de.pieczewski.ingresstool.entities.Team;

public class GameScoreMapper {

	
	public HashMap<String, GameScore> parse(InputStream inputStream) throws JsonProcessingException, IOException {
		HashMap<String, GameScore> result = new HashMap<String, GameScore>();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(inputStream);
		
		JsonNode resistanceScore = rootNode.findValue("resistanceScore");
		JsonNode alienScore = rootNode.findValue("alienScore");
		
		result.put(Team.ALIENS.toString(), new GameScore(Team.ALIENS, alienScore.asLong()));
		result.put(Team.RESISTANCE.toString(), new GameScore(Team.RESISTANCE, resistanceScore.asLong()));
		
		return result;
	}
}
