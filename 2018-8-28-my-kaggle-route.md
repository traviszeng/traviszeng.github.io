---
layout: default
---


# Kaggle学习之路 #

8.28开始在Coursera上学习[How to Win a Data Science Competition: Learn from Top Kagglers](https://www.coursera.org/learn/competitive-data-science)

### Kaggle竞赛的五个要素 ###

- Data
- Model
- Submission
- Evaluation
- Leaderboard

数据会分为两个部分，public和private，private用于最后的测试评估，public则帮助你校验自己模型的准确性，并反映在leaderboard上。

![](https://i.imgur.com/4vrR0rO.png)

一次完整的competition过程如下：

![](https://i.imgur.com/0ktCSWg.png)

常见data science competition平台：

- Kaggle
- DrivenData
- CrowdAnalityx
- CodaLab
- DataScienceChallenge.net
- Datascience.net
- Single-competition sites(like KDD,VizDooM)


### Kaggle竞赛 vs Real world application ###


- Things need to think about:

![](https://i.imgur.com/Vg2143A.png)

### 几种常用模型简介 ###

- Linear Model(Logistic regression,SVM)
- Tree-based model
- KNN
- Neural networks

Disadvantages of Random Forest:

1. Random forests 在小训练集上训练效果较差
2. Random forests是一种预测工具，而不是解释工具，无法查看和理解自变量和因变量之间的关系。
3. 相较于决策树，Random forests训练代价较大。
4. 回归问题下，决策树和随机森林的因变量范围由训练集中的值确定，不能获得训练数据之外的值

Advantages of Random Forests:

1. Since we are using multiple decision trees, the bias remains same as that of a single decision tree. However, the variance decreases and thus we decrease the chances of overfitting. I have explained bias and variance intuitively at The curse of bias and variance.

2. When all you care about is the predictions and want a quick and dirty way out, random forest comes to the rescue. You don't have to worry much about the assumptions of the model or linearity in the dataset. 

Conclusion:

- There is no “silver bullet” algorithm
- Linear models split space into 2 subspaces
- Tree-based methods splits space into boxes 
- k-NN methods heavy rely on how to measure points “closeness”
- Feed-forward NNs produce smooth non-linear decision
boundary

### Feature preprocessiong and generation ##

- Numeric features

![](https://i.imgur.com/W6qyTD9.png)

![](https://i.imgur.com/Lv8Ynxc.png)

- Categorical and ordinal features

![](https://i.imgur.com/BH3NkVl.png)


- Datetime and coordinates

![](https://i.imgur.com/cKKIflU.png)

- Handling missing values

![](https://i.imgur.com/mzVb80k.png)

[更多关于特征工程的资料1](https://machinelearningmastery.com/discover-feature-engineering-how-to-engineer-features-and-how-to-get-good-at-it/ "特征工程")

[更多关于特征工程的资料2](https://www.quora.com/What-are-some-best-practices-in-Feature-Engineering)

### 自然语言处理

Word of bags

![](https://i.imgur.com/mjataqs.png)

TF-IDF

![](https://i.imgur.com/rnewKhL.png)

![](https://i.imgur.com/O7YdRNq.png)

N-grams

![](https://i.imgur.com/oQhPFui.png)

Text preprocessing

- Lowercase

![](https://i.imgur.com/JJArXs0.png)

- Lemmatization and stemming

![](https://i.imgur.com/yVlzDal.png)

- Stop words

![](https://i.imgur.com/e5CIgU1.png)


**Conclusion**

![](https://i.imgur.com/xZtJl0r.png)