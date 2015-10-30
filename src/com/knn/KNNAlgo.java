package com.knn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.omg.CORBA.FREE_MEM;
import org.omg.CORBA.IDLTypeOperations;

public class KNNAlgo {

	/*
	 * private HashMap<String, Integer> wordMap_train; private
	 * ArrayList<ArrayList<Integer>> fetvectorList_train; private
	 * ArrayList<String> wordList_train; private ArrayList<String>
	 * docTopicList_train; private HashMap<String, Integer> wordMap_test;
	 * private ArrayList<ArrayList<Integer>> fetvectorList_test; private
	 * ArrayList<String> wordList_test; private ArrayList<String>
	 * docTopicList_test;
	 */

	private ArrayList<HashMap<String, Integer>> train_hashList;
	private ArrayList<ArrayList<String>> train_docWordList;
	private ArrayList<String> train_docTopicList;

	private ArrayList<HashMap<String, Integer>> test_hashList;
	private ArrayList<ArrayList<String>> test_docWordList;
	private ArrayList<String> test_docTopicList;

	private HashMap<String, ArrayList<Integer>> wordMap;

	private int corect_hamming_k1 = 0;
	private int corect_hamming_k3 = 0;
	private int corect_hamming_k5 = 0;

	private int corect_euclid_k1 = 0;
	private int corect_euclid_k3 = 0;
	private int corect_euclid_k5 = 0;

	private int corect_cosine_k1 = 0;
	private int corect_cosine_k3 = 0;
	private int corect_cosine_k5 = 0;

	public KNNAlgo() {

		runKnn();
	}

	private void runKnn()

	{
		
		TrainStructure train = new TrainStructure();
		TestStructure test = new TestStructure();

		train_hashList = train.getHashList();
		train_docWordList = train.getDocWordList();
		train_docTopicList = train.getDocTopicList();

		wordMap = train.getWordMap();

		test_hashList = test.getHashList();
		test_docWordList = test.getDocWordList();
		test_docTopicList = test.getDocTopicList();

		int size = test_docTopicList.size();
		int train_size = train_docTopicList.size();
		System.out.println("Calculating.....");
		for (int docIndex = 0; docIndex < size; docIndex++) {

			ArrayList<Distance> disList = new ArrayList<Distance>();
			for (int t_doc = 0; t_doc < train_size; t_doc++) {

				int words = test_docWordList.get(docIndex).size();
				int dis_hamming = 0;
				int dis_euclidian = 0;
				double dot_product = 0;
				double vector_x_sum = 0;
				double vector_y_sum = 0;
				for (int i = 0; i < words; i++) {
					String key = test_docWordList.get(docIndex).get(i);
					int occur = test_hashList.get(docIndex).get(key);

					// calculating TF_IDF
					double TF = (double) occur
							/ (double) test_docWordList.get(docIndex).size();
					double D = train_docWordList.size() + 1;
					double cw = 1;
					if (wordMap.containsKey(key))
						cw += wordMap.get(key).size();
					double IDF = get2baseLog(D / cw);

					double TF_IDF = TF * IDF;

					vector_x_sum += TF_IDF * TF_IDF;

					if (train_hashList.get(t_doc).containsKey(key)) {
						int frq = train_hashList.get(t_doc).get(key);
						dis_euclidian += Math.pow((occur - frq), 2);

						double _TF = (double) frq
								/ (double) train_docWordList.get(t_doc).size();
						double _D = test_docWordList.size();
						double _cw = 1;
						if (wordMap.containsKey(key))
							_cw += wordMap.get(key).size();
						double _IDF = get2baseLog(_D / _cw);

						double _TF_IDF = _TF * _IDF;
						dot_product += TF_IDF * _TF_IDF;

					} else {
						dis_hamming++;
						dis_euclidian += Math.pow(occur, 2);

					}
				}

				words = train_docWordList.get(t_doc).size();

				for (int i = 0; i < words; i++) {
					String key = train_docWordList.get(t_doc).get(i);
					int occur = train_hashList.get(t_doc).get(key);

					double TF = (double) occur
							/ (double) train_docWordList.get(t_doc).size();

					double IDF;
					double TF_IDF;

					if (test_hashList.get(docIndex).containsKey(key)) {
						// int frq=test_hashList.get(docIndex).get(key);
						// dis_euclidian+=Math.pow((occur-frq), 2);
						double D = train_docWordList.size() + 1;
						double cw = 1;
						if (wordMap.containsKey(key))
							cw += wordMap.get(key).size();
						IDF = get2baseLog(D / cw);
						TF_IDF = TF * IDF;
					} else {
						dis_hamming++;
						dis_euclidian += Math.pow(occur, 2);

						double D = train_docWordList.size();
						double cw = 0;
						if (wordMap.containsKey(key))
							cw += wordMap.get(key).size();
						IDF = get2baseLog(D / cw);
						TF_IDF = TF * IDF;
					}
					vector_y_sum += TF_IDF * TF_IDF;
				}

				// System.out.println("dstance: "+ dis+"  doc: "+
				// docTopicList_train.get(t_doc));
				double dis_cosine = dot_product
						/ (Math.sqrt(vector_x_sum) * Math.sqrt(vector_y_sum));
				Distance distance = new Distance(train_docTopicList.get(t_doc),
						dis_hamming, dis_euclidian, dis_cosine);
				disList.add(distance);

			}
			Collections.sort(disList, new DistanceComparatorByHamming());

			getClassTypeByHamming(disList, test_docTopicList.get(docIndex));

			 Collections.sort(disList, new DistanceComparatorByEuclidian());

			 getClassTypeByEuclid(disList, test_docTopicList.get(docIndex));

			 Collections.sort(disList, new DistanceComparatorByCosine());

			 getClassTypeByCosine(disList, test_docTopicList.get(docIndex));

		}
		/*System.out.println("Cooerct Hamming K1: " + corect_hamming_k1
				+ "  total: " + test_docTopicList.size());

		System.out.println("Cooerct Hamming k3: " + corect_hamming_k3
				+ "  total: " + test_docTopicList.size());
		System.out.println("Cooerct Hamming k5: " + corect_hamming_k5
				+ "  total: " + test_docTopicList.size());*/

		// System.out.println("Cooerct Euclidian: " + corect_euclid +
		// "  total: "
		// + test_docTopicList.size());

		// System.out.println("Cooerct cosine: " + corect_cosine + "  total: "
		// + test_docTopicList.size());
		
		writeInFile();

	}

