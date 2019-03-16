以此文来记录找实习和找工作的经历

### 20190311 菜鸟一面

1.MySQL的索引底层的数据结构是什么？（B+树）

2.如何处理高并发大数据量的请求？例如同时间内有100万次请求，如何处理？（分布式多服务器负载均衡）


### 20190313 腾讯面试

1.构造函数和析构函数哪个可以有虚函数？

**构造函数不能为虚函数，而析构函数可以且常常是虚函数。**

（1）构造函数不能是虚函数。

这就要涉及到C++对象的构造问题了，C++对象在三个地方构建：（1）函数堆栈；（2）自由存储区，或称之为堆；（3）静态存储区。无论在那里构建，其过程都是两步：首先，分配一块内存；其次，调用构造函数。好，问题来了，如果构造函数是虚函数，那么就需要通过虚函数表来调用，但此时面对一块 raw memeory，到哪里去找虚函数表呢？毕竟，虚函数表是在构造函数中才初始化的啊，而不是在其之前。因此构造函数不能为虚函数。
 

（2）析构函数可以是虚函数，且常常如此

这个就好理解了，因为此时析构函数已经初始化了；况且我们通常通过基类的指针来销毁对象，如果析构函数不为虚的话，就不能正确识别对象类型，从而不能正确销毁对象。

2.派生类先调用基类构造函数还是先调用派生类？

先调用基类。

3.写一个简单的找到一个数组中第二大数的程序

4.一个结构体内的二分查找问题，不能使用递归来做。

### 20190316 ###

字节跳动笔试

第三题

有n个人站成一个圈 和他们对应的分数 要求和旁边的相比分数高的获得的礼物不能少于分数低的 然后每个人至少都要有一份礼物 问最少需要多少份礼物 

	#include<iostream>
	#include<algorithm>
	
	using namespace std;
	struct circleNode{
		int score;
		circleNode *left;
		circleNode *right;
	};
	int compare(int *score,int *presentNum,int i,int j,int k) {
		if (score[j] > score[i] && score[j] > score[k])
			return max(presentNum[i], presentNum[k]) + 1;
		if (score[j] < score[i] && score[j] < score[k])
			return 1;
		if (score[j] > score[i] && score[j] <= score[k])
			return presentNum[i] + 1;
		if (score[j] <= score[i] && score[j] > score[k])
			return presentNum[k] + 1;
		
		if (score[i] < score[j] && score[j] == score[k])
			return presentNum[i] + 1;
		if (score[i] == score[j] && score[j] > score[k])
			return presentNum[k] + 1;
		else
			return 1;
	
	}
	bool check(int *score, int *presentNum,int numOfPerson) {
		for (int i = 1; i < numOfPerson - 1; i++) {
			if (score[i] > score[i - 1] && presentNum[i] <= presentNum[i - 1])
				return false;
			if (score[i] > score[i + 1] && presentNum[i] <= presentNum[i + 1])
				return false;
	
		}
		if (numOfPerson >= 2)
		{
			if (score[0] > score[1] && presentNum[0] <= presentNum[1])
				return false;
			if (score[0] > score[numOfPerson - 1] && presentNum[0] <= presentNum[numOfPerson - 1])
				return false;
	
			if (score[numOfPerson-1] > score[numOfPerson-2] && presentNum[numOfPerson-1] <= presentNum[numOfPerson-2])
				return false;
			if (score[numOfPerson-1] > score[0] && presentNum[numOfPerson-1] <= presentNum[0])
				return false;
		}
		return true;
	}
	
	int main() {
		int n;
		cin >> n;
		while (n-- > 0) {
			int numOfPerson;
			cin >> numOfPerson;
			int *score = new int[numOfPerson];
			for (int i = 0; i < numOfPerson; i++) {
				cin >> score[i];
			}
	
			int *presentNum = new int[numOfPerson];
			for (int i = 0; i < numOfPerson; i++)
				presentNum[i] = 1;
	
			while(!check(score,presentNum,numOfPerson))
				for (int i = 0; i < numOfPerson; i++) {
					if (i == 0) {
						presentNum[i] = compare(score, presentNum, numOfPerson - 1, 0, 1);
					}
					else if (i == numOfPerson - 1)
						presentNum[i] = compare(score, presentNum, i - 1, i, 0);
					else
						presentNum[i] = compare(score, presentNum, i - 1, i, i + 1);
				}
			int sum = 0;
			for (int i = 0; i < numOfPerson; i++) {
				//cout << presentNum[i] << " ";
				sum += presentNum[i];
			}
			cout << sum << endl;
			//system("pause");
		}
	}

