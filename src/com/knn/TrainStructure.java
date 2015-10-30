package com.knn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class TrainStructure {

	private int TOTAL_DOC = 0;
	private HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
	private ArrayList<ArrayList<Integer>> fetvectorList = new ArrayList<ArrayList<Integer>>();
	private ArrayList<String> wordList = new ArrayList<String>();
	private ArrayList<String> docTopicList=new ArrayList<String>();
	
	
	public int getTOTAL_DOC() {
		return TOTAL_DOC;
	}

	public void setTOTAL_DOC(int tOTAL_DOC) {
		TOTAL_DOC = tOTAL_DOC;
	}

	public HashMap<String, Integer> getWordMap() {
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
	}

	public TrainStructure() {
		System.out.println("Train:\n\n");
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

			br = new BufferedReader(new FileReader("training.data"));

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

			br = new BufferedReader(new FileReader("training.data"));

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.length() == 0) {
					prevNewLine++;
				} else {
					if (prevNewLine >= 2) {
						docIndex++;
						// System.out.println("" + count);
						// isNewDoc=true;
					}
					prevNewLine = 0;
				}
				if (count == 0) {
					docIndex = 0;
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
						if (wordMap.containsKey(values[i])) {
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
						}
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
		System.out.println("Word Map size: " + wordMap.size()
				+ " wordList size: " + wordList.size() + " fetList size: "
				+ fetvectorList.size());

	}
	


	
}
