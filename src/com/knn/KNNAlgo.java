package com.knn;

import java.util.ArrayList;
import java.util.HashMap;

public class KNNAlgo {
	
	private HashMap<String, Integer> wordMap_train ;
	private ArrayList<ArrayList<Integer>> fetvectorList_train ;
	private ArrayList<String> wordList_train;
	private ArrayList<String> docTopicList_train;
	private HashMap<String, Integer> wordMap_test;
	private ArrayList<ArrayList<Integer>> fetvectorList_test;
	private ArrayList<String> wordList_test;
	private ArrayList<String> docTopicList_test;
	public KNNAlgo()
	{
		
	}
	private void runKnn()
	
	{
		TrainStructure train=new TrainStructure();
		TestStructure test=new TestStructure();
		
		
		
	}

}
