第19章 Encoding 类
---

[TOC]

## 19.1 Ruby 的编码与字符串
字符编码是计算机进行字符操作的基础，这一点我们已经在第14 章的专栏中进行了介绍。就像在专栏中介绍的那样，字符编码有多种，而且即使是在同一个程序中，有时候输入/ 输出的字符编码也有可能不一样，例如程序输入是UTF-8 字符编码，而输出却是Shift_JIS 字符编码等。虽然“あ”的UTF-8 的字符编码与Shift_JIS 的字符编码实际上是不同的，但经过适当的转换，也是可以编写这样的程序的。

至于程序如何处理字符编码，不同的编程语言有不同的解决方案。Ruby 的每个字符串对象都包含“字符串数据本身”以及“该数据的字符编码”两个信息。其中，关于字符编码的信息即我们一般所讲的编码。

创建字符串对象一般有两种方法，一种是在脚本中直接以字面量的形式定义，另外一种是从程序的外部（文件、控制台、网络等）获取字符串数据。数据的获取方式决定了它的编码方式。截取字符串的某部分，或者连接多个字符串生成新字符串对象时，会继承原有的字符串的编码。
程序向外部输出字符串时，必须指定适当的编码。Ruby 会按照以下信息决定字符串对象的编码，或者在输入/ 输出处理时转换编码：
- **脚本编码**
  决定字面量字符串对象的编码的信息，与脚本的字符编码一致。详细内容请参考19.2 节。
- **内部编码与外部编码**
  内部编码是指从外部获取的数据在程序中如何处理的信息。与之相反，外部编码是指程序向外部输出时与编码相关的信息。两者都与 IO 对象有关联。详细内容请参考19.5 节。

## 19.2 脚本编码与魔法注释
通过在脚本的开头书写魔法注释，能够指定Ruby 脚本的编码。

脚本自身的编码称为脚本编码（script encoding）。脚本中的字符串、正则表达式的字面量会依据脚本编码进行解释。脚本编码为EUC-JP 时，字符串、正则表达式的字面量也都为EUC-JP。同样，如果脚本编码为Shift_JIS，那么字符串、正则表达式的字面量也为Shift_JIS。我们把指定脚本编码的注释称为魔法注释（magic comment）。

Ruby 在开始解释脚本前，会先读取魔法注释来决定脚本编码。魔法注释必须写在脚本的首行（第1 行以 #! ～开头时，则写在第2 行）。下面是将脚本编码指定为UTF-8 的例子：
```ruby
# encoding: utf-8
```

> **备注**：在Unix 中，赋予脚本执行权限后，就可以直接执行脚本。这时，可以在文件开头以“# ！命令的路径”的形式来指定执行脚本的命令。在本书的例子中，我们经常使用“>ruby 脚本名”这样的形式来表示在命令行执行脚本的命令为 ruby ，但若像“ #! /usr/bin/ruby ”这样，在文件开头写上ruby 命令的路径的话，那么就能直接以“> 脚本名”的形式执行脚本了。

此外，为了可以兼容Emacs、VIM 等编辑器的编码指定方式，也可以像下面这样写。
```ruby
# -*- coding: utf-8 -*- # 编辑器为Emacs 时
# vim:set fileencoding=utf-8: # 编辑器为Vim 时
```

由于不使用魔法注释时的脚本编码就是UTF-8，因此用UTF-8 作为编码的脚本不需要使用魔法注释。但如果使用了UTF-8 以外的字符，就需要通过魔法注释指定适当的编码。

用String#encoding 方法可获取字符串的编码，用伪变量__ENCODING__ 可获取当前运行脚本的编码。顺便提一下，UTF-8 有比其他编码更灵活的用法，不管脚本编码是什么，只要在字符串字面量中使用特殊字符\u，就可得到UTF-8 字符串。下面的代码片段就是在GBK 编码的脚本中使用UTF-8 字符串的例子。
```ruby
# encoding: GBK
p __ENCODING__                  #=> #<Encoding:GBK>
s = "\u57FA\u7840\u6559\u7A0B"  #=> UTF-8 的“基础教程”
puts s                          #=> “基础教程”
ps.encoding                     #=> #<Encoding:UTF-8>
```

## 19.3 Encoding 类
如前所述， String#encoding 方法会返回 Encoding 对象。

在脚本中使用不同的编码时，需要进行必要的转换。我们可以用 String#encode 方法转
换字符串对象的编码。
```ruby
str = " 你好"
p str.encoding #=> #<Encoding:UTF-8>
str2 = str.encode("GBK")
p str2.encoding #=> #<Encoding:GBK>
```
在本例中，我们尝试把UTF-8 字符串对象转换为新的GBK 字符串对象。
在操作字符串时，Ruby 会自动进行检查。如果要连接不同编码的字符串则会产生错误。例如：`Encoding::CompatibilityError: incompatible character encodings: UTF-8 and GBK`

