package com.knn;

import java.util.Comparator;

public class DistanceComparatorByHamming implements Comparator<Distance>{

	@Override
	public int compare(Distance o1, Distance o2) {
		if(o1.getDistace_hamming()>o2.getDistace_hamming())
			return 1;
		else
			if(o1.getDistace_hamming()<o2.getDistace_hamming())
				return -1;
		
		 return 0;
	}

}
