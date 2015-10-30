package com.knn;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Distance>{

	@Override
	public int compare(Distance o1, Distance o2) {
		if(o1.getDistace()>o2.getDistace())
			return 1;
		else
			if(o1.getDistace()<o2.getDistace())
				return -1;
		
		 return 0;
	}

}
