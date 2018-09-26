# 第14章 字符串
---

[TOC]

## 14.1 字符串的创建
创建字符串最简单的方法就是把字符的集合用 `" "` 或者 `' '` 括起来并直接写到程序中。除此以外，使用 " " 时还可以执行用 #{} 括起来的Ruby 表达式，并将执行结果嵌入到字符串中。这个 #{} 就称为**内嵌表达式**（embedded expressions）。
```ruby
moji = " 字符串"
str1 = " 那也是#{moji}"
p str1    #=> " 那也是字符串"
str2 = ' 那也是#{moji}'
p str2    #=> " 那也是\#{moji}"
```

使用 " " 时，可以显示使用 `\` 转义的特殊字符
| 特殊字符 | 意义 |
| -- | -- |
| \t | 水平制表符（0x09） |
| \n | 换行符（0x0a） |
| \r | 回车（0x0d） |
| \f | 换页（0x0c） |
| \b | 退格（0x08） |
| \a | 响铃（0x07） |
| \e | 溢出（0x1b） |
| \s | 空格（0x20） |
| \v | 垂直制表符（0x0b） |
| \nnn | 八进制表示方式（n 为0 ～ 7） |
| \Xnn | 十六进制表示方式（n 为0 ～ 9、a ～ f、A ～ F） |
| \Cx、\C-x | Control(Ctrl) + x |
| \M-x | Meta(Alt) + x |
| \M-\C-x | Meta(Alt) + Control(Ctrl) + x |
| \x | 表示x 字符本身（x 为除以上字符外的字符） |
| \unnnn | Unicode 字符的十六进制表示方式（n 为0 ～ 9、a ～ f、A ～ F） |

### 14.1.1 使用 %Q 与 %q
当创建包含 " 或者 ' 的字符串时，比起使用 \" 、 \' 进行转义，使用 %Q 或者 %q 会更简单。 %Q 相当于用 " " 创建字符串，%q 则相当于用 ' ' 创建字符串。
```ruby
desc = %Q{Ruby 的字符串中也可以使用'' 和""。}
str = %q|Ruby said, 'Hello world!'|
```

### 14.1.2 使用Here Document
Here Document 是源自于Unix 的shell 的一种程序写法，使用 << 来创建字符串。创建包含换行的长字符串时用这个方法是最简单的。
```ruby
<<" 结束标识符"
字符串内容
结束标识符
```

<< 后面的结束标识符可以用 " " 括起来的字符串或者用 ' ' 括起来的字符串来定义。用" " 括起来的字符串中可以使用转义字符和内嵌表达式，而用 ' ' 括起来的字符串则不会进行任何特殊处理，只会原封不动地显示。使用既没有 " " 也没有 ' ' 的字符串时，则会被认为是用 " " 创建的字符串。

由于Here Document 整体就是字符串的字面量，因此可以被赋值给变量，也可以作为方法的参数。
我们一般将 EOF 或者 EOB 作为结束标识符使用。Here Document 的结束标识符一定要在行首，EOF 是“End of File”的缩写， EOB 是“End of Block”的缩写。

在程序缩进比较深的地方使用Here Document 的话，有时会出现整个缩进乱掉的情况.
```ruby
10.times do |i|
  print(<<"EOB")
i: #{i}
EOB
end
```

若希望缩进整齐，可以像下面这样用 <<- 代替 << ，这样程序就会忽略结束标识符前的空格和制表符，结束标识符也就没有必要一定要写在行首了。而且，我们可以使用<<~ 消除行首的空白，这样一来，i: #{i} 的缩进也变得整齐了。
```ruby
10.times do |i|
  print(<<~"EOB")
    i: #{i}
  EOB
