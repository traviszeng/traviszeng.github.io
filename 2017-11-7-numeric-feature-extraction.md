---
layout: default
---

## 特征提取 ##
在机器学习中，特征提取常常是一个巨大的工程，常见的特征提取有数字型和文本型最为常见。

### 数字型特征提取  ##

数字性特征可以直接用作特征，但是对于一个多维的特征，如果其取值范围特别大，则很很容易导致其他特征对结果的影响被忽略，这个时候我们需要对数字型特征进行预处理。常见的数字型特征与处理的方式有以下几种：

（1）标准化

将特征数据的分布调整成标准正太分布，也叫高斯分布，也就是使得数据的均值维0，方差为1.
标准化的原因在于如果有些特征的方差过大，则会主导目标函数从而使参数估计器无法正确地去学习其他特征。

经过标准化后的数据具有两个特征：去均值的中心化（均值变为0）和方差的规模化（方差变为1）

	from sklearn import preprocessing
	import numpy as np

	# 创建一组特征数据，每一行表示一个样本，每一列表示一个特征
	x = np.array([[1., -1., 2.],
              	[2., 0., 0.],
              	[0., 1., -1.]])
	# 将每一列特征标准化为标准正太分布，注意，标准化是针对每一列而言的
	x_scale = preprocessing.scale(x)
	x_scale
		array([[ 0.        , -1.22474487,  1.33630621],
       		[ 1.22474487,  0.        , -0.26726124],
       		[-1.22474487,  1.22474487, -1.06904497]])
	# 可以查看标准化后的数据的均值与方差，已经变成0,1了
	x_scale.mean(axis=0)
		array([ 0.,  0.,  0.])
	# axis=1表示对每一行去做这个操作，axis=0表示对每一列做相同的这个操作
	x_scale.mean(axis=1)
		array([ 0.03718711,  0.31916121, -0.35634832])
	# 同理，看一下标准差
	x_scale.std(axis=0)
		array([ 1.,  1.,  1.])

preprocessing这个模块还提供了一个实用类StandardScaler，当之前的训练集做了标准化操作之后，将相同的转换操作应用到测试训练集中。
这样当之后有新的数据进来的时候也可以直接调用该类，则不用将所有数据放在一起重新计算一次。

	# 调用fit方法，根据已有的训练数据创建一个标准化的转换器
	scaler = preprocessing.StandardScaler().fit(x)
	scaler
		StandardScaler(copy=True, with_mean=True, with_std=True)

	# 使用上面这个转换器去转换训练数据x,调用transform方法
	scaler.transform(x)
		array([[ 0.        , -1.22474487,  1.33630621],
		       [ 1.22474487,  0.        , -0.26726124],
		       [-1.22474487,  1.22474487, -1.06904497]])
	# 好了，比如现在又来了一组新的样本，也想得到相同的转换
	new_x = [[-1., 1., 0.]]
	scaler.transform(new_x)
		array([[-2.44948974,  1.22474487, -0.26726124]])

StandardScaler()中可以传入两个参数：with_mean,with_std.这两个都是布尔型的参数，默认情况下都是true,但也可以自定义成false.即不要均值中心化或者不要方差规模化为1.

（2）正则化（Regularization）

正则化是将样本在向量空间模型上的一个转换，常用在分类和聚类中。

机器学习中几乎都可以看到损失函数后面会添加一个额外项，常用的额外项一般有两种，一般英文称作ℓ1-norm和ℓ2-norm，中文称作L1正则化和L2正则化，或者L1范数和L2范数。

L1正则化和L2正则化可以看做是损失函数的惩罚项。所谓『惩罚』是指对损失函数中的某些参数做一些限制。对于线性回归模型，使用L1正则化的模型建叫做Lasso回归，使用L2正则化的模型叫做Ridge回归（岭回归）。下图是Python中Lasso回归的损失函数，式中加号后面一项即为L1正则化项。

