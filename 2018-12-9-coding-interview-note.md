---
layout: default
---

#剑指Offer笔记#

## 面试题二 ##
实现一个singleton模式，设计一个类，我们只能生成该类的一个实例。

### Solution1 ###
	
	 public sealed class Singleton1
    {
        private Singleton1()
        {
        }

        private static Singleton1 instance = null;
        public static Singleton1 Instance
        {
            get
            {
                if (instance == null)
                    instance = new Singleton1();

                return instance;
            }
        }
    }

只适用于单线程环境，倘若两个线程同时判断instance是否为null，当前若无instance，则会同时创建实例，则此时不符合singleton模式。

### Solution2
	
	public sealed class Singleton2
    {
        private Singleton2()
        {
        }

        private static readonly object syncObj = new object();

        private static Singleton2 instance = null;
        public static Singleton2 Instance
        {
            get
            {
                lock (syncObj)
                {
                    if (instance == null)
                        instance = new Singleton2();
                }

                return instance;
            }
        }
    }

加锁过程太耗时，若两个同时申请加锁一个需要等待，等锁释放后却发现已经存在实例了，没有必要耗费不需要的加锁时间。

### solution 3

	public sealed class Singleton3
    {
        private Singleton3()
        {
        }

        private static object syncObj = new object();

        private static Singleton3 instance = null;
        public static Singleton3 Instance
        {
            get
            {
                if (instance == null)
                {
                    lock (syncObj)
                    {
                        if (instance == null)
                            instance = new Singleton3();
                    }
                }

                return instance;
            }
        }
    }

解决solution2的问题，只在第一次试图创建实例的时候需要加锁，确保多线程环境下也只创建一个实例。

### solution 4

使用静态构造函数

	 public sealed class Singleton4
    {
        private Singleton4()
        {
            Console.WriteLine("An instance of Singleton4 is created.");
        }

        public static void Print()
        {
            Console.WriteLine("Singleton4 Print");
        }

        private static Singleton4 instance = new Singleton4();
        //静态构造方法，在第一次使用Singleton4的时候就调用，先初始化静态变量，
        //再调用静态构造方法，然后如果有构造方法 在调用初始化非静态变量和执行构造方法
        public static Singleton4 Instance
        {
            get
            {
                return instance;
            }
        }
    }

静态构造函数，只在第一次调用的时候调用一次。在Singleton4中，实例instance并不是在第一次调用属性Singleton4.Instance的时候被创建，而是在第一次使用到Singleton4就会被创建。若在Singleton4中有一个静态方法不需要创建实例，但在调用该方法前会提前创建Instance，则会降低内存的使用效率。

### Solution 5 Best solution

	public sealed class Singleton5
    {
        Singleton5()
        {
            Console.WriteLine("An instance of Singleton5 is created.");
        }

        public static void Print()
        {
            Console.WriteLine("Singleton5 Print");
        }

        public static Singleton5 Instance
        {
            get
            {
                return Nested.instance;
            }
        }

        class Nested
        {
            static Nested()
            {
            }

            internal static readonly Singleton5 instance = new Singleton5();
        }
    }	
在内部定义一个私有类型Nested，只有在Nested被调用的时候才会使用静态构造方法生成instance。而Nested仅在Singleton5的Instance中被调用。因此，当调用不需要使用Singleton5的instance的时候，就不会触发.Net运行时调用Nested，则不会创建实例，做到按需创建。

##面试题三

**NOTE：在C/C++中，当数组作为函数的参数进行传递时，数组就自动退化为同类型的指针。**

example:

	#include<iostream>
	using namespace std;
	int GetSize(int data[]) {
		return sizeof(data);
	}
	
	int main() {
		int data1[] = { 1,2,3,4,5 };
		int size1 = sizeof(data1);
	
		int* data2 = data1;
		int size2 = sizeof(data2);
		int size3 = GetSize(data1);
		cout << size1 << " " << size2 << " " << size3 << endl;
	}

