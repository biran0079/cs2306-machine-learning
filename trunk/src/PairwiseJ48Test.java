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

public class PairwiseJ48Test {

	public static void corssValidation(DataSource source, int foldNum)
			throws Exception {
		Instances data = source.getDataSet(), train = new Instances(data), test = new Instances(
				data);

		data.setClassIndex(data.numAttributes() - 1);
		train.setClassIndex(data.numAttributes() - 1);
		test.setClassIndex(data.numAttributes() - 1);

		Instance[] inss = new Instance[data.numInstances()];
		for (int i = 0; i < inss.length; i++) {
			inss[i] = data.instance(i);
		}
		double res = 0.0, t;
		for (int i = 0; i < foldNum; i++) {
			DatasetSpliter.cross_split(inss, foldNum, i, train, test);
			PairwiseJ48 dt = new PairwiseJ48();
			dt.buildClassifier(train);
			t = Tester.classifyDataset(dt, test);
			System.out.println(t);
			res += t;
		}
		System.out.println("error rate:");
		System.out.println(1.0 - res / foldNum);

	}

	public static void trainAndTest(DataSource trainSource,
			DataSource testSource) throws Exception {
		Instances train = trainSource.getDataSet(), test = testSource
				.getDataSet();
		train.setClassIndex(train.numAttributes() - 1);
		test.setClassIndex(test.numAttributes() - 1);
		PairwiseJ48 dt = new PairwiseJ48();
		dt.buildClassifier(train);
		int ac = 0, insNum = test.numInstances();
		Instance ins;
		for (int i = 0; i < insNum; i++) {
			ins = test.instance(i);
			if (dt.classifyInstance(ins) == ins.classValue())
				ac++;
		}
		System.out.println("error rate:");
		System.out.println((insNum - ac) * 1.0 / insNum);
	}

	public static void main(String[] args) throws Exception {
		DataSource train = new DataSource("C:/users/biran/desktop/MNIST_N_A.arff"), test = new DataSource(
				"C:/users/biran/desktop/MNIST_t10k_N_A.arff");
		trainAndTest(train, test);

	}
}
