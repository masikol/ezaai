package leb.util.common;


// A DTO class, so the fields are public
public class ParallelPairwiseAAIInDTO {

	public int i, j;
	public String label1, label2;
	public String faa1, faa2;

	public ParallelPairwiseAAIInDTO(int i,
									int j,
									String label1,
									String label2,
									String faa1,
									String faa2) {
		this.i = i;
		this.j = j;
		this.label1 = label1;
		this.label2 = label2;
		this.faa1 = faa1;
		this.faa2 = faa2;
	}
}