输出结果为20 4 4，size1求得是数组的大小，5个整数，每个整数占4个字节，因此占20个字节，size2，求的是int类型指针的大小，在32位系统上，对任意指针求sizeof都是4个字节。在C/C++中，当数组作为函数的参数进行传递时，数组就自动退化为同类型的指针。因此，尽管GetSize的参数data为数组，但它会退化为指针。

面试题3（一）：找出数组中重复的数字

题目：在一个长度为n的数组里的所有数字都在0到n-1的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，
也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。例如，如果输入长度为7的数组{2, 3, 1, 0, 2, 5, 3}，
那么对应的输出是重复的数字2或者3。

若用排序做，则时间复杂度为O(nlogn)。

若使用哈希表做，则时间复杂度为O(1),空间复杂度为O(n)。从头到尾扫描数组，若哈希表中不存在该元素，则将该元素添加至哈希表中，否则找到一个重复数字。

若存在重复数字，输出true，并将重复数字赋给duplication（引用）。总的时间复杂度为O(n),空间复杂度为O(1).

	bool duplicate(int numbers[], int length, int* duplication)
	{
		if (numbers == nullptr || length <= 0)
			return false;
	
		for (int i = 0; i < length; ++i)
		{
			if (numbers[i] < 0 || numbers[i] > length - 1)
				return false;
		}
	
		for (int i = 0; i < length; ++i)
		{
			while (numbers[i] != i)
			{
				if (numbers[i] == numbers[numbers[i]])
				{
					*duplication = numbers[i];
					return true;
				}
	
				// 交换numbers[i]和numbers[numbers[i]]             
				int temp = numbers[i];
				numbers[i] = numbers[temp];
				numbers[temp] = temp;
			}
		}
	
		return false;
	}

面试题3（二）：不修改数组找出重复的数字

题目：在一个长度为n+1的数组里的所有数字都在1到n的范围内，所以数组中至少有一个数字是重复的。请找出数组中任意一个重复的数字，但不能修改输入的数组。例如，如果输入长度为8的数组{2, 3, 5, 4, 3, 2, 6, 7}，那么对应的输出是重复的数字2或者3。

使用类似二分查找的思想，每次将数组以中间数为界限分成两部分，例如长度为8的数组中最大为7，则以4作为分界数，扫描一次，比4大的分一堆，比四小的分一堆，查看哪一堆的数字数大于3，则确定重复数字在哪一堆，在进行递归。

	int countRange(const int* numbers, int length, int start, int end);
	
	// 参数:
	//        numbers:     一个整数数组
	//        length:      数组的长度
	// 返回值:             
	//        正数  - 输入有效，并且数组中存在重复的数字，返回值为重复的数字
	//        负数  - 输入无效，或者数组中没有重复的数字
	int getDuplication(const int* numbers, int length)
	{
	    if(numbers == nullptr || length <= 0)
	        return -1;
	
	    int start = 1;
	    int end = length - 1;
	    while(end >= start)
	    {
	        int middle = ((end - start) >> 1) + start;
	        int count = countRange(numbers, length, start, middle);
	        if(end == start)
	        {
	            if(count > 1)
	                return start;
	            else
	                break;
	        }
	
	        if(count > (middle - start + 1))
	            end = middle;
	        else
	            start = middle + 1;
	    }
	    return -1;
	}
	
	int countRange(const int* numbers, int length, int start, int end)
	{
	    if(numbers == nullptr)
	        return 0;
	
	    int count = 0;
	    for(int i = 0; i < length; i++)
	        if(numbers[i] >= start && numbers[i] <= end)
	            ++count;
	    return count;
	}

## 面试题四 ##

二维数组中的查找

题目：在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。

每次取右上角的数字，若要寻找的数字=右上角，则找到；否则，如果比右上角数字大，则去掉当前行；如果比右上角数字小，则去掉当前列。

	bool Find(int* matrix, int rows, int columns, int number)
	{
	    bool found = false;
	
	    if(matrix != nullptr && rows > 0 && columns > 0)
	    {
	        int row = 0;
	        int column = columns - 1;
	        while(row < rows && column >=0)
	        {
	            if(matrix[row * columns + column] == number)
	            {
	                found = true;
	                break;
	            }
	            else if(matrix[row * columns + column] > number)
	                -- column;
	            else
	                ++ row;
	        }
	    }
	
	    return found;
	}

