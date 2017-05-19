---
layout: default
---

# 使用Python几个库打造自己的REPL #

当我们使用python为用户提供应用接口的时候，有时需要自己为用户打造专属REPL，接下来我们便使用Prompt Toolkit，Click，Pygments和Fuzzy Finder简单打造一个专属的用户命令行工具。我们以打造一个SQL命令行工具为例子：

## Python Prompt Toolkit ##
Prompt Toolkit这个库是打造REPL的瑞士军刀，可以代替readline和curses等库的功能。

首先我们先从简单入手，先打造一个会回显你输入的内容的REPL:
![](http://i.imgur.com/yCueh1T.png)

此时的Prompt Toolkit代替了readline的功能

接下来我们添加输入命令历史纪录的功能，使得REPL可以通过上下按钮访问到历史命令，而且我们还可以通过CTRL-R来对命令进行搜索。
![](http://i.imgur.com/rk9890K.png)

然后我们添加自动提示功能和自动填充功能，当我们输入命令的时候，REPL会根据我们的输入提示可能的命令，并可以根据我们给出的库自动填充命令：
![](http://i.imgur.com/kf277Dx.png)

![](http://i.imgur.com/VKa0mlo.png)

## Click模块 ##
当有时用户输入的命令太长的时候，直接使用命令行输入很很难进行调试，因此我们使用Click模块，使得用户输入长命令的时候可以在文本文档先进行书写，再导入到REPL进行执行。需要加入以下代码：

    import click
	message = click.edit()

## Fuzzy Finder ##
当我们需要对命令进行模糊匹配的时候，此时我们可以使用Fuzzy Finder。
基本用法如下：
![](http://i.imgur.com/qb14oo0.png)

将模糊匹配的功能集成到SQL REPL：
![](http://i.imgur.com/lVAV3LK.png)

当我们输入se的时候，系统就会自动对SQLKeywords中的关键词进行模糊匹配，并提醒我们可能输入的是什么。

## Pygments ##
为SQL REPL添加关键词高亮的功能时，我们要用到Pygments这个库，通过关键词的高亮显示可以帮助用户及早发现语句的结构错误。使用Pygments之前，要先用pip工具进行安装：

    pip install Pygments

将高亮功能集成到SQL REPL中：


## Conclusion ##
至此，我们通过使用Prompt Toolkit,Click,Pygments,Fuzzy Finder这四个库开发了一个简易的SQL REPL。当我们在为用户开发用户命令行的时候，这些库显然可以很容易帮助我们建立一个更好的REPL。