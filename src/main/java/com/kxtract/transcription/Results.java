package com.kxtract.transcription;

import java.util.List;

public class Results {
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public SpeakerLabels getSpeaker_labels() {
		return speakerLabels;
	}
	public void setSpeaker_labels(SpeakerLabels speakerLabels) {
		this.speakerLabels = speakerLabels;
	}
	public List<Transcript> getTranscripts() {
		return transcripts;
	}
	public void setTranscripts(List<Transcript> transcripts) {
		this.transcripts = transcripts;
	}
	private List<Item> items;
	private SpeakerLabels speakerLabels;
	private List<Transcript> transcripts;
}