![](https://i.imgur.com/x7N0Hqv.png)


下图是Python中Ridge回归的损失函数，式中加号后面一项2的平方项即为L2正则化项。

![](https://i.imgur.com/pZ4Nctc.png)

一般回归分析中回归w表示特征的系数，从上式可以看到正则化项是对系数做了处理（限制）。L1正则化和L2正则化的说明如下：

 L1正则化是指权值向量w中各个元素的绝对值之和，通常表示为![](https://i.imgur.com/3naLPsu.png)


 L2正则化是指权值向量w中各个元素的平方和然后再求平方根（可以看到Ridge回归的L2正则化项有平方符号），通常表示为![](https://i.imgur.com/rfhPP6H.png)

一般都会在正则化项之前添加一个系数，Python中用α表示，一些文章也用λ表示。这个系数需要用户指定。

那添加L1和L2正则化有什么用？下面是L1正则化和L2正则化的作用，这些表述可以在很多文章中找到。

 L1正则化可以产生稀疏权值矩阵，即产生一个稀疏模型，可以用于特征选择

 L2正则化可以防止模型过拟合（overfitting）；一定程度上，L1也可以防止过拟合


函数normalize 提供了一个快速有简单的方式在一个单向量上来实现这正则化的功能。正则化有l1,l2等，这些都可以用上：

	x_normalized = preprocessing.normalize(x, norm='l2')

	print x
	print x_normalized

		[[ 1. -1.  2.]
 		[ 2.  0.  0.]
 		[ 0.  1. -1.]]
		[[ 0.40824829 -0.40824829  0.81649658]
 		[ 1.          0.          0.        ]
 		[ 0.          0.70710678 -0.70710678]]

preprocessing这个模块还提供了一个实用类Normalizer,实用transform方法同样也可以对新的数据进行同样的转换

	# 根据训练数据创建一个正则器
	normalizer = preprocessing.Normalizer().fit(x)
	normalizer
		Normalizer(copy=True, norm='l2')

	# 对训练数据进行正则
	normalizer.transform(x)
		array([[ 0.40824829, -0.40824829,  0.81649658],
			   [ 1.        ,  0.        ,  0.        ],
		       [ 0.        ,  0.70710678, -0.70710678]])

	# 对新的测试数据进行正则
	normalizer.transform([[-1., 1., 0.]])
		array([[-0.70710678,  0.70710678,  0.        ]])


（3） 归一化

归一化即使得特征的分布是在一个给定最小值和最大值的范围内的。一般情况是将其映射到[0,1]区间内，或者特征中绝对值最大的那个数为1，其他数以此为标准分布在[-1,1]内。

以上两者分别可以通过MinMaxScaler 或者 MaxAbsScaler方法来实现。
之所以需要将特征规模化到一定的[0,1]范围内，是为了对付那些标准差相当小的特征并且保留下稀疏数据中的0值。

MinMaxScaler

在MinMaxScaler中是给定了一个明确的最大值与最小值。它的计算公式如下：

X_std = (X - X.min(axis=0)) / (X.max(axis=0) - X.min(axis=0))

X_scaled = X_std / (max - min) + min

以下这个例子是将数据规与[0,1]之间，每个特征中的最小值变成了0，最大值变成了1，请看：

	min_max_scaler = preprocessing.MinMaxScaler()
	x_minmax = min_max_scaler.fit_transform(x)
	x_minmax
		array([[ 0.5       ,  0.        ,  1.        ],
		       [ 1.        ,  0.5       ,  0.33333333],
		       [ 0.        ,  1.        ,  0.        ]])

当有新的数据要处理时如下：

	x_test = np.array([[-3., -1., 4.]])
	x_test_minmax = min_max_scaler.transform(x_test)
	x_test_minmax
		array([[-1.5       ,  0.        ,  1.66666667]])

MaxAbsScaler

原理与上面的很像，只是数据会被规模化到[-1,1]之间。也就是特征中，所有数据都会除以最大值。这个方法对那些已经中心化均值维0或者稀疏的数据有意义。

	max_abs_scaler = preprocessing.MaxAbsScaler()
	x_train_maxsbs = max_abs_scaler.fit_transform(x)
	x_train_maxsbs
		array([[ 0.5, -1. ,  1. ],
		       [ 1. ,  0. ,  0. ],
		       [ 0. ,  1. , -0.5]])

	# 同理，也可以对新的数据集进行同样的转换
	x_test = np.array([[-3., -1., 4.]])
	x_test_maxabs = max_abs_scaler.transform(x_test)
	x_test_maxabs
		array([[-1.5, -1. ,  2. ]])


