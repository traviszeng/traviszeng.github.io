---
layout: default
---

# TensorFlow 初印象 #

在人工智能如此大热的时候，Google选择将TensorFlow开源可谓是明智之举。TensorFlow一方面可以通过开源使得更多的开发者参与完善这个框架，另一方面也可以降低人工智能深度学习、机器学习的门槛，这对Google和使用者来说都是一个双赢的局面。

- TensorFlow既是一个实现机器学习算法的接口，同时也是执行机器学习算法的框架。
- TensorFlow使用数据流式图来规划计算流程，可以将计算映射到不同的硬件和操作系统平台。

----------


## 核心概念 ##


- TensorFlow中的计算表示为一个有向图(directed graph)或者计算图(computation graph)，每一个操作运行(operation)作为一个节点(node)，节点与节点之间的连接成为边(edge)。
- 计算图描述了数据的计算流程，也负责维护和更新状态，用户可以对计算图的分支进行条件控制或循环操作。每一个节点都描述了一种运算操作，可以有任意多的输入和输出，节点可以算是操作运算的实例化结果。
- 在计算图的边中流动(flow)的数据成为张量(tensor)，因此这个框架称之为TensorFlow。
- TensorFlow为训练和预测的共同部分提供了一个恰当的抽象。
- TensorFlow支持分布式运算和单机运算。
- 在使用梯度下降(gradient descent)等机器学习的时候，TensorFlow可以帮助我们自动求解梯度(gradient)，不用我们手动实现BP算法(Backpropagation)。
- TensorFlow通过数据并行、模型并行、流水线并行的并行计算模式来对神经网络的训练进行加速。

## 用一个例子说明 ##

<div align = "center" width = "300" height = "300">

<img src="http://i.imgur.com/nfzc7sb.jpg" width = "300" height = "300" alt="图片名称" align=center />

</div>

以上图的运算流程为例，一个完整的tensorflow运算流程如下：

    import tensorflow as tf
	b = tf.Variable(tf.zeros([100]))	#生成100维的向量，初始化为0
	w = tf.Variable(tf.random_uniform([784,100],-1,1))	#生成784*100的随机矩阵w
	x = tf.placeholder(name = 'x')	#输入的Placeholder
	relu = tf.nn.relu(tf.matmul(w,x)+b)	#ReLU(wx+b)
	C=[...]		#根据ReLU函数的结果计算Cost
	s=tf.Session()
	for step in range(0,100):
		input = ...construct 100-D input array... #为输入创建一个100维的向量
		result = s.run(C,feed_dict={x:input})	#获取Cost，供给输入x
		print(step,result)

其中session是用户使用TensorFlow使得交互接口，用户可以通过Session的Extend方法添加新的节点和边，用以创建计算图，然后就可以通过Session的Run方法执行计算图：用户给出需要计算的节点，同时提供输入数据，TensorFlow就会自动寻找所有需要计算的节点并按依赖顺序执行它们。
在以上代码中，流程常常会循环很多次，而数据往往不会被存储，然而Variable则常常被用以持久化保存一些参数。例如在神经网络中使用gradient descent时的weight(权值)

## TensorFlow的Hello World ##

接下来用一个例子来走一遍TensorFlow的流程，这里我们使用的训练集(training set)、测试集(test set)、验证集(validation set)是使用TensorFlow内置的一个例子:识别MNIST手写数字，相当于TensorFlow的Hello World任务。

### Step 1：安装编译TensorFlow ###

TensorFlow既可以用CPU进行计算，也可以使用GPU集群运算，这里在实验的时候，我们使用CPU版本进行实验。在windows上面先安装好Python3.5环境即可安装TensorFlow。

