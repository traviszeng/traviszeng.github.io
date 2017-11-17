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

使用KNN算法用于监督学习的时候也是非常简单的：

如下图所示：

![](https://i.imgur.com/ZCe5cbG.png)

### 使用K近邻算法检测异常操作 ###

在黑客入侵Web服务器之后，通常会使用一些系统漏洞进行提权，获得服务器的root权限，在接下来的工作中，我们通过搜集Linux服务器的bash操作日志，通过训练识别出特定用户的使用习惯，然后进一步识别出异常操作。

数据情况大概如下：

![](https://i.imgur.com/vEZNnNt.png)

训练数据中包含50个用户的操作日志，每个日志包含了15000条操作命令，其中前5000条为正常操作，后面10000条随机包含有异常操作。为了方便训练，我们将100个命令视为一个操作序列，在label.txt中是对应每个用户的后100个操作序列中是否异常的标签。正常为0，异常为1。

源码如下：

	# -*- coding:utf-8 -*-
	import sys
	import urllib
	import re
	from hmmlearn import hmm
	import numpy as np
	from sklearn.externals import joblib
	import nltk
	import matplotlib.pyplot as plt
	from nltk.probability import FreqDist
	from sklearn.feature_extraction.text import CountVectorizer
	from sklearn.neighbors import KNeighborsClassifier
	from sklearn.metrics import classification_report
	from sklearn import metrics
	#测试样本数
	N=90
	def load_user_cmd(filename):
    	cmd_list=[]
    	dist_max=[]
    	dist_min=[]
    	dist=[]
    	with open(filename) as f:
    	    i=0
    	    x=[]
    	    for line in f:
    	        line=line.strip('\n')
    	        x.append(line)
    	        dist.append(line)
    	        i+=1
    	        #每100条命令组成一个操作序列
    	        if i == 100:
    	            cmd_list.append(x)
    	            x=[]
    	            i=0
    	#统计最频繁使用的前50个命令和最少使用的50个命令
    	fdist =list(FreqDist(dist).keys())
    	dist_max=set(fdist[0:50])
    	dist_min = set(fdist[-50:])
    	return cmd_list,dist_max,dist_min

	#特征化用户使用习惯
	def get_user_cmd_feature(user_cmd_list,dist_max,dist_min):
    	user_cmd_feature=[]
    	for cmd_block in user_cmd_list:
        	#以100个命令为统计单元，作为一个操作序列，去重后的操作命令个数作为特征
        	#将list转为set去重
        	f1=len(set(cmd_block))
        	#FreqDist转为统计字典转化为命令:出现次数的形式
        	fdist = list(FreqDist(cmd_block).keys())
        	#最频繁使用的10个命令
        	f2=fdist[0:10]
        	#最少使用的10个命令
        	f3 = fdist[-10:]
        	f2 = len(set(f2) & set(dist_max))
        	f3 = len(set(f3) & set(dist_min))
        	#返回该统计单元中和总的统计的最频繁使用的前50个命令和最不常使用的50个命令的重合程度
        	#f1:统计单元中出现的命令类型数量
        	#f2:统计单元中最常使用的10个命令和总的最常使用的命令的重合程度
        	#f3:统计单元中最不常使用的10个命令和总的最不常使用的命令的重合程度
        	x=[f1,f2,f3]
        	user_cmd_feature.append(x)
    	return user_cmd_feature

	def get_label(filename,index=0):
    	x=[]
    	with open(filename) as f:
        	for line in f:
            	line=line.strip('\n')
            	x.append( int(line.split()[index]))
    	return x

	if __name__ == '__main__':    
    	user_cmd_list,user_cmd_dist_max,user_cmd_dist_min=load_user_cmd("../data/MasqueradeDat/User3")
    	user_cmd_feature=get_user_cmd_feature(user_cmd_list,user_cmd_dist_max,user_cmd_dist_min)
    	#index=2 即为User3对应的label
    	labels=get_label("../data/MasqueradeDat/label.txt",2)
    	#前5000个记录为正常操作 即前50个序列为正常操作
    	y=[0]*50+labels
    	x_train=user_cmd_feature[0:N]
    	y_train=y[0:N]
    	x_test=user_cmd_feature[N:150]
    	y_test=y[N:150]
    	neigh = KNeighborsClassifier(n_neighbors=3)
    	neigh.fit(x_train, y_train)
    	y_predict=neigh.predict(x_test)
    	score=np.mean(y_test==y_predict)*100
    	print(y_test)
    	print(y_predict)
    	print(score)
    	print(classification_report(y_test, y_predict))
    	print(metrics.confusion_matrix(y_test, y_predict))
	
运行一下，可以看到准确率约为83.3%，结果不是很理想。

![](https://i.imgur.com/dMAAFaI.png)

如果我们想提高我们的验证效果，我们需要更加全盘考虑异常操作的出现。因此接下来，我们使用全量比较，来重新对用户的是否异常进行训练。

在数据搜集和清洗的时候，我们使用dict数据结构，将全部命令去重之后形成一个大型的向量空间，每个命令代表一个特征，首先通过遍历全部命令生成对应的词集。因此我们重写load_user_cmd方法，如下：

	def load_user_cmd_new(filename):
    	cmd_list=[]
    	dist=[]
    	with open(filename) as f:
    	    i=0
    	    x=[]
    	    for line in f:
    	        line=line.strip('\n')
    	        x.append(line)
    	        dist.append(line)
    	        i+=1
    	        if i == 100:
    	            cmd_list.append(x)
    	            x=[]
    	            i=0
    	fdist = list(FreqDist(dist).keys())
    	return cmd_list,fdist

得到词集之后，将命令向量化，方便模型的训练。因此再重写get_user_feature方法，重新获取用户特征。

	def get_user_cmd_feature_new(user_cmd_list,dist):
    	user_cmd_feature=[]
    	for cmd_list in user_cmd_list:
        	v=[0]*len(dist)
        	for i in range(0,len(dist)):
        	    if dist[i] in cmd_list:
        	        v[i]+=1
        	user_cmd_feature.append(v)
    	return user_cmd_feature

训练新模型和之前的方法一样。

效果验证的时候，使用交叉验证，通过十次随机取样和验证，提高验证的可信度：

	score=cross_val_score(neigh,user_cmd_feature,y,n_jobs = -1,cv=10)

最后可以得到一个准确率约在93%左右的较好的模型。

![](https://i.imgur.com/UNfjUks.png)

除了以上的几个例子，使用KNN来进行webshell检测和rootkit检测的例子可以参考[https://github.com/traviszeng/MLWithWebSecurity/tree/master/code/KNNsample](https://github.com/traviszeng/MLWithWebSecurity/tree/master/code/KNNsample)
	
