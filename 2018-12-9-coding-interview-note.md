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