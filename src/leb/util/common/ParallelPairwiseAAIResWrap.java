package leb.util.common;

import java.util.List;


// A DTO class, so the fields are public
public class ParallelPairwiseAAIResWrap {
	public int i, j;
	public List<String> res;

	public ParallelPairwiseAAIResWrap(int i, int j, List<String> res) {
		this.i = i;
		this.j = j;
		this.res = res;
	}
}
