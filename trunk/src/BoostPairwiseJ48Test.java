import weka.classifiers.meta.AdaBoostM1;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class BoostPairwiseJ48Test {

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

		AdaBoostM1[] ada = new AdaBoostM1[foldNum];
		for (int i = 0; i < foldNum; i++) {
			DatasetSpliter.cross_split(inss, foldNum, i, train, test);
			ada[i] = new AdaBoostM1();
			String[] options = { "-I", "10", "-W", "PairwiseJ48" };
			ada[i].setOptions(options);
			/*
			 * once option is set, the "options" variable is cleared..... One
			 * "options" variable can only use once
			 */
			ada[i].buildClassifier(train);
			t = Tester.classifyDataset(ada[i], test);
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
		AdaBoostM1 ada = new AdaBoostM1();
		String[] options = { "-I", "32", "-W", "PairwiseJ48" };
		ada.setOptions(options);
		ada.buildClassifier(train);
		int ac = 0, insNum = test.numInstances();
		Instance ins;
		for (int i = 0; i < insNum; i++) {
			ins = test.instance(i);
			if (ada.classifyInstance(ins) == ins.classValue())
				ac++;
		}
		System.out.println("error rate:");
		System.out.println((insNum - ac) * 1.0 / insNum);
	}

	public static void main(String[] args) throws Exception {
		DataSource train = new DataSource(
		"C:/users/biran/desktop/MNIST_N_A.arff"),test= new DataSource(
		"C:/users/biran/desktop/MNIST_t10k_N_A.arff");
		trainAndTest(train,test);
	}

}