思考：

感觉多次循环有点冗余，应该正向循环检查一次，反向再检查一次即可。


### 网易游戏 ###

**第一题**：

约瑟夫问题原题

	#include<iostream>
	using namespace std;
	int Joseph(int m, int k)
	{
		if (m <= 0 || k <= 0)
		{	
			return -1;
		}
		else
		{
			if (m == 1)
			{
				return 0;
			}
			else
			{
				return ((Joseph(m - 1, k) + k) % m);
			}
		}
	}
	int main() {
		int n;
		int m;
		cin >> n>>m;
		cout << Joseph(n, m)+1;
	}

**第二题：巴黎铁塔翻转再翻转**

一个字符串遵循以下三种变换：

1.将第一个字母移除
2.如果长度为偶数，则反转字符串
3.将第一个字母移到最末

例如：

abccd的移除顺序为acbdc

现在已知移除顺序，求字符串

	#include<iostream>
	#include<vector>
	#include<string>
	#include<algorithm>
	using namespace std;
	
	int main() {
		vector<char> c;
		string a;
		getline(cin, a);
		for (int i = 0; i < a.length(); i++)
			c.push_back(a[i]);
		//cout << a.length() << endl;
	
		vector<char> temp;
		int num = 0;
		while (num < a.length()-1) {
			reverse(temp.begin(), temp.end());
			temp.push_back(c[c.size() - 1 - num]);
			reverse(temp.begin(), temp.end());
			reverse(temp.begin(), temp.end() - 1);
			reverse(temp.begin(), temp.end());
			if (temp.size() % 2 == 0)
				reverse(temp.begin(), temp.end());
			
			num++;
			/*for (int i = 0; i < temp.size(); i++)
				cout << temp[i];
			cout << endl;*/
		}
		
		reverse(temp.begin(), temp.end());
		temp.push_back(c[0]);
		reverse(temp.begin(), temp.end());
		for (int i = 0; i < temp.size(); i++)
			cout << temp[i];
		//system("pause");
		//cout << a;
	}

**第三题：分配车位**

有m辆车，n个车位充电，输入m辆车充电时间，问最短时间是多少

	#include<iostream>
	#include<vector>
	#include<algorithm>
	using namespace std;
	
	int min(vector<int> &a) {
		int min = a[0];
		int index = 0;
		for (int i = 1; i < a.size(); i++) {
			if (a[i] < min) {
				min = a[i];
				index = i;
			}
		}
		return index;
	}
	
	int max(vector<int> &a) {
		int max = a[0];
		int index = 0;
		for (int i = 1; i < a.size(); i++) {
			if (a[i] > max) {
				max = a[i];
				index = i;
			}
		}
		return index;
	}
	
	
	int main() {
		int m, n;
		vector<int> time;
		cin >> m >> n;
		for (int i = 0; i < m; i++) {
			int temp;
			cin >> temp;
			time.push_back(temp);
		}
	
		sort(time.begin(), time.end());
		reverse(time.begin(), time.end());
		if (m <= n)
			cout << time[0]<<endl;
		else {
			int num = m-n;
			int sum=0;
	
			vector<int> currentTime(n, 0);
			for (int i = 0; i < n; i++)
				currentTime[i] += time[i];
			while (num > 0) {
				int index = min(currentTime);
				int mi = currentTime[index];
				for (int i = 0; i < currentTime.size(); i++)
				{
					currentTime[i] -= mi;
				}
				currentTime[index] += time[m-num];
				sum += mi;
				num--;
			}
			cout << sum + currentTime[max(currentTime)]<<endl;
			//*min(currentTime.begin(), currentTime.end());
			
	
		}
		system("pause");
	}

**第四题：自己实现二元线性回归梯度下降或者最小二乘**