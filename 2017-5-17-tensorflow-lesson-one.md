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
<div align = "center">
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
