---
layout: default
---

# 神经网络笔记之感知机 #
由于最近需要开始研究神经网络方面的项目，于是乎在重新再看Geoffrey Hinton大神的Neural Network for machine learning的公开课，跟着Hinton大神再把神经网络的东西过一遍。

## 神经网络的分类 ##
**Feed-forward neural networks**

![](https://i.imgur.com/lq4eOrh.png)

**Recurrent neural networks(RNN)**

![](https://i.imgur.com/3dk3M5r.png)



- Recurrent neural networks are a very natural way to model sequential data.

- They have the ability to remember information in their hidden state for a long time. 
	

**Symmetrically connected networks**

和RNN结构类似，但是单元之间的连接是对称的(每个方向具有相同的权重)。

## 第一代神经网络--感知机（Perceptrons） ##

![](https://i.imgur.com/cyNRAuo.png)

基本结构和现在的Neural Network很像，如下图所示：（有bias）

![](https://i.imgur.com/T88CjBp.png)

那么，感知机是怎么训练其参数的呢？
大致算法如下：

- If the output unit is correct,leave its weights alone.
- If the output unit is incorrectly outputs a zero, add the input vector to the weight vector.
- If the output unit is incorrectly outputs a one, substract the input vector to the weight vector.

简而言之，只对分类错误的样本作反应，真值为1的样本在权重上加上输入向量，真值为0的样本在权重上减去输入向量。无限循环，直至收敛。

### 感知机的几何解释

这里不是使用权重作为超平面，输入数据作为空间内点的解释方式；相反，这里使用权重作为空间内点，每一维对应于一维权重，输入数据被看做constrain，限制合法权重的空间。

具体的，输入数据和权重维数相同（使用bias，而不是threshold），则每个输入数据可以对应于权重空间中的一个向量（起始点为原点），则对该数据的分类取决于权重向量（起始点为原点）和输入数据向量的夹角是锐角还是钝角；换种表示为，与输入数据垂直的超平面对权重空间做了划分，位于输入数据向量一侧的权重空间会把输入数据判为正样本，相反则会判为负样本。基于输入数据的真值，即可确定能够对输入数据正确分类的权重空间。

如下图所示，真值为1的输入数据，输入数据向量为图中蓝色箭头所示，与其垂直的超平面为黑色直线所示，因为真值为1，所以绿色箭头因与输入数据向量夹角为锐角，所以为合法权重空间，相反红色箭头所示权重为非法权重。

![](https://i.imgur.com/q9K8QJd.png)

而当输入数据真值为0的时候，则确定的合法权重空间则刚好相反：

![](https://i.imgur.com/NNO8TME.png)

多个输入数据，每个输入数据-真值对，都会对合法权重空间加以限制，最终满足所有输入数据限制的权重空间，其内对应的所有权重都可以将所有输入数据正确分类。因为所有的输入数据限制都是通过一个过原点的超平面划分，所以最终确定的合法权重空间必然是一个圆锥形。若输入数据线性可分，则必然存在一个合法权重空间，且合法权重无限；相反则不会出现此合法权重空间，即不存在某个权重可以正确分类所有输入数据。注意到合法空间是连续的，即任意两个合法权重的均值仍然是合法的，进而推出该问题是凸问题（convex）。 

![](https://i.imgur.com/cGGwRsQ.png)

以两个输入数据为例子，其可以确定的合法权重空间如下图所示：

![](https://i.imgur.com/BQcaAO8.png)

###Octave实现的感知机训练

在Chapter3的编程训练中，就要求使用Octave实现感知机的训练，其中关键的代码如下：

	function [w] = update_weights(neg_examples, pos_examples, w_current)
	%% 
	% Updates the weights of the perceptron for incorrectly classified points
	% using the perceptron update algorithm. This function makes one sweep
	% over the dataset.
	% Inputs:
	%   neg_examples - The num_neg_examples x 3 matrix for the examples with target 0.
	%       num_neg_examples is the number of examples for the negative class.
	%   pos_examples- The num_pos_examples x 3 matrix for the examples with target 1.
	%       num_pos_examples is the number of examples for the positive class.
	%   w_current - A 3-dimensional weight vector, the last element is the bias.
	% Returns:
	%   w - The weight vector after one pass through the dataset using the perceptron
	%       learning rule.
	%%
	w = w_current;
	num_neg_examples = size(neg_examples,1);
	num_pos_examples = size(pos_examples,1);
	for i=1:num_neg_examples
	    this_case = neg_examples(i,:);
	    x = this_case'; %Hint
	    activation = this_case*w;
	    if (activation >= 0)
	        %YOUR CODE HERE
	        w = w-x;
	    end
	end
	for i=1:num_pos_examples
	    this_case = pos_examples(i,:);
	    x = this_case';
	    activation = this_case*w;
	    if (activation < 0)
	        %YOUR CODE HERE
	        w = w+x;
	    end
	end

关键之处在两个for语句当中，第一个for是对数据集中的所有negative(=0)的样本进行计算，倘若计算得到的activation>=0(分类=1),则说明分类出错,则要将weight减去输入向量x,注意：这里的x是this_case的转置矩阵。类似的，第二个for是对数据集中的所有positive(=1)的样本进行计算，倘若计算得到的activation<0(分类=0),则说明分类出错,则要将weight加上输入向量x。

