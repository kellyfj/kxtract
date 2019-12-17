package com.kxtract.transcription;

import java.util.List;

public class SpeakerLabels {
	public int getSpeakers() {
		return speakers;
	}
	public void setSpeakers(int speakers) {
		this.speakers = speakers;
	}
	public List<Segment> getSegments() {
		return segments;
	}
	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}
	private int speakers;
	private List<Segment> segments;
	
}
