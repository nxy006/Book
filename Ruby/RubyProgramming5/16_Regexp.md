﻿# 第16章 正则表达式类
---
Ruby 的特点是“万物皆对象”，正则表达式也不例外。正则表达式对象所属的类就是接下来 我们将要介绍的 Regexp 类。

[TOC]

## 16.1 关于正则表达式

### 16.1.1 正则表达式的写法与用法
正如我们在2.3 节中介绍的那样，正则表达式描述的是一种“模式”，该模式被用于匹配字符串。一般情况下，我们把正则表达式模式的对象（ Regexp 类对象）称为“正则表达式对象”， 或直接称为“正则表达式”。
到目前为止，我们都是使用纯文本字符作为模式，而实际上还有更复杂的模式。例如，通过 模式可以很简单地匹配“首字符为A 到D 中的某个字母，从第2 个字符开始为数字”这样的字 符串（这个模式可写为 /[A-D]\d+/ ）。 
但模式也并非是万能的，例如像“与Ruby 类似的字符串”这种含糊的模式就无法书写。模 式说明的东西应该具体一些，例如“以 R 开头，以 y 结尾，由4 个字母组成”（这个模式可写 为/ R..y/）。 
为了能够熟练掌握正则表达式，首先就需要理解正则表达式模式的写法。因此，在学习 正则表达式的具体用法前，本章将首先介绍一下正则表达式各种模式的写法，然后再介绍如何 使用正则表达式。

### 16.1.2 正则表达式对象的创建方法
在程序中，通过用 `//` 将表示正则表达式模式的字符串括起来，就可以非常简单地创建出正 则表达式。另外，我们也可以使用类方法 Regexp.new(str) 来创建对象。当程序中已经定义了字符 串对象str，且希望根据这个字符串来创建正则表达式时，就可以使用这个方法。 
```ruby
re = Regexp.new("Ruby")
```
除上述两种方法外，与数组、字符串一样，我们也可以通过使用 `%` 的特殊语法来创建正则 表达式。正则表达式的情况下使用的是 `%r` ，如果正则表达式中包含 / ，用这种方法会比较方 便。%r 的语法如下所示。
```ruby
%r( 模式) 
%r<模式> 
%r | 模式| 
%r! 模式!
```

## 16.2 正则表达式的模式与匹配
`=~` 方法是正则表达式中常用的方 法，可以用来判断正则表达式与指定字符串是否匹配。 
```ruby
正则表达式 =~ 字符串
```
无法匹配时返回 nil， 匹配成功则返回该字符串起始字符的位置。
我还可以使用 !~ 来颠倒“真”与“假”。

### 16.2.1 匹配普通字符
当模式中只写有英文、数字时， 正则表达式会单纯地根据目标字符串中是否包含该模式中的字符来判断是否匹配

### 16.2.2 匹配行首与行尾
`/ABC/` 模式的情况下，只要是包含 ABC 的字符串就都可以匹配。但如果我们只想匹配 ABC 这一字符串，可以使用模式 `/^ABC$/ `。 
`^` 、 `$` 是有特殊意义的字符，它们并不用于匹配字符 ^ 与 $ 。像这样的特殊字符，我们称为 **元字符**（meta character）。`^` 表示匹配行首， `$` 表示匹配行尾。

### 专栏：行首与行尾
`^` 、 `$` 分别匹配“行首”“行尾”，而不是“字符串的开头”“字符串的末尾”。匹配字符串 的开头用元字符`\`A`， 匹配字符串的末尾用元字符`\ z`。 这两种情况有什么不同呢？ Ruby 的字符串，也就是 String 对象中，所谓的“行”就 是用换行符( \n ) 隔开的字符串，因此模式 /^ABC/ 也可以匹配字符串 "012\nABC" 。

那么为什么要将行首/ 行尾与字符串的开头/ 末尾分开定义呢？这是有历史原因的。 具体来说，原本正则表达式只能逐行匹配字符串，不能匹配多行字符串，因此就可以认 为一个“字符串”就是一“行”。 但是，随着正则表达式的广泛使用，人们开始希望可以匹配多行字符串。而如果仍 用 ^ 、 $ 来匹配字符串的开头、末尾的话就很容易造成混乱，因此就另外定义了匹配字符串 开头、末尾的元字符。 
另外，还有一个与 \z 类似的元字符，就是 `\Z` ，不过两者的作用有点不一样。 `\Z` 虽然 也是匹配字符串末尾的元字符，但它有一个特点，就是如果字符串末尾是换行符，则匹配换行符前一个字符。 
```ruby
p "abc\n".gsub(/\z/, "!") => "abc\n!" 
p "abc\n".gsub(/\Z/, "!") => "abc!\n!"
```
我们一般常用\ z， 而很少使用\ Z。