end
```

此外，我们还可以把Here Document 赋值给变量。
```ruby
str = <<-EOB
Hello!
Hello!
EOB
```

### 14.1.3 使用 sprintf 方法
就像用八进制或十六进制的字符串来表示数值那样，我们也可以用sprintf 方法输出某种格式的字符串。关于 sprintf 的具体使用方法，请参考专栏 “printf 方法与sprintf方法”。

### 14.1.4 使用``
使用 \` 命令 \` 的形式，可以得到命令的标准输出并将其转换为字符串对象。

下面是获取Linux 的ls 命令与cat 命令的输出内容的例子。
```ruby
> irb --simple-prompt
>> `ls -l /etc/hosts`
=> "-rw-r--r-- 1 root root 158 Jan 12 2016 /etc/hosts\n"
>> puts `cat /etc/hosts`
# Host Database
#
127.0.0.1 localhost
255.255.255.255 broadcasthost
::1 localhost
fe80::1%lo0 localhost
=> nil
```

### 专栏：printf 方法与sprintf 方法
下面介绍两个格式化字符串必不可少的方法，即 printf 方法和 sprintf 方法。

printf 方法可以按照指定的格式输出字符串。例如在输出数值时，除了十进制，还能以八进制、十六进制的形式输出，对于小数则可以限制显示的位数等，这些用 printf 方法都能非常轻松地实现。
```ruby
printf(format[, arg1[, ...]])
sprintf(format[, arg1[, ...]])
```

第一个参数format 是字符串，它以“% 字符”的形式指定了我们希望输出的格式。format 后面的参数是与“% 字符”对应的数值。printf 方法会将格式化后的字符串向控制台输出，而sprintf 方法则会将格式化后的字符串以对象的形式返回。sprintf 方法有一个别名叫format，与“字符串 % 数组”这个形式是等价的。

- **指示符**
  “% 指示符”是格式化字符串的基础，指示符将决定如何格式化所得到的数据。

    | 指示符 | 意义 |
    | -- | -- |
    | %c | 输出码位对应的字符 |
    | %s | 输出字符串（调用参数.to_s） |
    | %p | 与p 方法的执行结果一样（调用参数.inspect） |
    | %b, %B | 以二进制形式输出整数 |
    | %o | 以八进制形式输出整数 |
    | %d, %i | 以十进制形式输出整数 |
    | %x, %X | 以十六进制形式输出整数 |
    | %f | 输出浮点数 |
    | %e | 以指数形式输出浮点数 |
    | %% | 输出% 字符本身 |

- **标记、最小宽度、精度**
  在% 和指示符之间可以再指定标记、最小宽度和精度。
    | 标记 | 意义 |
    | -- | -- |
    | # | 对于%b、%B、%o、%x、%X，输出加上前缀后的结果(0b、0B、0、0x、0X) |
    | - | 指定了宽度时，使输出结果左对齐 |
    | + | 输出符号+ 或- |
    | 空格 | 仅当负数时输出符号- |
    | 0 | 指定最小宽度时，多出的位数不预埋空格而是预埋0 |

标记后面的最小宽度与精度用于指定输出的位数，最小宽度与精度指定的格式为“最小宽度. 精度”。最小宽度表示输出的最小位数，当结果比指定的宽度长时则会溢出显示。精度表示指示符为%f 时小数点后面的位数，当指示符为%s 和%p 时表示的是最大位数。最小宽度与精度用“*”代替时，表示从参数取值。
```ruby
p sprintf("%8s", "Ruby")            #=> " Ruby"
p sprintf("%8.8s", "Hello Ruby")    #=> "Hello Ru"
p sprintf("%#010x", 100)            #=> "0x00000064"
p sprintf("%+.2f", Math::PI)        #=> "+3.14"
p sprintf("%*.*f", 5, 2, Math::PI)  #=> " 3.14"
```

## 14.2 获取字符串的长度
我们用 length 方法和 size 方法获取字符串的长度。两者都返回相同的结果。（中文字符串则返回字符数。）
如果想获取的不是字符数，而是字节数，可以用 bytesize 方法。
```ruby
p "just another ruby hacker,".length  #=> 25
p "just another ruby hacker,".size    #=> 25
p ' 面向对象编程语言'.length           #=> 8
p ' 面向对象编程语言'.bytesize         #=> 24
```
想知道字符串的长度是否为 0 时，可以使用 empty? 方法。
```ruby
p "".empty? #=> true
p "foo".empty? #=> false
```

## 14.3 字符串的索引
获取字符串中指定位置的字符，例如获取开头第3 位的字符时，与数组一样，我们也需要用到索引。
```ruby
str = " 全新的String 类对象"
p str[0]     #=> " 全"
p str[3]     #=> "S"
p str[9]     #=> " 类"
p str[2, 8]  #=> " 的String 类"
p str[4]     #=> "t"
```

## 14.4 字符串的连接
连接字符串有以下两种方法：
- 将两个字符串合并为新的字符串
- 扩展原有的字符串

创建新的字符串时，可以使用 +；为原有字符串连接其他字符串时，可以使用 << 或者 concat 方法。
```ruby
hello = "Hello, "
world = "World!"
str = hello + world
p str                  #=> "Hello, World!"
hello << world
p hello                #=> "Hello, World!"
hello.concat(world)
p hello                #=> "Hello, World!World!"
```

使用 + 也能连接原有字符串，如下所示。
```ruby
hello = hello + world
```
用 + 连接原有字符串的结果会被再次赋值给变量 hello ，这与使用 << 的结果是一样的。但用 + 连接后的字符串对象是新创建的，并没有改变原有对象，因此即使有其他变量与 hello 同时指向原来的对象，那些变量的值也不会改变。而使用 << 与 concat 方法时会改变原有的对象，因此就会对指向同一对象的其他变量产生影响。

一般情况下使用 << 与 concat 方法会更加有效率，但是我们也应该根据实际情况来选择适当的字符串连接方法。

## 14.5 字符串的比较
我们可以使用 = = 或者 != 来判断字符串是否相同；判断是否为相似的字符串时，可以使用正则表达式。

**比较字符串的大小**
字符串也有大小关系，由字符编码的顺序决定。在排列英语或日语的字符串时，英文字母按照“ABC”的顺序排列，日语的平假名与片假名按照“あいうえお”的顺序排列。

> 不过，Ruby 中日语的排序规则与字典中的顺序是不同的。例如对“かけ”“かこ”“がけ”这3 个单词排序时，字典中的顺序是“かけ”“がけ”“かこ”，而在Ruby 中，从小到大依次是“かけ”“かこ”“がけ”。

### 专栏：字符编码
计算机中的字符都是用数值来管理的，这样的数值也称为编码。我们把上表字符与数值一一对应的关系称为字符编码。但字符编码并不是一个正确的专业术语，因此使用时需要注意。

ASCII 编码是计算机的基础。ASCII 编码把英文字母、数值、其他符号，以及换行符、制表符这样的特殊字符集合起来，为它们分配 1 到 127 之间的数值，并使之占用 1 个字节的空间（1 个字节可以表示 0 到 255）。另外，在欧美，一种名为 ISO-8859-1 的编码曾经被广泛使用，该编码包含了欧洲常用的基本字符（拼写符号、原音变音等），并为它们分配 128 到 255 之间的数值。也就是说，大部分的字符都是占用 1 个字节的空间。

使用日语时不可能不使用平假名、片假名或者汉字，但只用1 个字节来表示这些字符是不可能的。因此，为了表示这些字符，用2 个字节表示1 个字符的技术就诞生了。不过，非常可惜的是日语的字符编码并不只有1 种，而是大概可以分为以下4 种编码方式，并且相同编码方式得到的字符也不一定相等：
| 编码方式 | 主要使用的地方 |
| -- | -- |
| UTF-8 | Unix 文本、HTML 等 |
| Shift_JIS | Windows 文本 |
| EUC-JP | Unix 文本 |
| ISO-2022-JP | 电子邮件等 |

为字符分配与之对应的数值，这样的分配方式就称为字符编码方式（character encoding scheme）。在日本，常用的编码方式有Shift_JIS、EUC-JP、ISO-2022-JP 这3 种，它们是日语标准字符编码JIS X0208 的基础。字符编码的名称一般都直接使用编码方式的名称。

编码方式不同的情况下，即使是相同的字符，对其分配的数值也会不一样。编码方式的不同，就是导致俗称为“乱码”的问题的原因之一。

## 14.6 字符串的分隔
用特定字符分割字符串时可以使用 split 方法，分割后的各项字符串会以数组的形式返回。
```ruby
column = str.split(/:/)
```

## 14.7 换行符的使用方法
用 each_line 等方法从标准输入读取字符串时，末尾会有换行符。然而，在实际处理字符串时，换行符有时会很碍事。这种情况下，我们就需要删除多余的换行符。
| - | 删除最后一个字符 | 删除换行符 |
| -- | -- | -- |
| 非破坏性的 | chop | chomp |
| 破坏性的 | chop! | chomp! |

chop 方法与 chop! 方法会删除字符串行末的任意字符， chomp 方法与 chomp! 方法则只在行末为换行符时才将其删除。

用 each_line 方法循环读取新的行时，一般会使用具有破坏性的 chomp! 方法直接删除换行符。
```ruby
f.each_line do |line|
  line.chomp!
  处理line
