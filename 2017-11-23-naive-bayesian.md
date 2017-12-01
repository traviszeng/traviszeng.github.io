## 朴素贝叶斯 ##
### 何为朴素贝叶斯算法 ###
朴素贝叶斯算法可能是机器学习里面名气最大的算法，早期的垃圾邮件的算法就使用了朴素贝叶斯算法。朴素贝叶斯算法中的朴素指的是特征条件独立假设，贝叶斯指的是该算法基于[贝叶斯定理](https://en.wikipedia.org/wiki/Bayes%27_theorem)。贝叶斯定理解决了现实生活中经常会遇到的问题：已知某条件概率，如何得到两个事件交换后的概率，也就是在已知P(A|B)的情况下如何求得P(B|A)。贝叶斯定理基本的求解公式为：

![](https://i.imgur.com/KML1zeH.png)

朴素贝叶斯包括一下算法：

高斯朴素贝叶斯、多项式朴素贝叶斯、伯努利朴素贝叶斯。

### 朴素贝叶斯分类的原理 ###
朴素贝叶斯分类的基本思想是这样的：对于给出的待分类项，求解在此项出现的条件下各个类别出现的概率，哪个最大，就认为此待分类项属于哪个类别。其正式定义如下：

![](https://i.imgur.com/MIsi6Zn.png)

现在的关键是如何计算第三步中的各个条件概率，我们可以采取如下方法：

![](https://i.imgur.com/beywAEe.png)

总结一下，整个朴素贝叶斯分为三个阶段：

第一阶段——准备工作阶段，这个阶段的任务是为朴素贝叶斯分类做必要的准备，主要工作是根据具体情况确定特征属性，并对每个特征属性进行适当划分，然后由人工对一部分待分类项进行分类，形成训练样本集合。这一阶段的输入是所有待分类数据，输出是特征属性和训练样本。这一阶段是整个朴素贝叶斯分类中唯一需要人工完成的阶段，其质量对整个过程将有重要影响，分类器的质量很大程度上由特征属性、特征属性划分及训练样本质量决定。

第二阶段——分类器训练阶段，这个阶段的任务就是生成分类器，主要工作是计算每个类别在训练样本中的出现频率及每个特征属性划分对每个类别的条件概率估计，并将结果记录。其输入是特征属性和训练样本，输出是分类器。这一阶段是机械性阶段，根据前面讨论的公式可以由程序自动计算完成。

第三阶段——应用阶段。这个阶段的任务是使用分类器对待分类项进行分类，其输入是分类器和待分类项，输出是待分类项与类别的映射关系。这一阶段也是机械性阶段，由程序完成。

### 特征属性为连续值时的条件概率和拉普拉斯平滑 ###

当特征属性为连续值时，通常假定其值服从高斯分布（也称正态分布）。即：

![](https://i.imgur.com/nCeN9gi.png)

因此只要计算出训练样本中各个类别中此特征项划分的各均值和标准差，代入上述公式即可得到需要的估计值。均值与标准差的计算在此不再赘述。

另外一个问题就是当分母为0的时候我们应该如何处理，当某个类别下某个特征项划分没有出现时，就是产生这种现象，这会令分类器质量大大降低。为了解决这个问题，我们引入拉普拉斯平滑的机制，它的思想非常简单，就是对没类别下所有划分的计数加1，这样如果训练样本集数量充分大时，并不会对结果产生影响，并且解决了上述频率为0的尴尬局面。

### 使用朴素贝叶斯检测DGA域名 ###

域名生成算法（Domain Generation Algorithm,DGA），是中心结构僵尸网络赖以生存的关键武器，该技术给打击和关闭该类型僵尸网络造成了不小的麻烦。安全人员需要快速掌握域名生成算法和输入，以便对生成的域名几时进行处理。在这里，我们尝试使用朴素贝叶斯算法来区分正常域名和DGA域名。

首先我们先加载alexa前1000的域名作为正常样本，并标记为0

	#加载白样本网址
	def load_alexa(filename):
    	domain_list = []
    	csv_reader = csv.reader(open(filename))
    	for row in csv_reader:
    	    domain = row[1]
    	    if len(domain)>=MIN_LEN:
    	        domain_list.append(domain)
    	return domain_list

	#加载alexa前1000的域名作为白样本，标记为0
    alexa_domain_list = load_alexa("../../data/DGA_data/top-1000.csv")

然后，分别加载crytolocker和post-tovar-goz家族的DGA域名，分别标记为1和2

	def load_dga(filename):
    	domain_list=[]
    	#xsxqeadsbgvpdke.co.uk,Domain used by Cryptolocker - Flashback DGA for 13 Apr 2017,2017-04-13,
    	# http://osint.bambenekconsulting.com/manual/cl.txt
    	with open(filename) as f:
    	    for line in f:
    	        #数据项第一个为网址
    	        domain=line.split(",")[0]
    	        #如果长度大于最小长度
    	        if len(domain)>= MIN_LEN:
    	            domain_list.append(domain)
    	return  domain_list

	cryptolocker_domain_list = load_dga("../../data/DGA_data/dga-cryptolocke-1000.txt")
    post_tovar_goz_domain_list = load_dga("../../data/DGA_data/dga-post-tovar-goz-1000.txt")

对样本进行标记：

	y1=[0]*len(alexa_domain_list)
    y2=[1]*len(cryptolocker_domain_list)
    y3=[2]*len(post_tovar_goz_domain_list)

接下来，对数据进行特征化，以2-gram分割域名，切割单元为字符，以整个数据集合的2-gram结果作为词汇表并进行映射，得到特征化的向量

	cv = CountVectorizer(ngram_range=(2, 2), decode_error="ignore",
                                          token_pattern=r"\w", min_df=1)
    x= cv.fit_transform(x_domain_list).toarray()

最后是训练样本，使用高斯贝叶斯对样本进行训练：

	clf = GaussianNB()
    score  = cross_val_score(clf, x, y, n_jobs=-1, cv=3)

可以得到一个分类准确率约为96%的DGA检测器

### 朴素贝叶斯的局限 ###

接下来，我们尝试使用朴素贝叶斯来进行验证码的识别实践。我们使用tensorflow内置的MNIST数据库，来看看朴素贝叶斯算法在多分类的情景下的表现如何。我们使用提前下载好的tensorflow的MNIST数据集，首先我们需要加载MNIST数据集，

	from tensorflow.examples.tutorials.mnist import input_data
	
	mnist = input_data.read_data_sets("../../data/MNIST",one_hot = True)
    x1,yy1=mnist.train.images,mnist.train.labels
    x2,yy2=mnist.test.images,mnist.test.labels

由于MNIST中的labels是一个n行10列的二维数组，而我们在使用GaussianNB的时候只支持单向量形式的label，因此我们还需要对加载进来的数据进行处理：

	def label2Vec(yy1):
    	y1 = []
    	for yy in yy1:
        	flag = 0
        	for yyy in yy:
           		if yyy==1:
                	y1.append(flag)
            	else:
                	flag+=1
    	return y1

最后得到准确率约在55%左右，效果比较不理想，这个例子很直观的告诉我们，朴素贝叶斯算法在处理二分类的问题上准确率挺高，但是在多分类问题上的表现就明显不如其他算法了。

