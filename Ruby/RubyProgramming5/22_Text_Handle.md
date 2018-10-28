# 第22章 文本处理
---

[TOC]

## 22.1 准备文本
## 22.1.1 下载文件
这里，我们以鲁迅先生的短篇小说《孔乙己》为例进行说明。这篇小说于1919 年刊登在《新青年》上，是鲁迅先生的第2 部白话小说。现可在图灵社区上获得在线版。以下是《孔乙己》的HTML 版本的URL： http://www.ituring.com.cn/article/274457

虽然也可以使用浏览器访问上述URL 来阅读这篇小说，不过既然已经学习了Ruby，下面就让我们来试试用Ruby 下载。
```ruby
require "open-uri"
url = "http://www.ituring.com.cn/article/274457"
filename = "kongyiji.html"
File.open(filename, "wb") do |f|
  text = open(url, "r:utf-8").read
f.write text # UTF8 环境使用此段代码
  #f.writetext.encode("GB18030") # 中文简体版Windows 使用此段代码
end
```

在处理文本时，我们需要注意换行符。操作系统有各自的标准换行符，由于这次我们下载的HTML 文件的换行符是 CR + LF，为了能在任何环境中都能直接保存，我们指定File.open 方法的第2 个参数为"wb"，以二进制的方式写入文件。

另外，在处理中文字符时还需要注意编码问题。特别是从外部输入时，要尽量指定相同的编码。从Mac OS 以及Unix 的命令行传进来的字符串多为UTF-8 编码，因此文件编码建议也保持一致。例子中的HTML 文件为UTF-8 编码，在简体中文版 Windows 环境下可以使用encode 方法转换为GB18030 后，再用write 方法输出到文件。

### 22.1.2 获取正文
从如上代码得到的是用于在浏览器中显示的HTML 文件。这个文件中有很多像头部、底部这样的不需要的部分，因此这里我们只把正文部分抽取出来。首先必须定义好正文的起始位置与结束位置，为此就必须先看看HTML 中的内容。下面，我们来好好研究一下刚才下载的kongyiji.html：
```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7, IE=9" />
    <title> 图灵社区 : 阅读 : 孔乙己</title>
<link
href="/Content/2012/css/style?v=vmP5Np5uGvkEjQSa_T7iDBuqMY1QJ3qWm2K3UbnRIkc1"
rel="stylesheet"/>
…
<div class="span8">
<div class="post-text">
<pre><code> 鲁镇的酒店的格局，是和别处不同的：
…
<p><em> 本文选自《呐喊》，鲁迅著，亿部文化有限公司，2012 年11 月出版</em></p>
</div>
```

通过上面的内容可以看出，以“ <div class="post-text"> ”开始的行就是正文的开始。同样，从“ <div class="copyright-announce"> ”这一行开始则为底部，之后的内容与正文无关。然后，对这两行做上标记，把正文部分摘取出来。
```ruby
htmlfile = "kongyiji.html"
textfile = "kongyiji.txt"

html = File.read(htmlfile)

File.open(textfile, "w") do |f|
  in_header = true
  html.each_line do |line|
    if in_header && /<div class="post-text">/ !~ line
      next
    else
      in_header = false
    end
    break if /<div class="copyright-announce">/ =~ line
    f.write line
  end
end
```

在这个脚本中，以包含字符串 <div class="post-text"> 的行作为开始，包含字符串 <div class="copyright-announce"> 的行作为结束，把中间的内容保存到kongyiji.txt文件中。

首先，使用 File.read 方法读取HTML 文件的全部内容。

接下来，对HTML 文件的字符串使用 each_line 方法，逐行读取内容并赋值给变量line，然后再将其保存到文件中。不过，在保存之前，需要先把 in_header 变量设为 true 。这个变量可用于检查正在处理的行是否为头部。第9 行的 if 语句会利用这个变量值进行判断，如果是在头部内，并且正在读取的行不包含 <div class="post-text">， 则跳出本次循环。而除此以外的情况下，则表示已经离开了头部部分，因此就要将 in_header 设置为 false 。这样一来，从下个循环开始就不会再执行next 了。