end
```

## 14.8 字符串的检索与替换
字符串处理一般都离不开检索与替换。Ruby 可以很轻松地处理字符串。

### 14.8.1 字符串的检索
我们可以用 index 方法或者 rindex 方法，来检查指定的字符串是否存在于某字符串中。
index 方法会从左到右检查字符串中是否存在参数指定的字符串，而 rindex 方法则是按照从右到左的顺序来检查。找到字符串时， index 方法和 rindex 方法会返回字符串首个字符的索引值，没找到时则返回 nil。

如果只是想知道字符串中是否有参数指定的字符串，可以使用 include? 方法。

### 专栏：换行符的种类
所谓换行符就是指进行换行的符号。就像在专栏“字符编码”中介绍的那样，计算机中使用的字符都被分配了一个与之对应的数值，同理，换行符也有一个与之对应的数值。不过麻烦的是，不同的OS 对换行符的处理也不同。

下面是常用的OS 的换行符。这里，LF（LineFeed）的字符为 `\n` ，CR（Carriage Return）的字符为 `\r` 。
| OS 种类 | 换行符 |
| -- | -- |
| Unix 系列 | LF |
| Windows 系列 | CR + LF |
| Mac OS 9 以前 | CR |

Ruby 中的标准换行符为 LF，一般在 IO#each_line 等方法中使用。也就是说，Ruby 在处理Max OS 9 以前版本的文本时，可能会出现不能正确换行的情况。

我们可以通过参数指定 each_line 方法使用的换行符，默认为 each_line("\n")。

### 14.8.2 字符串的替换
有时我们可能会需要用其他字符串来替换目标字符串中的某一部分，这时可以使用 sub 方法与 gsub 方法。
> **相关**：关于 sub 方法与 gsub 方法，我们将会在16.6.1 节中详细介绍。

## 14.9 字符串与数组的共同方法
字符串中有很多与数组通用的方法。
当然，继承了 Object 类的实例的方法，在字符串（ String 类的实例）以及数组（ Array 类的实例）中也都能使用。除此以外，下面的方法也都能使用：
1. 与索引操作相关的方法
2. 与 Enumerable 模块相关的方法
3. 与连接、反转（reverse）相关的方法

### 14.9.1 与索引操作相关的方法
- `s[n] = str`,`s[n..m] = str`,`s[n, len] = str`,`s.slice(n)`,`s.slice(n..m)`,`s.slice(n, len)`：用str 替换字符串s 的一部分。这里 n、m、len 都是以字符为单位的。
- `s.slice!(n)`,`s.slice!(n..m)`,`s.slice!(n, len)`：删除字符串 s 的一部分，并返回删除的部分。

### 14.9.2 返回 Enumerator 对象的方法
在处理字符串的方法中，有以行为单位进行循环处理的 each_line 方法、以字节为单位进行循环处理的 each_byte 方法，以及以字符为单位进行循环处理的 each_char 方法。调用这些方法时若不带块，则会直接返回 Enumerator 对象，因此，通过使用这些方法，就可以像下面的例子这样使用 Enumerable 模块的方法了。
```ruby
# 用collect 方法处理用each_line 方法获取的行
str = " 壹\n 贰\n 叁\n"
tmp = str.each_line.collect do |line|
  line.chomp * 3
