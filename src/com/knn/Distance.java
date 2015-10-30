package com.knn;

public class Distance {
	
	private String topic;
	private int distace_hamming;
	private int distance_euclidian;
	private double cosine_similarity;
	
	
	public Distance(String topic, int distace_hamming, int distance_euclidian,
			double cosine_similarity) {
		super();
		this.topic = topic;
		this.distace_hamming = distace_hamming;
		this.distance_euclidian = distance_euclidian;
		this.cosine_similarity = cosine_similarity;
	}
	public double getCosine_similarity() {
		return cosine_similarity;
	}
	public void setCosine_similarity(double cosine_similarity) {
		this.cosine_similarity = cosine_similarity;
	}
	public int getDistace_hamming() {
		return distace_hamming;
	}
	public void setDistace_hamming(int distace_hamming) {
		this.distace_hamming = distace_hamming;
	}
	public int getDistance_euclidian() {
		return distance_euclidian;
	}
	public void setDistance_euclidian(int distance_euclidian) {
		this.distance_euclidian = distance_euclidian;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
}