## 面试题五 ##

为了节省空间，C/C++把常量字符串放在一个单独的内存区域，当几个指针赋值给相同的常量字符串时，它们实际上会指向相同的内存地址。但用常量内存初始化数组是，情况却又不一样。

	char str1[] = "hello world";
	char str2[] = "hello world";
	
	char* str3 = "hello world";
	char* str4 = "hello world";
	
	str1==str2 -->false
	str3==str4 -->true

str1和str2是两个初始地址不同的数组，因此str1和str2不同，str3和str4
是两个指针同时指向内存中hello world的地址，由于该字符串为常量字符串，因此在内存中仅有一个拷贝，str3和str4指向同一个地址。

而在C#中，String是不可变的，一旦试图改变String的内容，就会产生一个新的实例。
	
	String str = "hello";
	str.ToUpper();
	str.Insert(0,"World");

str的内容不改变，改变的字符串会通过返回值通过新的String实例返回。若需要改变String的值，则使用StringBuilder。

面试题5：替换空格

题目：请实现一个函数，把字符串中的每个空格替换成"%20"。例如输入“We are happy.”，则输出“We%20are%20happy.”。



	/*length 为字符数组str的总容量，大于或等于字符串str的实际长度*/
	void ReplaceBlank(char str[], int length)
	{
		if (str == nullptr && length <= 0)
			return;
	
		/*originalLength 为字符串str的实际长度*/
		int originalLength = 0;
		int numberOfBlank = 0;
		int i = 0;
		while (str[i] != '\0')
		{
			++originalLength;
	
			if (str[i] == ' ')
				++numberOfBlank;
	
			++i;
		}
	
		/*newLength 为把空格替换成'%20'之后的长度*/
		int newLength = originalLength + numberOfBlank * 2;
		if (newLength > length)
			return;
	
		int indexOfOriginal = originalLength;
		int indexOfNew = newLength;
		while (indexOfOriginal >= 0 && indexOfNew > indexOfOriginal)
		{
			if (str[indexOfOriginal] == ' ')
			{
				str[indexOfNew--] = '0';
				str[indexOfNew--] = '2';
				str[indexOfNew--] = '%';
			}
			else
			{
				str[indexOfNew--] = str[indexOfOriginal];
			}
	
			--indexOfOriginal;
		}
	}

## 面试题七 ##

题目：输入某二叉树的前序遍历和中序遍历的结构，请重建该二叉树。假设输入的前序遍历和中序遍历中都不不含重复的数字。

前序遍历第一个数字就是根节点的值，找到中序遍历中该对应值的位置，则左子树的序列在根节点左边，右子树的序列在右边。可利用递归的方法分别构建左右子树。

	BinaryTreeNode* ConstructCore(int* startPreorder, int* endPreorder, int* startInorder, int* endInorder);
	
	BinaryTreeNode* Construct(int* preorder, int* inorder, int length)
	{
		if (preorder == nullptr || inorder == nullptr || length <= 0)
			return nullptr;
	
		return ConstructCore(preorder, preorder + length - 1,
			inorder, inorder + length - 1);
	}
	
	BinaryTreeNode* ConstructCore
	(
		int* startPreorder, int* endPreorder,
		int* startInorder, int* endInorder
	)
	{
		// 前序遍历序列的第一个数字是根结点的值
		int rootValue = startPreorder[0];
		BinaryTreeNode* root = new BinaryTreeNode();
		root->m_nValue = rootValue;
		root->m_pLeft = root->m_pRight = nullptr;
	
		if (startPreorder == endPreorder)
		{
			if (startInorder == endInorder && *startPreorder == *startInorder)
				return root;
			else
				throw std::exception("Invalid input.");
		}
	
		// 在中序遍历中找到根结点的值
		int* rootInorder = startInorder;
		while (rootInorder <= endInorder && *rootInorder != rootValue)
			++rootInorder;
	
		if (rootInorder == endInorder && *rootInorder != rootValue)
			throw std::exception("Invalid input.");
	
		int leftLength = rootInorder - startInorder;
		int* leftPreorderEnd = startPreorder + leftLength;
		if (leftLength > 0)
		{
			// 构建左子树
			root->m_pLeft = ConstructCore(startPreorder + 1, leftPreorderEnd,
				startInorder, rootInorder - 1);
		}
		if (leftLength < endPreorder - startPreorder)
		{
			// 构建右子树
			root->m_pRight = ConstructCore(leftPreorderEnd + 1, endPreorder,
				rootInorder + 1, endInorder);
		}
	
		return root;
	}

