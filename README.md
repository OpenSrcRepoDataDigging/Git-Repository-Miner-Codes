# Git-Repository-Miner-Codes
南京大学“开源仓库代码挖掘和可视化”创新项目代码

***Remember to "git fetch origin master" and "git merge origin master" before your Modification!***



## 关于代码结构

刚刚更新的时候遇到了点问题，所以加了.gitignore把一些配置相关，还有target都ignore掉了。同时也因此注意到代码冲突带来的问题，所以为了减少耦合度，代码结构改了一下，大体逻辑是这样的：

- **GitRepository：**是一个记录git仓库的类，维护一个ContributorMap，后期主要传参对象
- **ContributorMap：**该类主要负责建立贡献者String和贡献者Contributor的索引，通过该类访问贡献者信息
- **Contributor：**记录贡献者的各种信息
- **CommitMessages：**记录格式化后的Commit的信息

把不同的功能最后写在不同的类里，传递一个共同的GitRepository，这样比较不会有merge时代码的冲突

不过其实就是改一下= =，原来的代码都可以正常跑的。刚刚看了一下，CalculateLOC功能已经变强大了，统计了很多东西，也许可以考虑用IDEA的重构功能改个名字之类的。考虑效率的话，CalculateLOC类里有对项目所有的Commit记录的遍历，可以直接用。而通过ContributorMap，可分别遍历每个贡献者的Commit记录，各取所需。

​																			——**By Young：**

## 项目进度与统计

#### 一、实现功能：

- [x] 修改Main类中的文件名，根据文件名会自动下载对应仓库
- [x] 计算每个相邻commit的增加的行数和列数。
- [x] 显示每次commit的贡献者信息，包括名字、邮箱、提交的时间
- [x] 系统记录每个提交者的信息，对应成表
- [x] 系统记录每个提交者的LOC
- [x] 用户信息的保存成本地文件excel或csv



#### 二、待修复的bug：

- [x] 最后删除下载的仓库时无法删除某些文件。（git.close()）



#### 三、需要完成的提高（任务分配）：

- [ ] Commit的条形码

- [ ] 代码结构的完善

- [ ] 图像的保存

- [ ] 保存用户邮箱信息

- [ ] 
