# 使用Recon-ng收集信息

> Recon-ng是一种信息收集工具，它使用许多不同的来源来收集数据，例如：Google，Twitter和Shodan。

## 准备

Recon-ng的某些模块需要API密钥用于查询在线服务。此外，拥有API密钥将允许您在某些服务中执行更高级的搜索或避免查询限制。可以通过在每个搜索引擎上完成注册来生成这些密钥。

## 开始

一个基本的查询

- 启动 `recon-NG`

- 在命令行界面查看模块 `show modules`

- 假设要搜索域名的所有子域名和DNS服务器不同响应区域传输，可以暴力破解子域名，要做到这个

- 首先加载`brute_hosts`模块

  ```shell
  use recon/domains-hosts/brute_hosts
  ```

- 如果要了解使用的模块时需要配置的选项，我们可以使用`show potions`命令

- 要为选项制定值，我们使用命令`set:set  ourcezonetransfer.me` eg:

  ```shell
  set source zonetransfer.me
  ```

- 一旦设置了所有选项，发出`run`命令执行模块:

  ```shell
  run
  [*] zw.zonetransfer.me => No record found.
  [*] zulu.zonetransfer.me => No record found.
  
  -------
  SUMMARY
  -------
  [*] 5 total (5 new) hosts found.
  [recon-ng][default][brute_hosts] > show hosts
  
    +--------------------------------------------------------------------------------------------------------+
    | rowid |          host          |   ip_address  | region | country | latitude | longitude |    module   |
    +--------------------------------------------------------------------------------------------------------+
    | 1     | email.zonetransfer.me  | 74.125.206.26 |        |         |          |           | brute_hosts |
    | 2     | home.zonetransfer.me   | 127.0.0.1     |        |         |          |           | brute_hosts |
    | 3     | office.zonetransfer.me | 4.23.39.254   |        |         |          |           | brute_hosts |
    | 4     | owa.zonetransfer.me    | 207.46.197.32 |        |         |          |           | brute_hosts |
    | 5     | www.zonetransfer.me    | 5.196.105.14  |        |         |          |           | brute_hosts |
    +--------------------------------------------------------------------------------------------------------+
  
  [*] 5 rows returned
  ```

  ## 原理

  Recon-ng是查询搜索引擎、众多社交工具和API的包装器媒体，通过互联网档案和数据库，以获取有关网站、网络信息、应用程序、服务器、主机、用户、电子邮件地址等。它的工作原理是整合不同功能的模块，例如Google、Twitter、LinkedIn或Shodan等，或者对DNS服务器执行查询。它还具有将结果导入数据库或生成各种格式报告的功能，例如HTML，MS Excel或CSV。

  另外有**Maltego**（https://www.paterva.com/web7/buy/maltego-clients/maltego-ce.php）

