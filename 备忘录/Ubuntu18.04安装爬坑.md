---
title: Ubuntu18.04爬坑
date: 2018-11-27 16:17:01
tags:
---
# Ubuntu 18.04 安装爬坑指南

## 安装过程

### 镜像

[清华镜像](https://mirrors.tuna.tsinghua.edu.cn/)

[中科大镜像](https://mirrors.ustc.edu.cn/)

### 制作USB安装盘

[refus](https://rufus.ie/zh_CN.html)

[ultraiso](https://www.ultraiso.com/)

### 安装

分区（[别人家的分区](https://blog.csdn.net/qq_37258787/article/details/80270463))：

- / 20G
- swap 500M
- /boot:500M
- /Home:剩下的所有

## 显卡配置

### 开机循环或者卡住

禁用KMS，开机之后在引导界面，按`E`,在`quiet splash`后面添加：

```
acpi_osi=linux nomodese  #不载入所有有关显卡的驱动
nouveau.modeset=0  #关闭nvidia的驱动
```

### close nouveau

修改`blacklist.conf`

```bash
sudo gedit /etc/modprobe.d/blacklist.conf
```

在末尾加上几行

```bash
blacklist nouveau
options nouveau modeset=0
# 下面的我也不知大是不是必须的。。。。
blacklist vga16fb
blacklist rivafb
blacklist nvidiafb
blacklist rivatv
```

修改`blacklist-nouveau.conf`

```bash
sudo gedit /etc/modprobe.d/blacklist-nouveau.conf
```

同样加上几行

```bash
blacklist nouveau
blacklist lbm-nouveau
options nouveau modeset=0
alias nouveau off
alias lbm-nouveau off
```

保存，然后运行命令

```bash
sudo update-initramfs -u
```

重启，运行命令

```bash
lsmod|grep nouveau
```

结果不会显示任何东西

### 更换国内镜像源

[清华源](https://mirrors.tuna.tsinghua.edu.cn/help/ubuntu/)	[蜗壳源](https://mirrors.ustc.edu.cn/help/ubuntu.html)

直接编辑`/etc/apt/sources.list`文件，需要`sudo`权限。更改完`sources.list`之后，运行`sudo apt-get update`更新索引生效。

### Install NVIDIA Driver

```bash
 ubuntu-drivers devices
```

首先识别显卡型号和推荐驱动模组。然后卸载原先的驱动

```bash
sudo apt remove --purge nvidia*
```



几个方法

- 直接在Softwares & Driver里面下载安装

- `sudo sudo ubuntu-drivers autoinstall`然后重启

- 使用PPA仓库自动化安装

  将`ppa:graphics-drivers/ppa`加入到系统存储库，然后安装

  ```bash
  sudo add-apt-repository ppa:graphics-drivers/ppa
  sudo apt update
  sudo apt install nvidia-390 #显卡型号根据推荐的来选择
  ```

- 在NVIDIA官网下载驱动安装，我习惯放在Downloads

在我吸取了足够多的人生经验之后放弃尝试第一种和第二种方法了。。。我个人更喜欢这种。

查看显卡信息

```bash
lspci -vnn | grep VGA
```

下载[NVIDIA官方驱动](https://www.nvidia.com/Download/index.aspx),存储到相应路径。进入命令行界面：`Ctrl-Alt+F1`输入用户名密码

```bash

cd Downloads
sudo chmod +x NVIDIA-Linux-x86_64-390.77.run
sudo ./NVIDIA-Linux-x86_64-390.77.run -no-x-check -no-nouveau-check -no-opengl-files
```

- `–no-opengl-files `只安装驱动文件，不安装OpenGL文件。这个参数最重要
- `–no-x-check `安装驱动时不检查X服务
- `–no-nouveau-check` 安装驱动时不检查nouveau

后面的两个参数不是最重要的。

安装过程选项：

除下面所述，选`OK`就完事了。

- The distribution-provided pre-install script failed! Are you sure you want to continue? 选择 yes 继续。

- Would you like to register the kernel module souces with DKMS? This will allow DKMS to automatically build a new module, if you install a different kernel later? 选择 No 继续。

- Would you like to run the nvidia-xconfigutility to automatically update your x configuration so that the NVIDIA x driver will be used when you restart x? Any pre-existing x confile will be backed up. 选择 Yes 继续

挂载驱动

```bash
modprobe nvidia
```

查看是否安装成功

```bash
nvidia-smi
```

## 常用软件配置

### 安装Chrome

```bash
wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo dpkg -i *.deb
```

### 卸载一些无用的链接

```bash
sudo apt-get remove unity-webapps-common #卸载亚马逊

sudo apt-get remove thunderbird totem rhythmbox empathy brasero simple-scan gnome-mahjongg aisleriot 

sudo apt-get remove gnome-mines cheese transmission-common gnome-orca webbrowser-app gnome-sudoku  landscape-client-ui-install    

sudo apt-get remove onboard deja-dup

sudo apt-get remove libreoffice-common #卸载LibreOffice
```

### 安装Gdebi

```bash
sudo apt-get install gdebi
```

### 安装WPS

[官网下载安装](http://www.wps.cn/product/wpslinux/)

### 安装搜狗输入法

[官网下载安装](https://pinyin.sogou.com/linux/?r=pinyin)

### 安装网易云

[安装](https://music.163.com/#/download)

依赖修复

```bash
sudo apt --fix-broken install
```

玄学启动，启动网易云后，进电源操作界面然后回到主界面。。。

### Git&Vim&Curl&toscks,jq

```bash
sudo apt-get install git  #git

sudo apt-get install vim  #vim

sudo apt-get install curl tsocks jq # curl,toscks,jq
```

### 翻墙(填坑不忘再挖坑，VPN还没过期，嘻嘻)

#### 安装启动ssr

#### 配置

## 美化设置

### 安装Gnome-tweak-tool

```bash
sudo apt-get install gnome-tweak-tool
```

- 打开软件，扩展
- 启动两个插件
- 打开软件商城，附加组件
- 安装
  - user themes
  - dash to dock
  - hide top bar
  - weather in the clock

### 安装ocs-url

```bash
wget https://www.linux-apps.com/p/1136805/startdownload?file_id=1517920714&file_name=ocs-url_3.0.3-0ubuntu1_amd64.deb&file_type=application/x-debian-package&file_size=54198&url=https%3A%2F%2Fdl.opendesktop.org%2Fapi%2Ffiles%2Fdownloadfile%2Fid%2F1517920714%2Fs%2F95a8a998d7917cbf5d33ba03ceaa334b%2Ft%2F1523007214%2Fu%2F71764%2Focs-url_3.0.3-0ubuntu1_amd64.deb

sudo dpkg -i ocs-url_3.0.3-0ubuntu1_amd64.deb
```

### 安装Gnome Them Shell

打开[Gnome-Look](https://www.gnome-look.org/browse/cat/134/)，安装喜欢的主题.

安装图标包

打开[Flat Remix ICON theme](https://www.gnome-look.org/p/1012430/)

重启

### 安装VSCODE

[官网](https://code.visualstudio.com/)

[FAQ](https://code.visualstudio.com/docs/supporting/faq)

### 安装Toolbox

[官网](https://www.jetbrains.com/toolbox/app/)

### 安装guake

```bash
sudo apt-get install guake
```

### 安装zsh

```bash
sudo apt-get install zsh
#安装oh-my-zsh
wget https://github.com/robbyrussell/oh-my-zsh/raw/master/tools/install.sh -O - | sh
# 在.zshrc中修改自己喜欢的主题
```



### 安装albert

```bash
sudo add-apt-repository ppa:noobslab/macbuntu
sudo apt-get update
sudo apt-get install albert
```

## 坑二 CUDA+cnDNN+Tensorflow(Pytorch)安装

暂时用不上，挖着先

## 安装各种语言爬坑

### Python(安装Anaconda)

```bash
cd /tmp
#下载从Anaconda官网复制的链接
curl -O https://repo.anaconda.com/archive/Anaconda3-5.1.0-Linux-x86_64.sh

#通过SHA-256验证数据完整性
sha256sum Anaconda3-5.1.0-Linux-x86_64.sh

#运行脚本，根据提示往下走
bash Anaconda3-5.1.0-Linux-x86_64.sh

#激活安装
source ~/.bashrc

#使用conda验证安装
conda list
```

[anaconda使用说明](https://cloud.tencent.com/developer/article/1162755)

### Java

#### 安装OpenJDK，开源而且没有版权，美滋滋

```bash
# 更新源
sudo apt update
# 查看当前Java版本
java -version
# 如果没有安装Java会出现下面情况
# Command 'java' not found, but can be installed with:
#
# apt install default-jre
# apt install openjdk-11-jre-headless
# apt install openjdk-8-jre-headless
# apt install openjdk-9-jre-headless

# 安装OpenJDK
sudo apt install default-jre
# 验证安装
java -version
# openjdk version "10.0.1" 2018-04-17
# OpenJDK Runtime Environment (build 10.0.1+10-Ubuntu-3ubuntu1)
# OpenJDK 64-Bit Server VM (build 10.0.1+10-Ubuntu-3ubuntu1, mixed mode)

# 安装jdk
sudo apt install default-jdk
# 查看版本
javac -version
```

管理不同的Java，见[这篇文章](https://cloud.tencent.com/developer/article/1162527)

#### 配置环境变量

```bash
#确认Java安装位置
sudo update-alternatives --config java
```

使用一个熟悉的编辑器打开`/etc/environment`

```bash
sudo nano(gedit\vi) /etc/environment
```

在末尾加上：

```bash
JAVA_HOME="/usr/lib/jvm/java-11-openjdk-amd64/bin/java"
```

保存文件并退出，上述操作为系统上所有用户设置JAVA_HOME路径

重新加载文件：

```bash
source /etc/environment
```

验证是否设置：

```bash
echo $JAVA_HOME
```



参考文献：

[Ubuntu 18.04 安装Nvidia 显卡驱动](https://blog.mahonex.com/index.php/2018/07/18/ubuntu-18-04-%E5%AE%89%E8%A3%85nvidia-%E6%98%BE%E5%8D%A1%E9%A9%B1%E5%8A%A8/)

[Ubuntu16.04安装Nvidia显卡驱动（cuda）--解决你的所有困惑](https://blog.csdn.net/ghw15221836342/article/details/79571559?utm_source=copy)

[Ubuntu17.10／Ubuntu18.04配置以及美化](https://zhuanlan.zhihu.com/p/35362159)

[Ubuntu18.04: CUDA9.1+cuDNN7.1.2+Pytorch环境搭建](https://zhuanlan.zhihu.com/p/36502449)