第14 行中使用了 if 修饰符。break if... 这样的形式常被用于跳出循环。这种写法的优点在于可以紧凑地书写不太长的 if 条件。在这里，程序会判断是否为表示正文结束的行，成功匹配则跳出循环。

接下来是第15 行，程序能走到这里就证明 line 是正文部分，因此，使用 write 方法将 line 的内容输出到文件。

### 22.1.3 删除标签
但是输出到文件的正文部分中还残留着HTML 标签（tag）。虽然即使有HTML 标签也可以进行文本处理，但在本章中并不需要标签。因此接下来我们就来考虑一下如何把标签删除，以获取纯文本（plain text）格式的文件。

一般情况下，要删除HTML 标签，可以考虑使用解析HTML 用的类库，不过在本例中，我们只是单纯地通过正则表达式来替换。

```ruby
require 'cgi/util'
htmlfile = "kongyiji.html"
textfile = "kongyiji.txt"

html = File.read(htmlfile)

File.open(textfile, "w") do |f|
  in_header = true
  html.each_line do |line|
    if in_header && /<div class="post-text">/ !~ line
      next
    else
      in_header = false
    end
    break if /<div class="copyright-announce">/ =~ line
    line.gsub!(/<[^>]+>/, '')
    esc_line = CGI.unescapeHTML(line)
    f.write esc_line
  end
end
```

代码清单22.3 在代码清单22.2 的基础上添加了删除标签的功能，而实际上两者只有第16 行和第17 行的代码是不一样的。

在第16 行中，用正则表达式 /<[^>]+>/ 表示标签。由于HTML 标签是以 < 开始，以 > 结束的，因此这样就可以匹配标签部分。在第17 行中，使用 CGI.unescapeHTML 方法，将HTML 标签的 &amp; 、 &lt; 等转义字符转换为普通字符 & 、 < 等。通过在第1 行追加 require'cgi/util' ，我们就可以使用这个方法了。

做出这样的修改后，我们就可以得到以下文本。
```
鲁镇的酒店的格局，是和别处不同的：都是当街一个曲尺形的大柜台，柜里面预备着热水，可以随时温酒。做工的人，
傍午傍晚散了工，每每花四文铜钱，买一碗酒，——这是二十多年前的事，现在每碗要涨到十文，——靠柜外站着，
热热的喝了休息；倘肯多花一文，便可以买一碟盐煮笋，或者茴香豆，做下酒物了，如果出到十几文，那就能买
一样荤菜，但这些顾客，多是短衣帮，大抵没有这样阔绰。只有穿长衫的，才踱进店面隔壁的房子里，要酒要菜，
慢慢地坐喝。
```

## 22.2 扩展simple_grep.rb ：显示次数
接下来，我们来看看simple_grep.rb。这里对第3 章中的例子稍微做了一点修改（代码清单22.4）。
```ruby
pattern = Regexp.new(ARGV[0])
filename = ARGV[1]
File.open(filename) do |file|
  file.each_line do |line|
    if pattern =~ line
      print line
    end
  end
end
```

由于我们在 File.open 方法中使用了块，因此 File#close 就不再需要了，这样一来，程序也清爽了好多。

利用这个程序，我们来调查一下正文中“孔乙己”这个单词总共出现了多少次。

**计算匹配行**
由于通过simple_grep.rb 匹配的行会原封不动地显示出来，因此Mac OS X 或Linux 的情况下，就可以配合wc 命令A 查看文本的行数。
```
> ruby simple_grep.rb ' 孔乙己' kongyiji.txt | wc
11 108 6652
```
在Windows 中则像下面这样使用find 命令。
```
> ruby simple_grep.rb ' 孔乙己' kongyiji.txt | find /c /v ""
```

但是，我们不能确切地说正文中“孔乙己”出现了11 次，这是因为如果1 行中该单词出现了多次的话，那么只通过计算行数是不能得出正确的结果的。
下面我们来改造这个程序，用 String#scan 方法，计算匹配的次数
```ruby
pattern = Regexp.new(ARGV[0])
filename = ARGV[1]
count = 0
File.open(filename) do |file|
  file.each_line do |line|
    if pattern =~ line
      line.scan(pattern) do |s|
        count += 1
      end
      print line
    end
  end
end
puts "count: #{count}"
```

