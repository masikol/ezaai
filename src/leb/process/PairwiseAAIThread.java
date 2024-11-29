package leb.process;


import leb.util.common.Prompt;
import leb.process.ProcCalcPairwiseAAI;
import leb.util.common.ParallelPairwiseAAIInDTO;
import leb.util.common.ParallelPairwiseAAIResWrap;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;



public class PairwiseAAIThread implements Callable<List<ParallelPairwiseAAIResWrap>> {

	private int workerId;
	private String path_mmseqs;
	private List<ParallelPairwiseAAIInDTO> inputDataList;
	private AtomicInteger atomicJobCounter;
	private int sz;

	private String globaltmp = "/tmp/ezaai";
	private int mode = ProcCalcPairwiseAAI.MODE_MMSEQS;
	private int nthread = 1;
	private double identity = 40.0, coverage = 0.5;
	private BufferedWriter maw = null;


	public PairwiseAAIThread(int workerId,
							 String path_mmseqs,
							 AtomicInteger atomicJobCounter,
							 int sz) {
		this.workerId = workerId;
		this.path_mmseqs = path_mmseqs;
		this.inputDataList = new ArrayList<>();
		this.atomicJobCounter = atomicJobCounter;
		this.sz = sz;
	}


	public void setGlobaltmp(String globaltmp) {this.globaltmp = globaltmp;}
	public void setMode(int mode) {this.mode = mode;}
	public void setNthread(int nthread) {this.nthread = nthread;}
	public void setIdentity(double identity) {
		this.identity = identity;
	}
	public void setCoverage(double coverage) {
		this.coverage = coverage;
	}
	public void setMatchout(BufferedWriter maw) {
		this.maw = maw;
	}

	public void addInputData(ParallelPairwiseAAIInDTO inData) {
		this.inputDataList.add(inData);
	}


	public List<ParallelPairwiseAAIResWrap> call() {

		ProcCalcPairwiseAAI procAAI = new ProcCalcPairwiseAAI(workerId);

		procAAI.setPath(path_mmseqs);
		procAAI.setMode(ProcCalcPairwiseAAI.MODE_MMSEQS);
		procAAI.setGlobaltmp(globaltmp);
		procAAI.setNthread(nthread);
		procAAI.setIdentity(identity);
		procAAI.setCoverage(coverage);
		if (maw != null) procAAI.setMatchout(maw);

		List<String> res;
		List<ParallelPairwiseAAIResWrap> outDataList = new ArrayList<>();
		for (ParallelPairwiseAAIInDTO inputData : inputDataList) {
			Prompt.talk(
				String.format(
					"Calculating AAI... [Worker #%d, starting task %d/%d, i=%d,j=%d]",
					workerId,
					atomicJobCounter.get(),
					sz,
					inputData.i,
					inputData.j
				)
			);
			try {
				res = procAAI.calculateProteomePairWithDetails(
					inputData.label1,
					inputData.label2,
					inputData.faa1,
					inputData.faa2
				);
			} catch (IOException e) {
				e.printStackTrace();
				return outDataList;
			}
			
			outDataList.add(
				new ParallelPairwiseAAIResWrap(inputData.i, inputData.j, res)
			);
			Prompt.print(
				String.format(
					"Task %d/%d done [Worker #%d]",
					atomicJobCounter.incrementAndGet(),
					sz,
					workerId
				)
			);
		}

		return outDataList;
	}
}