package com.knn;

import java.util.Comparator;

public class DistanceComparatorByEuclidian implements Comparator<Distance>{

	@Override
	public int compare(Distance o1, Distance o2) {
		if(o1.getDistance_euclidian()>o2.getDistance_euclidian())
			return 1;
		else
			if(o1.getDistance_euclidian()<o2.getDistance_euclidian())
				return -1;
		
		 return 0;
	}

}