## 面试题八 ##

题目：给定一棵二叉树和其中的一个结点，如何找出中序遍历顺序的下一个结点？树中的结点除了有两个分别指向左右子结点的指针以外，还有一个指向父结点的指针。

如果一个节点由右子树，那么他的下一个节点就是他的右子树中的最左节点。也就是说，从右子节点出发一直沿着指向左子节点的指针，就能找到它的下一个节点。

若一个节点没有右子树，如果节点是他的父节点的左子节点，那么他的下一个节点就是他的父节点。

若一个节点既没有右子树，他还是他的父节点的右子节点，我们可以沿着指向父节点的指针一直向上遍历，直到找到一个是他父节点的左子树的节点，如果这样的节点存在，那么这个节点的父节点，就是我们要找的下一个节点。

	BinaryTreeNode* GetNext(BinaryTreeNode* pNode)
	{
		if (pNode == nullptr)
			return nullptr;
	
		BinaryTreeNode* pNext = nullptr;
		if (pNode->m_pRight != nullptr)
		{
			BinaryTreeNode* pRight = pNode->m_pRight;
			while (pRight->m_pLeft != nullptr)
				pRight = pRight->m_pLeft;
	
			pNext = pRight;
		}
		else if (pNode->m_pParent != nullptr)
		{
			BinaryTreeNode* pCurrent = pNode;
			BinaryTreeNode* pParent = pNode->m_pParent;
			while (pParent != nullptr && pCurrent == pParent->m_pRight)
			{
				pCurrent = pParent;
				pParent = pParent->m_pParent;
			}
	
			pNext = pParent;
		}
	
		return pNext;
	}

## 面试题九 ##

用两个栈实现队列

题目：用两个栈实现一个队列。队列的声明如下，请实现它的两个函数appendTail和deleteHead，分别完成在队列尾部插入结点和在队列头部删除结点的功能。

思路如下：

添加到尾部时，只需要将其push到stack1中；

若需要在头部删除节点的时候，若stack2为空，则将stack1中的元素全部push到stack2，因为元素在添加到stack1时是先进后出，因此push到stack2后又将变成先进先出（顺序会和stack1调转），此后在使用stack2.top弹出头部元素即可实现队列的先进先出。

	/**
	往队列尾部添加元素
	*/
	template<typename T>
	void CQueue::appendTail(const T& node) {
		if (stack1 == nullptr)
			stack1 = new stack<T>();
		if (stack2 == nullptr)
			stack2 = new stack<T>();
		stack1.push(node);
	}
	/**
	删除队列的头元素，并将其返回
	*/
	template<typename T>
	T CQueue::deleteHead() {
		if (stack2.size() <= 0)
		{
			while (stack1.size()>0)
			{
				T& data = stack1.top();
				stack1.pop();
				stack2.push(data);
			}
		}
	
		if (stack2.size() == 0)
			throw new exception("queue is empty");

		T head = stack2.top();
		stack2.pop();

		return head;
	}

### 关联题目：用两个队列实现一个栈 ##

