---
layout: default
---

# 无监督学习 #
和supervised learning相比，无监督学习不要求建立模型之后对结果进行新的预测，没有相应的标签（label），只是根据数据的特征（feature）对数据进行聚类（cluster）。常见的使用场景例如Google新闻中的各种新闻的分类等等。而在unsupervised learning中我们最常用的聚类算法就是K-MEANS.

#  K-Means算法  #
在unsupervised learning中我们常常面对如下图的聚类场景：

![](http://i.imgur.com/vgD6mqc.png)

要求我们将图中的点分为k个类别，这里我们可以使用K-MEANS算法来完成相应的要求。K-MEANS是一个迭代算法，假设我们想将数据点聚类分成k个组，K-MEANS的大致步骤如下：

1. 首先随机选择k个点，成为聚类中心（cluster centroids）
2. 对于数据集中的每一个点，根据其距离k个聚类中心点的距离，将其与距离最近的中心点关联起来，与同一个中心点关联的所有点聚成一类。
3. 计算每一个组的平均值，将该组所关联的中心点移到平均值的位置。
4. 重复步骤2-3，直至中心点不再变化。

用伪代码的方式写出来如下：

μ1，μ2，μ3...μk是k个cluster centroids

c(1)，c(2)，c(3)...c(m)存储m个数据点的归属的聚类索引

数据点的数量为m

	repeat{
		for i = 1 to m
			c(i):=index(from 1 to k) of cluster centroid closest to x(i)
		for k = 1 to k
			μk:=average(mean) of points assigned to cluster k
	}

### 评估K-MEANS的性能指标Distortion function ###

相当于supervised learning的cost function，K-MEANS算法也有它的optimization objective，在K-MEANS中成为Distortion function，如下图所示。

![](http://i.imgur.com/ZYW7YN1.png)

我们的优化目标是找到使得Distortion function最小的μ1，μ2，μ3...μk和c(1)，c(2)，c(3)...c(m)。

### Random Initialization ###

在初始化聚类中心的时候，我们使用的方法是随机的选取k个数据点作为聚类中心，然后在进行聚类计算。多次随机选择不同的聚类中心，可能会帮助我们找到不同的最优解。

### Choose the number of cluster ###

在我们选择最优的聚类数目的时候，应该遵循Elbow method。

![](http://i.imgur.com/B9dUOrl.png)

由上图可以看到，当我们选择k=3的时候，可以获得一个较好的J.

# 使用PCA进行降维（Dimensionality Reduction） #

当我们使用的数据的特征(feature)很多的时候，我们进行学习的时候的维度会很大，有时候许多的feature都是相关的，因此我们可以通过降维的方式对数据进行压缩，从而加快我们的学习速率。

![](http://i.imgur.com/yWYcWcV.png)

如上图所示，我们可以使用降维的方法，将一个2维的特征矩阵用一维的方式来进行表示。

我们还可以通过降维的方法来对数据进行可视化操作(visualization)。

# 主成分分析法（PCA） #

主成分分析（Principal Component Analysis）是我们常用的对features进行降维的处理方法。
在PCA中我们要做到的是找到一个方向向量（vector direction），并将数据都投射到该向量上，我们希望投射平均均方误差尽可能地少。

方向向量是一个经过原点的向量，而投射误差是从特征向量向该方向向量作垂线的长度。如图所示：

![](http://i.imgur.com/Jxn7yaT.png)

PCA技术的一大好处是对数据进行降维的处理。我们可以对新求出的“主元”向量的重要性进行排序，根据需要取前面最重要的部分，将后面的维数省去，可以达到降维从而简化模型或者是对数据进行压缩的效果。同时最大程度的保持了原有的数据的信息。

PCA还有另外一个优点是，它是完全无参数限制的。在PCA的计算过程中，完全不需要人为的设定参数货是根据任何经验模型对计算进行干预，最后的结果只与数据相关，和用户是独立的。

### 主成分分析算法 ###
PCA算法将特征从n维减少到k维的算法步骤如下：

1.均值归一化（feature scaling and normaliztion）。

2.计算协方差矩阵(covariance matrix)Ʃ:

![](http://i.imgur.com/Fi1vy4K.png)

3.计算协方差矩阵Ʃ的特征向量(eigenvectors):

在matlab中可以使用svd函数计算特征矩阵：

	[U,S,V] = svd(sigma)

得到的U便是n个特征向量组成的一个n*n的矩阵。

![](http://i.imgur.com/2XsxBmV.png)

4.计算新的特征值Z。

对于一个n\*n维度的矩阵，U是一个具有与数据之间最小投射误差的方向向量构成的矩阵。如果要将数据从n维降为k维，则取前k个方向向量，获得一个一个n\*k维度的向量矩阵Ureduce。

则新的特征值的计算如下：

![](http://i.imgur.com/76rwrTg.png)

向量形式表示：

![](http://i.imgur.com/fXZDGrh.png)


### 选择主成分的数量 ###
主成分分析是减少投射的平均均方误差。我们希望在平均均方误差和训练集方差的比例尽可能小的情况下选择尽可能小的K值。

如果我们希望这个比例小于1%，则意味着原本数据的偏差由99%都保存下来了，如果我们选择保留95%的偏差，便能非常显著地降低模型中特征的维度了。

我们可以先令 K=1，然后进行主要成分分析，获得 Ureduce 和 z，然后计算比例是否小于
1%。如果不是的话再令 K=2，如此类推，直到找到可以使得比例小于 1%的最小 K 值（原因
是各个特征之间通常情况存在某种相关性）。

我们在matlab中还有一个更好地方式选择K，当我们调用svd函数的时候，

	[U,S,V]=svd(sigma)

有一个S矩阵如下：

![](http://i.imgur.com/lpOIkKo.png)

我们可以利用这个S矩阵计算平均均方误差与训练集方差的比例：

![](http://i.imgur.com/2ybSvq2.png)

也就是：

![](http://i.imgur.com/AzWJXLO.png)

### 重建的压缩表示 ###

当我们需要对压缩的数据进行回复重建的时候，如下图所示：

![](http://i.imgur.com/WLNPM75.png)

我们可以利用如下公式对被压缩的数据进行重建：

![](http://i.imgur.com/49Xihl5.png)