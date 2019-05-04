# Linux容器技术

> 类型：工程开发项目
>
> ​	嵌入式系统的功能越来越多；但是兼容性和资源限制始终制约着系统快速集成。虚拟化是一个解决方案，但是它消耗太多的资源而且效率低下，Linux容器技术大大的减少了资源消耗，效率几乎等价于原生程序，使得在嵌入式系统上快速集成变为可能。
>
> ​	Docker 是目前非常火、应用也很广泛的容器引擎，我们希望在此基础上进行横向和纵向开发。
>
> ​	横向：基于Docker集成支撑某种运行环境的容器，比如Python/Go/Java
>
> ​	纵向：吸收Docker的经验，基于LXC做出更加轻量级的容器并在嵌入式系统上应用
>
> 主要功能如下：
>
> ​	(1)基于Docker定制容器
>
> ​		集成Python\Go\Java等运行环境的Docker容器，要求做到最精简
>
> ​	(2)更轻量级的容器
>
> ​		嵌入式系统的资源稀缺，我们希望可以有更轻量级的容器，可以接受一定的约束或者限制。

**目标问题：**

- 1基于Docker集成支撑支持某种运行环境的容器
- 2吸收Docker的经验，基于LXC做出更轻量级的容器，并应用在嵌入式系统上

**分析： **

- 首先要会使用Docker
- 了解Docker内部原理
- 理解Docker实现细节
- 根据需求能自己写出来一个轻量级容器

# 1 Docker 学习

## 1.1 基本概念

### 1.1.1 镜像

- 对于Linux，内核启动后，会挂载 `root` 文件系统为其提供用户空间支持。
- Docker 镜像（Image），就相当于是一个 `root` 文件系统。比如官方镜像 `ubuntu:18.04` 就包含了完整的一套 ubuntu 18.04 最小系统的 `root` 文件系统。
- Docker 镜像是一个特殊的文件系统，除了提供容器运行时所需的程序、库、资源、配置等文件外，还包含了一些为运行时准备的一些配置参数（如匿名卷、环境变量、用户等）。**镜像不包含任何动态数据，其内容在构建之后也不会被改变**。

#### 分层存储