要实现栈的先进后出，则先将数据存在queue1，若需要弹栈操作，则将queue1中除了最后一个元素以外所有数据都先进先出退出队列，存到queue2中，然后将最后一个元素弹出返回。若下次再要压栈操作，则将数据存在queue2。两个栈轮流使用存放数据。

	template<typename T> T CStack<T>::popStack() {
		if (flag == 0) {
			if (queue1.size() == 1) { 
				T data = queue1.front();  
				queue1.pop(); 
				return data;
			}
			//数据在queue1中
			if (queue1.size() == 0) throw new exception("stack is empty!");
			while (queue1.size() > 1) {
				T data = queue1.front();
				queue1.pop();
				queue2.push(data);
			}
	
			flag = 1;
			T result = queue1.front();
			queue1.pop();
			return result;
		}
		else {
			if (queue2.size() == 1) {
				T data = queue2.front();
				queue2.pop();
				return data;
			}
			if (queue2.size() == 0) throw new exception("stack is empty!");
			while (queue2.size() > 1) {
				T data = queue2.front();
				queue2.pop();
				queue1.push(data);
			}
	
			flag = 0;
			T result = queue2.front();
			queue2.pop();
			return result;
		}
	}
	
	template<typename T> void CStack<T>::pushStack(const T& node) {
		if (flag == 1) {
			queue2.push(node);
		}
		else {
			queue1.push(node);
		}
	}

## 算法和数据操作

如果面试题要求在二维数组上搜索路径，那么可以尝试用回溯法。回溯法很适合使用递归的代码实现，当不可使用递归的时候可以使用栈来模拟递归的过程。

若是求某个问题的最优解且可以分成多个子问题，则可以尝试动态规划，为避免不必要的重复计算，使用自下而上的的循环代码来实现，也就是把子问题的最优解先算出来再用数组保存下来，接下来基于子问题的解求解大问题的解。

若有提醒说在分解子问题的时候是否存在某个特殊的选择，如果采用这个选择将一定能得到最优解，那么通常意味着这个面试题可以适用于贪婪算法。

## 各种排序算法比较

###1 快速排序（QuickSort）

快速排序是一个就地排序，分而治之，大规模递归的算法。从本质上来说，它是归并排序的就地版本。快速排序可以由下面四步组成。

（1） 如果不多于1个数据，直接返回。

（2） 一般选择序列最左边的值作为支点数据。

（3） 将序列分成2部分，一部分都大于支点数据，另外一部分都小于支点数据。

（4） 对两边利用递归排序数列。

快速排序比大部分排序算法都要快。尽管我们可以在某些特殊的情况下写出比快速排序快的算法，但是就通常情况而言，没有比它更快的了。快速排序是递归的，对于内存非常有限的机器来说，它不是一个好的选择。 

###2 归并排序（MergeSort）

归并排序先分解要排序的序列，从1分成2，2分成4，依次分解，当分解到只有1个一组的时候，就可以排序这些分组，然后依次合并回原来的序列中，这样就可以排序所有数据。合并排序比堆排序稍微快一点，但是需要比堆排序多一倍的内存空间，因为它需要一个额外的数组。

###3 堆排序（HeapSort）

堆排序适合于数据量非常大的场合（百万数据）。

堆排序不需要大量的递归或者多维的暂存数组。这对于数据量非常巨大的序列是合适的。比如超过数百万条记录，因为快速排序，归并排序都使用递归来设计算法，在数据量非常大的时候，可能会发生堆栈溢出错误。

堆排序会将所有的数据建成一个堆，最大的数据在堆顶，然后将堆顶数据和序列的最后一个数据交换。接下来再次重建堆，交换数据，依次下去，就可以排序所有的数据。

###4 Shell排序（ShellSort）

Shell排序通过将数据分成不同的组，先对每一组进行排序，然后再对所有的元素进行一次插入排序，以减少数据交换和移动的次数。平均效率是O(nlogn)。其中分组的合理性会对算法产生重要的影响。现在多用D.E.Knuth的分组方法。

Shell排序比冒泡排序快5倍，比插入排序大致快2倍。Shell排序比起QuickSort，MergeSort，HeapSort慢很多。但是它相对比较简单，它适合于数据量在5000以下并且速度并不是特别重要的场合。它对于数据量较小的数列重复排序是非常好的。

