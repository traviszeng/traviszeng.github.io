---
layout: default
---

## 二叉搜索树 ##

### 二叉搜索树的插入算法

为了向二叉搜索树中插入一个新元素，必须先检查这个元素是否在树中存在。因此需要在插入之前先使用搜索算法搜索插入的新元素。如果搜索成功，则说明已在树中存在该元素。如果搜索不成功，那么将新节点添加到搜索操作停止的地方。

	//私有函数：在以ptr为根的二叉搜索树中插入值为e1的结点。
	//若在树中已有含e1的结点则不插入，返回false；若成功插入，返回true.
	template <typename E, typename K>bool BST<E, K>::Insert(E &e1, BSTNode<E, K> *&ptr) {
		if (ptr == NULL) {   //新结点作为叶结点插入	
			ptr = new BSTNode<E, K>(e1);
			assert(ptr);
			return true;
		}
		if (e1 < ptr->data) {			//左子树插入
	
			return Insert(e1, ptr->left);
		}
		if (e1 > ptr->data) {	//右子树插入
			return Insert(e1, ptr->right);
		}
		return false;			//x已在树中,不再插入
	}

需要关注参数列表中的引用型指针参数ptr。当ptr!=NULL的时候，它一定指向一棵子树的根，可用它所包含的关键码和给定的值进行比较以继续搜索插入位置。如果ptr=NULL，一定递归到空树的位置，此时将创建的新节点的地址送给ptr，因为ptr是实参（即为搜索失败前ptr所停留的最后节点的left或者right域）的别名，新节点的地址自然送入上述某一个指针域，从而自动将新节点连入二叉搜索树中。


### 二叉搜索树的删除算法 ###

思路如下：

1.如果被删除节点的右子树为空，可以拿它的左子女节点顶替他的位置，再释放它。

2.如果被删除节点的左子树为空，可以拿它的右子女节点顶替。

3.若左右节点都不为空，则在右子树上找中序第一个子女（关键码最小）填补。也可以找左子树的中序最后一个子女（关键码最大）填补。

	//在以 ptr 为根的二叉搜索树中删除含 x 的结点
	template <typename E, typename K>bool BST<E, K>::Remove(K x, BSTNode<E, K> *& ptr) {
		if (!ptr) {
			return false;
		}
	
		BSTNode<E, K> *temp;
		if (x < ptr->data.key) { //在左子树中执行删除
			return Remove(x, ptr->left);
		}
		else if (x > ptr->data.key) {//在右子树中执行删除
			return Remove(x, ptr->right);
		}
		else if (ptr->left != NULL && ptr->right != NULL) {//ptr指示关键码为x的结点，它有两个子女
			temp = ptr->right;		//到右子树搜寻中序下第一个结点
			while (temp->left != NULL) {
				temp = temp->left;
			}
			ptr->data = temp->data;	//用该结点数据代替根结点数据，这样存在两个值为取代x的结点（temp结点和根结点）
			return Remove(temp->data.key, ptr->right);//删除temp结点（原来的值为temp->data的节点删除）
		}
		else {//ptr指示关键码为x的结点有一个子女
			temp = ptr;
			if (ptr->left == NULL) {
				ptr = ptr->right;
			}
			else {
				ptr = ptr->left;
			}
			delete temp;
			return true;
		}
	}

