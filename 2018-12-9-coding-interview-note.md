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

