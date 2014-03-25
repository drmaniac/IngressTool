package de.pieczewski.ingresstool.entities;

public class GameScore {
	private Long score;
	private Team team;

	public GameScore(Team team, long score) {
		this.team = team;
		this.score = score;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}
}