	private void writeInFile() {

		try {

			

			File file = new File("output_1.txt");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			//writing results
			
			String str="Cooerct Hamming K1: " + corect_hamming_k1
					+ "  total: " + test_docTopicList.size();
			System.out.println(str);
			bw.write(str+"\n");
			
			str="Cooerct Hamming k3: " + corect_hamming_k3
			+ "  total: " + test_docTopicList.size();
			System.out.println(str);
			bw.write(str+"\n");
			
			str="Cooerct Hamming k5: " + corect_hamming_k5
			+ "  total: " + test_docTopicList.size();
			System.out.println(str);
			bw.write(str+"\n");
			

			 str="Cooerct Euclidian K1: " + corect_euclid_k1
					+ "  total: " + test_docTopicList.size();
			 System.out.println(str);
			 bw.write(str+"\n");
			
			str="Cooerct Euclidian k3: " + corect_euclid_k3
			+ "  total: " + test_docTopicList.size();
			System.out.println(str);
			bw.write(str+"\n");
			
			str="Cooerct Euclidian k5: " + corect_euclid_k5
			+ "  total: " + test_docTopicList.size();
			System.out.println(str);
			bw.write(str+"\n");
			
			

			str="Cooerct Cosine Similarity K1: " + corect_cosine_k1
					+ "  total: " + test_docTopicList.size();
			System.out.println(str);
			bw.write(str+"\n");
			
			str="Cooerct Cosine Similarity k3: " + corect_cosine_k3
			+ "  total: " + test_docTopicList.size();
			System.out.println(str);
			bw.write(str+"\n");
			
			str="Cooerct Cosine Similarity k5: " + corect_cosine_k5
			+ "  total: " + test_docTopicList.size();
			System.out.println(str);
			bw.write(str+"\n");
			
			
			
			
			bw.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double get2baseLog(double value) {
		if (value == 0)
			return 0;
		double logs = Math.log(value) / Math.log(2);
		// System.out.println("2base log"+ logs+" input: "+ value);
		return logs;
	}

	private void temp() {
		/*
		 * wordMap_train = train.getWordMap(); fetvectorList_train =
		 * train.getFetvectorList(); wordList_train = train.getWordList();
		 * docTopicList_train = train.getDocTopicList();
		 * 
		 * wordMap_test = test.getWordMap(); fetvectorList_test =
		 * test.getFetvectorList(); wordList_test = test.getWordList();
		 * docTopicList_test = test.getDocTopicList();
		 * 
		 * int size = docTopicList_test.size(); int train_size =
		 * docTopicList_train.size();
		 * 
		 * for (int docIndex = 0; docIndex < size; docIndex++) {
		 * 
		 * ArrayList<Distance> disList=new ArrayList<Distance>(); for (int t_doc
		 * = 0; t_doc < train_size; t_doc++) {
		 * 
		 * int words = wordList_test.size(); int dis = 0; for (int i = 0; i <
		 * words; i++) { int freq = fetvectorList_test.get(i).get(docIndex); if
		 * (freq > 0) { String w = wordList_test.get(i); if
		 * (wordMap_train.containsKey(w)) {
		 * 
		 * int location=wordMap_train.get(w); int
		 * occur=fetvectorList_train.get(location).get(t_doc); if(occur==0)
		 * dis++; } else { dis++; } } }
		 * 
		 * words = wordList_train.size(); for (int i = 0; i < words; i++) { int
		 * freq = fetvectorList_train.get(i).get(t_doc); if (freq > 0) { String
		 * w = wordList_train.get(i); if (wordMap_test.containsKey(w)) {
		 * 
		 * int location=wordMap_test.get(w); int
		 * occur=fetvectorList_test.get(location).get(docIndex); if(occur==0)
		 * dis++; } else { dis++; } } } // System.out.println("dstance: "+
		 * dis+"  doc: "+ docTopicList_train.get(t_doc)); Distance distance=new
		 * Distance(docTopicList_train.get(t_doc), dis); disList.add(distance);
		 * 
		 * } Collections.sort(disList,new DistanceComparator());
		 * 
		 * getClassType(disList,docTopicList_test.get(docIndex));
		 * 
		 * if(docIndex>10) break;
		 * 
		 * }
		 */

	}

	private void getClassTypeByHamming(ArrayList<Distance> disList, String test) {
		HashMap<String, Integer> maps = new HashMap<String, Integer>();
		ArrayList<String> list = new ArrayList<String>();

		if (disList.get(0).getTopic().equals(test))
			corect_hamming_k1++;

		for (int i = 0; i < 3; i++) {
			if (maps.containsKey(disList.get(i).getTopic())) {
				int val = maps.get(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), val + 1);
			} else {
				list.add(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), 0);
			}
		}
		int MAX = -1;
		String topic = "";
		for (int i = 0; i < list.size(); i++) {
			if (maps.get(list.get(i)) > MAX) {
				MAX = maps.get(list.get(i));
				topic = list.get(i);
			}

		}
		if (topic.equals(test))
			corect_hamming_k3++;

		for (int i = 3; i < 5; i++) {
			if (maps.containsKey(disList.get(i).getTopic())) {
				int val = maps.get(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), val + 1);
			} else {
				list.add(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), 0);
			}
		}
		MAX = -1;
		topic = "";
		for (int i = 0; i < list.size(); i++) {
			if (maps.get(list.get(i)) > MAX) {
				MAX = maps.get(list.get(i));
				topic = list.get(i);
			}

		}
		if (topic.equals(test))
			corect_hamming_k5++;
		// System.out.println("result :" + topic + "  output: " + test);
	}

	private void getClassTypeByEuclid(ArrayList<Distance> disList, String test) {
		HashMap<String, Integer> maps = new HashMap<String, Integer>();
		ArrayList<String> list = new ArrayList<String>();

		if (disList.get(0).getTopic().equals(test))
			corect_euclid_k1++;

		for (int i = 0; i < 3; i++) {
			if (maps.containsKey(disList.get(i).getTopic())) {
				int val = maps.get(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), val + 1);
			} else {
				list.add(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), 0);
			}
		}
		int MAX = -1;
		String topic = "";
		for (int i = 0; i < list.size(); i++) {
			if (maps.get(list.get(i)) > MAX) {
				MAX = maps.get(list.get(i));
				topic = list.get(i);
			}

		}
		if (topic.equals(test))
			corect_euclid_k3++;

		for (int i = 3; i < 5; i++) {
			if (maps.containsKey(disList.get(i).getTopic())) {
				int val = maps.get(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), val + 1);
			} else {
				list.add(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), 0);
			}
		}
		MAX = -1;
		topic = "";
		for (int i = 0; i < list.size(); i++) {
			if (maps.get(list.get(i)) > MAX) {
				MAX = maps.get(list.get(i));
				topic = list.get(i);
			}

		}

		if (topic.equals(test))
			corect_euclid_k5++;
		// System.out.println("result :" + topic + "  output: " + test);
	}

	private void getClassTypeByCosine(ArrayList<Distance> disList, String test) {
		HashMap<String, Integer> maps = new HashMap<String, Integer>();
		ArrayList<String> list = new ArrayList<String>();

		if (disList.get(0).getTopic().equals(test))
			corect_cosine_k1++;
		for (int i = 0; i < 3; i++) {
			if (maps.containsKey(disList.get(i).getTopic())) {
				int val = maps.get(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), val + 1);
			} else {
				list.add(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), 0);
			}
		}
		int MAX = -1;
		String topic = "";
		for (int i = 0; i < list.size(); i++) {
			if (maps.get(list.get(i)) > MAX) {
				MAX = maps.get(list.get(i));
				topic = list.get(i);
			}

		}
		if (topic.equals(test))
			corect_cosine_k3++;

		for (int i = 3; i < 5; i++) {
			if (maps.containsKey(disList.get(i).getTopic())) {
				int val = maps.get(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), val + 1);
			} else {
				list.add(disList.get(i).getTopic());
				maps.put(disList.get(i).getTopic(), 0);
			}
		}
		MAX = -1;
		topic = "";
		for (int i = 0; i < list.size(); i++) {
			if (maps.get(list.get(i)) > MAX) {
				MAX = maps.get(list.get(i));
				topic = list.get(i);
			}

		}
		if (topic.equals(test))
			corect_cosine_k5++;

		// System.out.println("result :" + topic + "  output: " + test);
	}

}
