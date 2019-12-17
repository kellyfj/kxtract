package com.kxtract.transcription;

public class SegmentItems {
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
	public String getSpeaker_label() {
		return speaker_label;
	}
	public void setSpeaker_label(String speaker_label) {
		this.speaker_label = speaker_label;
	}
	private Double start_time;
	private Double end_time;
	private String speaker_label;
}
