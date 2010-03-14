import weka.core.Instance;
import weka.core.Instances;

public class DatasetSpliter {
	
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
}
