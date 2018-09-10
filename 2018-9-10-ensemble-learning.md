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

bagging是bootstrap aggregating的简写，bootstrap是一种有放回的抽样方法，目的是为了得到统计量的分布以及置信区间，