# 使用Nmap扫描和识别应用服务

> Nmap是世界上使用最多的端口扫描器，它可以用于识别活动主机、扫描TCP和UDP开放端口、检测防火墙、获取在远程主机上运行的服务版本，甚至使用脚本发现和利用漏洞。

- 判断主机是否启动

  ```shell
  root@kali:~# nmap -sn 192.168.169.4
  Starting Nmap 7.70 ( https://nmap.org ) at 2019-04-01 09:20 EDT
  mass_dns: warning: Unable to determine any DNS servers. Reverse DNS is disabled. Try using --system-dns or specify valid servers with --dns-servers
  Nmap scan report for 192.168.169.4
  Host is up (0.00042s latency).
  MAC Address: 08:00:27:50:A3:D5 (Oracle VirtualBox virtual NIC)
  Nmap done: 1 IP address (1 host up) scanned in 0.19 seconds
  ```

- 查看有哪些端口打开

  ```shell
  root@kali:~# nmap 192.168.169.4
  Starting Nmap 7.70 ( https://nmap.org ) at 2019-04-01 09:24 EDT
  mass_dns: warning: Unable to determine any DNS servers. Reverse DNS is disabled. Try using --system-dns or specify valid servers with --dns-servers
  Nmap scan report for 192.168.169.4
  Host is up (0.00016s latency).
  Not shown: 991 closed ports
  PORT     STATE SERVICE
  22/tcp   open  ssh
  80/tcp   open  http
  139/tcp  open  netbios-ssn
  143/tcp  open  imap
  443/tcp  open  https
  445/tcp  open  microsoft-ds
  5001/tcp open  commplex-link
  8080/tcp open  http-proxy
  8081/tcp open  blackice-icecap
  MAC Address: 08:00:27:50:A3:D5 (Oracle VirtualBox virtual NIC)
  
  Nmap done: 1 IP address (1 host up) scanned in 0.80 seconds
  ```

- 使用Nmap向服务器询问他正在运行的服务器的版本，并基于此来判断操作系统

  ```shell
  root@kali:~# nmap -sV -o 192.168.169.4 # error
  # 输出文件的选项中，192.168.169.4被识别为文件名而不是IP地址
  
  root@kali:~# nmap -sV -o myscan.gnmap 192.168.169.4
  Starting Nmap 7.70 ( https://nmap.org ) at 2019-04-01 09:55 EDT
  mass_dns: warning: Unable to determine any DNS servers. Reverse DNS is disabled. Try using --system-dns or specify valid servers with --dns-servers
  Nmap scan report for 192.168.169.4
  Host is up (0.00036s latency).
  Not shown: 991 closed ports
  PORT     STATE SERVICE     VERSION
  22/tcp   open  ssh         OpenSSH 5.3p1 Debian 3ubuntu4 (Ubuntu Linux; protocol 2.0)
  80/tcp   open  http        Apache httpd 2.2.14 ((Ubuntu) mod_mono/2.4.3 PHP/5.3.2-1ubuntu4.30 with Suhosin-Patch proxy_html/3.0.1 mod_python/3.3.1 Python/2.6.5 mod_ssl/2.2.14 OpenSSL...)
  139/tcp  open  netbios-ssn Samba smbd 3.X - 4.X (workgroup: WORKGROUP)
  143/tcp  open  imap        Courier Imapd (released 2008)
  443/tcp  open  ssl/https?
  445/tcp  open  netbios-ssn Samba smbd 3.X - 4.X (workgroup: WORKGROUP)
  5001/tcp open  java-rmi    Java RMI
  8080/tcp open  http        Apache Tomcat/Coyote JSP engine 1.1
  8081/tcp open  http        Jetty 6.1.25
  1 service unrecognized despite returning data. If you know the service/version, please submit the following fingerprint at https://nmap.org/cgi-bin/submit.cgi?new-service :
  SF-Port5001-TCP:V=7.70%I=7%D=4/1%Time=5CA2183E%P=x86_64-pc-linux-gnu%r(NUL
  SF:L,4,"\xac\xed\0\x05");
  MAC Address: 08:00:27:50:A3:D5 (Oracle VirtualBox virtual NIC)
  Service Info: OS: Linux; CPE: cpe:/o:linux:linux_kernel
  
  Service detection performed. Please report any incorrect results at https://nmap.org/submit/ .
  Nmap done: 1 IP address (1 host up) scanned in 19.92 seconds
  ```

## 原理
  Nmap是一个端口扫描器；这代表着它将数据包发送到指定IP地址上的一些TCP或UDP端口，并且检查是否有响应。如果有，则表示端口是开放的；即服务在该端口上运行。

-  在第一条命令中，对于 `-sn`参数，我们指示Nmap只检查该服务器是否响应ICMP请求（或者是ping）。我们的服务器给出了响应，所以该主机是在存活状态的。
-  第二个命令是调用Nmap最简单的方法;它只指定目标IP地址。它所做的是ping服务器;如果它做出响应，那么Nmap将向1000个TCP端口的列表发送探测器，以查看哪个端口响应以及如何响应，然后它报告结果，显示哪些端口是打开的。
-  第三条命令在第二条命令的基础上添加了以下两个参数：
  - **-sV**请求找到每个开放端口banner-header或自我标识，也就是每个端口运行服务的版本
  - **-o**：告诉Nmap尝试使用从开放端口和版本信息的信息来猜测目标上运行的操作系统。

使用Nmap时的其他有用参数如下所示：

- **-sT****：**默认情况下，当它作为root用户运行时，Nmap使用一种称为SYN扫描的扫描类型。使用这个参数，我们强制扫描器执行完全连接扫描，它速度较慢，并且会在服务器日志中留下一条记录，但不太可能被入侵检测系统检测到或被防火墙阻止。
- **-Pn****：**如果我们已经知道主机时存活的但是没有响应ping，我们可以使用这个参数告诉Nmap跳过ping测试并扫描所有指定的目标（假设它们已经启动）
- **-v****：**这是冗长模式。Nmap将展示更多关于它正在扫描中的信息。这个参数可以在同一个命令中多次使用，使用的越多，得到的反馈就越多（-vv或-v –v –v –v ）
- **-p N1,N2,****…N3：**如果要测试特定端口或一些非标准端口，我们可能需要使用这个参数，其中N1到Nn时我们希望Nmap扫描的端口号。例如，要扫描21、80到90和137，参数奖是-p 21，80-90，137。另外，使用-p- Nmap将扫描0到65之间的所有端口和536端口
- **--script=script_name****：**Nmap包含许多有用的脚本，用于漏洞检查，扫描或者识别，登录测试，命令执行，用户枚举等等。使用此参数告诉Nmap在目标的开放端口上运行脚本。您可能想了解以下Nmap使用的一些脚本：https://nmap.org/nsedoc/scripts/。

