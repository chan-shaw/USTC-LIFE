# DOS攻击

​	今天刚上完，提前记录下，以免遗忘。

​	首先DOS 全程是 Denial of service attack, 拒绝服务攻击，目的是使得目标计算机网络或者系统资源耗尽，使得服务暂时中断或者停止，导致正常用户无法访问。

​	当黑客使用两个或者更多的被攻陷的计算机作为僵尸向特定的目标发起DOS时，就被称为拒绝服务攻击。

## 攻击现象

- 网络异常缓慢
- 特定的网站无法打开
- 无法访问任何网站
- 垃圾邮件的数量增加
- 无线或者有限网络连接异常断开
- 长时间尝试访问网站或者任何互联网服务的时候被拒绝
- 服务器容易断线和卡顿

## 攻击方式

主要有两种：

- **带宽消耗性**

  分为两个层次：

  - **泛洪攻击**

    泛洪攻击的特点就是利用僵尸程序发送大量流量至受损的受害者系统，目的在于堵塞其带宽。

  - **放大攻击**

    是通过恶意放大流量限制受害者系统的带宽；其特点是利用僵尸程序通过伪造的源IP(即攻击目标IP)向某些存在漏洞的服务器发送请求，服务器在处理请求后向伪造的源IP发送应答，由于这些服务的特殊性导致应答包比请求包更长，因此使用少量的带宽就能使服务器发送大量的应答到目标主机上。

- **资源消耗型**

​	在课堂上，老师着重于讲解了资源消耗性的DOS,切入点则是`php`的数组，php的数组是以`Hashmap`形式存储的，然后时以拉链法来处理哈希碰撞的，且由于`php`数组的最大长度是65535,所以我们构建一组具有一定规律的数字，$ 0,65536,2  \times 65536, 3 \times 65536 ... $,这样的数组在存储的时候，稳定会产生碰撞，构建的时候，查找长度就从平均长度$O(n) $ 到了最大长度$O(n^2) $，这样的话，对网页的资源消耗也更大。

​	然后，以传递字符串为例，讲解了一个找到hash碰撞构造相同的碰撞的方式。

```c
uint32_t hash(const char *arKey, uint32_t nKeyLength){
    uint32_t hash = 5381;
    
    for(;nKeyLength > 0; nKeyLength -= 1){
        hash = ((hash << 5) + hash) + *arKey++;
    }
    return hash;
}
```

换成数学表达式就是，给定一个字符串`S`,有:
$$
Hash(S_0) = 5381 \\
Hash(S_i) = Hash(S_{i-1}) \times 33 + s_i
$$
那，对于这个公式而言，我们非常容易就能找到两个$hash$相同的字串，然后根据这两个字串，我们就能够构造出多个$hash$相同的串,也就是等效字串，eg:

$ Hash(ab) = Hash(cd) $

我们要构造16个$hash$ 相同的串，有如下的方式：

设$' ab' = 0, 'cd' = 1 $

则有：

```
0000	//abababab
0001	//abababcd
...
1111	//cdcdcdcd

```

上面的字符串，$ hash$ 值相同。

同样，对于Java的`Hash函数:java.lang.String.HashCode()`如下：

```c
uint32_t hash(const char *arKey, uint32_t nKeyLength){
    uint32_t hash =0;
    for(;nKeyLength > 0; nKeyLength -= 1){
        hash = ((hash << 5) - hash) + *arKey++;
    }
    return hash;
}
```

$$
Hash(S) = \displaystyle\sum_{i=0}^n 31^{n-i} \times s_i
$$

但是针对另外一种Hash方法：

```c
uint32_t hash(const char *arKey, uint32_t nKeyLength){
    uint32_t hash =0;
    for(;nKeyLength > 0; nKeyLength -= 1){
        hash = ((hash << 5) - hash) ^ *arKey++;
    }
    return hash;
}
```

有：
$$
Hash(S_i) = [Hash(S_{i-1}) \times 31] \oplus s_i
$$
那，针对这种情况，我们尝试暴力碰撞，平均次数：

$ n = \tfrac {1 +2+3 + ... + 2^{32}} {2^{32}} = 2^{31}$次， 如果我们从2个特殊的hash中找到一个呢，平均查找次数又要除以2，所以有：
$$
Hit\space one\space  in\space  2^n\space specific \space hash \space valuse:
need \space 2^{31-n} \space times
$$
针对这个情况，设计一种中间相遇的算法，基于以下两个表达式:

- $ A \oplus B \oplus B = A \space $
- $ (33 \times 1041204193)  \equiv 1 \space mod \space 2^{32}$

基于以上两个表达式，开始计算，ABCD的hash有：

$Hash(ABCD) = \lbrace  [(hash(0)  \times 33 \oplus A) \times 33 \oplus B] \times  33 \oplus C \rbrace \times 33 \oplus D$

同时有：

$Hash(AB) = (hash(0)  \times 33\oplus A) \times 33 \oplus B$

$ HashBack(CD)= (Hash(ABCD) \oplus D \times 1041204193) \oplus C \times  1041204193$

由公式有：

$ HashBack(D) =  Hash(ABC) \times 33 \oplus D  \oplus D \times 1041204193$

$ HashBack(D) = Hash(ABC) \times 33 \times 1041204193$

$ HashBack(D)  \equiv Hash(ABC)$

同理有：

$ HashBack(CD) = Hash(AB) $

​	根据这个方法，我们就能比较快速的找到Hash冲突串。那么，确定HashBack的长度的原理是什么呢？

我们知道,每多hash一位，平均复杂度会×2，那么很明显就成为一个解方程了。 假设有ABCD四个数，我们找到一个分割，使得两堆的复杂度最小，即求
$$
\underset{\alpha}{\mathrm{argmin}} [ 2^\alpha+2^{4-\alpha}]
$$
基本不等式，可以知道，在中间的时候是最小的。

对于上述32位数来说，算法复杂度能降低到$2^{16}$。

代码如下：

```c
uint32_t hash_back(char *suffix, uint32_t length, uint32_t end){
    uint32_t hash = end;
    
    for(;length > 0; length -=1){
        hash = hash ^ suffix[length-1] * 1041204193;
    }
    return hash;
}
```