​	镜像包含操作系统完整的 `root` 文件系统，其体积往往是庞大的，因此在 Docker 设计时，就充分利用 [Union FS](https://en.wikipedia.org/wiki/Union_mount) 的技术，将其设计为分层存储的架构。所以严格来说，镜像并非是像一个 ISO 那样的打包文件，镜像只是一个虚拟的概念，其实际体现并非由一个文件组成，而是由一组文件系统组成，或者说，由多层文件系统联合组成。

​	镜像构建时，会一层层构建，前一层是后一层的基础。每一层构建完就不会再发生改变，后一层上的任何改变只发生在自己这一层。比如，删除前一层文件的操作，实际不是真的删除前一层的文件，而是仅在当前层标记为该文件已删除。在最终容器运行的时候，虽然不会看到这个文件，但是实际上该文件会一直跟随镜像。因此，在构建镜像的时候，需要额外小心，每一层尽量只包含该层需要添加的东西，任何额外的东西应该在该层构建结束前清理掉。

​	分层存储的特征还使得镜像的复用、定制变的更为容易。甚至可以用之前构建好的镜像作为基础层，然后进一步添加新的层，以定制自己所需的内容，构建新的镜像。

### 1.1.2 Docker 容器

​	镜像（`Image`）和容器（`Container`）的关系，就像是面向对象程序设计中的 `类`和 `实例` 一样，镜像是静态的定义，容器是镜像运行时的实体。容器可以被创建、启动、停止、删除、暂停等。

- 容器的实质是进程，但与直接在宿主执行的进程不同，容器进程运行于属于自己的独立的 [命名空间](https://en.wikipedia.org/wiki/Linux_namespaces)。
- 容器可以拥有自己的 `root` 文件系统、自己的网络配置、自己的进程空间，甚至自己的用户 ID 空间。
- 容器内的进程是运行在一个隔离的环境里，使用起来，就好像是在一个独立于宿主的系统下操作一样。
- 这种特性使得容器封装的应用比直接在宿主运行更加安全。

​	容器也是使用的分层存储结构。每一个容器运行时，是以镜像为基础层，在其上创建一个当前容器的存储层，我们可以称这个为容器运行时读写而准备的存储层为**容器存储层**。

​	容器存储层的生存周期和容器一样，容器消亡时，容器存储层也随之消亡。因此，任何保存于容器存储层的信息都会随容器删除而丢失。

​	按照 Docker 最佳实践的要求，容器不应该向其存储层内写入任何数据，容器存储层要保持无状态化。所有的文件写入操作，都应该使用 [数据卷（Volume）](https://yeasy.gitbooks.io/docker_practice/content/data_management/volume.html)、或者绑定宿主目录，在这些位置的读写会跳过容器存储层，直接对宿主（或网络存储）发生读写，其性能和稳定性更高。**(不理解)**

​	数据卷的生存周期独立于容器，容器消亡，数据卷不会消亡。因此，使用数据卷后，容器删除或者重新运行之后，数据却不会丢失。

### 1.1.3 仓库

​	镜像构建完成后，可以很容易的在当前宿主机上运行，但是，如果需要在其它服务器上使用这个镜像，我们就需要一个集中的存储、分发镜像的服务，[Docker Registry](https://yeasy.gitbooks.io/docker_practice/content/repository/registry.html)就是这样的服务。

- 一个 **Docker Registry** 中可以包含多个**仓库**（`Repository`）
- 每个仓库可以包含多个**标签**（`Tag`）；每个标签对应一个镜像。

​	通常，一个仓库会包含同一个软件不同版本的镜像，而标签就常用于对应该软件的各个版本。我们可以通过 `<仓库名>:<标签>` 的格式来指定具体是这个软件哪个版本的镜像。如果不给出标签，将以 `latest` 作为默认标签。

​	以 [Ubuntu 镜像](https://hub.docker.com/_/ubuntu) 为例，`ubuntu` 是仓库的名字，其内包含有不同的版本标签，如，`16.04`, `18.04`。我们可以通过 `ubuntu:14.04`，或者 `ubuntu:18.04` 来具体指定所需哪个版本的镜像。如果忽略了标签，比如 `ubuntu`，那将视为 `ubuntu:latest`。

​	仓库名经常以 *两段式路径* 形式出现，比如 `jwilder/nginx-proxy`，前者往往意味着 Docker Registry 多用户环境下的用户名，后者则往往是对应的软件名。但这并非绝对，取决于所使用的具体 Docker Registry 的软件或服务。

#### Docker Registry 公开服务

​	Docker Registry 公开服务是开放给用户使用、允许用户管理镜像的 Registry 服务。一般这类公开服务允许用户免费上传、下载公开的镜像，并可能提供收费服务供用户管理私有镜像。

#### 私有 Docker Registry

​	除了使用公开服务外，用户还可以在本地搭建私有 Docker Registry。Docker 官方提供了 [Docker Registry](https://hub.docker.com/_/registry/) 镜像，可以直接使用做为私有 Registry 服务。

​	开源的 Docker Registry 镜像只提供了 [Docker Registry API](https://docs.docker.com/registry/spec/api/) 的服务端实现，足以支持 `docker` 命令，不影响使用。但不包含图形界面，以及镜像维护、用户管理、访问控制等高级功能。在官方的商业化版本 [Docker Trusted Registry](https://docs.docker.com/datacenter/dtr/2.0/) 中，提供了这些高级功能。

## 1.2 Docker 安装

### 1.2.1 卸载旧版本

旧版本的 Docker 称为 `docker` 或者 `docker-engine`，使用以下命令卸载旧版本：

```bash
$ sudo apt-get remove docker \
               docker-engine \
               docker.io
```

### 1.2.2 使用apt安装

​	由于 `apt` 源使用 `HTTPS` 以确保软件下载过程中不被篡改。因此，我们首先需要添加使用 `HTTPS `传输的软件包以及 CA 证书。

```bash
$ sudo apt-get update

$ sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
```

为了确认所下载软件包的合法性，需要添加软件源的 `GPG` 密钥：

```bash
$ curl -fsSL https://mirrors.ustc.edu.cn/docker-ce/linux/ubuntu/gpg | sudo apt-key add -


# 官方源
# $ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
```

然后，我们需要向 `source.list` 中添加 Docker 软件源：

```bash
$ sudo add-apt-repository \
    "deb [arch=amd64] https://mirrors.ustc.edu.cn/docker-ce/linux/ubuntu \
    $(lsb_release -cs) \
    stable"


# 官方源
# $ sudo add-apt-repository \
#    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
#    $(lsb_release -cs) \
#    stable"
```

### 1.2.3 安装 Docker CE

更新 apt 软件包缓存，并安装 `docker-ce`：

```bash
$ sudo apt-get update

$ sudo apt-get install docker-ce
```

### 1.2.4 启动Docker CE

```bash
$ sudo systemctl enable docker
$ sudo systemctl start docker
```

### 1.2.5 建立Docker用户组

默认情况下，`docker` 命令会使用 [Unix socket](https://en.wikipedia.org/wiki/Unix_domain_socket) 与 Docker 引擎通讯。而只有 `root`用户和 `docker` 组的用户才可以访问 Docker 引擎的 Unix socket。

出于安全考虑，一般 Linux 系统上不会直接使用 `root` 用户。因此，更好地做法是将需要使用 `docker` 的用户加入 `docker` 用户组。

建立 `docker` 组：

```bash
$ sudo groupadd docker
```

将当前用户加入 `docker` 组：

```bash
$ sudo usermod -aG docker $USER
```

### 1.2.6 镜像加速

- [阿里云加速器(需登录账号获取)](https://cr.console.aliyun.com/cn-hangzhou/mirrors)

## 1.3 使用Docker 镜像

- 镜像是Docker的三大组件之一
- Docker 运行容器前需要本地存在对应的镜像，如果本地不存在该镜像，Docker 会从镜像仓库下载该镜像。

### 1.3.1 获取镜像

从 Docker 镜像仓库获取镜像的命令是 `docker pull`。其命令格式为：

```bash
docker pull [选项] [Docker Registry 地址[:端口号]/]仓库名[:标签]
```

- Docker 镜像仓库地址：地址的格式一般是 `<域名/IP>[:端口号]`。默认地址是 Docker Hub。
- 仓库名：如之前所说，这里的仓库名是两段式名称，即 `<用户名>/<软件名>`。对于 Docker Hub，如果不给出用户名，则默认为 `library`，也就是官方镜像。

比如：

```bash
$ docker pull ubuntu:18.04
18.04: Pulling from library/ubuntu
84ed7d2f608f: Pull complete 
be2bf1c4a48d: Pull complete 
a5bdc6303093: Pull complete 
e9055237d68d: Pull complete 
Digest: sha256:868fd30a0e47b8d8ac485df174795b5e2fe8a6c8f056cc707b232d65b8a1ab68
Status: Downloaded newer image for ubuntu:18.04
```

上面的命令中没有给出 Docker 镜像仓库地址，因此将会从 Docker Hub 获取镜像。而镜像名称是 `ubuntu:18.04`，因此将会获取官方镜像 `library/ubuntu` 仓库中标签为 `18.04` 的镜像。

从下载过程中可以看到我们之前提及的分层存储的概念，镜像是由多层存储所构成。下载也是一层层的去下载，并非单一文件。下载过程中给出了每一层的 ID 的前 12 位。并且下载结束后，给出该镜像完整的 `sha256` 的摘要，以确保下载一致性。

在使用上面命令的时候，你可能会发现，你所看到的层 ID 以及 `sha256` 的摘要和这里的不一样。这是因为官方镜像是一直在维护的，有任何新的 bug，或者版本更新，都会进行修复再以原来的标签发布，这样可以确保任何使用这个标签的用户可以获得更安全、更稳定的镜像。

####  运行

有了镜像后，我们就能够以这个镜像为基础启动并运行一个容器。以上面的 `ubuntu:18.04` 为例，如果我们打算启动里面的 `bash` 并且进行交互式操作的话，可以执行下面的命令。

```bash
$ docker run -it --rm \
    ubuntu:18.04 \
    bash
    
root@60252b0e2f2f:/# cat /etc/os-release
NAME="Ubuntu"
VERSION="18.04.1 LTS (Bionic Beaver)"
ID=ubuntu
ID_LIKE=debian
PRETTY_NAME="Ubuntu 18.04.1 LTS"
VERSION_ID="18.04"
HOME_URL="https://www.ubuntu.com/"
SUPPORT_URL="https://help.ubuntu.com/"
BUG_REPORT_URL="https://bugs.launchpad.net/ubuntu/"
PRIVACY_POLICY_URL="https://www.ubuntu.com/legal/terms-and-policies/privacy-policy"
VERSION_CODENAME=bionic
UBUNTU_CODENAME=bionic
root@60252b0e2f2f:/# exit
exit
```

`docker run`就是运行容器的命令。

- `-it`：这是两个参数，一个是 `-i`：交互式操作，一个是 `-t` 终端。我们这里打算进入 `bash` 执行一些命令并查看返回结果，因此我们需要交互式终端。
- `--rm`：这个参数是说容器退出后随之将其删除。默认情况下，为了排障需求，退出的容器并不会立即删除，除非手动 `docker rm`。我们这里只是随便执行个命令，看看结果，不需要排障和保留结果，因此使用 `--rm` 可以避免浪费空间。
- `ubuntu:18.04`：这是指用 `ubuntu:18.04` 镜像为基础来启动容器。
- `bash`：放在镜像名后的是**命令**，这里我们希望有个交互式 Shell，因此用的是 `bash`。

​	进入容器后，我们可以在 Shell 下操作，执行任何所需的命令。这里，我们执行了 `cat /etc/os-release`，这是 Linux 常用的查看当前系统版本的命令，从返回的结果可以看到容器内是 `Ubuntu 18.04.1 LTS` 系统。

​	最后我们通过 `exit` 退出了这个容器。

### 1.3.2 列出镜像

要想列出已经下载下来的镜像，可以使用 `docker image ls` 命令。

```bash
$ docker image ls
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
ubuntu              18.04               1d9c17228a9e        5 days ago          86.7MB
hello-world         latest              4ab4c602aa5e        3 months ago        1.84kB
```

列表包含了 `仓库名`、`标签`、`镜像 ID`、`创建时间` 以及 `所占用的空间`。

其中仓库名、标签在之前的基础概念章节已经介绍过了。**镜像 ID** 则是镜像的唯一标识，一个镜像可以对应多个**标签**。因此，在上面的例子中，我们可以看到 `ubuntu:18.04` 和 `ubuntu:latest` 拥有相同的 ID，因为它们对应的是同一个镜像。

#### 1.3.2.1 镜像体积

​	如果仔细观察，会注意到，这里标识的所占用空间和在 Docker Hub 上看到的镜像大小不同。比如，`ubuntu:18.04` 镜像大小，在这里是 `86.7 MB`，但是在 [Docker Hub](https://hub.docker.com/r/library/ubuntu/tags/) 显示的却是 `50 MB`。这是因为 Docker Hub 中显示的体积是压缩后的体积。在镜像下载和上传过程中镜像是保持着压缩状态的，因此 Docker Hub 所显示的大小是网络传输中更关心的流量大小。而 `docker image ls` 显示的是镜像下载到本地后，展开的大小，准确说，是展开后的各层所占空间的总和，因为镜像到本地后，查看空间的时候，更关心的是本地磁盘空间占用的大小。

​	另外一个需要注意的问题是，`docker image ls` 列表中的镜像体积总和并非是所有镜像实际硬盘消耗。由于 Docker 镜像是多层存储结构，并且可以继承、复用，因此不同镜像可能会因为使用相同的基础镜像，从而拥有共同的层。由于 Docker 使用 Union FS，相同的层只需要保存一份即可，因此实际镜像硬盘占用空间很可能要比这个列表镜像大小的总和要小的多。

你可以通过以下命令来便捷的查看镜像、容器、数据卷所占用的空间。

```bash
$ docker system df
TYPE                TOTAL               ACTIVE              SIZE                RECLAIMABLE
Images              2                   1                   86.7MB              86.7MB (99%)
Containers          4                   0                   0B                  0B
Local Volumes       0                   0                   0B                  0B
Build Cache         0                   0                   0B                  0B

```

#### 1.3.2.2 中间层镜像

​	为了加速镜像构建、重复利用资源，Docker 会利用 **中间层镜像**。所以在使用一段时间后，可能会看到一些依赖的中间层镜像。默认的 `docker image ls` 列表中只会显示顶层镜像，如果希望显示包括中间层镜像在内的所有镜像的话，需要加 `-a` 参数。

​	这样会看到很多无标签的镜像，与之前的虚悬镜像不同，这些无标签的镜像很多都是中间层镜像，是其它镜像所依赖的镜像。这些无标签镜像不应该删除，否则会导致上层镜像因为依赖丢失而出错。实际上，这些镜像也没必要删除，因为之前说过，相同的层只会存一遍，而这些镜像是别的镜像的依赖，因此并不会因为它们被列出来而多存了一份，无论如何你也会需要它们。只要删除那些依赖它们的镜像后，这些依赖的中间层镜像也会被连带删除。

#### 1.3.2.3 列出部分镜像

​	不加任何参数的情况下，`docker image ls` 会列出所有顶级镜像，但是有时候我们只希望列出部分镜像。`docker image ls` 有好几个参数可以帮助做到这个事情。

- 根据仓库名列出镜像

```bash
$ docker image ls ubuntu
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
ubuntu              18.04               1d9c17228a9e        5 days ago          86.7MB
```

- 列出特定的某个镜像，也就是说指定仓库名和标签

```bash
$ docker image ls ubuntu:18.04
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
ubuntu              18.04               1d9c17228a9e        5 days ago          86.7MB

```

除此以外，`docker image ls` 还支持强大的过滤器参数 `--filter`，或者简写 `-f`。

#### 1.3.2.4 以特定格式显示



## 1.3.3 

# 2 Docker 原理

## 2.1 Docker

- Docker = Container?

### 2.1.1 简介

​	**Docker**是一个开源工具，可以将我们的应用打包成一个标准格式的镜像，并且以容器(Container)的方式运行。

### 2.1.2 容器

​	**容器是什么？**容器是一个让我们能对某个应用和应用整个运行时环境一起进行打包或者隔离的技术。让我们能够在不用的环境之间轻松的迁移应用的同时，能保留应用的全部功能。  
​	**容器能做什么？**假定，我们在使用一台电脑进行开发，然后，我的开发环境在我电脑上是配置完成的，而我的小伙伴和我同时也要协同工作，但是，他的开发环境和我的不大一样。我当前的开发是依赖于我电脑上的某些特定文件，这些文件小伙伴是没有的，但是，我想让我的开发应用能在小伙伴的电脑上能不出问题的成功部署，这是时候，我就可以使用容器。
容器能确保应用拥有必须的配置和文件，使得应用能够在从开发到测试再到生产的整个流程中能够顺利运行
如下图就是一个容器的架构图：
![container](D:/OneDrive/USTC/Linux%E5%AE%B9%E5%99%A8%E6%8A%80%E6%9C%AF%E7%A0%94%E7%A9%B6/container.png)

### 2.1.3 容器与虚拟机

**容器和虚拟化的区别是什么**我们来回顾一下虚拟机:

- 虚拟机让许多的操作系统能够同时在单个系统上运行。
- 虚拟机是需要模拟整台机器包括硬件，每台虚拟机都要有自己的操作系统，一旦被开启，预分配的资源将被全部占用。
- 虚拟机包括应用，必要的二进制和库，以及一个完整的用户操作系统

看下面的图片是虚拟化和容器的系统架构区别：
![virtual&container](D:/OneDrive/USTC/Linux%E5%AE%B9%E5%99%A8%E6%8A%80%E6%9C%AF%E7%A0%94%E7%A9%B6/virtualization-vs-containers.png)
​         可以看到虚拟化的实现是基于Hypervisor(VMM,virtual machine monitor)的，我们的操作系统使用Hypervisor来建立与执行虚拟机器的软件、硬件。
而容器呢，是基于文件系统的，它并没有模拟一个完整的操作系统，而是对进程进行隔离，它能达成更加轻量化的效果。

### 2.1.4 Docker与容器

我们知道，Docker是一项容器化技术，用于支持创建和使用Linux容器。
Docker技术，使用Linux内核功能(如：Namespace、Cgroups、Union File System)来分离进程,以便于各进程相互独立运行。这种独立性就是我们想使用Docker的原因，使用Docker,我们可以独立运行多种进程，多个应用程序，更加从分的发挥基础设施的作用，同时保持各个独立系统的安全性。
容器工具(Docker)提供基于镜像的部署模式，使得我们能够轻松的跨多种环境，并且能够自动部署应用程序。

Docker首先是基于lxc技术构建的，但是，现在它在lxc技术上扩展了许多其他功能，下图是lxc和Docker的对比：
![Lxc&Docker](D:/OneDrive/USTC/Linux%E5%AE%B9%E5%99%A8%E6%8A%80%E6%9C%AF%E7%A0%94%E7%A9%B6/traditional-linux-containers-vs-docker_0.png)

### 2.1.5 为什么使用Docker

- 模块化
  Docker能单独截取部分应用程序进行更新或者修复

- 层和镜像版本控制
  每个Docker都包含多个层。这些层组合在一起，构成单个镜像。每当镜像发生改变时，一个新的镜像层即被创建出来。用户每次发出命令（例如 *run* 或 *copy*）时，都会创建一个新的镜像层。

- 回滚

  每个镜像都拥有多个层。比如说，如果你不喜欢迭代后的镜像版本？完全可以通过回滚，返回之前的版本。

- 快速部署

  基于 Docker 的容器可将部署时间缩短到几秒。通过为每个进程构建容器，您可以快速将这些类似进程应用到新的应用程序中。而且，由于无需启动操作系统即可添加或移动容器，因此大幅缩短了部署时间。除此之外，得益于这种部署速度，您可以轻松无虞、经济高效地创建和销毁容器创建的数据。

## 2.2 Docker技术

Dockers采用了C/S 架构，包括客户端和服务端。Docker守护进程(Daemon)作为服务端来接收来自客户端的请求，并处理这些请求（创建、运行、分发容器）。

客户端和服务端既可以运行在一个机器上，也可以通过RESTful 、socket或者网络接口与远程Docker服务器端进行通信。

### 2.2.1 Linux Namespace

命名空间是Linux Kernel的一个功能，他可以隔离一系列的系统资源，比如PID（Process ID)、User ID、NetWork等等。

每个容器都有自己单独的命名空间，运行在其中的应用都像是在妒忌的操作系统中运行一样。命名空间保证了容器之间比起互不影响。

- pid Namespace

  **PID Namespace** 就是用来隔离进程ID的。一个进程在不同的PID Namespace里可以拥有不同的PID。

- net Namespace

  **Netwoek Namespace** 用来隔离网络设备、IP地址端口等网络栈的Namespace。

- ipc Namespace

  **IPC(,interprocess communication,进程间交互方法) Namespace** 用来隔离 System V IPC 和 POSIX message queues。每一个IPC Namespace 都有自己的 System V IPC 和 POSIX message queue。容器的进程间交互实际上还是 host 上具有相同 pid 命名空间中的进程间交互

- mnt Namespace

  **Mount Namespace** 用来隔离各个进程看到的挂载点视图。在不同的Namespace里，能看到的文件系统层次是不一样的。

- uts Namespace

  **UTS Namespace** 用来隔离nodename和domainname两个系统标识。在 UTS Namespace里，每一个Namespace允许自己有自己的hostname。使其在网络上可以被视作一个独立的节点而非 主机上的一个进程。

- user Namespace

  **User Namespace** 主要是用来隔离用户的用户组ID。一个进程的User ID  和  Group ID 在User Namespace内外可以是不同的。

### 2.2.2 Linux Cgroups

Linux Cgroups 技术能够限制每个空间的大小，保证他们之间不会互相争抢。

**什么是Linux Cgroups?** Linux Cgroups(Linux Control Groups)提供了对一组进程以及将来的子进程的资源限制、控制和统计的能力，这些资源包括CPU、内存、存储、网络等等，通过Cgroups，能够方便的控制分配到容器的资源，避免当多个容器同时运行时的对系统资源的竞争。

Cgroups 的3个组件：

- **cgroup**

  cgroup是对进程分组管理的一种机制，一个cgroup包含了一组进程，并且可以在这个cgroup上增加Linux subsystem的各种参数配置，将一组进程和一组subsystem的系统参数关联起来。

- **subsystem**

  subsystem是一组资源控制的模块，一般包含以下几项：

  - **blkio** 设置对块设备（比如硬盘）的输入输出访问控制

  - **cpu**设置cgroup中进程的CPU调度策略

  - **cpuacct**统计cgroup中的CPU占用

  - **cpuset** 在多核机器上设置cgroup中进程可以使用的CPU和内存

  - **devices** 控制cgroup中进程对设备的访问

  - **freezer**用于挂起(suspend)和恢复(resume)cgroup中的进程

  - **memory**用于控制cgroup中进程的内存占用

  - **net_cls**用于将cgroup中进程产生的网络包分类，以便Linux的tc（traffic controller）可以根据分类区分出来自某一个cgroup的包，并作限流或者监控。

  - **net_prio**设置cgroup中进程产生的网络流量的优先级

  - **ns**作用是使cgroup中的进程在新的Namespace中fork新进程时，创建出一个新的cgroup，这个cgroup包含新的Namespace中的进程。

    每一个subsystem会关联定义了相应限制的cgroup上，并对这个cgroup中的进程做相应的限制和控制，这些subsystem是逐步合并到内核当中的。

- **hierarchy**

  hierarchy是吧一组cgroup串成一个树状的结构，一个这样的树就是一个hierarchy，通过这个树结构，Cgroup能做到继承。

- **三个组件之间的关系**

  Cgroup是通过这三个组件之间的相互协作实现的。

  - 系统首先会创建新的hierarchy，在这之后，系统中所有的进程会加入到这个hierarchy的根节点，这个根节点是hierchy默认创建的。
  - 一个subsystem只能附加到一个hierarchy上
  - 一个hierarchy上能够附加多个subsystem
  - 一个进程可以最为多个cgroup的成员，但是这些cgroup必须在不同的hierarchy中。
  - 一个进程fork出子进程时，子进程和父进程是在同一个cgroup中的，我们也可以根据需要将其移动到其他的cgroup中。

### 2.2.3 Linux File system

**什么是Union File system?**

Union File System是一种为Linux、FreeBSD、和NetBSD操作系统设计的，把其他文件系统联合到一个联合挂载点的文件系统服务。

它使用branch把不同文件系统的文件和目录透明地覆盖，这些branch要么是read-only的，要呢是read-write的。

**AUFS**

AUFS（Advanced Multi-Layered Unification Filesystem），它是Docker选用的第一种存储驱动，AUFS具有快速启动容器，高效利用存储和内存的优点。迄今为止，AUFS仍然是Docker支持的一种存储类型。

# 3 针对Docker原理的学习

## 3.1 Linux Namespace 

​	Namespace是Linux Kernel的一个功能，他可以隔离一系列的系统资源，比如PID（Process ID)、User ID、NetWork等等。每个容器都有自己单独的命名空间，运行在其中的应用都像是在独立的操作系统中运行一样。命名空间保证了容器之间比起互不影响。

​	当前Linux一共实现了6种不同类型的Namespace。

|   Namespace类型   | 系统调用参数  | 内核版本 |
| :---------------: | :-----------: | :------: |
|  Mount Namespace  |  CLONE_NEWNS  |  2.4.19  |
|   UTS Namespace   | CLONE_NEWUTS  |  2.6.19  |
|   IPC Namespace   | CLONE_NEWIPC  |  2.6.19  |
|   PID Namespace   | CLONE_NEWPID  |  2.6.24  |
| Network Namespace | CLONE_NEWNET  |  2.6.29  |
|  User Namespace   | CLONE_NEWUSER |   3.8    |

### 3.1.1 UTS Namespace

在UTS namespace里面，每个Namespace都允许拥有自己的hostname。

hostname 是用于显示和设置系统的主机名称的。环境变量HOSTNAME也保存了当前的主机名。在使用hostname命令设置主机名后，系统并不会永久保存新的主机名，重新启动机器之后还是原来的主机名。

```go
package main

import (
	"log"
	"os"
	"os/exec"
	"syscall"
)

func main(){
    // 指定被fork出来的新进程的初始命令，默认使用sh来执行
	cmd := exec.Command("sh")
    // 设置系统调用参数，根据上面表中所述，使用CLONE_NEWNS这个标识符去创建一个UTSNamespace.
	cmd.SysProcAttr = &syscall.SysProcAttr{
		Cloneflags: syscall.CLONE_NEWUTS,
	}

	cmd.Stdin = os.Stdin
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr

	if err:= cmd.Run(); err != nil {
		log.Fatal(err)
	}
}
```

执行代码，进入一个sh环境，

```sh
pstree -pl
# 得到以下输出
|bash(3266)--go(3277)--main(3299)--sh(3304)-pstree(3305)

# 查看当前PID
echo $$
3304

# 验证父进程在不同的UTS namespace中
readlink /proc/3299/ns/uts
# uts:[4026531838]
readlink /proc/3304/ns/uts
# uts:[4026532448]
```

​	可以看到，`main`和`sh`不在同一个uts namespace中，由于uts对Namespace做了隔离。下载做一个测试

```sh
# 在本sh环境中，做如下测试
# 修改hostname为bird然后打印出来
hostname -b bird
hostname
# 输出:bird
# 在外部shell 中运行hostname
# 输出：ubuntu
```

![1545630946779](C:\Users\chanshaw\AppData\Roaming\Typora\typora-user-images\1545630946779.png)

![1545631008559](C:\Users\chanshaw\AppData\Roaming\Typora\typora-user-images\1545631008559.png)

### 3.1.2 IPC  Namespace

​	用来隔离System V IPC 和 POSIX message queues。

- System V IPC 系统进程间通信。提供了三种方法

  - 消息队列

    消息队列允许一个应用程序提交消息，其他应用程序可以在以后获得该消息，甚至是在发送应用程序已结束之后。

  - 信号量

    信号量确保多个应用程序可以锁定资源并避免争用条件。

  - 共享内存

    共享内存允许多个应用程序共享一个公共内存段，从而提供了一种传递和共享大量数据的快速方法。

- POSIX message queues 也是Linux进程通信的消息队列的一种。在不同进程间发送特定格式的消息数据

```sh
# 在宿主机上打开一个shell
# 查看现有的ipc Message Queues
ipcs -q
# 输出如下：
# ------ Message Queues --------
# key        msqid      owner      perms      used-bytes   messages  
# 创建一个message queue
ipcmk -Q
# Message queue id: 0
# 再查看
# ------ Message Queues --------
# key        msqid      owner      perms      used-bytes   messages    
# 0x160d6adb 0          root       644        0            0          
```

可以看到，现在已经有一个消息队列了，现在在另外一个shell里面，运行代码：

```go
package main

import (
	"log"
	"os"
	"os/exec"
	"syscall"
)

func main(){
    // 指定被fork出来的新进程的初始命令，默认使用sh来执行
	cmd := exec.Command("sh")
    // 修改一下这位置，添加一个系统调用CLONE_IPC
	cmd.SysProcAttr = &syscall.SysProcAttr{
		Cloneflags: syscall.CLONE_NEWUTS|syscall.CLONE_NEWIPC,
	}

	cmd.Stdin = os.Stdin
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr

	if err:= cmd.Run(); err != nil {
		log.Fatal(err)
	}
}
```

```sh
# 在新的sh里面，查看队列
ipcs -q
# ------ Message Queues --------
# key        msqid      owner      perms      used-bytes   messages 
```

在新的Namespace里，看不到宿主机上已经创建了message queue，说明了IPC已经隔离了。

### 3.1.3 PID Namespace

​	PID Namespace很显然就是用来隔离进程ID的，在不同的PID Namespace里可以拥有不同的PID。

同样修改一下上面的代码：

```go
package main

import (
	"log"
	"os"
	"os/exec"
	"syscall"
)

func main(){
    // 指定被fork出来的新进程的初始命令，默认使用sh来执行
	cmd := exec.Command("sh")
    // 设置系统调用参数，例如，使用CLONE_NEWNS这个标识符去创建一个UTSNamespace.
	cmd.SysProcAttr = &syscall.SysProcAttr{
		Cloneflags: syscall.CLONE_NEWUTS|syscall.CLONE_NEWIPC|
  syscall.CLONE_NEWPID|syscall.CLONE_NEWPNS|syscall.CLONE_NEWPUSER|
        syscall.CLONE_NEWNET,
	}

	cmd.Stdin = os.Stdin
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr

	if err:= cmd.Run(); err != nil {
		log.Fatal(err)
	}
}
```

两个shell，查看PID

```sh
# 一个sh查看修改前
pstree -pl
# |bash(3266)-go(3621)-main(3643)-sh(3648)

# 一个查看修改后
echo $$
# 1
```

打印当前Namespace的PID 可以看到，是1。也就是说从外部的main(3643)被映射到Namespace后的PID1。

### 3.1.4 Mount Namespace

​	Mount 用来隔离挂载点视图。在不同的Namespace中，我们看到的文件系统层次是不一样的。在Namespace中，调用mount()和unmount()仅仅只会影响当前的Namespace内的文件系统。对于全局的文件系统则是没有影响的。

两个shell

```sh
# 一个sh修改前,查看内核和内核模块
ls /proc
1     127   1510  1709  199   225   252   2779  292   3084  3407  42   53   82    buddyinfo      mounts
10    128   1519  171   2     226   253   278   2920  3088  3408  420  54   827   bus            mpt
100   1283  152   172   20    227   254   2783  2921  3092  341   421  55   83    cgroups        mtrr
101   129   1529  173   200   228   255   2785  2927  31    342   422  56   84    cmdline        net
102   1291  153   174   201   229   256   2786  2928  3112  3428  423  57   85    consoles       pagetypeinfo
103   13    1530  175   202   23    257   2788  293   3119  343   424  58   86    cpuinfo        partitions
104   130   1533  176   203   230   258   279   2930  3124  3470  425  59   861   crypto         sched_debug
105   131   154   177   204   231   259   2793  2934  3129  35    426  60   87    devices        schedstat
106   132   155   178   205   232   26    2797  2936  315   3531  427  61   88    diskstats      scsi
107   133   156   179   206   233   260   28    294   3154  36    428  62   883   dma            self
108   134   157   1791  207   234   261   2806  2945  316   3695  429  63   889   driver         slabinfo
109   135   158   18    208   2348  262   281   295   3162  37    43   64   89    execdomains    softirqs
11    136   1581  180   209   235   2627  2810  2957  3167  38    430  65   894   fb             stat
110   1362  159   181   21    236   263   282   2961  3172  39    431  66   9     filesystems    swaps
111   137   1597  182   210   237   264   2821  297   3178  399   432  67   90    fs             sys
112   1373  16    183   211   2372  2649  2824  298   318   4     433  68   900   interrupts     sysrq-trigger
113   1374  160   184   212   238   265   2826  2982  3182  40    434  687  91    iomem          sysvipc
114   138   1607  185   213   239   2651  283   299   319   400   435  69   910   ioports        timer_list
115   139   161   186   2134  24    266   2830  2995  32    405   436  692  92    ipmi           timer_stats
116   14    162   187   214   240   267   2837  3     3224  407   44   7    93    irq            tty
117   140   163   188   215   241   268   284   30    3240  408   45   70   94    kallsyms       uptime
118   141   1633  189   216   242   269   2840  300   3247  409   46   71   948   kcore          version
1186  142   164   19    217   243   27    2847  3005  3248  41    465  72   949   key-users      version_signature
119   143   1645  190   2171  244   270   285   301   3264  410   47   73   95    kmsg           vmallocinfo
12    144   165   191   218   2442  271   2850  302   3265  411   48   74   950   kpagecount     vmstat
120   145   166   192   2183  245   272   286   3027  3266  412   480  75   951   kpageflags     zoneinfo
121   146   1669  1926  219   246   273   287   303   3276  413   481  76   952   latency_stats
122   147   167   193   22    247   2739  288   3045  33    414   49   77   953   loadavg
123   148   1672  194   220   248   274   289   3059  3306  415   5    78   96    locks
124   149   168   195   221   249   2750  29    3060  3355  416   50   79   97    mdstat
125   15    169   196   222   25    2759  290   3062  3394  417   51   8    98    meminfo
126   150   17    197   223   250   276   291   3071  34    418   513  80   99    misc
1268  151   170   198   224   251   277   2917  3072  3406  419   52   81   acpi  modules

# 看不懂的内核模块调用信息

# 运行代码后的模块
mount -t proc proc /proc
ls /proc
1	   consoles   execdomains  ipmi        kpageflags     modules	    sched_debug  swaps		uptime
3	   cpuinfo    fb	   irq	       latency_stats  mounts	    schedstat	 sys		version
acpi	   crypto     filesystems  kallsyms    loadavg	      mpt	    scsi	 sysrq-trigger	version_signature
buddyinfo  devices    fs	   kcore       locks	      mtrr	    self	 sysvipc	vmallocinfo
bus	   diskstats  interrupts   key-users   mdstat	      net	    slabinfo	 timer_list	vmstat
cgroups    dma	      iomem	   kmsg        meminfo	      pagetypeinfo  softirqs	 timer_stats	zoneinfo
cmdline    driver     ioports	   kpagecount  misc	      partitions    stat	 tty
# 还是看不懂，但是少了好多文件。
# 查看下系统进程
ps -ef
UID         PID   PPID  C STIME TTY          TIME CMD
root          1      0  0 22:46 pts/0    00:00:00 sh
root          4      1  0 22:48 pts/0    00:00:00 ps -ef
```

可以看到，在当前的namespace中，sh进程是PID为1的进程，说明的是，当前的Mount Namespace和外部的空间是隔离的。

### 3.1.5 User Namespace

​	隔离用户组ID。简单的例子是，一个用户在外部没有root权限，在Namespace内部有。代码：

```go

```

```sh
# 外部
id
# uid=0(root) gid=0(root) groups=0(root)

# 内部
id
# uid=65534(nobody) gid=65534(nogroup) groups=65534(nogroup)
```

可以看到，UID 不同。

### 3.1.6 Network Namespace

隔离网络设备、IP地址端口等网络栈的Namespace。

```sh
# 外部
ifconfig
eth0      Link encap:Ethernet  HWaddr 00:0c:29:5d:4b:57  
          inet addr:192.168.183.132  Bcast:192.168.183.255  Mask:255.255.255.0
          inet6 addr: fe80::20c:29ff:fe5d:4b57/64 Scope:Link
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
          RX packets:1607 errors:0 dropped:0 overruns:0 frame:0
          TX packets:264 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000 
          RX bytes:186974 (186.9 KB)  TX bytes:32795 (32.7 KB)

lo        Link encap:Local Loopback  
          inet addr:127.0.0.1  Mask:255.0.0.0
          inet6 addr: ::1/128 Scope:Host
          UP LOOPBACK RUNNING  MTU:65536  Metric:1
          RX packets:54 errors:0 dropped:0 overruns:0 frame:0
          TX packets:54 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:0 
          RX bytes:4745 (4.7 KB)  TX bytes:4745 (4.7 KB)

# 内部
ifconfig
# 什么都没有输出
```

可以看到，Namespace里面什么都灭有。断定处于隔离状态了。

## 3.2 Linux Cgroups

 	Linux Cgroups(LInux Control Groups)提供了对一组进程以及将来的子进程的资源限制、控制和统计的能力，这些资源包括CPU、内存、存储、网络等等，通过Cgroups，能够方便的控制分配到容器的资源，避免当多个容器同时运行时的对系统资源的竞争。

三个组件

- cgroup

  对进程分组管理的一种机制，一个cgroup包含了一组进程，并且可以在这个cgroup上增加Linux subsystem的各种参数配置，将一组进程和一组subsystem的系统参数关联起来。

- subsystem

```sh
lssubys -a
# 可以看到Lernel支持的subsystem
cpuset       # 在多核机器上设置cgroup中进程可以使用的CPU和内存
cpu          # 设置cgroup中进程的CPU调度策略
cpuacct      # 统计cgroup中的CPU占用
memory       # 用于控制cgroup中进程的内存占用
devices      # 控制cgroup中进程对设备的访问
freezer      # 用于挂起(suspend)和恢复(resume)cgroup中的进程
blkio        # 设置对块设备（比如硬盘）的输入输出访问控制
perf_event   # 面向事件的观察工具
hugetlb      # 文件系统
```

- hierarchy

  hierarchy是吧一组cgroup串成一个树状的结构，一个这样的树就是一个hierarchy，通过这个树结构，Cgroup能做到继承。

- Cgroup是通过这三个组件之间的相互协作实现的。

  - 系统首先会创建新的hierarchy，在这之后，系统中所有的进程会加入到这个hierarchy的根节点，这个根节点是hierchy默认创建的。
  - 一个subsystem只能附加到一个hierarchy上
  - 一个hierarchy上能够附加多个subsystem
  - 一个进程可以最为多个cgroup的成员，但是这些cgroup必须在不同的hierarchy中。
  - 一个进程fork出子进程时，子进程和父进程是在同一个cgroup中的，我们也可以根据需要将其移动到其他的cgroup中。

#### 3.2.1 创建一个hierarchy

```sh
# 创建一个hierarchy
mkdir cgroup-test
# 挂载一个hierarchy
sudo mount -t cgroup -o none,name=cgroup-test cgroup-test ./cgroup-test
# 挂在后我们就可以看到系统在这个目录下生成了一些默认文件
cgroup.clone_children  cgroup.procs          notify_on_release  tasks
cgroup.event_control   cgroup.sane_behavior  release_agent
```

这个文件就是这个hierarchy中cgroup根节点的配置项。

- cgroup.clone_children,cpuset的subsystem会读取这个配置文件。默认为0，如果这个值是1，子cgroup才会继承父cgroup的cpuset配置。
- cgroup.procs是树中当前节点cgroup中的进程ID。
- notify_on_release和release_agent 一起使用。notify_on_release标识cgroup最后一个进程退出的时候是否执行了release_agent。而release_agent则是一个路径。通常用于进程退出之后自动清理掉不再使用的cgroup。

#### 3.2.2 扩展两个子Cgroup

```sh
chanshaw@ubuntu:~/cgroup-test$ sudo mkdir cgroup-1
chanshaw@ubuntu:~/cgroup-test$ sudo mkdir cgroup-2
chanshaw@ubuntu:~/cgroup-test$ tree
.
├── cgroup-1
│   ├── cgroup.clone_children
│   ├── cgroup.event_control
│   ├── cgroup.procs
│   ├── notify_on_release
│   └── tasks
├── cgroup-2
│   ├── cgroup.clone_children
│   ├── cgroup.event_control
│   ├── cgroup.procs
│   ├── notify_on_release
│   └── tasks
├── cgroup.clone_children
├── cgroup.event_control
├── cgroup.procs
├── cgroup.sane_behavior
├── notify_on_release
├── release_agent
└── tasks
```

创建文件夹的时候，kernel会把文件夹标记为这个cgroup的子cgroup。会继承父cgroup的属性。

### 3.2.3 在Cgroup中添加移动进程

```sh
echo $$
3927
# 将我所在的终端进程移动到cgroup-1中
sudo sh -c "echo $$ >> tasks"
cat /proc/3927/cgroup
# 可以看到3927已经被加入进程中了
12:name=cgroup-test:/cgroup-1
11:name=systemd:/user/1000.user/c2.session
10:hugetlb:/user/1000.user/c2.session
9:perf_event:/user/1000.user/c2.session
8:blkio:/user/1000.user/c2.session
7:freezer:/user/1000.user/c2.session
6:devices:/user/1000.user/c2.session
5:memory:/user/1000.user/c2.session
4:cpuacct:/user/1000.user/c2.session
3:cpu:/user/1000.user/c2.session
2:cpuset:/
```

# 参考文献： 
[1] 周毅,邓玉辉.一种概率模型的Docker镜像删减策略[J].小型微型计算机系统,2018,39(09):1908-1913

[2] 吴强. 镜像构建系统的设计与实现[D].南京大学,2017.

[3] 李伟. 基于Docker的镜像组合技术研究与实现[D].华南理工大学,2017.

[4] [什么是linux容器](https://www.redhat.com/zh/topics/containers/whats-a-linux-container)

[5] [什么是Docker](https://www.redhat.com/zh/topics/containers/what-is-docker)

[6] [Docker-从入门到实践](https://legacy.gitbook.com/book/yeasy/docker_practice/details)

[7] [一篇堪称Docker经典教科书的文章](https://zhuanlan.zhihu.com/p/47712081)

[8] [自己动手写Docker](https://book.douban.com/subject/27082348/)

[9] [容器安全防护的十个级别](https://www.redhat.com/cms/managed-files/cl-container-security-openshift-cloud-devops-tech-detail-f7530kc-201705-a4-zh.pdf)