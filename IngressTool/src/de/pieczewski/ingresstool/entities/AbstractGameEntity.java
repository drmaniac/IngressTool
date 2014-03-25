package de.pieczewski.ingresstool.entities;

public class AbstractGameEntity {

	private Long id;
	private String key;
	private Team team;

	public AbstractGameEntity(Long id, String key, Team team) {
		this.key = key;
		this.id = id;
		this.team = team;
	}

	public Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	protected void setKey(String key) {
		this.key = key;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
