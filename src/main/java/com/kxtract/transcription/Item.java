package com.kxtract.transcription;

import java.util.List;

public class Item {
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Double getStart_time() {
		return start_time;
	}
	public void setStart_time(Double start_time) {
		this.start_time = start_time;
	}
	public Double getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Double end_time) {
		this.end_time = end_time;
	}
	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	public void setAlternatives(List<Alternative> alternatives) {
		this.alternatives = alternatives;
	}
	private String type;
	private Double start_time;
	private Double end_time;
	private List<Alternative> alternatives;
}