为了防止错误，在连接字符串前，必须使用 encode 方法等把两者转换为相同的编码。还有，在进行字符串比较时，如果编码不一样，即使表面的值相同，程序也会将其判断为不同的字符串。
```ruby
# encoding: utf-8
p " 你" = = " 你".encode("GBK") #=> false
```
另外，在本例中，用 String#encode 指定编码时，除了可以使用编码名的字符串外，还可以直接使用 Encoding 对象来指定。

**Encoding 类的方法**
接下来，我们将会介绍 Encoding 类的方法。
- `Encoding.compatible?(str1, str2)`：  检查两个字符串的兼容性。这里所说的兼容性是指两个字符串是否可以连接。可兼容则返回字符串连接后的编码，不可兼容则返回 nil。
  ```ruby
  p Encoding.compatible?("AB".encode("GBK"),
  "你".encode("GBK")) #=> #<Encoding:UTF-8>
  p Encoding.compatible?(" 你".encode("GBK"),
  "你".encode("GBK")) #=> nil
  ```
  AB 这个字符串的编码无论是GBK 还是UTF-8 都是一样的，因此，将其转换为GBK 后也可以与UTF-8字符串连接；而“你”这个字符串则无法连接，因此返回 nil。
- `Encoding.default_external`：返回默认的外部编码，这个值会影响 IO 类的外部编码，详细内容请参考19.5 节。
- `Encoding.default_internal`：返回默认的内部编码，这个值会影响 IO 类的内部编码，详细内容请参考19.5 节。
- `Encoding.find(name)`：返回编码名name 对应的 Encoding 对象。预定义的编码名由不含空格的英文字母、数字与符号构成。查找编码时不区分name 的大小写。
  预定义的特殊的编码名如下表：

  | 名称 | 意义 |
  | -- | -- |
  | locale | 根据本地信息决定的编码 |
  | external | 默认的外部编码 |
  | internal | 默认的内部编码 |
  | filesystem | 文件系统的编码 |

- `Encoding.list`, `Encoding.name_list`：返回 Ruby 支持的编码一览表。 list 方法返回的是 Encoding 对象一览表，Encoding.name_list 返回的是表示编码名的字符串一览表，两者的结果都以数组形式返回。
- `enc.name`：返回 Encoding 对象enc 的编码名。
- `enc.names`：像ASCII-8BIT、BINARY 这样，有些编码有多个名称。这个方法会返回包含 Encoding 对象的名称一览表的数组。只要是这个方法中的编码名称，就都可以在通过 Encoding.find 方法检索时使用。

### 专栏：ASCII-8BIT 与字节串
ASCII-8BIT 是一个特殊的编码，被用于表示二进制数据以及字节串，因此有时也称这个编码为BINARY。
此外，把字符串对象用字节串形式保存时也会用到这个编码。例如，使用Array#pack方法将二进制数据生成为字符串时，或者使用 Marsha1.dump 方法将对象序列化后的数据生成为字符串时，都会使用该编码。

下面是用 Array#pack 方法把IP 地址的4 个数值转换为4 个字节的字节串的例子。
```ruby
str = [127, 0, 0, 1].pack("C4")
p str #=> "\x7F\x00\x00\x01"
p str.encoding # => #<Encoding:ASCII-8BIT>
```
Array#pack 方法的参数为用于字节串化的模式，C4 表示 4 个 8 位的不带符号的整数。执行结果为 4 个字节的字节串，编码为ASCII-8BIT。此外，在使用 open-uri 库等工具通过网络获取文件时，有时并不知道字符编码是什么，这时也默认使用ASCII-8BIT 编码。

即使是编码为ASCII-8BIT 的字符串，只要知道字符编码，就可以使用 force_encoding 方法。这个方法并不会改变字符串的值（二进制数据），而只是改变编码信息。
```ruby
# encoding: utf-8
require 'open-uri'
str = open("http://www.example.cn/").read
str.force_encoding("GBK")
p str.encoding #=> #<Encoding:GBK>
```
这样一来，我们就可以把ASCII-8BIT 的字符串当作GBK 字符串来处理了。
使用 force_encoding 方法时，即使指定了不正确的编码，也不会马上产生错误，而是在对该字符串进行操作时才会产生错误。检查编码是否正确，可以用 valid_encoding? 方法，不正确时则返回 false 。
```ruby
str = " 你好"
str.force_encoding("US-ASCII") #=> 不会产生错误
str.valid_encoding? #=> false
str + " 大家" #=> Encoding::CompatibilityError
```

## 19.4 正则表达式与编码
与字符串同样，正则表达式也有编码信息。正则表达式的编码即与其匹配的字符串的编码。例如，用GBK 的正则表达式对象去匹配UTF-8 字符串时就会产生错误，反之亦然。

