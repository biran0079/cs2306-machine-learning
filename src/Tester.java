import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;


public class Tester {
	static double classifyDataset(final Classifier ada, final Instances data) {
		int ac=0;
		Instance ins;
		for(int i=0;i<data.numInstances();i++){
			ins=data.instance(i);
			try {
				if(ins.classValue()==ada.classifyInstance(ins))
					ac++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 1.0*ac/data.numInstances();
	}
}
