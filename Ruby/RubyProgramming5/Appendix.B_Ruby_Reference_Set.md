# 附录B Ruby参考集
---

[TOC]

## B.1 RubyGems
RubyGems 是一个统一安装和管理Ruby 的库、程序的Ruby 标准工具。在RubyGems 中，每个单独的库称为GEM。通过RubyGems，我们可以搜索GEM、显示GEM 相关的信息、安装或卸载GEM、升级旧版本的GEM，以及查看GEM 的安装进度一览表，等等。

**GEM 命令**
我们一般在命令行使用RubyGems，命令名为gem。

- `gem list`
  显示GEM 的安装进度一览表。
- `gem search`
  用于搜索GEM 文件，没有指定选项时，会搜索远程仓库的GEM 文件。
  指定－l 选项后，则搜索的目标为本地已安装的GEM。
- `gem install`
  安装GEM 文件。安装所需的文件会自动从互联网下载。
- `gem update`
  把GEM 更新为最新版本。
  RubyGems 自身的更新也使用这个命令，这时需要加上--system 选项。

除此之外还有许多GEM 命令，下表为GEM 命令的一览表。

| 选项 | 意义 |
| -- | -- |
| build | 根据gemspec 创建GEM |
| cert | 管理、签署RubyGems 的许可证时使用 |
| check | 检查GEM |
| cleanup | 整理已安装的旧版本的GEM |
| contents | 显示已安装的GEM 的内容 |
| dependency | 显示已安装的GEM 的依赖关系 |
| environment | 显示RubyGems、Ruby 等相关的环境信息 |
| fetch | 把GEM 文件下载到本地目录，但不安装 |
| generate_index | 创建GEM 服务器所需的索引文件 |
| help | 显示GEM 命令的帮助说明 |
| install | 安装GEM 至本地仓库 |
| list | 显示GEM 的一览表 |
| lock | 锁定GEM 版本，并输出锁定后的GEM 列表 |
| mirror | 创建GEM 仓库的镜像 |
| open | 用编辑器编辑已安装的GEM |
| outdated | 显示所有需要更新的GEM 列表 |
| owner | 管理GEM 所有者的资料 |
| pristine | 从GEM 缓存中获取已安装的GEM，并将其恢复为初始状态 |
| push | 向服务器上传GEM |
| query | 搜索本地或者远程仓库的GEM 信息 |
| rdoc | 生成已安装的GEM 的RDoc 文件 |
| regenerate_binstubs | 变更用GEM 安装的命令的shebang |
| search | 显示名字包含指定字符串的GEM |
| server | 启动HTTP 服务器，用于管理GEM 的文档及仓库 |
| sources | 管理搜索GEM 时所需的RubyGems 的源以及缓存 |
| specification 以yaml | 形式显示GEM 的详细信息 |
| stale | 按最后访问的时间顺序显示GEM 的一览表 |
| uninstall | 从本地卸载GEM |
| unpack | 在本地目录解压已安装的GEM |
| update | 更新指定的GEM（或者全部GEM） |
| which | 显示读取GEM 时引用的类库 |
| wrappers | regenerate_binstubs 的别名 |
| yank | 撤回已上传到服务器的GEM 文件 |

## B.2 命令行选项
Ruby的命令行选项

