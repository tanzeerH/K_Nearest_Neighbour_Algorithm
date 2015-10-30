package com.knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class KNNAlgo {

	private HashMap<String, Integer> wordMap_train;
	private ArrayList<ArrayList<Integer>> fetvectorList_train;
	private ArrayList<String> wordList_train;
	private ArrayList<String> docTopicList_train;
	private HashMap<String, Integer> wordMap_test;
	private ArrayList<ArrayList<Integer>> fetvectorList_test;
	private ArrayList<String> wordList_test;
	private ArrayList<String> docTopicList_test;

	public KNNAlgo() {

		runKnn();
	}

	private void runKnn()

	{
		TrainStructure train = new TrainStructure();
		TestStructure test = new TestStructure();

		wordMap_train = train.getWordMap();
		fetvectorList_train = train.getFetvectorList();
		wordList_train = train.getWordList();
		docTopicList_train = train.getDocTopicList();

		wordMap_test = test.getWordMap();
		fetvectorList_test = test.getFetvectorList();
		wordList_test = test.getWordList();
		docTopicList_test = test.getDocTopicList();

		int size = docTopicList_test.size();
		int train_size = docTopicList_train.size();

		for (int docIndex = 0; docIndex < size; docIndex++) {
			
			ArrayList<Distance> disList=new ArrayList<Distance>();
			for (int t_doc = 0; t_doc < train_size; t_doc++) {
				
				int words = wordList_test.size();
				int dis = 0;
				for (int i = 0; i < words; i++) {
					int freq = fetvectorList_test.get(i).get(docIndex);
					if (freq > 0) {
						String w = wordList_test.get(i);
						if (wordMap_train.containsKey(w)) {
							
							int location=wordMap_train.get(w);
							int occur=fetvectorList_train.get(location).get(t_doc);
							if(occur==0)
								dis++;
						} else {
							dis++;
						}
					}
				}
				
				words = wordList_train.size();
				for (int i = 0; i < words; i++) {
					int freq = fetvectorList_train.get(i).get(t_doc);
					if (freq > 0) {
						String w = wordList_train.get(i);
						if (wordMap_test.containsKey(w)) {
							
							int location=wordMap_test.get(w);
							int occur=fetvectorList_test.get(location).get(docIndex);
							if(occur==0)
								dis++;
						} else {
							dis++;
						}
					}
				}
			//	System.out.println("dstance: "+ dis+"  doc: "+ docTopicList_train.get(t_doc));
				Distance distance=new Distance(docTopicList_train.get(t_doc), dis);
				disList.add(distance);
				
			}
			Collections.sort(disList,new DistanceComparator());
			
			getClassType(disList,docTopicList_test.get(docIndex));
			
			if(docIndex>10)
				break;

		}
   
	}
	private void getClassType(ArrayList<Distance> disList,String test)
	{
		HashMap<String,Integer> maps=new HashMap<String, Integer>();
		ArrayList<String> list=new ArrayList<String>();
		
		for(int i=0;i<5;i++)
		{
			if(maps.containsKey(disList.get(i).getTopic()))
			{
				int val=maps.get(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), val+1);
			}
			else
			{
				list.add(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), 0);
			}
		}
		int MAX=-1;
		String topic="";
	   for(int i=0;i<list.size();i++)
	   {
		   if(maps.get(list.get(i))>MAX)
		   {
			   MAX=maps.get(list.get(i));
			   topic=list.get(i);
		   }
			   
				  
	   }
	   System.out.println("result :"+ topic+"  output: "+test);
	}

}
