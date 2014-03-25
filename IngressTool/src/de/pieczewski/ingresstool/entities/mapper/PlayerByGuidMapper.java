package de.pieczewski.ingresstool.entities.mapper;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PlayerByGuidMapper {

	private static final String TAG = PlayerByGuidMapper.class.getName();
	
	public String parse(InputStream inputStream) throws JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(inputStream);
		Log.v(TAG, rootNode.toString());
		return rootNode.findValue("nickname").asText();
	}
}
