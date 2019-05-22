# 附录A 搭建Ruby运行环境
---

[TOC]

## A.1 安装Ruby
## A.2 在Windows 下安装
本节介绍如何使用RubyInstaller 安装Ruby。首先可从以下网站下载RubyInstaller。
Ruby Installer for Windows：http://rubyinstaller.org/

点击首页的Download 链接会跳转到下载页面，从上面可以看到安装包的一览表，点击对应的下载链接就可以下载安装包。下面我们来介绍通过RubyInstaller 安装Ruby 的步骤。

### A.2.1 开始安装
双击下载后的rubyinstaller-2.3.0-x64.exe 图标，启动Installer。

在语言选择对话框中直接点击OK 按钮。

### A.2.2 同意软件使用许可协议
接下来显示的是RubyInstaller 的软件使用许可协议。RubyInstaller 本身是遵循BSD 许可证的，Ruby 以及第三方的软件的许可证需要另外再确认。如果不是用于商业用途，一般不会有什么问题。确认后，选择I accept the License，点击Next 按钮。

### A.2.3 确认安装路径以及选项
安装时可以指定安装路径以及一些安装选项，选项有以下3 个。
- **安装Tcl/Tk支持**
  Tcl/Tk 是用于创建视窗程序的GUI 类库。虽然本书并没有涉及该内容，不过安装了也不会有什么影响。
- **把Ruby执行文件设置到环境变量PATH中**
  通过设置环境变量PATH，可以在普通的命令行直接执行Ruby.exe。与其他程序的DLL读取也会有关联，在不清楚影响范围时，请不要选择。
- **把.rb与.rbw文件与Ruby关联**
  双击扩展名为.rb 与.rbw 的文件时，会把文件作为Ruby 脚本来执行。

选择需要的选项后，点击Install 按钮。

### A.2.4 安装进度
显示安装进度。

### A.2.5 安装完成
安装完成后，点击Finish 按钮结束RubyInstaller 的安装。

### A.2.6 启动控制台
- 系统为 Windows 10 时，点击开始菜单左下角的“所有程序”后，会按字母顺序显示所有的程序，找到并打开Ruby 2.3.0-p0-x64 文件夹。点击文件夹中的Start Command Prompt with Ruby，系统会自动加载执行Ruby 需要的环境变量，然后启动控制台。或者也可以在开始菜单的搜索栏输入ruby 来快速定位控制台程序
- 系统为Windows 8 时，点击开始界面上的Start Command Prompt with Ruby 启动控制台

启动控制台后，控制台的第一行就显示了Ruby 的版本。

## A.3 在Mac OS X 下安装
Mac OS X 虽然会默认安装Ruby，但因为是旧版本的，所以我们需要自行安装最新版本。
为了确认当前Ruby 的版本，首先要启动控制台。可以通过在Finder 中选择应用程序→工具→终端来启动控制台。输入Ruby -v 就可以显示当前Ruby 的版本。

自行安装最新版Ruby 时，可以选择使用软件包管理工具安装，或者从源代码编译安装。安装步骤与在Unix 下的安装是一样的。

## A.4 在Unix 下安装
Unix 下有可能已经默认安装了Ruby，可在控制台执行 ruby -v 命令确认。

### A.4.1 从源代码编译
首先通过以下URL 下载Ruby 的源代码： ftp://ftp.ruby-lang.org/pub/ruby/ruby-版本号+补丁级别.tar.gz

“版本号+ 补丁级别”部分表示的是Ruby 的版本。本书翻译时最新的版本为2.3.0，因此最新的URL 如下所示：ftp://ftp.ruby-lang.org/pub/ruby/ruby-2.3.0.tar.gz

执行以下命令，解压压缩包后，会自动创建ruby-2.3.0 目录。
```
> tar zxvf ruby-2.3.0.tar.gz
```
进入该目录，按顺序执行以下命令后，就安装完毕了。
```
> cd ruby-2.3.0
> ./configure
> make
> make test
> make install
```
最后的 make install 命令需要配合sudo命令，使用超级管理员权限执行。没有超级管理员权限时，要么请拥有权限的管理人员安装，要么安装在当前用户可读写的目录下。例如，执行以下命令可将Ruby 安装在当前用户的主目录下。
```
> ./configure --prefix=$HOME
```