我们可以从[https://github.com/tensorflow/tensorflow](https://github.com/tensorflow/tensorflow)这上面下载预编译好的whl文件，也可以使gcc自己编译tensorflow。在这里，为了简便，我们从上述连接下载好对应的whl文件。然后用pip进行安装:

    pip install [path-to-tensorflow]

即可完成安装。Linux版本的安装编译要麻烦一点，具体步骤就不详述了。

### Step 2：导入数据集 ###

我们在这里使用的数据集是tensorflow的一个example,MNIST数字的呈现方式是一个28*28像素的灰度图，如下图所示：

<div align = "center">
<img src="http://i.imgur.com/22aNSeL.png" width = "400" height = "100" alt="图片名称" align=center />
</div>

空白的部分为0，有颜色的地方根据颜色深浅分别为0到1的取值。在MNIST数据集中，将每张图片的28\*28=784个像素点编程一个一维的vector，至此，这张图片的可以由一个1\*784的向量进行表示,每张图片的784个值表示为图片的特征(feature)。而我们的目标是训练出来一个可以识别手写数字是多少的机器。从0-9有是个类别，因此这是一个multi-class classification的问题，所以对于每一个图片的分类我们都用一个1\*10的向量进行标识，例如[0,0,1,0,0,0,0,0,0,0]表示这个数字是2。了解这些之后，我们可以开始我们的tensorflow之旅了，第一步就是导入数据集：

	import tensorflow as tf		
	from tensorflow.examples.tutorials.mnist import input_data		
	mnist = input_data.read_data_sets("MNIST_data/",one_hot = True)		#导入数据集,并设定数据及维one_hot编码

	print(mnist.train.images.shape,mnist.train.labels.shape)		#-->(55000,784),(55000,10) 训练集有55000个样本，784个input unit, 10个类别（10维的label）（multiclass）
	print(mnist.test.images.shape,mnist.test.labels.shape)		#10000个测试集样本
	print(mnist.validation.images.shape,mnist.validation.labels.shape)		#5000个验证集样本

可以得到我们的训练集、验证集和测试集的维数：

![](http://i.imgur.com/kaiSqzi.png)

可以看到我们的训练集中有55000个样本，验证集中有5000个样本，测试集中有10000个样本。每一个样本都有对应的label，就是告诉我们这个样本实际上是什么数字。

### Step 3：使用Softmax Regression模型分类 ###

当我们在处理multi-class classification的问题的时候，常常会使用Softmax Regression的模型，当我们的模型在对一张图片进行预测的时候，Softmax Regression会对每一个类别估算一个概率，最后取概率最大的作为我们最后的结果。

当我们在进行这个实验的时候，由于简单期间，因此使用的神经网络只有简单的输入层和输出层，softmax的流程如下图所示：

<div align = "center">
<img src="http://i.imgur.com/FtNyihO.jpg" width = "500" height = "200" alt="图片名称" align=center />
</div>
Softmax Regression相当于是一个筛选器的作用，帮助我们通过计算得到的结果获得最终希望获得的0、1序列，在softmax中还会对结果进行正则化操作(regularization)。用公式化的语言表示：
**y=softmax(w\*x+b)**

其中，w是权值向量，b是bias。

使用tensorflow装配时，首先为这个计算图分配一个session，然后定义一个输入数据的placeholder。接着要为Softmax Regression模型中的weights 和biases创建Variable对象，Variable对象是用来存储模型参数的，在模型迭代的时候是持久化的，可以长期存在并每轮迭代被更新
我们将weight和biaes都初始化为0。：

	session = tf.InteractiveSession()
	x = tf.placeholder(tf.float32,[None,784])		#placeholder是输入数据的地方，第一个参数是数据类型，第二个参数代表tensor的shape，None表示不限条数的输入
	W = tf.Variable(tf.zeros([784,10]))
	b = tf.Variable(tf.zeros([10]))
	#实现softmax算法
	y = tf.nn.softmax(tf.matmul(x,W)+b)		#tf.matmul是矩阵乘法函数,softmax是tf.nn神经网络模块中的一个组件

这样我们就实现了神经网络的正向传播算法(forward propagation)。

### Step 4：定义Cost Function ###
在我们训练模型的时候，我们需要定义cost Function来衡量这个模型到底和数据的契合度是多少。Cost越小说明契合度越高，训练的目的就是为了不断将这个Cost降低，直至找到一个全局最优解(global optimal)或者是局部最优解。在这里我们使用cross-entropy作为我们的costFunction:
<div align = "center">
<img src="http://i.imgur.com/LDPHDLZ.png" width = "300" height = "100" alt="图片名称" align=center />
</div>

定义cross-entropy方法如下：

	y_ = tf.placeholder(tf.float32,[None,10])
	#先定义一个placeholder，输入是真实的label,用来计算cost function
	#tf.reduce_sum-->求和
	#tf.reduce_mean--->对每个batch数据结果求均值
	cross_entropy = tf.reduce_mean(-tf.reduce_sum(y_ * tf.log(y),reduction_indices = [1]))	

### Step 5：优化求解 ###
有了正向传播算法和cost Function之后，我们就可以指定一种优化算法进行训练了。常常使用随机梯度下降(Stochastic Gradient Descent)的算法。定义好优化算法，TensorFlow就可以根据我们定义的整个计算图，自动求导，并通过每一次递归减少cost。

TensorFlow为我们省去了反向传播和梯度下降的步骤，我们只需要获得一个封装好的优化器，每次迭代的时候将数据喂给它就可以了。这里我们直接调用tf.train.GradientDescentOptimizer，并设置learning rate=0.5，优化目标设定为cross-entropy，得到的训练操作为train_step:

	train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

之后使用TensorFlow全局参数优化器，执行run方法:

	tf.global_variables_initializer().run()

最后我们迭代进行train_step，每次随机从训练集抽取100个样本构成一个mini-batch，并喂给placeholder，然后调用train_step进行训练。
	
	for i in range(1000):
	batch_xs,batch_ys = mnist.train.next_batch(100)
	train_step.run({x:batch_xs,y_:batch_ys})

这样我们就完成了训练了，对训练结果进行验证：
	
	#tf.argmax(y,1)寻找各个预测数字中概率最大的那个
	#tf.argmax(y_1)寻找真实的数字类别
	correct_prediction = tf.equal(tf.argmax(y,1),tf.argmax(y_,1))
	accuracy = tf.reduce_mean(tf.cast(correct_prediction,tf.float32))
	print(accuracy.eval({x:mnist.test.images,y_:mnist.test.labels}))

### 总结 ###
使用TensorFlow实现一个流程大致分为四部分：

1. 定义正向传播算法公式
2. 定义cost Function，选定优化器，并对cost进行优化
3. 迭代使用训练集进行训练
4. 在验证集和测试集上进行准确率测评 