| 选项 | 意义 |
| -- | -- |
| -0octal | 用八进制指定 IO.gets 等识别的换行符 |
| -a | 指定为自动分割模式( 与-n 或者-p 选项一起使用时则将$ F 设为$ _.split($;)） |
| -c | 只检查脚本的语法 |
| -Cdirectory | 在脚本执行前，先移动到directory 目录下 |
| -d、--debug | 使用debug 模式（将 $DEBUG 设为 true ） |
| -e 'command' | 通过command 指定一行代码的程序。本选项可指定多个 |
| -Eex[:in]、--encoding=ex[:in] | 指定默认的外部编码（ex）以及默认的内部编码（in） |
| -Fpattern | 指定 String#split 方法使用的默认分隔符（ $;) |
| -i[extension] | 以替换形式编辑 ARGV 文件（指定extension 时则会生成备份文件） |
| -Idirectory | 指定追加到 $LOAD_PATH 的目录。本选项可指定多个 |
| -l | 删除-n 或者-p 选项中的 $_ 的换行符 |
| -n | 使脚本整体被 'while gets() ; ... end' 包围（将 gets() 的结果设定到$_ 中） |
| -p | 在-n 选项的基础上，在每次循环结束时输出$_ |
| -rlibrary | 在执行脚本前通过 require 引用library |
| -s | 要使脚本解析标志（flag）的功能有效（'ruby -s script -abc'，则 $abc 为 true ） |
| -S | 从环境变量PATH 开始搜索可执行的脚本 |
| -Tlevel | 指定不纯度检查模式 |
| -U | 将内部编码的默认值（ Encoding.default_internal ）设为UTF-8 |
| -v、--verbose | 显示版本号，冗长模式设定为有效（ $VERBOSE 设定为 true ） |
| -w | 冗长模式设定为有效 |
| -Wlevel | 指定冗长模式的级别[0= 不输出警告，1= 只输出重要警告，2= 输出全部警告（默认值）]  |
| -xdirectory | 忽略执行脚本中 #!ruby 之前的内容 |
| --copyright | 显示版权信息 |
| --enable=feature[, ...] | 使feature 有效 |
| --disable=feature[, ...] | 使feature 无效 |
| --external-encoding=encoding | 指定默认的外部编码 |
| --internal-encoding=encoding | 指定默认的内部编码 |
| --version | 显示版本信息 |
| --help | 显示帮助信息 |

下表为 --enable 以及 --disable 选项可指定的feature（功能名）。

| 功能名 | 意义 |
| -- | -- |
| gems | RubyGems 是否有效（默认有效） |
| rubyopt | 是否引用环境变量RUBYOPT（默认引用） |
| did_you_mean | 是否打开拼写检查功能（默认打开） |
| frozen-string-literal | 是否freeze 所有字符串字面量（默认否） |
| all | 上述功能是否全部有效 |

## B.3 预定义变量、常量

### B.3.1 预定义变量
预定义变量是指Ruby 预先定义好的变量。它全部都是以 $ 开头的变量，因此可以像全局变量那样引用。像 $< 相对于 ARGF 那样，有容易看懂的别名时，建议尽量使用该别名。

| 变量名 | 内容 |
| -- | -- |
| $! | 最后发生的异常的相关信息 |
| \$" | \$LOADED_FEATURES 的别名 |
| $$ | 当前执行中的Ruby 的进程 ID |
| $& | 最后一次模式匹配后得到的字符串 |
| $' | 最后一次模式匹配中匹配部分之后的字符串 |
| $* | ARGV 的别名 |
| $+ | 最后一次模式匹配中最后一个() 对应的字符串 |
| $, | Array#join 的默认分割字符串（默认为nil） |
| $. | 最后读取的文件的行号 |
| $/ | 输入数据的分隔符（默认为"\n"） |
| $0 | $PROGRAM_NAME 的别名 |
| \$1、\$2…… | 最后一次模式匹配中与() 匹配的字符串（第n 个() 对应$n） |
| $: | $LOAD_PATH 的别名 |
| $; | String#split 的默认分割字符串（默认为nil） |
| $< | ARGF 的别名 |
| $> | print、puts、p 等的默认输出位置（默认为STDOUT） |
| $? | 最后执行完毕的子进程的状态 |
| $@ | 最后发生的异常的相关位置信息 |
| $\ | 输出数据的分隔符（默认为nil） |
| $_ | gets 方法最后读取的字符串 |
| $` | 最后一次模式匹配中匹配部分之前的字符串 |
| $~ | 最后一次模式匹配相关的信息 |
| $DEBUG | 指定debug 模式的标识（默认为nil） |
| $FILENAME | ARGF 当前在读取的文件名 |
| $LOADED_FEATURES | require 读取的类库名一览表 |
| $LOAD_PATH | 执行require 读取文件时搜索的目录名数组 |
| $PROGRAM_NAME | 当前执行中的Ruby 脚本的别名 |
| $SAFE | 安全模式等级（默认0） |
| $VERBOSE | 指定冗长模式的标识（默认为nil） |
| $stdin | 标准输入（默认为STDIN） |
| $stdout | 标准输出（默认为STDOUT） |
| $stderr | 标准错误输出（默认为STDERR） |

### B.3.2 预定义常量
与预定义变量一样，Ruby 也有预先定义好的常量。下表为预定义常量的一览表。

| 常量名 | 内容 |
| -- | -- |
| ARGF | 参数，或者从标准输入得到的虚拟文件对象 |
| ARGV | 命令行参数数组 |
| DATA | 访问 _END_ _ 以后数据的文件对象 |
| ENV | 环境变量 |
| RUBY_COPYRIGHT | 版权信息 |
| RUBY_DESCRIPTION | ruby -v 显示的版本信息 |
| RUBY_ENGINE | Ruby 的处理引擎 |
| RUBY_PATCHLEVEL | Ruby 的补丁级别 |
| RUBY_PLATFORM | 运行环境的信息（OS、CPU） |
| RUBY_RELEASE_DATE | Ruby 的发布日期 |
| RUBY_VERSION | Ruby 的版本 |
| STDERR | 标准错误输出 |
| STDIN | 标准输入 |
| STDOUT | 标准输出 |

## B.3.3 伪变量
伪变量虽然可以像变量那样引用，但是不能改变其本身的值，对其赋值会产生错误。

| 变量名 | 内容 |
| -- | -- |
| self | 默认的接收者 |
| nil、 true、 false | nil、 true、 false |
| _FILE_ _ | 执行中的Ruby 脚本的文件名 |
| _LINE_ _ | 执行中的Ruby 脚本的行编号 |
| _ENCODING_ _ | 脚本的编码 |

## B.3.4 环境变量

| 变量名 | 内容 |
| -- | -- |
| RUBYLIB | 追加到预定义变量$LOAD_PATH 中的目录名（各目录间用: 分隔） |
| RUBYOPT | 启动Ruby 时的默认选项（RUBYOPT ＝ "-U -v" 等） |
| RUBYPATH | -S 选项指定的、解析器启动时脚本的搜索路径 |
| PATH | 外部命令的搜索路径 |
| HOME | DIR.chdir 方法的默认移动位置 |
| LOGDIR | 没有HOME 时的 DIR.chdir 方法的默认移动位置 |
| LC_ALL、LC_CTYPE、LANG | 决定默认编码的本地信息（平台依赖） |
| RUBYSHELL、COMSPEC | 执行外部命令时，shell 需要使用的解析器路径（平台依赖） |