这种情况下，ruby 命令、irb 命令等将会安装在$HOME/bin 目录下。

### A.4.2 使用二进制软件包
在Unix 下编译安装Ruby 虽然简单，但使用各平台的软件包管理工具安装则会便于日后管理。在Mac OS X 下我们常用Homebrew 和MacPorts 等软件包管理工具。对于Unix 系的平台，在现在广泛使用的版本（各发行版的Linux、BSD 等）中，都有各自对应的二进制或者从源码编译好的软件包，读者可根据各平台的使用方法安装Ruby。

### A.4.3 使用Ruby 软件包管理工具
Ruby 并非某个平台独占的语言，还有一些跨平台的Ruby 专用的软件包管理工具，其中rvm以及rbenv 比较有名。这里我们来介绍一下rbenv 的使用方法。

rbenv 的源码托管在github 网站，可以通过git 命令获取。下面，我们把 rbenv 的源码下载到当前用户的主目录下的.rbenv 目录中。
```
> git clone git://github.com/sstephenson/rbenv.git ~/.rbenv
```

环境变量PATH 以及rbenv 的初始化信息可在shell 的设定文件中设定。使用bash 时，执行以下命令，将必要的初始化信息设定到环境设定文件~/.bashrc 中。如果是在Mac OS X 下，则需要把~/.bashrc 替换为~/.bash_profile 再执行。
```
> echo 'export PATH="$HOME/.rbenv/bin:$PATH"' >> ~/.bashrc
> echo 'eval "$(rbenv init -)"' >> ~/.bashrc
```
然后使用下面的命令更新shell 的状态，也可以直接重启shell。
```
> exec $SHELL -l
```
使用git 命令下载ruby-build。
```
> git clone git://github.com/sstephenson/ruby-build.git ~/.rbenv/plugins/ruby-build
```
这样就可以使用rbenv install 命令了。如果执行rbenv rehash 命令，使用rbenv global 命令，则可将ruby 命令设定为2.3.0 版本。
```
> rbenv install 2.3.0
> rbenv rehash
> rbenv global 2.3.0
> ruby -v
ruby 2.3.0p451 (2015-12-25 revision 53290) [x86_64-darwin14]
```

## A.5 编辑器与IDE
一般我们用文本编辑器编写Ruby 程序。Ruby 语法的编辑器提供了诸如在适当的地方进行代码缩进，对if、while 等关键字、常量、字符串等加上颜色等提高编程效率的功能。

读者可参考使用本节介绍的编辑器、IDE 工具，当然也可以选择符合自己使用习惯的工具。

在Unix、Mac OS X 系统上经常使用的编辑器有Vim 和Emacs。可免费使用的Unix 中，一般都可以通过OS 提供软件包管理工具安装。最近，Sublime Text 和Atom 也越来越受欢迎。Windows 平台下的默认安装的记事本（notepad），其编辑功能非常弱，几乎不能用于编写程序。因此需要自行安装可支持Ruby 编程的编辑器。

除了普通的编辑器外，还可以使用所谓的IDE（集成开发环境）提供的编辑器编写Ruby 程序。IDE 除了可以编写程序，还集成了执行、测试等功能。常用的IDE 有RubyMine。

**没有好用的编辑器就不能编写Ruby 程序吗**

看到这里，也许有的读者会认为没有好用的编辑器就不能用Ruby 编写程序。但实际情况并非如此。在创建简单的程序，或者只是稍微简单修改一下程序时，也可以使用记事本或通过irb 等工具直接执行Ruby 程序。这种情况下，虽然需要在代码缩进等方面下点功夫，但也没想象中那么花时间。

但对于初学者来说，为了便于快速入门，建议还是使用上手门槛较低的编辑器，因此我们在这里介绍了Ruby 的各种常用的编辑器。熟练掌握编辑器的使用方法，是写出优质程序的捷径。