```
ruby simple_scan.rb ' 孔乙己' kongyiji.txt
　　我从此便整天的站在柜台里，专管我的职务。虽然没有什么失职，但总觉得有些单调，有些无聊。掌柜是一
副凶脸孔，主顾也没有好声气，教人活泼不得；只有孔乙己到店，才可以笑几声，所以至今还记得。
　　…
count: 33
```

结果显示，“孔乙己”总共出现了33 次。
如果不需要逐行处理，而只是单纯计算出现次数的话，则有更简单的实现方法
```ruby
pattern = Regexp.new(ARGV[0])
filename = ARGV[1]
count = 0
File.read(filename).scan(pattern) do |s|
  count += 1
end
puts "count: #{count}"
```

## 22.3 扩展simple_grep.rb ：显示匹配的部分
### 22.3.1 突出匹配到的位置
虽然显示出了匹配的行，但具体匹配到的位置却很难看清楚。因此下面我们就来试试在显示的时候突出匹配的部分
```ruby
pattern = Regexp.new(ARGV[0])
filename = ARGV[1]

count = 0
File.open(filename) do |file|
  file.each_line do |line|
    if pattern =~ line
      line.scan(pattern) do |s|
        count += 1
      end
      print line.gsub(pattern){|str| "<<#{str}>>"}
    end
  end
end
puts "count: #{count}"
```

在第11 行，使用 gsub 方法将原来直接输出变量 line 的地方进行转换后再输出。由于对 gsub 方法使用块后，匹配部分就可以通过块变量获取，因此这里在匹配部分的前后加上 <<>> 并返回。



### 22.3.2 显示前后各10 个字符
然而，在上述simple_match.rb 的执行结果中，匹配部分在行中的位置比较分散，这时我们就希望显示效果能更加紧凑些，例如显示匹配部分及其前后各10 个字符。

```ruby
pattern = Regexp.new("(.{10})("+ARGV[0]+")(.{10})")
filename = ARGV[1]
count = 0
File.open(filename) do |file|
  file.each_line do |line|
    line.scan(pattern) do |s|
      puts "#{s[0]}<<#{s[1]}>>#{s[2]}"
      count += 1
    end
  end
end
puts "count: #{count}"
```

正则表达式中的{n} 表示重复之前的模式n 次。因此，本例中的 .{10} 就表示匹配10 个任意字符。

通过执行可以看出，出现的次数减少了。这是因为，就像执行示例第3 行所示，“孔乙己”刚好在前后各10 个字符之内时不会被匹配，因此我们来修改一下：
```ruby
pattern = Regexp.new(ARGV[0])
filename = ARGV[1]

count = 0
File.open(filename) do |file|
  file.each_line do |line|
    line.scan(pattern) do |s|
      pre = $`
      post = $'
      puts "#{pre[-10,10]}<<#{s}>>#{post[0,10]}"
      count += 1
    end
  end
end
puts "count: #{count}"
```

随着第1 行的正则表达式的修改，第8 行到第10 行的输出部分也进行了相应的修改。把正则表达式匹配结果的前后部分的变量$` 以及$' 赋值给变量pre 以及post 后，在第10 行的输出部分中会各自截取10 个字符。

再次执行程序，这次的结果终于对了。

### 22.3.3 让前后的字符数可变更
现在我们默认前后的字符只能是10 个。不过这个数量如果能修改，那就灵活多了。于是，我们再次改造一下程序
```ruby
pattern = Regexp.new(ARGV[0])
filename = ARGV[1]
len = ARGV[2].to_i

count = 0
File.open(filename) do |file|
  file.each_line do |line|
    line.scan(pattern) do |s|
      pre = $`
      post = $'
      puts "#{pre[-len,len]}<<#{s}>>#{post[0,len]}"
      count += 1
    end
  end
end
puts "count: #{count}"
```

将指定长度的位置替换为变量 len， 可以通过 ARGV[2] 将其作为第3个参数来指定。
