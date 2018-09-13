# 第03章 创建命令
---

[TOC]

## 3.1 命令行的输入数据
现在我们来尝试向程序传递数据，最简单的方法是使用命令行。Ruby中使用 `ARGV` 这个预定义数组获取从命令行传来的数，即字符串参数。
```ruby
puts "首个参数：#{ ARGV[0] }"
puts "第2参数：#{ ARGV[1] }"
puts "第3参数：#{ ARGV[2] }"
puts "第4参数：#{ ARGV[3] }"
puts "第5参数：#{ ARGV[4] }"
```
执行示例：
```
> ruby print_argv.rb 1st 2nd 3rd 4th 5th
首个参数：1st
第2参数：2nd
第3参数：3rd
第4参数：4th
第5参数：5th
```

使用数组 `ARGV` 后，程序需要使用的数据就不需要固定到代码中。注意获取到的数据都是字符串，可能需要类型转换。比如转换为整数可以使用 `to_i` 方法。

## 3.2 文件的读取

### 3.2.1 从文件中读取内容并输出
首先，我们创建一个单纯读取文件内容的程序，流程如下所示：

```ruby
filename = ARGV[0]
file = File.open(filename)  # 1. 打开文件
text = file.read            # 2. 读取文件的文本数据
print text                  # 3. 输出文件的文本数据
file.close                  # 4. 关闭文件
```

逐行代码解释略，如果只是读取内容、可以使代码更加简单，还可以省去变量：
```ruby
print File.read(AGRV[0])
```

### 3.2.2 从文件中逐行读取内容并输出
我们已经了解了如何读取并输出文件内容，但刚才的程序有如下问题：
1. 一次读取全部文件会很耗时
2. 读取的文件会暂存在内存，遇到大文件时，程序可能因此崩溃

因此，我们希望程序能够只读取所需要的行，程序改造如下：
```ruby
filename = ARGV[0]
file = File.open(filename)
file.each_line do | line |
  print line
end
file.close
```

### 3.2.3 从文件中读取指定模式的内容并输出
Unix 中有一个叫 `grep` 的命令，它利用正则表达式搜索文本资源，输出按照模式匹配到的行，我们试试用 Ruby 实现 grep 命令：
```ruby
pattern = Regexp.new(ARGV[0])
filename = ARGV[1]

file = File.open(filename)
file.each_line do | line |
  if pattern =~ line
    print line
  end
end
file.close
```

## 3.3 方法的定义
定义的方法基本语法如下：
```ruby
def 方法名
  希望执行的处理
end
```

注意定义方法后，还需要调用方法，方法才能执行。

## 3.4 其他文件引用
大部分编程语言都提供了把多个不同程序组合为一个新程序的功能，像这样被其他程序引用的程序，我们称之为 **库**（library）。
Ruby 使用 `require` 方法或者 `require_relative` 方法来引用库。
- `require` 用于引用已存在的库，程序将在预定义路径下查找并读取与 Ruby 一起安装的库
-  `require_relative` 在查找库时根据程序执行目录进行，这有利于程序在读取不同文件的代码

```ruby
require 希望使用的库名
require_relative 希望使用的库名
```

库名可以省略后缀 .rb ，调用 require 方法后，Ruby 会搜索并读取库的全部内容，读取完毕后才会继续执行 require 方法后面的处理。

### 专栏：pp方法
Ruby 除了提供 p 方法外，还提供了 `pp` 方法。`pp` 是英语 `pretty print` 的缩写，要使用 `pp` 方法，需要引用 pp 库： `require "pp"`

与 `p` 方法不同，`pp` 在输出对象的结果时，为了更容易地看懂，会是当地换行以调整输出结果。建议在确定嵌套内容的时候使用 `pp` 方法。
