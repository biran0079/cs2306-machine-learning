import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Sourcable;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.WeightedInstancesHandler;

public class PairwiseJ48 extends Classifier implements
		WeightedInstancesHandler, Sourcable {

	J48[][] dt;

	@Override
	public double classifyInstance(Instance ins) throws Exception {
		int numClass=ins.numClasses();
		int[] ct = new int[numClass];
		Random rand = new Random();

		for (int i = 0; i < numClass; i++)
			ct[i] = 0;
		int label = 0;
		for (int i = 0; i < numClass; i++)
			for (int j = i + 1; j < numClass; j++) {
				try {
					label = (int) dt[i][j].classifyInstance(ins);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ct[label]++;
			}
		int max_vote = -1;
		double[] candidates = new double[numClass];
		int top = 0;
		for (int i = 0; i < numClass; i++) {
			if (ct[i] > max_vote) {
				top = 0;
				max_vote = ct[i];
				candidates[top++] = i;
			} else if (ct[i] == max_vote) {
				candidates[top++] = i;
			}
		}
		return candidates[Math.abs(rand.nextInt()) % top];
	}

	@Override
	public void buildClassifier(Instances data) throws Exception {

		int numClass=data.numClasses();
		Instances[][] trainSet = new Instances[numClass][numClass];
		dt = new J48[numClass][numClass];
		//String[] option = { "-C 1", "-M 20" };
		for (int i = 0; i < numClass; i++)
			for (int j = i + 1; j < numClass; j++) {
				trainSet[i][j] = new Instances(data, 1);
				dt[i][j] = new J48();
				try {
					//dt[i][j].setOptions(option);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		for (int i = 0; i < numClass; i++)
			for (int j = 0; j < i; j++) {
				trainSet[i][j] = trainSet[j][i];
				dt[i][j] = dt[j][i];
			}
		for (int idx = 0; idx < data.numInstances(); idx++) {
			Instance ins = data.instance(idx);
			int i = (int) ins.classValue();
			for (int j = 0; j < numClass; j++) {
				if (i == j)
					continue;
				trainSet[i][j].add(ins);
			}
		}
		for (int i = 0; i < numClass; i++)
			for (int j = i + 1; j < numClass; j++) {
				try {
					dt[i][j].buildClassifier(trainSet[i][j]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}



	public double classifyDataset(Instances data) {

		int numClass=data.numClasses();
		int[] ct = new int[numClass];
		int ac = 0, res;
		Random rand = new Random();
		for (int idx = 0; idx < data.numInstances(); idx++) {
			Instance ins = data.instance(idx);
			for (int i = 0; i < numClass; i++)
				ct[i] = 0;
			int label = 0;
			for (int i = 0; i < numClass; i++)
				for (int j = i + 1; j < numClass; j++) {
					try {
						label = (int) dt[i][j].classifyInstance(ins);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ct[label]++;
				}
			int max_vote = -1;
			int[] candidates = new int[numClass];
			int top = 0;
			for (int i = 0; i < 10; i++) {
				if (ct[i] > max_vote) {
					top = 0;
					max_vote = ct[i];
					candidates[top++] = i;
				} else if (ct[i] == max_vote) {
					candidates[top++] = i;
				}
			}
			res = candidates[Math.abs(rand.nextInt()) % top];
			if (res == (int) ins.classValue()) {
				ac++;
			}
		}
		return (1.0 * ac / data.numInstances());
	}
	
	
	@Override
	public String toSource(String className) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
