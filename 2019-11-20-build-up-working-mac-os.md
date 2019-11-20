# Mac OS捣鼓笔记
记录建立可用于编程工作的mac

### 将homebrew的源设置为国内源

直接使用homebrew有时候update brew需要时间特别久，可以将brew的更新源设置为国内源，加快软件下载速度。

在替换源时需要设置三个仓库地址：

#### 1.替换/还原brew.git的源

	#替换成阿里的brew.git源:
	cd "$(brew --repo)"
	git remote set-url origin https://	mirrors.aliyun.com/homebrew/brew.git
 
	# 还原为官方提供的 brew.git源:
	cd "$(brew --repo)"
	git remote set-url origin https://github.com/Homebrew/brew.git
	
#### 2.替换/还原homebrew-core.git的源

	#替换成阿里的homebrew-core.git源:
	cd "$(brew --repo)/Library/Taps/homebrew/homebrew-core"
	git remote set-url origin https://mirrors.aliyun.com/homebrew/homebrew-core.git
	
	# 还原为官方提供的 homebrew-core.git源
	cd "$(brew --repo)/Library/Taps/homebrew/homebrew-core"
	git remote set-url origin https://github.com/Homebrew/homebrew-core.git
	
#### 3.替换/还原homebrew-bottles

	# 替换 homebrew-bottles 访问 URL:
	echo 'export HOMEBREW_BOTTLE_DOMAIN=https://mirrors.aliyun.com/homebrew/homebrew-bottles' >> ~/.bash_profile
	source ~/.bash_profile
	
	# 还原为官方提供的 homebrew-bottles 访问地址
	vi ~/.bash_profile
	# 然后，删除 HOMEBREW_BOTTLE_DOMAIN 这一行配置
	source ~/.bash_profile
	
### 为xcode添加头文件路径和链接库文件

1.添加头文件依次找到 
Header Search Paths: 添加#include <>的路径 
User Header Search Paths: 添加#include “”路径

2.添加库文件 
Library Search Paths: 添加库所在目录 
Other Linker Flags: 比如要链接的库是libboost_regex.a,那么此处应该添加-lboost_regex即可


