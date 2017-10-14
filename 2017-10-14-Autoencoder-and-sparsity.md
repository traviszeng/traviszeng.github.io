---
layout: default
---

# **自编码器和稀疏性** #
自编码器是具有一层隐含层的神经网络，其一般结构如下图所示：

![](https://i.imgur.com/XAYz83P.png)

自编码神经网络尝试学习一个使得输出等于输入的的函数，即希望得到：

![](https://i.imgur.com/kWH8atn.png)

这样的一个恒等函数，这样往往可以发现输入数据的一些有趣特征，最终我们会用隐藏层的神经元代替原始数据。当隐藏神经元数目少于输入的数目时，自编码神经网络可以达到数据压缩的效果（因为最终我们可以用隐藏神经元替代原始输入，输入层的n个输入转换为隐藏层的m个神经元，其中n>m,之后隐藏层的m个神经元又转换为输出层的n个输出，其输出等于输入）；当隐藏神经元数目较多时，我们仍然可以对隐藏层的神经元加入稀疏性限制来发现输入数据的有趣结构。

自编码器常常包含两个部分：编码器（encoder）和解码器（decoder）。从输入层到隐藏层是编码的过程，从隐藏层到输出是解码的过程。

![](https://i.imgur.com/1MOBi8z.png)

自动编码器就是一种尽可能复现输入信号的神经网络。为了实现这种复现，自动编码器就必须捕捉可以代表输入数据的最重要的因素，就像PCA那样，找到可以代表原信息的主要成分。

## 自编码器的tensorflow实现 ##
在tensorflow实战一书中为我们讲述了自编码器如何使用tensorflow生成实现的方法。
首先，我们要先导入库和数据：

	import numpy as np
	import sklearn.preprocessing as prep
	import tensorflow as tf
	from tensorflow.examples.tutorials.mnist import input_data

在自编码器中使用到xavier initialization用于初始化参数

	"""define xavier initialization function, mean distribution or gauss distribution 
	mean = 0, var = 2/(n_in+n_out)""" 
	def xavier_init(fan_in, fan_out, constant=1): 
	"""xavier initialization function 
	fan_in: input node number 
	fan_out: output node number""" 
	low = -constant*np.sqrt(6/(fan_in+fan_out)) 
	high = constant*np.sqrt(6/(fan_in+fan_out)) 
	return tf.random_uniform((fan_in, fan_out), minval=low, maxval=high, dtype=tf.float32)

定义一个去噪自编码器的类，包括一个构建函数init(),完成一些成员变量输出化和网络构建，定义权重初始化函数_initialize_weights()，定义损失函数cost和单步训练函数partial_fit().

	class AdditiveGaussianNoiseAutoencoder(object): 
	"""define construct function """
		def __init__(self, n_input, n_hidden, transfer_function=tf.nn.softplus, 
					optimizer=tf.train.AdamOptimizer(), scale = 0.1): 
			self.n_input = n_input 
			self.n_hidden = n_hidden 
			self.transfer = transfer_function 
			self.scale = tf.placeholder(tf.float32) 
			self.training_scale = scale 
			network_weights = self._initialize_weights() 
			self.weights = network_weights 
			""" define auto-encoder net structure"""
			with tf.name_scope('input'): 
				self.x = tf.placeholder(tf.float32, [None, self.n_input]) 
			with tf.name_scope('hidden_layr'): 
				self.hidden = self.transfer(tf.add(tf.matmul(self.x+scale*tf.random_normal((n_input,)), self.weights['w1']), self.weights['b1'])) 
			tf.summary.histogram('hidden',self.hidden) 
			"""tf.summary.image('hidden_image',self.hidden)""" 
			with tf.name_scope('output_layr'): 
				self.reconstruction = tf.add(tf.matmul(self.hidden,self.weights['w2']), self.weights['b2']) 
			"""define loss function"""
			with tf.name_scope('loss_func'): 
				self.cost = 0.5*tf.reduce_mean(tf.pow(tf.subtract(self.reconstruction,self.x),2.0)) 
			self.optimizer = optimizer.minimize(self.cost) 
			"""initialize all variables""" 
			init = tf.global_variables_initializer() 
			self.sess = tf.Session() self.sess.run(init) 
			self.merged = tf.summary.merge_all() 
			"""parameter initialize function""" 
		def _initialize_weights(self): 
			all_weights = dict() 
			all_weights['w1'] = tf.Variable(xavier_init(self.n_input,self.n_hidden)) 
			all_weights['b1'] = tf.Variable(tf.zeros([self.n_hidden],dtype=tf.float32)) 
			all_weights['w2'] = tf.Variable(tf.zeros([self.n_hidden, self.n_input],dtype=tf.float32)) 
			all_weights['b2'] = tf.Variable(tf.zeros([self.n_input],dtype=tf.float32)) 
			return all_weights 
		"""1 step train function""" 
		def partial_fit(self, X): 
			cost, opt, merged = self.sess.run((self.cost, self.optimizer, self.merged),feed_dict={self.x:X, self.scale:self.training_scale}) 
			return cost, merged 
		"""loss function""" 
		def calc_total_cost(self,X): 
			return self.sess.run(self.cost, feed_dict={self.x:X, self.scale:self.training_scale})

最后，在主函数中对训练数据进行预处理：

	if __name__  == '__main__':
    	mnist = input_data.read_data_sets('MNIST_DATA', one_hot=True)
    	logdir = './auto_encoder_logdir'
    	summary_writer = tf.summary.FileWriter(logdir)

    	with tf.Graph().as_default():
        	# define standard scale fucntion
       	 	def standard_scale(X_train, X_test):
            	preprocessor = prep.StandardScaler().fit(X_train)
            	X_train = preprocessor.transform(X_train)
            	X_test = preprocessor.transform(X_test)
            	return X_train, X_test

        	# define get random block function
        	def get_random_block_from_data(data, batch_size):
            	start_index = np.random.randint(0, len(data)-batch_size)
            	return data[start_index:(start_index+batch_size)]

        	X_train, X_test = standard_scale(mnist.train.images, mnist.test.images)

        	n_samples = int(mnist.train.num_examples)        
        	training_epochs = 20
       		batch_size = 128
        	display_step = 2

        	autoencoder = AdditiveGaussianNoiseAutoencoder(n_input = 784, 
                                                       	   n_hidden = 200,
                                                       t   ransfer_function=tf.nn.softplus,
                                                           optimizer = tf.train.AdamOptimizer(learning_rate=0.001),
                                                           scale = 0.01
                                                           )

        	# training process

        	for epoch in range(training_epochs):
            	avg_cost = 0
            	total_batch = int(n_samples/batch_size)
            	for i in range(total_batch):

                	batch_xs = get_random_block_from_data(X_train, batch_size)
                	#cost = autoencoder.partial_fit(batch_xs)
                	cost, merged = autoencoder.partial_fit(batch_xs)
                	summary_writer.add_summary(merged, i)
                	avg_cost += cost/n_samples*batch_size 
                	if epoch%display_step == 0:
                    	print('Epoch:','%04d'%(epoch+1), 'cost=','{:.9f}'.format(avg_cost))

            	print('Total cost:'+str(autoencoder.calc_total_cost(X_test)))
        	summary_writer.close()



## 稀疏性 ##

稀疏性可以被简单地解释如下。如果当神经元的输出接近于1的时候我们认为它被激活，而输出接近于0的时候认为它被抑制，那么使得神经元大部分的时间都是被抑制的限制则被称作稀疏性限制。这里我们假设的神经元的激活函数是sigmoid函数（如果你使用tanh作为激活函数的话，当神经元输出为-1的时候，我们认为神经元是被抑制的）。

## 稀疏自动编码器 ##

我们还可以继续加上一些约束条件得到新的Deep Learning方法，如：如果在AutoEncoder的基础上加上L1的Regularity限制（L1主要是约束每一层中的节点中大部分都要为0，只有少数不为0，这就是Sparse名字的来源），我们就可以得到稀疏自动编码器。

如果隐藏节点比可视节点（输入、输出）少的话，由于被迫的降维，自编码器会自动习得训练样本的特征（变化最大，信息量最多的维度）。但是如果隐藏节点数目过多，甚至比可视节点数目还多的时候，自编码器不仅会丧失这种能力，更可能会习得一种“恒等函数”——直接把输入复制过去作为输出。这时候，我们需要对隐藏节点进行稀疏性限制。

所谓稀疏性，就是对一对输入图像，隐藏节点中被激活的节点数（输出接近1）远远小于被抑制的节点数目（输出接近0）。那么使得神经元大部分的时间都是被抑制的限制则被称作稀疏性限制。

![](https://i.imgur.com/NF7prKS.png)

如上图，其实就是限制每次得到的表达code尽量稀疏。因为稀疏的表达往往比其他的表达要有效（人脑好像也是这样的，某个输入只是刺激某些神经元，其他的大部分的神经元是受到抑制的）。


