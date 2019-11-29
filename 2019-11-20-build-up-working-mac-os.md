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


### 安装编译lightgbm

因为一些项目需要用到lightgbm，因此想在mac里安装一个lightgbm。

但是发现直接用

	pip3 install lightgbm
	
安装的lightgbm并不能用，因此需要手动编译安装。

编译lightgbm所需的编译工具，cmake、gcc

因为用的mbp2019系统默认有安装clang版本的g++，之前以为安装了gcc。后来发现是xcode-commandline安装的clang版本，并不是真正的gcc源码，因此在编译lightgbm之前需要重新安装gcc。

	brew install cmake
	brew install cmake

然后可以进到/usr/local/opt查看安装好的gcc版本

发现有gcc@9字样，于是确定是gcc-9的版本

之后因为gcc-9和gcc是并存的，为了不影响xcode，下次希望直接调用gcc的时候可以使用gcc-9字样。

下面一切就绪，可以开始编译lightgbm源码

#### 下载lightgbm

	git clone --recursive https://github.com/Microsoft/LightGBM ; cd LightGBM
	mkdir build ; cd build
	
在build目录下，设置编译工具：

	export CXX=g++-9 CC=gcc-9
	sudo cmake -DCMAKE_CXX_COMPILER=g++-9 -DCMAKE_C_COMPILER=gcc-9 ..
	
开始编译：

	sudo make -j

如果没有报错的话就表示成功编译：

第一次编译的时候发现会有

		Undefined symbols for architecture x86_64:
		"_GOMP_atomic_end", Undefinedreferenced from:
		symbols   for   architecture x86_64:
		"_GOMP_atomic_end", referenced from:
		
类似字样，发现可能是之前的cmake工具写错了，于是重新下载lightgbm源码重来一遍即可。

	
下面执行以下命令，注意cd这一步实在build路径下执行的。

	cd ../python-package
	sudo python setup.py install --precompile
	pip3 install lightgbm



    
