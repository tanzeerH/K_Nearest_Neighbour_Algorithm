package com.knn;

import java.util.Comparator;

public class DistanceComparatorByCosine implements Comparator<Distance>{

	@Override
	public int compare(Distance o1, Distance o2) {
		if(o1.getCosine_similarity()<o2.getCosine_similarity())
			return 1;
		else
			if(o1.getCosine_similarity()>o2.getCosine_similarity())
				return -1;
		
		 return 0;
	}

}
