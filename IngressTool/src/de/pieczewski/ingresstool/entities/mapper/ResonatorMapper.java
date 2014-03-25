package de.pieczewski.ingresstool.entities.mapper;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import de.pieczewski.ingresstool.entities.Resonator;

public class ResonatorMapper {
	public ResonatorMapper() {
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Resonator> mapResonators(JsonNode resonatorsNodes) {
		ArrayList<Resonator> resonators = new ArrayList<Resonator>();
		for (JsonNode resonatorNode : resonatorsNodes.get("resonators")) {
			Resonator resonator = new Resonator();
			if (!resonatorNode.isNull()) {
				resonator.setSlot(resonatorNode.get("slot").asInt());
				resonator.setLevel(resonatorNode.get("level").asInt());
				resonator.setEnergyTotal(resonatorNode.get("energyTotal").asInt());
				resonator.setId(resonatorNode.get("id").asText());
				resonator.setOwnerGuid(resonatorNode.get("ownerGuid").asText());

				resonators.add(resonator);
			}
		}
		return resonators;
	}
}
