package com.knn;

public class Distance {
	
	private String topic;
	private int distace;
	public Distance(String topic, int distace) {
		super();
		this.topic = topic;
		this.distace = distace;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public int getDistace() {
		return distace;
	}
	public void setDistace(int distace) {
		this.distace = distace;
	}

}