###5 插入排序（InsertSort）

插入排序通过把序列中的值插入一个已经排序好的序列中，直到该序列的结束。插入排序是对冒泡排序的改进。它比冒泡排序快2倍。一般不用在数据大于1000的场合下使用插入排序，或者重复排序超过200数据项的序列。

###6 冒泡排序（BubbleSort）

冒泡排序是最慢的排序算法。在实际运用中它是效率最低的算法。它通过一趟又一趟地比较数组中的每一个元素，使较大的数据下沉，较小的数据上升。它是O(n^2)的算法。

###7 交换排序（ExchangeSort）和选择排序（SelectSort）

这两种排序方法都是交换方法的排序算法，效率都是 O(n2)。在实际应用中处于和冒泡排序基本相同的地位。它们只是排序算法发展的初级阶段，在实际中使用较少。

###8 基数排序（RadixSort）

基数排序和通常的排序算法并不走同样的路线。它是一种比较新颖的算法，但是它只能用于整数的排序，如果我们要把同样的办法运用到浮点数上，我们必须了解浮点数的存储格式，并通过特殊的方式将浮点数映射到整数上，然后再映射回去，这是非常麻烦的事情，因此，它的使用同样也不多。而且，最重要的是，这样算法也需要较多的存储空间。

![](https://i.imgur.com/tnafAfH.png)

（转载自(http://flyingbread.cnblogs.com/)）

## 回溯法 ##

回溯法可以看作是brutal force的升级版，可以将解决问题的所有选项看成是一个树状结构。从根节点到叶节点的一条路径可以看作是问题的一个解，若路径满足题目要求，则找到一个解决方案。若在叶节点不满足条件，我们需要回溯到它的上一个节点寻找其他选项，若该节点所有选项都尝试过了，则在回到上一节点。若所有路径都不满足，则问题无解。

## 面试题13：机器人的运动范围##


题目：地上有一个m行n列的方格。一个机器人从坐标(0, 0)的格子开始移动，它每一次可以向左、右、上、下移动一格，但不能进入行坐标和列坐标的数位之和大于k的格子。例如，当k为18时，机器人能够进入方格(35, 37)，因为3+5+3+7=18。但它不能进入方格(35, 38)，因为3+5+3+8=19。请问该机器人能够到达多少个格子？

	int movingCountCore(int threshold, int rows, int cols, int row, int col, bool* visited);
	bool check(int threshold, int rows, int cols, int row, int col, bool* visited);
	int getDigitSum(int number);
	
	int movingCount(int threshold, int rows, int cols)
	{
		if (threshold < 0 || rows <= 0 || cols <= 0)
			return 0;
	
		bool *visited = new bool[rows * cols];
		for (int i = 0; i < rows * cols; ++i)
			visited[i] = false;
	
		int count = movingCountCore(threshold, rows, cols,
			0, 0, visited);
	
		delete[] visited;
	
		return count;
	}
	
	int movingCountCore(int threshold, int rows, int cols, int row,
		int col, bool* visited)
	{
		int count = 0;
		if (check(threshold, rows, cols, row, col, visited))
		{
			visited[row * cols + col] = true;
	
			count = 1 + movingCountCore(threshold, rows, cols,
				row - 1, col, visited)
				+ movingCountCore(threshold, rows, cols,
					row, col - 1, visited)
				+ movingCountCore(threshold, rows, cols,
					row + 1, col, visited)
				+ movingCountCore(threshold, rows, cols,
					row, col + 1, visited);
		}
	
		return count;
	}
	//判断能否进入row，col的格子
	bool check(int threshold, int rows, int cols, int row, int col,
		bool* visited)
	{
		if (row >= 0 && row < rows && col >= 0 && col < cols
			&& getDigitSum(row) + getDigitSum(col) <= threshold
			&& !visited[row* cols + col])
			return true;
	
		return false;
	}
	
	int getDigitSum(int number)
	{
		int sum = 0;
		while (number > 0)
		{
			sum += number % 10;
			number /= 10;
		}
	
		return sum;
	}

## 动态规划以及贪心算法 ##

若我们需要求一个问题的最优解，通常可以将其分解成若干个子问题，且子问题也可能有最优解。如果我们可以将小问题的最优解组合起来得到整个大问题的最优解。那么就可以使用动态规划来解决。为避免不必要的重复计算，使用自下而上的的循环代码来实现，也就是把子问题的最优解先算出来再用数组保存下来，接下来基于子问题的解求解大问题的解。

### 面试题14：剪绳子 ###

题目：给你一根长度为n绳子，请把绳子剪成m段（m、n都是整数，n>1并且m≥1）。每段的绳子的长度记为k[0]、k[1]、……、k[m]。k[0]*k[1]*…*k[m]可能的最大乘积是多少？例如当绳子的长度是8时，我们把它剪成长度分别为2、3、3的三段，此时得到最大的乘积18。

我们使用一个长度为length+1的int数组存储长度为i的最优解。对于每一个子问题，我们通过循环求解可能存在的每一种情况，并将最大值保存在production数组中。

	int maxProductAfterCutting_dynamicProgramming(int length) {
		if (length < 2)
			return 0;
		if (length == 2)
			return 1;
		if (length == 3)
			return 2;
	
		int *production = new int[length + 1];
		production[0] = 0;
		production[1] = 1;
		production[2] = 2;
		production[3] = 3;
	
		for (int i = 4; i < length + 1; i++) {
			int max = 0;
			//循环求解子问题的最优解
			for (int j = 1; j <= i / 2; j++) {
				int temp = production[i - j] * production[j];
				max = max > temp ? max : temp;
			}
			production[i] = max;
		}
		int max = production[length];
		delete[] production;
		return max;
	}

### 贪婪算法 ###

在用贪心算法解决问题的时候，每一步都可以做出一个贪婪的选择，基于此选择，我们可以确定能够得到最优解。

在这个问题中，我们的贪心策略是尽可能多地剪长度为3的线段。

	int maxProductAfterCutting_greedy(int length)
	{
		if (length < 2)
			return 0;
		if (length == 2)
			return 1;
		if (length == 3)
			return 2;
	
		// 尽可能多地减去长度为3的绳子段
		int timesOf3 = length / 3;
	
		// 当绳子最后剩下的长度为4的时候，不能再剪去长度为3的绳子段。
		// 此时更好的方法是把绳子剪成长度为2的两段，因为2*2 > 3*1。
		if (length - timesOf3 * 3 == 1)
			timesOf3 -= 1;
	
		int timesOf2 = (length - timesOf3 * 3) / 2;
	
		return (int)(pow(3, timesOf3)) * (int)(pow(2, timesOf2));
	}

## 位运算 ##

在右移运算符处理有符号数时，需要用数字的符号位填补原来的n位。

例如： 1000110>>2=1110001

### 面试题15：二进制中的1 ###

题目：请实现一个函数，输入一个整数，输出该数二进制表示中1的个数。例如把9表示成二进制是1001，有2位是1。因此如果输入9，该函数输出2。


可能存在死循环的算法 

	int NumberOf1_wrongSolution(int n) {
		int count = 0;
		while (n) {
			if (n & 1)
				count++;
			n = n >> 1;
		}
		return count;
	}

如果输入的为一个负数，例如0x800000，若右移一位，并不是0x400000，而是0xC00000，若不断右移，则会变成0xFFFFFF,陷入死循环。

为避免死循环，我们可以采用以下思路：

	int NumberOf1_Solution1(int n)
	{
		int count = 0;
		unsigned int flag = 1;
		while (flag)
		{
			if (n & flag)
				count++;
	
			flag = flag << 1;
		}
	
		return count;
	}

提高效率的话：可以发现把一个整数减去1，在和原来的整数做与运算，会把该整数最右边的1变为0.那么有多少个1就会执行多少次。

	int NumberOf1_Solution2(int n)
	{
		int count = 0;
	
		while (n)
		{
			++count;
			n = (n - 1) & n;
		}
	
		return count;
	}
