本文主要以Iris数据集为例子讲解如何训练一个简单的Deep Neural Network。
## 环境配置 ##

python 3.5.4

TensorFlow 1.4

## 完整源码 ##

	import os
	import urllib

	import numpy as np
	import tensorflow as tf

	# Data sets
	IRIS_TRAINING = "iris_training.csv"
	IRIS_TRAINING_URL = "http://download.tensorflow.org/data/iris_training.csv"

	IRIS_TEST = "iris_test.csv"
	IRIS_TEST_URL = "http://download.tensorflow.org/data/iris_test.csv"

	def main():
  		# If the training and test sets aren't stored locally, download them.
  		if not os.path.exists(IRIS_TRAINING):
   			 raw = urllib.request.urlopen(IRIS_TRAINING_URL).read().decode()
    	with open(IRIS_TRAINING, "w") as f:
      		f.write(raw)
  		if not os.path.exists(IRIS_TEST):
    		raw = urllib.request.urlopen(IRIS_TEST_URL).read().decode()
    	with open(IRIS_TEST, "w") as f:
      		f.write(raw)
  		# Load datasets.
  		training_set = tf.contrib.learn.datasets.base.load_csv_with_header(
      		filename=IRIS_TRAINING,
      		target_dtype=np.int,
      		features_dtype=np.float32)
  		test_set = tf.contrib.learn.datasets.base.load_csv_with_header(
      		filename=IRIS_TEST,
      		target_dtype=np.int,
      		features_dtype=np.float32)
  		# Specify that all features have real-value data
  		feature_columns = [tf.contrib.layers.real_valued_column("", dimension=4)]
  		# Build 3 layer DNN with 10, 20, 10 units respectively.
  		classifier = tf.contrib.learn.DNNClassifier(feature_columns=feature_columns,
                                              hidden_units=[10, 20, 10],
                                              n_classes=3,
                                              model_dir="/tmp/iris_model")
  		# Define the training inputs
  		def get_train_inputs():
    		x = tf.constant(training_set.data)
    		y = tf.constant(training_set.target)

    		return x, y
  		# Fit model.
  		classifier.fit(input_fn=get_train_inputs, steps=2000)
  		# Define the test inputs
 		 def get_test_inputs():
   			 x = tf.constant(test_set.data)
   			 y = tf.constant(test_set.target)
    		return x, y
  		# Evaluate accuracy.
  		accuracy_score = classifier.evaluate(input_fn=get_test_inputs,
                                       steps=1)["accuracy"]
  		print("\nTest Accuracy: {0:f}\n".format(accuracy_score))
  		# Classify two new flower samples.
  		def new_samples():
    		return np.array(
      		[[6.4, 3.2, 4.5, 1.5],
       		[5.8, 3.1, 5.0, 1.7]], dtype=np.float32)
  		predictions = list(classifier.predict(input_fn=new_samples))
  		print(
      		"New Samples, Class Predictions:    {}\n"
      		.format(predictions))
	if __name__ == "__main__":
    	main()

## 分步讲解 ##

Iris数据集是一个包含150个样本的数据集，主要是用于区分花的种类，其结构如下所示：

![](https://i.imgur.com/qNpt0Oa.png)

如图所示，花的种类有三种，用0,1,2表示，对应的特征有4个。在本例中，将150个样本划分为训练集（120个样本）和测试集（30个样本）。

首先第一次使用的时候，需要先从tensorflow上下载对应的训练集数据和测试集数据。

	import os
	import urllib

	import numpy as np
	import tensorflow as tf

	# Data sets
	IRIS_TRAINING = "iris_training.csv"
	IRIS_TRAINING_URL = "http://download.tensorflow.org/data/iris_training.csv"

	IRIS_TEST = "iris_test.csv"
	IRIS_TEST_URL = "http://download.tensorflow.org/data/iris_test.csv"

	
	#如果本地没有该数据集则从线上下载下来	
	if not os.path.exists(IRIS_TRAINING):
   		 raw = urllib.request.urlopen(IRIS_TRAINING_URL).read().decode()
    with open(IRIS_TRAINING, "w") as f:
    	f.write(raw)
  	if not os.path.exists(IRIS_TEST):
   		raw = urllib.request.urlopen(IRIS_TEST_URL).read().decode()
   	with open(IRIS_TEST, "w") as f:
    		f.write(raw)

接下来，使用learn.datasets.base中的load_csv_with_header()方法将训练和测试集装入数据集。load_csv_with_header()方法需要三个必需的参数：

1.filename，CSV文件的路径

2.target_dtype，接受数据集的目标值的numpy数据类型。

3.features_dtype，接受数据集的特征值的numpy数据类型。

在这里，target（你训练模型预测的值）是花种，它是一个从0-2的整数，所以对应的适当的numpy数据类型是np.int：

	training_set = tf.contrib.learn.datasets.base.load_csv_with_header(
    	filename=IRIS_TRAINING,
    	target_dtype=np.int,
    	features_dtype=np.float32)
	test_set = tf.contrib.learn.datasets.base.load_csv_with_header(
    	filename=IRIS_TEST,
    	target_dtype=np.int,
    	features_dtype=np.float32)

tf.contrib.learn中的Dataset是命名元组；您可以通过data和target字段访问特征数据和目标值。这里training_set.data和training_set.target分别包含训练集的特征数据和目标值；test_set.data和test_set.target分别包含测试集的特征数据和目标值。