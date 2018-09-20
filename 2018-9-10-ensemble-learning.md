---
layout: default
---

# 集成学习 Ensemble Learning #

在机器学习的有监督学习算法中，我们的目标是学习出一个稳定的且在各个方面表现都较好的模型，但实际情况往往不这么理想，有时我们只能得到多个有偏好的模型（弱监督模型，在某些方面表现的比较好）。集成学习就是组合这里的多个弱监督模型以期得到一个更好更全面的强监督模型，集成学习潜在的思想是即便某一个弱分类器得到了错误的预测，其他的弱分类器也可以将错误纠正回来。

集成学习在各个规模的数据集上都有很好的策略。



- 数据集大：划分成多个小数据集，学习多个模型进行组合


- 数据集小：利用Bootstrap方法进行抽样，得到多个数据集，分别训练多个模型再进行组合


## 基础集成技术 ##

### 1. 最大投票法 

最大投票法常用于分类问题，在这种技术中使用多个模型来预测每个数据点，每个模型的预测都视作一次投票，最后大多数模型得到的预测被用作最终预测结果。

示例代码：

这里x_train由训练数据中的自变量组成，y_train是训练数据的目标变量。验证集是x_test（自变量）和y_test（目标变量）。
	
	model1 = tree.DecisionTreeClassifier()
	model2 = KNeighborsClassifier()
	model3= LogisticRegression()

	model1.fit(x_train,y_train)
	model2.fit(x_train,y_train)
	model3.fit(x_train,y_train)

	pred1=model1.predict(x_test)
	pred2=model2.predict(x_test)
	pred3=model3.predict(x_test)

	final_pred = np.array([])
	for i in range(0,len(x_test)):
    	final_pred =np.append(final_pred, mode([pred1[i], pred2[i], pred3[i]]))

也可以在sklearn中使用VotingClassifier：

	from sklearn.ensemble import VotingClassifier

	model1 = LogisticRegression(random_state=1)
	model2 = tree.DecisionTreeClassifier(random_state=1)
	model = VotingClassifier(estimators=[('lr', model1), ('dt', model2)], voting='hard')

	model.fit(x_train,y_train)
	model.score(x_test,y_test)


### 2. 平均法

平均法类似于最大投票法，常用于回归问题。这里对每个数据点的多次预测结果进行算数平均，取最后的平均值作为预测的最终结果。除了可以用在回归问题，还可以用在计算分类问题的概率。

Demo:

	model1 = tree.DecisionTreeClassifier()
	model2 = KNeighborsClassifier()
	model3= LogisticRegression()

	model1.fit(x_train,y_train)
	model2.fit(x_train,y_train)
	model3.fit(x_train,y_train)

	pred1=model1.predict_proba(x_test)
	pred2=model2.predict_proba(x_test)
	pred3=model3.predict_proba(x_test)

	finalpred=(pred1+pred2+pred3)/3


### 3. 加权平均法

在平均法中简单的算术平均并不能体现每个模型的预测重要性，因而加权平均是对平均法的拓展。

Demo:

	model1 = tree.DecisionTreeClassifier()
	model2 = KNeighborsClassifier()
	model3= LogisticRegression()
	
	model1.fit(x_train,y_train)
	model2.fit(x_train,y_train)
	model3.fit(x_train,y_train)
	
	pred1=model1.predict_proba(x_test)
	pred2=model2.predict_proba(x_test)
	pred3=model3.predict_proba(x_test)
	
	finalpred=(pred1*0.3+pred2*0.3+pred3*0.4)



## Bagging ##

bagging是bootstrap aggregating的简写，bootstrap是一种有放回的抽样方法，目的是为了得到统计量的分布以及置信区间，我们有放回的从原始数据集上创建观察子集，子集的大小与原始集的大小相同。