### 16.2.3 指定匹配字符的范围
有时候我们会希望匹配“ ABC 中的1 个字符”。像这样，希望指定多个字符中的1 个字符时， 可以使用 [] 。
- [AB]……A或B
- ABC]……A或B或C
- [CBA]……同上（与[ ]中的顺序无关）
- [012ABC]……0、1、2、A、B、C中的1个字符

不过，如果按照这样的写法，那么匹配“从A 到Z 的全部英文字母”时就麻烦了。这种情 况下，可以在 [] 中使用 - ，来表示一定范围内的字符串。

- [ A-Z]……从A到Z的全部大写英文字母
- [ a-z]……从a 到z 的全部小写英文字母
- [ 0-9]……从0到9 的全部数字
- [ A-Za-z]……从A到Z与从a到z的全部英文字母
- [ A-Za-z_]……全部英 文字母与_

> **备注：**字符的范围也称为“字符类”。请注意这里的“类”与面向对象中的“类”的意义是不一样的。

如果 - 是 [] 中首个或者最后 1 个字符，那么就只是单纯地表示 - 字符。反过来说，如 果 - 表示的不是字符类，而是单纯的字符 - ，那么就必须写在模式的开头或者末尾。 
- [A-Za-z0-9_-]……全部英文字母、全部数字、_、-
在 [] 的开头使用 ^ 时， ^ 表示指定的字符以外的字符。 
- [^ABC]……A、B、C以外的字符
- [^a-zA-Z]……a到z，A到Z（英文字母）以外的字符

### 16.2.4 匹配任意字符
有时我们会希望定义这样的模式：不管是什么字符，只要匹配 1 个字符就行。这种情况下， 可以使用元字符 `.`。 
- `.` ……匹配任意字符

然而，如果任意字符都能匹配的话，也就没有必要特意指定了。在下面两种情况下，一般会使用这个元字符。 
- 在希望指定字符数时使用 `/^...$/` 这样的模式可以匹配字符数为 3 的行。
- 与稍后介绍的元字符*配合使用
  > 关于这部分的详细内容请参考16.2.6 节。

### 16.2.5 使用反斜杠的模式
与字符串一样，我们也可以使用“ \ ”+“1 个英文字母”这样的形式来表示换行、空白等特殊字符。
- `\s` 表示空白符，匹配空格（0 x20） 、制表符（Tab）、换行符、换页符
- `\d` 匹配0 到9 的数字
- `\w` 匹配英文字母与数字
- `\A` 匹配字符串的开头
- `\z` 匹配字符串的末尾

