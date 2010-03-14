import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.experiment.CrossValidationResultProducer;

public class J48Test {

	static J48[] ovaTrain(Instances data) {
		Instances[] trainSet = new Instances[10];
		J48[] dt = new J48[10];
		String[] option = { "-C 1", "-M 2" };
		for (int i = 0; i < 10; i++) {
			trainSet[i] = new Instances(data, 1);
			dt[i] = new J48();
			try {
				dt[i].setOptions(option);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int idx = 0; idx < data.numInstances(); idx++) {
			Instance ins = data.instance(idx), cp;
			for (int i = 0; i < 10; i++) {
				cp = (Instance) ins.copy();
				if (i == ins.classValue()) {
					cp.setClassValue(1);
				} else {
					cp.setClassValue(0);
				}
				trainSet[i].add(cp);
			}
		}
		for (int i = 0; i < 10; i++) {
			try {
				dt[i].buildClassifier(trainSet[i]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dt;
	}

	static J48[][] ovoTrain(Instances data) {
		Instances[][] trainSet = new Instances[10][10];
		J48[][] dt = new J48[10][10];
		String[] option = { "-C 1", "-M 2" };
		for (int i = 0; i < 10; i++)
			for (int j = i + 1; j < 10; j++) {
				trainSet[i][j] = new Instances(data, 1);
				dt[i][j] = new J48();
				try {
					dt[i][j].setOptions(option);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		for (int i = 0; i < 10; i++)
			for (int j = 0; j < i; j++) {
				trainSet[i][j] = trainSet[j][i];
				dt[i][j] = dt[j][i];
			}
		for (int idx = 0; idx < data.numInstances(); idx++) {
			Instance ins = data.instance(idx);
			int i = (int) ins.classValue();
			for (int j = 0; j < 10; j++) {
				if (i == j)
					continue;
				trainSet[i][j].add(ins);
			}
		}
		for (int i = 0; i < 10; i++)
			for (int j = i + 1; j < 10; j++) {
				try {
					dt[i][j].buildClassifier(trainSet[i][j]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		return dt;
	}

	static double ovaTest(J48[] dt, Instances data) {
		int[] ct = new int[10];
		int ac = 0;
		Random rand = new Random();
		int[] candidates = new int[10];
		int top, res;
		for (int idx = 0; idx < data.numInstances(); idx++) {
			Instance ins = data.instance(idx);
			top = 0;
			for (int i = 0; i < 10; i++) {
				try {
					if (dt[i].classifyInstance(ins) == 1.0) {
						candidates[top++] = i;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (top == 0) {
				res = Math.abs(rand.nextInt()) % 10;
			} else {
				res = candidates[Math.abs(rand.nextInt()) % top];
			}
			if (res == ins.classValue()) {
				ac++;
			}
		}
		return (1.0 * ac / data.numInstances());
	}

	static double ovoTest(J48[][] dt, Instances data) {
		int[] ct = new int[10];
		int ac = 0, res;
		Random rand = new Random();
		for (int idx = 0; idx < data.numInstances(); idx++) {
			Instance ins = data.instance(idx);
			for (int i = 0; i < 10; i++)
				ct[i] = 0;
			int label = 0;
			for (int i = 0; i < 10; i++)
				for (int j = i + 1; j < 10; j++) {
					try {
						label = (int) dt[i][j].classifyInstance(ins);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ct[label]++;
				}
			int max_vote = -1;
			int[] candidates = new int[10];
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
		System.out.println((1.0 * ac / data.numInstances()));
		return (1.0 * ac / data.numInstances());
	}

	static void cross_split(Instance[] inss, int foldNum, int testIdx,
			Instances train, Instances test) {
		train.delete();
		test.delete();
		int N = inss.length, st = N / foldNum * testIdx, ed = N / foldNum
				* (testIdx + 1);
		for (int i = 0; i < st; i++) {
			train.add(inss[i]);
		}
		for (int i = ed; i < N; i++) {
			train.add(inss[i]);
		}
		for (int i = st; i < ed; i++) {
			test.add(inss[i]);
		}
	}

	public static void main(String[] args) throws Exception {
		DataSource source = new DataSource(
				"C:/users/biran/desktop/Semeion_raw.arff");
		Instances data = source.getDataSet(), train = new Instances(data), test = new Instances(
				data);
		data.setClassIndex(data.numAttributes() - 1);
		train.setClassIndex(data.numAttributes() - 1);
		test.setClassIndex(data.numAttributes() - 1);

		Instance[] inss = new Instance[data.numInstances()];
		for (int i = 0; i < inss.length; i++) {
			inss[i] = data.instance(i);
		}
		double res = 0.0;
		int foldNum = 10;
		for (int i = 0; i < foldNum; i++) {
			cross_split(inss, foldNum, i, train, test);
			J48[][] dt = ovoTrain(train);
			res += ovoTest(dt, test);
		}
		System.out.println("error rate:");
		System.out.println(1.0 - res / foldNum);

	}
}