![](https://i.imgur.com/9pzR6Ja.png)

Bagging的常见步骤：

1.从原始数据集有放回的选择观测值来创建多个子集。

2.在每一个子集上创建一个基础模型（弱模型）。

3.这些模型同时运行，彼此独立。

4.通过组合所有模型的预测来确定最终预测。

![](https://i.imgur.com/nwImR95.png)

![](https://i.imgur.com/gXwnMvT.png)

## 堆叠Stacking

堆叠是一种集成学习技术，它使用多个模型（例如决策树，knn或svm）的预测来构建新模型。该新模型用于对测试集进行预测。

![](https://i.imgur.com/2QQ3rVF.png)

以下是简单堆叠集成法的逐步解释

1.把训练集分成10份

2.基础模型（假设是决策树）在其中9份上拟合，并对第10份进行预测。

3.对训练集上的每一份如此做一遍。

4.然后将基础模型（此处是决策树）拟合到整个训练集上。

5.使用此模型，在测试集上进行预测。

6.对另一个基本模型（比如knn）重复步骤2到4，产生对训练集和测试集的另一组预测。

7.训练集预测被用作构建新模型的特征。

8.该新模型用于对测试预测集进行最终预测。

Demo:

我们首先定义一个函数来对n折的训练集和测试集进行预测。此函数返回每个模型对训练集和测试集的预测。

	def Stacking(model,train,y,test,n_fold):
	
	   folds=StratifiedKFold(n_splits=n_fold,random_state=1)	
	   test_pred=np.empty((test.shape[0],1),float)	
	   train_pred=np.empty((0,1),float)

	   for train_indices,val_indices in folds.split(train,y.values):

	      x_train,x_val=train.iloc[train_indices],train.iloc[val_indices]
	      y_train,y_val=y.iloc[train_indices],y.iloc[val_indices]
		
	      model.fit(X=x_train,y=y_train)	
	      train_pred=np.append(train_pred,model.predict(x_val))	
	      test_pred=np.append(test_pred,model.predict(test))
	
	    return test_pred.reshape(-1,1),train_pred

然后，创建两个基本模型，Decision Tree和KNN

	model1 = tree.DecisionTreeClassifier(random_state=1) 
	
	test_pred1 ,train_pred1=Stacking(model=model1,n_fold=10, train=x_train,test=x_test,y=y_train)		
	train_pred1=pd.DataFrame(train_pred1)	
	test_pred1=pd.DataFrame(test_pred1)
		
	model2 = KNeighborsClassifier()
		
	test_pred2 ,train_pred2=Stacking(model=model2,n_fold=10,train=x_train,test=x_test,y=y_train)
	train_pred2=pd.DataFrame(train_pred2)	
	test_pred2=pd.DataFrame(test_pred2)

创建第三个模型，逻辑回归，在决策树和knn模型的预测之上。

	df = pd.concat([train_pred1, train_pred2], axis=1)	
	df_test = pd.concat([test_pred1, test_pred2], axis=1)
		
	model = LogisticRegression(random_state=1)	
	model.fit(df,y_train)
	model.score(df_test, y_test)

为了简化上面的解释，我们创建的堆叠模型只有两层。决策树和knn模型建立在零级，而逻辑回归模型建立在第一级。其实可以随意的在堆叠模型中创建多个层次。

## 混合Stacking

混合遵循与堆叠相同的方法，但仅使用来自训练集的一个留出(holdout)/验证集来进行预测。换句话说，与堆叠不同，预测仅在留出集上进行。留出集和预测用于构建在测试集上运行的模型。以下是混合过程的详细说明：

1.原始训练数据被分为训练集合验证集。

2.在训练集上拟合模型。

3.在验证集和测试集上进行预测。

4.验证集及其预测用作构建新模型的特征。

5.该新模型用于对测试集和元特征(meta-features)进行最终预测。

Demo:

我们将在训练集上建立两个模型，决策树和knn，以便对验证集进行预测。

	model1 = tree.DecisionTreeClassifier()
	model1.fit(x_train, y_train)
	
	val_pred1=model1.predict(x_val)	
	test_pred1=model1.predict(x_test)
	
	val_pred1=pd.DataFrame(val_pred1)	
	test_pred1=pd.DataFrame(test_pred1)
		
	model2 = KNeighborsClassifier()	
	model2.fit(x_train,y_train)
	
	val_pred2=model2.predict(x_val)	
	test_pred2=model2.predict(x_test)
	
	val_pred2=pd.DataFrame(val_pred2)	
	test_pred2=pd.DataFrame(test_pred2)

结合元特征和验证集，构建逻辑回归模型以对测试集进行预测。

	df_val=pd.concat([x_val, val_pred1,val_pred2],axis=1)	
	df_test=pd.concat([x_test, test_pred1,test_pred2],axis=1)
		
	model = LogisticRegression()	
	model.fit(df_val,y_val)	
	model.score(df_test,y_test)

## Boosting

在我们进一步讨论之前，这里有另一个问题：如果第一个模型错误地预测了某一个数据点，然后接下来的模型（可能是所有模型），将预测组合起来会提供更好的结果吗？Boosting就是来处理这种情况的。

![](https://i.imgur.com/3xvtY8r.png)

Boosting是一个顺序过程，每个后续模型都会尝试纠正先前模型的错误。后续的模型依赖于之前的模型。接下来一起看看boosting的工作方式：

1.从原始数据集创建一个子集。

2.最初，所有数据点都具有相同的权重。

3.在此子集上创建基础模型。

4.该模型用于对整个数据集进行预测。

![](https://i.imgur.com/hgq1iJh.png)

5.使用实际值和预测值计算误差。

6.预测错误的点获得更高的权重。（这里，三个错误分类的蓝色加号点将被赋予更高的权重）

7.创建另一个模型并对数据集进行预测（此模型尝试更正先前模型中的错误）.

![](https://i.imgur.com/aSVl8DN.png)

8.类似地，创建多个模型，每个模型校正先前模型的错误。

9.最终模型（强学习器）是所有模型（弱学习器）的加权平均值。

![](https://i.imgur.com/jVZuYwL.png)

因此，boosting算法结合了许多弱学习器来形成一个强学习器。单个模型在整个数据集上表现不佳，但它们在数据集的某些部分上表现很好。因此，每个模型实际上提升了集成的整体性能。

## 基于bagging和Boosting的算法

### Bagging算法

- Bagging meta-estimator

Bagging meta-estimator是一种集成算法，可用于分类(BaggingClassifier)和回归(BaggingRegressor)问题。它采用典型的bagging技术进行预测。以下是Bagging meta-estimator算法的步骤：

1.从原始数据集（Bootstrapping）创建随机子集。

2.数据集的子集包括所有特征。

3.用户指定的基础估计器在这些较小的集合上拟合。

4.将每个模型的预测结合起来得到最终结果。

Demo：

	from sklearn.ensemble import BaggingClassifier
	from sklearn import tree

	model = BaggingClassifier(tree.DecisionTreeClassifier(random_state=1))
	model.fit(x_train, y_train)	
	model.score(x_test,y_test)

回归问题：

	from sklearn.ensemble import BaggingRegressor	
	model = BaggingRegressor(tree.DecisionTreeRegressor(random_state=1))	
	model.fit(x_train, y_train)	
	model.score(x_test,y_test)

算法中用到的参数：

	base_estimator
	
		定义了在随机子集上拟合所用的基础估计器
	
		没有指明时，默认使用决策树
	
	n_estimators
	
		创建的基础估计器数量
	
		要小心微调这个参数，因为数字越大运行时间越长，相反太小的数字可能无法提供最优结果
	
	max_samples
	
		该参数控制子集的大小
	
		它是训练每个基础估计器的最大样本数量
	
	max_features
	
		控制从数据集中提取多少个特征
	
		它是训练每个基础估计器的最大特征数量
	
	n_jobs
	
		同时运行的job数量
	
		将这个值设为你系统的CPU核数
	
		如果设为-1，这个值会被设为你系统的CPU核数
	
	random_state
	
		定义了随机分割的方法。当两个模型的random_state值一样时，它们的随机选择也一样
	
		如果你想对比不同的模型，这个参数很有用


- 随机森林

随机森林是另一种遵循bagging技术的集成机器学习算法。它是bagging-estimator算法的扩展。随机森林中的基础估计器是决策树。与bagging meta-estimator不同，随机森林随机选择一组特征，这些特征用于决定决策树的每个节点处的最佳分割。

具体步骤：

1.从原始数据集（Bootstrapping）创建随机子集。

2.在决策树中的每个节点处，仅考虑一组随机特征来决定最佳分割。

3.在每个子集上拟合决策树模型。

4.通过对所有决策树的预测求平均来计算最终预测。

注意：随机林中的决策树可以构建在数据和特征的子集上。特别地，sklearn中的随机森林使用所有特征作为候选，并且候选特征的随机子集用于在每个节点处分裂。

总而言之，随机森林随机选择数据点和特征，并构建多个树（森林）。

Demo：

	from sklearn.ensemble import RandomForestClassifier
	
	model= RandomForestClassifier(random_state=1)
	
	model.fit(x_train, y_train)
	model.score(x_test,y_test)

可以通过在随机林中使用model.feature_importances_来查看特征重要性。

	for i, j in sorted(zip(x_train.columns, model.feature_importances_)):
    	print(i, j)

回归问题:

	from sklearn.ensemble import RandomForestRegressor

	model= RandomForestRegressor()
	model.fit(x_train, y_train)
	model.score(x_test,y_test)

有用参数：

	n_estimators
	
		定义随机森林中要创建的决策树数量
	
		通常，越高的值会让预测更强大更稳定，但是过高的值会让训练时间很长
	
	criterion
	
		定义了分割用的函数
	
		该函数用来衡量使用每个特征分割的质量从而选择最佳分割
	
	max_features
	
		定义了每个决策树中可用于分割的最大特征数量
	
		增加最大特征数通常可以改善性能，但是一个非常高的值会减少各个树之间的差异性
	
	max_depth
	
		随机森林有多个决策树，此参数定义树的最大深度
	
	min_samples_split
	
		用于在尝试拆分之前定义叶节点中所需的最小样本数
	
		如果样本数小于所需数量，则不分割节点
	
	min_samples_leaf
	
		定义了叶子节点所需的最小样本数
	
		较小的叶片尺寸使得模型更容易捕获训练数据中的噪声
	
	max_leaf_nodes
	
		此参数指定每个树的最大叶子节点数
	
		当叶节点的数量变得等于最大叶节点时，树停止分裂
	
	n_jobs
	
		这表示并行运行的作业数
	
		如果要在系统中的所有核心上运行，请将值设置为-1
	
	random_state
	
		此参数用于定义随机选择
	
		它用于各种模型之间的比较


### Boosting算法

- AdaBoost

自适应增强或AdaBoost是最简单的boosting算法之一。通常用决策树来建模。创建多个顺序模型，每个模型都校正上一个模型的错误。AdaBoost为错误预测的观测值分配权重，后续模型来正确预测这些值。

 

以下是执行AdaBoost算法的步骤：



第一步：最初，数据集中的所有观察值都具有相同的权重。

第二步：在数据子集上建立一个模型。

第三步：使用此模型，可以对整个数据集进行预测。

第四步：通过比较预测值和实际值来计算误差。

第五步：在创建下一个模型时，会给预测错误的数据点赋予更高的权重。

第六步：可以使用误差值确定权重。例如，误差越大，分配给观察值的权重越大。

第七步：重复该过程直到误差函数没有改变，或达到估计器数量的最大限制。

Demo:

	from sklearn.ensemble import AdaBoostClassifier
	
	model = AdaBoostClassifier(random_state=1)	
	model.fit(x_train, y_train)	
	model.score(x_test,y_test)

回归问题：

	from sklearn.ensemble import AdaBoostRegressor
	
	model = AdaBoostRegressor()	
	model.fit(x_train, y_train)	
	model.score(x_test,y_test)

参数：

	base_estimators
	
		它用于指定基础估计器的类型，即用作基础学习器的机器学习算法
	
	n_estimators
	
		它定义了基础估计器的数量
	
		默认值为10，但可以设为较高的值以获得更好的性能
	
	learning_rate
	
		此参数控制估计器在最终组合中的贡献
	
		在learning_rate和n_estimators之间需要进行权衡
	
	max_depth
	
		定义单个估计器的最大深度
	
		调整此参数以获得最佳性能
	
	n_jobs
	
		指定允许使用的处理器数
	
		将值设为-1，可以使用允许的最大处理器数量
	
	random_state
		
		用于指定随机数据拆分的整数值
	
		如果给出相同的参数和训练数据，random_state的确定值将始终产生相同的结果


- GBM

Gradient Boosting（梯度提升）适用于回归和分类问题。GBM使用boosting技术，结合了许多弱学习器，以形成一个强大的学习器。回归树用作基础学习器，每个后续的树都是基于前一棵树计算的错误构建的。

我们将使用一个简单的例子来理解GBM算法。我们会使用以下数据预测一群人的年龄：

![](https://i.imgur.com/FG0mmC5.png)

第一步：假设平均年龄是数据集中所有观测值的预测值。

第二步：使用该平均预测值和年龄的实际值来计算误差。

![](https://i.imgur.com/MapJZdH.png)

第三步：使用上面计算的误差作为目标变量创建树模型。我们的目标是找到最佳分割以最小化误差。

第四步：该模型的预测与预测1相结合：

![](https://i.imgur.com/Rc6LGc4.png)

第五步：上面计算的这个值是新的预测。

第六步：使用此预测值和实际值计算新误差：

![](https://i.imgur.com/jyjK2nB.png)

第七步：重复步骤2到6，直到最大迭代次数（或误差函数不再改变）

Demo：

	from sklearn.ensemble import GradientBoostingClassifier
	
	model= GradientBoostingClassifier(learning_rate=0.01,random_state=1)	
	model.fit(x_train, y_train)	
	model.score(x_test,y_test)

Regression Demo：

	from sklearn.ensemble import GradientBoostingRegressor
	
	model= GradientBoostingRegressor()	
	model.fit(x_train, y_train)	
	model.score(x_test,y_test)

参数：

	min_samples_split
	
		定义考虑被拆分的节点中所需的最小样本数（或观察值数）
	
		用于控制过配合。较高的值会阻止模型学习关系，这种关系可能对为一棵树选择的特定样本高度特定
	
	min_samples_leaf
	
		定义终端或叶节点中所需的最小样本数
	
		一般来说，应该为不平衡的分类问题选择较低的值，因为少数群体占大多数的地区将非常小
	
		min_weight_fraction_leaf
	
		与min_samples_leaf类似，但定义为观察总数的一个比例而不是整数
	
	max_depth
	
		树的最大深度。
	
		用于控制过拟合，因为更高的深度将让模型学习到非常特定于某个样本的关系
	
		应该使用CV进行调整
	
	max_leaf_nodes
	
		树中终端节点或叶子的最大数量
	
		可以用于代替max_depth。由于创建了二叉树，因此深度'n'将产生最多2 ^ n个叶子
	
		如果它被定义，则GBM会忽略max_depth
	
	max_features
	
		搜索最佳拆分时要考虑的特征数量。这些特征将被随机选择。
	
		作为一个经验法则，特征总数的平方根效果很好，但我们可以尝试直到特征总数的30-40％。
	
		较高的值可能导致过度拟合，但通常取决于具体情况。


- XGBM

让我们看看XGBoost为何比其他技术更好：

正则化：

标准GBM实现没有像XGBoost那样的正则化

因此，XGBoost还有助于减少过拟合

并行处理：

XGBoost实现并行处理，并且比GBM更快

XGBoost还支持Hadoop上的实现

高灵活性：

XGBoost允许用户自定义优化目标和评估标准，为模型添加全新维度

处理缺失值：

XGBoost有一个内置的例程来处理缺失值

树剪枝：

XGBoost先进行分割，直到指定的max_depth，然后开始向后修剪树并删除没有正向增益的分割

内置交叉验证：

XGBoost允许用户在提升过程的每次迭代中运行交叉验证，因此很容易在一次运行中获得精确的最佳提升迭代次数

	import xgboost as xgb
	
	model=xgb.XGBClassifier(random_state=1,learning_rate=0.01)
	model.fit(x_train, y_train)
	model.score(x_test,y_test)

	#Regression
	import xgboost as xgb
	
	model=xgb.XGBRegressor()
	model.fit(x_train, y_train)
	model.score(x_test,y_test)
参数

	nthread
	
	这用于并行处理，应输入系统中的核心数
	
	如果你希望在所有核心上运行，请不要输入此值。该算法将自动检测
	
	eta
	
		类似于GBM中的学习率
	
		通过缩小每一步的权重，使模型更加健壮
	
	min_child_weight
	
		定义子节点中所有观察值的最小权重和
	
		用于控制过拟合。较高的值会阻止模型学习关系，这种关系可能高度特定于为某个树所选的具体样本
	
	max_depth
	
		它用于定义最大深度
	
		更高的深度将让模型学习到非常特定于某个样本的关系
	
	max_leaf_nodes
	
		树中终端节点或叶子的最大数量
	
		可以用来代替max_depth。由于创建了二叉树，因此深度'n'将产生最多2 ^ n个叶子
	
		如果已定义，则GBM将忽略max_depth
	
	gamma
	
		仅当产生的分割能给出损失函数的正向减少时，才分割节点。Gamma指定进行分割所需的最小损失减少量。
	
		使算法保守。值可能会根据损失函数而有所不同，因此应进行调整
	
	subsample
	
		与GBM的子样本相同。表示用于每棵树随机采样的观察值的比例。
	
		较低的值使算法更加保守并防止过拟合，但是太小的值可能导致欠拟合。
	
	colsample_bytree
	
		它类似于GBM中的max_features
		
		表示要为每个树随机采样的列的比例


- Light GBM
- CatBoost




