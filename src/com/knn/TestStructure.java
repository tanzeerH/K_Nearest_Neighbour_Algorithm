package com.knn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TestStructure {

	private int TOTAL_DOC = 0;
	/*private HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
	private ArrayList<ArrayList<Integer>> fetvectorList = new ArrayList<ArrayList<Integer>>();
	private ArrayList<String> wordList = new ArrayList<String>();
	private ArrayList<String> docTopicList=new ArrayList<String>();*/
	
	private ArrayList<HashMap<String, Integer>> hashList=new ArrayList<HashMap<String,Integer>>();
	private ArrayList<ArrayList<String>>  docWordList=new ArrayList<ArrayList<String>>();
	private ArrayList<String> docTopicList = new ArrayList<String>();


	public TestStructure() {
		System.out.println("test:\n\n");
		readFileForCount();
		
		// createArraListsWithInitialization();
		readFileForParsing();
		
	}

	private void readFileForCount() {
		int prevNewLine = 0;
		int count = 0;
		BufferedReader br = null;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("test.data"));

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.length() == 0) {
					prevNewLine++;
				} else {
					if (prevNewLine >= 2) {
						TOTAL_DOC++;
						docTopicList.add(sCurrentLine);
						// System.out.println("" + count);
					}
					prevNewLine = 0;
				}
				if (count == 0)
				{
					docTopicList.add(sCurrentLine);
					TOTAL_DOC++;
				}
				// System.out.println(sCurrentLine);
				count++;

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("TOTAL DOC " + TOTAL_DOC + " DOC topics size: "+ docTopicList.size());
		

	}
	
	private ArrayList<Integer> getArraListsWithInitialization() {
		ArrayList<Integer> list = new ArrayList<Integer>(Collections.nCopies(
				TOTAL_DOC, 0));

		// for(int i=0;i<TOTAL_DOC;i++)
		// System.out.println(""+i+"  "+list.get(i));

		return list;

	}

	private void readFileForParsing() {
		int prevNewLine = 0;
		int count = 0;
		// boolean isNewDoc=false;
		int docIndex = -1;
		
		BufferedReader br = null;
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("test.data"));

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.length() == 0) {
					prevNewLine++;
				} else {
					if (prevNewLine >= 2) {
						docIndex++;
						HashMap<String,Integer> hash=new HashMap<String, Integer>();
						ArrayList<String> list=new ArrayList<String>();
						docWordList.add(list);
						hashList.add(hash);
						// System.out.println("" + count);
						// isNewDoc=true;
					}
					prevNewLine = 0;
				}
				if (count == 0) {
					docIndex = 0;
					HashMap<String,Integer> hash=new HashMap<String, Integer>();
					hashList.add(hash);
					ArrayList<String> list=new ArrayList<String>();
					docWordList.add(list);
					// isNewDoc=true;
				}
				// System.out.println(sCurrentLine);
				count++;

				//if (count > 200)
				//	break;

				if (sCurrentLine.length() != 0) {
					sCurrentLine = sCurrentLine.replaceAll(". ", " ")
							.replaceAll(",", "").replaceAll("\"", "")
							.toLowerCase();
					// System.out.println(sCurrentLine);
					String[] values = sCurrentLine.split("\\s+");

					for (int i = 0; i < values.length; i++) {
						HashMap<String,Integer> hash=hashList.get(docIndex);
						if(hash.containsKey(values[i]))
						{
							int val=hash.get(values[i]);
						     hash.put(values[i],val+1);
						}
						else
						{
							hash.put(values[i],1);
							ArrayList<String> list=docWordList.get(docIndex);
							list.add(values[i]);
						}
						/*if (wordMap.containsKey(values[i])) {
							int wordIndex = wordMap.get(values[i]);
							ArrayList<Integer> vList = fetvectorList
									.get(wordIndex);
							vList.set(docIndex, vList.get(docIndex) + 1);
						} else {
							int size = wordMap.size();
							wordMap.put(values[i], size);
							wordList.add(values[i]);
							ArrayList<Integer> vList = getArraListsWithInitialization();
							vList.set(docIndex, vList.get(docIndex) + 1);
							fetvectorList.add(vList);
						}*/
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Doc Index:"+docIndex);
	/*	System.out.println("Word Map size: " + wordMap.size()
				+ " wordList size: " + wordList.size() + " fetList size: "
				+ fetvectorList.size());*/
		
		System.out.println("DocWordList Size: "+ docWordList.size());

	}

	public int getTOTAL_DOC() {
		return TOTAL_DOC;
	}

	public void setTOTAL_DOC(int tOTAL_DOC) {
		TOTAL_DOC = tOTAL_DOC;
	}

	public ArrayList<HashMap<String, Integer>> getHashList() {
		return hashList;
	}

	public void setHashList(ArrayList<HashMap<String, Integer>> hashList) {
		this.hashList = hashList;
	}

	public ArrayList<ArrayList<String>> getDocWordList() {
		return docWordList;
	}

	public void setDocWordList(ArrayList<ArrayList<String>> docWordList) {
		this.docWordList = docWordList;
	}

	public ArrayList<String> getDocTopicList() {
		return docTopicList;
	}

	public void setDocTopicList(ArrayList<String> docTopicList) {
		this.docTopicList = docTopicList;
	}

	/*public HashMap<String, Integer> getWordMap() {
		return wordMap;
	}

	public void setWordMap(HashMap<String, Integer> wordMap) {
		this.wordMap = wordMap;
	}

	public ArrayList<ArrayList<Integer>> getFetvectorList() {
		return fetvectorList;
	}

	public void setFetvectorList(ArrayList<ArrayList<Integer>> fetvectorList) {
		this.fetvectorList = fetvectorList;
	}

	public ArrayList<String> getWordList() {
		return wordList;
	}

	public void setWordList(ArrayList<String> wordList) {
		this.wordList = wordList;
	}

	public ArrayList<String> getDocTopicList() {
		return docTopicList;
	}

	public void setDocTopicList(ArrayList<String> docTopicList) {
		this.docTopicList = docTopicList;
	}*/
	


	
}
