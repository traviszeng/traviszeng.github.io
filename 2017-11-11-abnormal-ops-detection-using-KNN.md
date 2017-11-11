## K-临近算法介绍和实践 #

### K-临近算法概述 ##

KNN算法（K-Nearest Neighbors）算法是机器学习领域广泛使用的分类算法之一，所谓KNN，说的就是每个样本的分类都可以用它最接近的K个邻居来代表。

KNN算法的基本思路是：

1、给定一个训练集数据，每个训练集数据都是已经分好类的。

2、设定一个初始的测试数据a，计算a到训练集所有数据的欧几里得距离，并排序。                       

3、选出训练集中离a距离最近的K个训练集数据。

4、比较k个训练集数据，选出里面出现最多的分类类型，此分类类型即为最终测试数据a的分类。

### Python中的KNN相关API ###

在python中，我们可以很容易使用sklearn中的neighbors module来创建我们的KNN模型。

在[API文档](http://scikit-learn.org/dev/modules/classes.html#module-sklearn.neighbors)中，neighbors模块中的方法如下：

![](https://i.imgur.com/lOgDwAM.png)

接下来我们用一个简单的例子只管阐述一下KNN算法的实现。

### K临近算法的hello world ###

首先我们要创建二维空间上的一些点用于我们测试：

	from sklearn.neighbors import NearestNeighbors
	import numpy as np
	X = np.array([[-1, -1], [-2, -1], [-3, -2], [1, 1], [2, 1], [3, 2]])

由以上代码可以看到，我们在二维空间中创建了6个测试点用于我们的实验，接下来我们便是要使用KNN算法，训练出一个合适我们这六个点的一个分类模型。

	#NearestNeighbors用到的参数解释 
	#n_neighbors=5,默认值为5，表示查询k个最近邻的数目 
	#algorithm='auto',指定用于计算最近邻的算法，auto表示试图采用最适合的算法计算最近邻 
	#fit(X)表示用X来训练算法 
	nbrs = NearestNeighbors(n_neighbors=2, algorithm="ball_tree").fit(X) 

这样，我们最简单的一个KNN的分类器就完成了，接下来看看效果。

	#返回的indices是距离该点较近的k个点的下标，distance则是距离
	distances, indices = nbrs.kneighbors(X)

结果如下图所示：

第一个矩阵是distances矩阵，第二个则表示k个距离该点较近的点的坐标。

![](https://i.imgur.com/EVv3ThI.png)  

还可以输入以下代码可视化结果：

	#输出的是求解n个最近邻点后的矩阵图，1表示是最近点，0表示不是最近点  
	print nbrs.kneighbors_graph(X).toarray() 

![](https://i.imgur.com/P7dzZsc.png)

	