**元字符转义 **
我们还可以用 `\` 对元字符进行转义。在\ 后添加 ^ 、 $ 、[ 等非字母数字的元字符后，该元 字符就不再发挥元字符的功能，而是直接被作为字符本身用于匹配（表16.11）。

### 16.2.6 重复
正则表达式中用以下元字符来表示重复匹配的模式。
- `*`……重复0次以上
- `+`……重复1次以上
- `?`……重复0次或1次
- `{n}`……重复n次
- `{n, m}`……重复n~m次

在正则表达式中使用 `{}` 可以更详细地指定重复的次数。{3} 表示刚好重复3 次，{3,} 表示重复3 次以上，{,3} 表示重复3 次以下，{3,5} 则表示重复3~5 次。

### 16.2.7 最短匹配
表示重复0 次以上的 * 以及表示重复1 次以上的 + 会匹配尽可能多的字符（也称贪婪匹配。——译者注）。相反，匹配尽可能少的字符B 时，可以用以下元字符。（也称懒惰匹配。——译者注）
- `*?` ……0次以上的重复中最短的部分
- `+?` ……1次以上的重复中最短的部分

### 16.2.8 ( ) 与重复
上面的例子只是以 1 个字符为单位进行重复，而通过使用 () ，还可以重复多个字符

### 16.2.9 选择
我们可以用 `|` 在几个候补中任意匹配一个。
例如 `/^(ABC|DEF)$/`

## 16.3 使用quote 方法的正则表达式
有时我们可能会希望转义（escape）正则表达式中的所有元字符，这种情况下可以使用quote 方法或者escape 方法（两个方法是等价的）。 quote 方法会返回转义了正则表达式的元字符后的字符串，然后再将其作为new 方法的参数生成新的正则表达式对象。
```ruby
re1 = Regexp.new("abc*def")
re2 = Regexp.new(Regexp.quote("abc*def"))
p (re1 =~ "abc*def") #=> nil
p (re2 =~ "abc*def") #=> 0
```

## 16.4 正则表达式的选项
正则表达式中还有选项，使用选项可以改变正则表达式的一些默认效果。
设定正则表达式的选项时，只需在 /…/ 的后面指定即可，如 /…/im， 这里的 i 以及 m 就是正则表达式的选项,分为以下几种：
- i：忽略英文字母大小写的选项。指定这个选项后，无论字符串中的字母是大写还是小写都会被
匹配。
- x：忽略正则表达式中的空白字符以及 # 后面的字符的选项。指定这个选项后，就可以使用 # 在正则表达式中写注释了。
- m：指定这个选项后，就可以使用 . 匹配换行符了。

正则表达式的选项
| 选项 | 选项常量 | 意义 |
| -- | -- | -- |
| i | Regexp::IGNORECASE | 不区分大小写 |
| x | Regexp::EXTENDED | 忽略模式中的空白字符 |
| m | Regexp::MULTILINE | 匹配多行 |
| o | （无） | 只使用一次内嵌表达式 |

Regexp.new 方法中的第 2 个参数可用于指定选项常量。未指定第2 个参数时，可以指定 nil 或者只用第 1 个参数。例如， /Ruby 脚本/i 这一正则表达式，可以像下面这样写。
```ruby
Regexp.new("Ruby 脚本", Regexp::IGNORECASE)
```
另外，我们还可以用 | 指定多个选项。这时，“ /Ruby 脚本/im ”这一正则表达式就变成了下面这样。
```ruby
Regexp.new("Ruby 脚本",
Regexp::IGNORECASE | Regexp::MULTILINE)
```

## 16.5 捕获
除了检查字符是否匹配外，正则表达式还有另外一个常用功能，甚至可以说是比匹配更加重要的功能——捕获（后向引用）。
所谓捕获，就是从正则表达式的匹配部分中提取其中的某部分。通过$1、$2 这种“ $ 数字 ”形式的变量，就可以获取匹配了正则表达式中用 () 括起来的部分的字符串。
```ruby
/(.)(.)(.)/ =~ "abc"
first = $1
second = $2
third = $3
p first #=> "a"
p second #=> "b"
p third #=> "c"
```
在进行匹配时，我们只知道是否匹配、匹配第几个字符之类的信息。而使用捕获后，就可以知道哪部分被匹配了。因此，通过这个功能，可以非常方便地对字符串进行分析。
在修改程序中的正则表达式时，如果改变了 () 的数量，那么将要引用的部分的索引也会随之改变，有时就会带来不便。这种情况下，可以使用 (?: ) 过滤不需要捕获的模式。
```ruby
/(.)(\d\d)+(.)/ =~ "123456"
p $1 #=> "1"
p $2 #=> "45"
p $3 #=> "6"
/(.)(?:\d\d)+(.)/ =~ "123456"
p $1 #=> "1"
p $2 #=> "6"
```

除了“ $ 数字”这种形式以外，保存匹配结果的变量还有 $` 、 $& 、 $' ，分别代表匹配部分前面的字符串、匹配部分的字符串、匹配部分后面的字符串。为了方便大家快速理解这 3 个变量的含义，我们来看看下面这个例子。
```ruby
/C./ =~ "ABCDEF"
p $` #=> "AB"
p $& #=> "CD"
p $' #=> "EF"
```
这样一来，我们就可以将字符串整体分为匹配部分与非匹配部分，并将其分别保存在 3 个不同的变量中。
> **备注**：匹配后的结果可以通过$~ 或者Regexp.last_match 获取。得到的返回值是MatchData 类的对象，可以使用$~[1] 代替$1，$~.pre_match 代替$`。

