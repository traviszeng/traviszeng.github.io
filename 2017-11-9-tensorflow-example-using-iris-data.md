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

### 下载并加载数据集 ###

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


###构建一个DNN分类器 ###

tf.contrib.learn提供了一系列预定义的模型，叫做Estimators。通过Estimator，可以帮助我们很方便地对数据进行训练和评估，在这里，我们配置一个深层神经网络分类器模型来适配IRIS数据，通过使用tf.contrib.learn，可以使用一行代码就帮助我们实例化一个tf.contrib.learn.DNNClassifier.

首先我们要定义模型的特征列，如下代码所示：

	feature_columns = [tf.contrib.layers.real_valued_column("", dimension=4)]

它制定了数据集中特征的数据类型，所有的特征数据都是连续的，因此tf.contrib.layers.real_valued_column适用于构造特征列的适当函数。数据集中有四个特征（萼片宽度，萼片高度，花瓣宽度和花瓣高度），因此相应的尺寸必须设置为4以保存所有数据。

然后，代码使用以下参数创建DNNClassifier模型：

feature_columns=feature_columns。上面定义的一组特征

hidden_units=[10, 20, 10]。三个隐藏层分别包含10，20，10个神经元。

n_classes=3。三个目标类，代表三个鸢尾物种。

model_dir=/tmp/iris_model。TensorFlow在模型训练期间将保存检查点数据的目录。有关使用TensorFlow进行日志记录和监视的更多信息，请见使用tf.contrib.learn记录和监视的基本知识。

###使用定义好的分类器用于IRIS训练集训练###

现在，你已经配置好了你的DNNclassifier模型，你可以使用fit方法来将Iris训练数据应用到分类器上。将特征数据（training_set.data），目标值（training_set.target）和要训练的步数（这里是2000）作为参数传递：

	classifier.fit(x=training_set.data, y=training_set.target, steps=2000)

模型的状态保存在classifier(分类器)中，这意味着如果你喜欢，你可以迭代地训练。

运行结果如下图：

![](https://i.imgur.com/pcC8RfZ.png)

可以看到，最后一轮迭代得到的loss为0.0252。这时候其实已经得到一个较好的训练模型了。

###评估训练效果###

现在已经将Iris的训练数据适配到了DNNClassifier模型上；现在，可以使用evaluate方法在Iris测试数据上检查其准确性。像fit（拟合）一样，evaluate（评估操作）将特征数据和目标值作为参数，并返回带有评估结果的dict（字典）。以下代码通过了Iris测试数据-test_set.data和test_set.target来评估和打印结果的准确性：

	accuracy_score = classifier.evaluate(x=test_set.data, y=test_set.target)["accuracy"]
	print('Accuracy: {0:f}'.format(accuracy_score))

可以得到结果的准确度大概在97%左右，当然这个结果可能在不同机器上有差异。

###分类新样本###

当我们有一个新的样本的时候，我们可以使用predict()方法来分类一个新的样本。

可以使用如下代码对他们的物种进行预测：

	new_samples = np.array(
    	[[6.4, 3.2, 4.5, 1.5], [5.8, 3.1, 5.0, 1.7]], dtype=float)
	prediction= list(classifier.predict(new_samples, as_iterable=True))
	print('Predictions: {}'.format(str(prediction)))

predict()方法返回了一个预测数组，每个样本对应其中的一个结果：

![](https://i.imgur.com/AfJHt6l.png)



##Conclusion##

这样，利用IRIS数据我们完整的执行了一次深度神经网络的构建和训练，可以看到，使用Tensorflow可以高效的完成这一工作，TF大法好。