end
p tmp   #=> [" 壹壹壹", " 贰贰贰", " 叁叁叁"]
# 用collect 方法处理用each_byte 方法获取的数值
str = "abcde"
tmp = str.each_byte.collect do |byte|
  -byte
end
p tmp   #=> [-97, -98, -99, -100, -101]
```

### 专栏：Enumerator 类
虽然 Enumerable 模块非常方便，但它将遍历元素的方法限定为 each 方法，这一点有些不太灵活。
String 对象有 each_byte、 each_line、 each_char 等用于循环的方法，如果这些方法都能使用 each_with_index 、 collect 等 Enumerable 模块的方法的话，那就方便多了。而 Enumerator 类就是为了解决这个问题而诞生的。

Enumerator 类能以 each 方法以外的方法为基础，执行 Enumerable 模块定义的方法。使用 Enumerator 类后，我们就可以用 String#each_line 方法替代 each 方法，执行 Enumerable 模块的方法了。

不带块的情况下，大部分Ruby 原生的迭代器在调用时都会返回 Enumerator 对象。因此，我们就可以对 each_line、 each_byte 等方法的返回结果继续使用 map 等方法。
```ruby
str = "AA\nBB\nCC\n"
p str.each_line.class                   #=> Enumerator
p str.each_line.map{|line| line.chop }  #=> ["AA", "BB", "CC"]
p str.each_byte.reject{|c| c = = 0x0a } #=> [65, 65, 66, 66, 67, 67]
```
### 14.9.3 与连接、反转相关的方法
除了与 Enumerable 模块、索引等相关的方法外，字符串中还有一些与数组通用的方法。

- `s.concat(s2)`,`s+s2`：与数组一样，字符串也能使用 concat 方法和 + 连接字符串。
- `s.delete(str)`,`s.delete!(str)`：从字符串s 中删除字符串str。
- `s.reverse`,`s.reverse!`：反转字符串s。


## 14.10 其他方法
- `s.strip`,`s.strip!`：
- `s.upcase`,`s.upcase!`,`s.downcase`,`s.downcase!`,`s.swapcase`,`s.swapcase!`,`s.capitalize`,`s.capitalize!`：所谓的case 在这里就是指英文字母的大、小写的意思。 ~case 方法就是转换字母大小写的方法：
  - upcase 方法会将小写字母转换为大写，大写字母保持不变。
  - downcase 方法则刚好相反，将大写字母转换为小写。
  - swapcase 方法会将大写字母转换为小写，将小写字母转换为大写。
  - capitalize 方法会将首字母转换为大写，将其余的字母转换为小写。
- `s.tr`,`s.tr!`：源自于Unix 的tr 命令的方法，用于替换字符。该方法与 gsub 方法有点相似，不同点在于 tr 方法可以像 s.tr("a-z", "A-Z") 这样一次替换多个字符。tr 方法不能用正则表达式替换字符，因为它只是用一个字符替换另外一个字符而已。

## 14.11 日语字符编码的转换
字符编码转换有两种方法，分别是使用 encode 方法和使用 nkf 库。

### 14.11.1 encode 方法
encode 方法是 Ruby 中字符编码转换的基本方法。将字符编码由EUC-JP 转换为UTF-8，程序可以像下面这样写：
```ruby
# encoding: EUC-JP
euc_str = " 日语EUC 编码的字符串"
p euc_str.encoding #=> #<Encoding:EUC-JP>
utf8_str = euc_str.encode("utf-8")
p utf8_str.encoding #=> #<Encoding:UTF-8>
```

另外，Ruby 中还定义了具有破坏性的 encode! 方法。encode 方法支持的字符编码，可通过 Encoding.name_list 方法获得。

### 14.11.2 nkf 库
使用 encode 方法可以进行字符编码的转换，但却不能进行半角假名与全角假名之间的转换，全半角假名的转换需要使用 nkf 库。
nkf 库由 NKF 模块提供。 NKF 模块是Unix 的nkf（Network Kanji code conversion Filter）过滤命令在 Ruby 中的实现。
NKF 模块用类似于命令行选项的字符串指定字符编码等。
```ruby
NKF.nkf( 选项字符串, 转换的字符串)
```

| 选项 | 意义 |
| -- | -- |
| -d | 从换行符中删除CR |
| -c | 向换行符中添加CR |
| -x | 不把半角假名转换为全角假名 |
| -m0 | 抑制MIME 处理 |
| -h1 | 把片假名转换为平假名 |
| -h2 | 把平假名转换为片假名 |
| -h3 | 互换平假名与片假名 |
| -Z0 | 把JIS X 0208 的数字转换为ASCII |
| -Z1 | 加上-Z0，把全角空格转换为半角空格 |
| -Z2 | 加上-Z0，把全角空格转换为两个半角空格 |
| -e | 输出的字符编码为EUC-JP |
| -s | 输出的字符编码为Shift-JIS |
| -j | 输出的字符编码为ISO-2022-JP |
| -w | 输出的字符编码为UTF-8（无BOM） |
| -E | 输入的字符编码为EUC-JP |
| -S | 输入的字符编码为Shift-JIS |
| -J | 输入的字符编码为ISO-2022-JP |
| -W | 输入的字符编码为UTF-8（无BOM） |
| -W16 | 输入的字符编码为UTF-16（Big Endian/ 无BOM） |


为了避免半角假名转换为全角假名，或者因电子邮件的特殊字符处理而产生问题，如果只是单纯对字符进行编码转换的话，一般使用选项 -x 和 -m0 （可以合并书写 -xm0 ）就足够了。下面是把 EUC-JP 字符串转换为 UTF-8 的例子。

```ruby
# encoding: EUC-JP
require "nkf"
euc_str = " 日语EUC 编码的字符串"
utf8_str = NKF.nkf("-E -w -xm0", euc_str)
```

不指定输入字符编码时， nkf 库会自动判断其编码，基本上都可以按如下方式书写。
```ruby
# encoding: EUC-JP
require "nkf"
euc_str = " 日语EUC 编码的字符串"
utf8_str = NKF.nkf("-w -xm0", euc_str)
```

NKF 模块在 Ruby 字符串还未支持 encoding 功能以前就已经开始被使用了。大家可能会感觉选项的指定方法等与现在 Ruby 的风格有点格格不入，这是因为 nkf 库把其他命令的功能硬搬了过来。如果不涉及一些太特殊的处理，一般使用 encode 方法就足够了。
 
### 专栏：字符串字面量与freeze
作为字面量的字符串一般都不会被冻结（freeze），可以被任意更改。
```ruby
str = "Ruby"
p str.upcase! #=> "RUBY"
```

对此，我们可以在脚本的开头添加魔法注释 frozen-string-literal: true，这样脚本中的所有字符串字面量都会被冻结，这与Object#freeze 方法的效果一样。

另外，我们还可以在执行脚本时指定选项 --enable=frozen-string-literal，这样即使没有魔法注释，脚本内的字符串字面量也都会被冻结。
