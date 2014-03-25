package de.pieczewski.ingresstool.entities.mapper;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import de.pieczewski.ingresstool.entities.Mod;

public class ModMapper {
	public ArrayList<Mod> mapMods(JsonNode modNodes) {
		ArrayList<Mod> mods = new ArrayList<Mod>();
		for (JsonNode modNode : modNodes.get("linkedModArray")) {
			Mod mod = new Mod();
			if (!modNode.isNull()) {
				mod.setInstallingUser(modNode.get("installingUser").asText());
				mod.setDisplayName(modNode.get("displayName").asText());
				mod.setRarity(modNode.get("rarity").asText());
				mod.setType(modNode.get("type").asText());
				mod.setMittigation(modNode.findValue("stats").get("MITIGATION").asInt());

				mods.add(mod);
			}
		}
		return mods;
	}
}