## 16.6 使用正则表达式的方法
字符串相关的方法中有一些使用了正则表达式，接下来我们就来介绍一下其中的 sub 方法、 gsub 方法、 scan 方法。

### 16.6.1 sub 方法与 gsub 方法
sub 方法与 gsub 方法的作用是用指定的字符串替换字符串中的某部分字符。
sub 方法与 gsub 方法都有两个参数。第 1 个参数用于指定希望匹配的正则表达式的模式，第 2 个参数用于指定与匹配的部分替换的字符串。 sub 方法只替换首次匹配的部分，而 gsub 方法则会替换所有匹配的部分。
```ruby
str = "abc def g hi"
p str.sub(/\s+/,' ') #=> "abc def g hi"
p str.gsub(/\s+/,' ') #=> "abc def g hi"
```
/\s+/ 是用于匹配1 个以上的空白字符的模式。因此在本例中， sub 方法与 gsub 方法会将匹配的空白部分替换为1 个空白。 sub 方法只会替换 abc 与 def 间的空白，而 gsub 方法则会将字符串后面匹配的空白部分全部替换。

sub 方法与 gsub 方法还可以使用块。这时，程序会将字符串中匹配的部分传递给块，并在块中使用该字符串进行处理。这样一来，块中返回的字符串就会替换字符串中匹配的部分。
```ruby
str = "abracatabra"
nstr = str.sub(/.a/) do |matched|
'<'+matched.upcase+'>'
end
p nstr #=> "ab<RA>catabra"
nstr = str.gsub(/.a/) do |matched|
'<'+matched.upcase+'>'
end
p nstr #=> "ab<RA><CA><TA>b<RA>"
```

在本例中，程序会将字符串 a 以及 a 之前的字母转换为大写，并用 <> 将其括起来。
sub 方法与 gsub 方法也有带 ! 的方法。 sub! 方法与 gusb! 方法会直接将作为接收者的对象变换为替换后的字符串

### 16.6.2 scan 方法
scan 方法能像 gsub 方法那样获取匹配部分的字符，但不能进行替换操作。因此，当需要对匹配部分进行某种处理时，可以使用该方法。
在正则表达式中使用 () 时，匹配部分会以数组的形式返回。
```ruby
"abracatabra".scan(/(.)(a)/) do |matched|
  p matched
end

# ["r", "a"]
# ["c", "a"]
# ["t", "a"]
# ["r", "a"]
```

如果没有指定块，则直接返回匹配的字符串数组。
```ruby
p "abracatabra".scan(/.a/) #=> ["ra", "ca", "ta", "ra"]
```

## 16.7 正则表达式的例子
找出类似于HTTP 的URL 的字符串：`/http:\/\//`
在此基础上，我们还可以进一步写出“获取类似于URL 的字符串中的某部分”的正则表达式：`/http:\/\/([^\/]*)\//`
上述例子中使用了较多 / ，不便于阅读，可以使用 %r 将其改写成像下面这样：`%r|http://([^/]*)/|`
我们再看看获取服务器地址以外部分的正则表达式：`%r|^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?|`

这是在 `RFC2396“Uniform Resource Identifiers(URI):Generic Syntax”`这个定义URI 语法的文件中使用的正则表达式。

这个正则表达式可以被原封不动地用在Ruby 中。如果用这个正则表达式进行匹配，则 HTTP 等协议名会被保存在 $2 中，服务器地址等会被保存在 $4 中，路径名会被保存在 $5 中，请求部分会被保存在 $7 中，片段（fragment）会被保存在 $9 中。

然而，写到这种程度，正则表达式已经变得非常复杂了。如果把正则表达式写成在任何情况下都能匹配的万能模式，就会使得正则表达式变得难以读懂，增加程序的维护成本。相比之下，只满足当前需求的正确易懂的正则表达式则往往更有效率。
例如，匹配邮政编码的正则表达式，可以写成这样：`/\d\d\d-\d\d\d\d/`

> **备注**：帮想进一步了解正则表达式的读者可以参考Jeffrey E.F.Friedl 的《精通正则表达式》一书。该书系统地介绍了正则表达式，而且评价也非常高。