通常情况下，正则表达式字面量的编码与脚本编码是一样的。
指定其他编码时，可使用 Regexp 类的 new 方法。在这个方法中，第1 个参数中会给出作为正则表达式模式的字符串，该字符串的编码就是该正则表达式的编码。
```ruby
str = " 模式".encode("GBK")
re = Regexp.new(str)
p re.encoding #=> #<Encoding:GBK>
```

##19.5 IO 类与编码

### 19.5.1 外部编码与内部编码
每个 IO 对象都包含外部编码与内部编码两种编码信息。外部编码指的是作为输入/ 输出对象的文件、控制台等的编码，内部编码指的是Ruby 脚本中的编码。 IO 对象的编码的相关方法如下表所示：

| 方法名 | 意义 |
| -- | -- |
| IO#external_encoding | 返回 IO 的外部编码 |
| IO#internal_encoding | 返回 IO 的内部编码 |
| IO#set_encoding | 设定 IO 的编码 |

没有明确指定编码时， IO 对象的外部编码与内部编码各自使用其默认值 Encoding.default_external 、 Encoding.default_internal。 默认情况下，外部编码会基于各个系统的本地信息设定，内部编码不设定。Windows 环境下的编码信息如下所示。
```ruby
p Encoding.default_external #=> #<Encoding:GBK>
p Encoding.default_internal #=> nil
File.open("foo.txt") do |f|
  p f.external_encoding #=> #<Encoding:GBK>
  p f.internal_encoding #=> nil
end
```

### 19.5.2 编码的设定
要设定 IO 对象的编码信息，可以使用 IO#set_encoding 方法，或者在 File.open 方法的参数中指定编码。
- `io.set_encoding(encoding)`：IO#set_encoding 方法以" 外部编码名: 内部编码名" 的形式指定字符串encoding。
把外部编码设置为Shift_JIS，内部编码设置为UTF-8 时，可以像下面这样设定。
  ```ruby
  $stdin.set_encoding("GBK:UTF-8")
  p $stdin.external_encoding #=> #<Encoding:GBK>
  p $stdin.internal_encoding #=> #<Encoding:UTF-8>
  ```

- `File.open(file, "mode:encoding")`：为了在打开文件file 时通过 File.open 方法指定编码encoding，可以在第 2 个参数中 mode 的后面用":" 分隔，并按顺序指定外部编码以及内部编码（内部编码可省略）。

  ```ruby
  # 指定外部编码为UTF-8
  File.open("foo.txt", "w:UTF-8")
  # 指定外部编码为GBK
  # 指定内部编码为UTF-8
  File.open("foo.txt", "r:GBK:UTF-8")
  ```

另外，编码的作用是告诉程序如何处理字符，因此对二进制文件是无效的。

在 17.3 节说明如何指定字节数操作文件时，我们介绍了IO#seek 方法与IO#read（size）方法，这些方法都不受编码影响，对任何数据都可以进行读写操作。IO#read（size）方法读取的字符串的编码为表示二进制数据的 ASCII-8BIT。

### 19.5.3 编码的作用
接下来，我们来看看 IO 对象中设定的编码信息是如何工作的。

**输出时编码的作用**

外部编码影响 IO 的写入（输出）。在输出时，会基于每个字符串的原有编码和 IO 对象的外部编码进行编码的转换（因此输出用的 IO 对象不需要指定内部编码）。

如果没有设置外部编码，或者字符串的编码与外部编码一致，则不会进行编码的转换。在需要进行转换时，如果输出的字符串的编码不正确（比如实际上是日文字符串，但编码却是中文），或者是无法互相转换的编码组合（例如用于日文与中文的编码），这时程序就会抛出异常。

编码相关的处理都是通过IO#write 方法来执行的。IO 对象的全部输出处理都是在内部调用write 方法，因此经常会受到编码的影响。

**输入时编码的作用**

IO 的读取（输入）会稍微复杂一点。首先，如果外部编码没有设置，则会使用 Encoding.default_external 的值作为外部编码。

设定了外部编码，但内部编码未设定时，则会将读取的字符串的编码设置为 IO 对象的外部编码。这种情况下并不会进行编码的转换，而是将文件、控制台输入的数据原封不动地保存为 String 对象。

最后，外部编码和内部编码都设定了的时候，则会执行由外部编码转换为内部编码的处理。输入与输出的情况一样，在编码转换的过程中如果数据格式或者编码组合不正确，程序都会抛出异常。

在IO 类的输入方法中，像gets 方法、getc 方法这样以行或者以字符为单位的读取都会受到编码的影响。read 方法在省略参数时，由于是从头到尾一次性读取全部文件，因此会受到编码的影响，但在指定读取长度时，由于数据是以二进制的方式处理的，因此不会受到编码的影响。此外，getbyte 方法等以字节为单位的读取也不会受编码的影响。
