package de.pieczewski.ingresstool.entities.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.pieczewski.ingresstool.cache.IngressEntityCache;
import de.pieczewski.ingresstool.entities.ControlField;
import de.pieczewski.ingresstool.entities.Edge;
import de.pieczewski.ingresstool.entities.Turret;
import de.pieczewski.ingresstool.intelmap.GameEntityTile;
import de.pieczewski.ingresstool.intelmap.MapManager;
import de.pieczewski.ingresstool.intelmap.MapManagerEvents;

public class GameEntityMapper {

	private static String TAG = GameEntityMapper.class.getName();
	private IngressEntityCache ingressEntityCache;

	public GameEntityMapper(IngressEntityCache ingressEntityCache) {
		this.ingressEntityCache = ingressEntityCache;
	}

	public List<GameEntityTile> parseInCurrentThread(InputStream inputStream,
			List<GameEntityTile> tilePackage) throws JsonProcessingException, IOException,
			JSONException {
		parse(inputStream, tilePackage);
		return null;
	}

	public List<GameEntityTile> parse(InputStream inputStream, List<GameEntityTile> tilePackage)
			throws JsonProcessingException, IOException, JSONException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(inputStream);

		for (GameEntityTile gameEntityTile : tilePackage) {
			JsonNode tileNode = rootNode.findValue(gameEntityTile.getKey());
			if (tileNode != null) {

				List<JsonNode> deletedGameEntityGuids = tileNode
						.findValues("deletedGameEntityGuids");
				if (deletedGameEntityGuids != null)
					parsedeletedGameEntityGuids(deletedGameEntityGuids, gameEntityTile);

				List<JsonNode> gameEntities = tileNode.findValues("gameEntities");
				if (gameEntities != null)
					parseGameEntities(gameEntities, gameEntityTile);
			}
		}
		return tilePackage;
	}
	
	public GameEntityTile parse(InputStream inputStream, GameEntityTile entityTile) throws JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(inputStream);
		JsonNode tileNode = rootNode.findValue(entityTile.getKey());
		if (tileNode != null) {

			List<JsonNode> deletedGameEntityGuids = tileNode
					.findValues("deletedGameEntityGuids");
			if (deletedGameEntityGuids != null)
				parsedeletedGameEntityGuids(deletedGameEntityGuids, entityTile);

			List<JsonNode> gameEntities = tileNode.findValues("gameEntities");
			if (gameEntities != null)
				parseGameEntities(gameEntities, entityTile);
		}
		return entityTile;
	}

	private void parsedeletedGameEntityGuids(List<JsonNode> gameEntities, GameEntityTile tile) {
		// TODO: make caching more
	}

	private void parseGameEntities(List<JsonNode> gameEntities, GameEntityTile gameEntityTile) {
		int turret_count = 0;
		int edge_count = 0;
		int field_count = 0;
		for (JsonNode entity : gameEntities) {
			// Log.v(TAG, "parse entity" + entity.toString());
			Iterator<JsonNode> iterator = entity.iterator();
			while (iterator.hasNext()) {
				JsonNode node = iterator.next();
				try {
					String key = node.get(0).textValue();
					Long id = node.get(1).longValue();
					JsonNode turretNode = node.findParent("turret");
					JsonNode edgeNode = node.findParent("edge");
					JsonNode fieldNode = node.findParent("capturedRegion");

					if (turretNode != null) {
						TurretMapper turretMapper = new TurretMapper();
						Turret turret = turretMapper.mapTurret(id, key, turretNode);
						ingressEntityCache.cacheTurret(turret);
						gameEntityTile.addGameEntity(key);
						turret_count ++;
					}
					if (edgeNode != null) {
						EdgeMapper edgeMapper = new EdgeMapper();
						Edge edge = edgeMapper.mapEdge(id, key, edgeNode);
						ingressEntityCache.cacheEdge(edge);
						gameEntityTile.addGameEntity(key);
						edge_count++;
					}
					if (fieldNode != null) {
						ControlFieldMapper controlFieldMapper = new ControlFieldMapper();
						ControlField controlField = controlFieldMapper.mapControllField(id, key, fieldNode);
						ingressEntityCache.cacheControlField(controlField);
						gameEntityTile.addGameEntity(key);
						field_count++;
					}
				} catch (Exception e) {
					Log.e(TAG, "Error while parsing elemt:" + node.toString(), e);
				}
			}
		}
		gameEntityTile.setLoaded(true);
		ingressEntityCache.cacheTile(gameEntityTile);
		redrawGameEntityTile(gameEntityTile);
		Log.v(TAG, String.format("game entities loaded for key %s (turrets: %d | edges: %d | fields: %s)" ,gameEntityTile.getKey(),
				turret_count, edge_count, field_count));
	}
	
	
	private void redrawGameEntityTile(GameEntityTile gameEntityTile) {
		MapManager.getInstance().sendAsynEventMsg(MapManagerEvents.DRAW_TILE, gameEntityTile);
	}
}
