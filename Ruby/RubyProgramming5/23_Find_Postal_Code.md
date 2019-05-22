# 第23章 检索邮政编码
---

[TOC]

## 23.1 获取邮政编码
日本的邮政编码可在官方网站上下载：http://www.post.japanpost.jp/zipcode/dl/kogaki-zip.html，点击“都道府県一覧”栏中最后的“全国一括”下载zip 文件，用zip 工具解压后就可以得到格式为CSV、编码为Shift_JIS 的全日本的邮政编码文件K EN_ALL.CSV。

> **备注**：所谓CSV（Comma-Separated Values）格式是指 "aaa","bb","ccccc" 这样的值之间用逗号隔开的数据格式。

在CSV 格式中，1 行就是1 组数据的集合，通常被称为“记录”。也就是说，1 行就是1 条记录。记录被逗号隔成多个值，每个值所在的区间称为“列”。刚才提到的KEN_ALL.CSV 的每条记录包含15 列。从记录的左边开始数起，第1 列、第2 列……以此类推，一般相同类型的数据会放在同一列中。
邮政编码数据中各个字段的含义，可在邮政编码的说明页面查询。前 9 个字段的含义如下所示。
1. 日本地方公共团体编码（JIS X0401、X0402）：半角数字
2. （旧）邮政编码（5 位）：半角数字
3. 邮政编码（7 位）：半角数字
4. 都道府县名：半角片假名（按编码顺序排序）
5. 市区町村名：半角片假名（按编码顺序排序）
6. 町域名：半角片假名（按五十音顺序排序）
7. 都道府县名：汉字（按编码顺序排序）
8. 市区町村名：汉字（按编码顺序排序）
9. 町域名：汉字（按五十音顺序排序）

另外，第13 个字段表示的是“1 个邮政编码表示多个町域”的情况，当这个值为1 时，表示同一个邮政编码会出现在多条数据中。

下面是实际文件中的1 条数据，可以看出，各项目之间用逗号（,）隔开，除了开头和末尾的项目外，其他都用"" 括了起来。
```
01101,"060 ","0600000"," ﾎｯｶｲﾄﾞｳ"," ｻｯﾎﾟﾛｼﾁｭｳｵｳｸ"," ｵｵﾄﾞｵﾘﾆｼ(1-19 ﾁｮｳﾒ)"," 北海道"," 札幌市中央区"," 大通西（１〜１９丁目）",0,0,0,0,0,0
```

## 23.2 csv 库
我们可以通过csv 库操作CSV 文件。这个库是Ruby 内建的，因此直接读入require "csv" 就可以使用该csv 库了。

读取已存在的CSV 文件时，使用CSV.open 方法打开文件，从头开始逐条读取记录。使用块时，则会把CSV 对象以块变量的形式传入块中执行，块结束时关闭文件。CSV#each 会逐条读取记录，同时会将以逗号区分的数据转换为数组后再执行块。现在就让我们用csv 库写一个简单的程序来操作KEN_ALL.CSV 文件。
CSV.open 方法与File.open 方法一样，可在参数中指定文件名、操作模式以及编码。

```ruby
require "csv" # 使用csv 库
code = ARGV[0] # 读取参数
start_time = Time.now # 获取操作开始时间
# 将Shift_JIS 转换为UTF-8 后打开CSV 文件
CSV.open("KEN_ALL.CSV", "r:Shift_JIS:UTF-8") do |csv|
  csv.each do |record|
    # 邮政编码与参数指定的一致则输出该记录
    puts record.join(" ") if record[2] == code
  end
end
p Time.now - start_time # 输出结束时间与开始时间之差
```

```
> rubyread_csv.rb 1000002
13101 100 1000002 ﾄｳｷｮｳﾄﾁﾖﾀﾞｸｺｳｷｮｶﾞｲｴﾝ 東京都 千代田区 皇居外苑0 0 0 0 0 0 6.655200747
```
从输出结果可以知道，在笔者的笔记本电脑上本次执行花费了6.6 秒。像上面这样从头到尾逐行读取记录进行查找的方式会很耗费时间，虽然很实用，但我们还是来找找更加快捷的处理办法吧。

## 23.3 sqlite3 库
为了加快数据处理的速度，可以使用数据库，这里我们使用开源的关系型数据库SQLite 以及操作数据库用的语言SQL，来对数据进行检索、更新等操作。由于SQLite 的第3 版是最新版本，因此有时我们会直接称之为SQLite3。（SQLite官方网站：http://www.sqlite.org/）

用Ruby 操作SQLite3，需要使用 SQLite3 库。它是以GEM 的形式发布的库，需要安装后才能使用。关于gem 的内容，请参考B.1 节。安装GEM 需要使用gem 命令。
```
gem install sqlite3
```

> **注**：sqlite3 的gem，在Windows 中是经过编译的，并已发布二进制包，而在Linux、Mac OS X 中，除了安装对应发行版的包以外，还需要执行gem 命令。

数据库中的数据是以“表”为单位管理的。1 张表就像1 个CSV 文件，表中有多行数据，每行数据中有多个项目。在数据库中创建很多这样的表，并将各种数据保存到表中，就可以使用SQL 对数据进行插入、更新、删除等操作。

让我们先看看用SQLite3 处理数据的例子。在保存数据前，首先需要准备保存数据用的表。这里，我们对名为address.db 的数据库文件创建 addresses 表，用于保存名字与住址。以下是创建表的程序。
```ruby
SQLite3::Database.open "address.db" do |db|
  db.execute(<<-SQL)
    CREATE TABLE addresses
      (name TEXT, address TEXT)
  SQL
end
```

本书只用了SQLite3 中的个别功能，实际上只使用了两个方法：一个是SQLite3::Database 类的类方法open 方法，另外一个是SQLite3::Database 类的实例方法 execute 方法。

SQLite3::Database.open 方法的第1 个参数为数据库的文件名。通过第2 行的Database#execute 方法，执行被用于在address.db 中创建新表 addresses 的 CREATETABLE 语句。CREATE TABLE 语句就是从第2 行的<<-SQL 到第5 行的SQL 中间的HereDocument 部分。SQL 文本过长时，使用Here Document 会便于阅读。这样我们就创建了一个包含name 和address 两个字段的addresses 表。为了可以保存任何长度的字符串，我们将各字段的类型都定义为没有长度限制的TEXT 类型。

创建表后，像下面这样对表插入数据。
```ruby
data = [" 山田实", " 东江户川区东江户川三丁目"]
SQLite3::Database.open "address.db" do |db|
  db.execute(<<-SQL,data)
    INSERT INTO addresses VALUES (?, ?)
  SQL
end
```

第1 行是需要插入的数据， 是一个仅包含必要数量的元素的数组。第2 行的SQLite3::Database.open 方法和之前的一样，第3 行的Database#execute 方法执行的是INSERT 语句，这是向数据库插入数据的SQL。(?, ?) 表示表中的字段，第2 个参数data 的元素会按顺序依次被替换到? 的位置。我们把像? 这样之后进行赋值的位置称为占位符（placeholder）。

占位符过多，会不便于理解其与数组元素的对应关系。我们可以像下面这样，把数据以散列的形式传进去，在占位符使用“: 键名”的形式读取参数值。
```ruby
data = {
  name: " 山田实",
  addr: " 东江户川区东江户川三丁目"
}
SQLite3::Database.open "address.db" do |db|
  db.execute(<<-SQL, data)
    INSERT INTO addresses VALUES (:name, :addr)
  SQL
end
```
读取插入后的数据时可以像下面这样：
```ruby
SQLite3::Database.open("address.db") do |db|
  db.execute(<<-SQL){|rows| p rows}
    SELECT name, address FROM addresses
  SQL
end
```

## 23.4 插入数据
下面我们来编写一个查询邮政编码的程序。程序的功能通过封装为 JZipCode 类的方法来实现。
首先需要设计邮政编码表的构成。这里，我们简单地设计为下面这样的表。

| - | 邮政编码 | 都道府县名 | 市区町村名 | 町域名 | 用于检索的住址 |
| -- | -- | -- | -- | -- | -- |
| 字段名 | code | pref | city | address | alladdress |
| 数据类型 | TEXT | TEXT | TEXT | TEXT | TEXT |

为了简化程序，我们将数据类型都定义为TEXT 类型，可保存任意长度的字符串数据。

开头4 个字段的数据直接来自CSV 文件。最后的“用于检索的住址”是连接都道府县名、市区町村名、町域名的字符串。用住址进行检索时，可用“东京都港区”A 这样的“都道府县名+市区町村名”的字符串进行检索。

使用SQL 的 CREATE TABLE 语句创建表。创建表23.1 的表的SQL 如下所示。这样，我们就创建了有5 个TEXT 类型字段的zip_codes 表。另外，IF NOT EXISTS 表示仅当没有同名表时才创建表。
```ruby
CREATE TABLE IF NOT EXISTS zip_codes
  (code TEXT, pref TEXT, city TEXT, addr TEXT, alladdr TEXT);
```

代码清单23.2 中的 JZipCode 类实现了把邮政编码数据插入到 zip_codes 表的功能（ JZipCode#create 方法）。

```
require 'sqlite3'
require "csv"
class JZipCode
  CSV_COLUMN = {code: 2, pref: 6, city: 7, addr: 8}

  def initialize(dbfile)
    @dbfile = dbfile # ①
  end

  def create(zipfile)
    return if File.exist?(@dbfile) # ②
    SQLite3::Database.open(@dbfile) do |db| # ③
      db.execute(<<-SQL)
        CREATE TABLE IF NOT EXISTS zip_codes
          (code TEXT, pref TEXT, city TEXT, addr TEXT, alladdr TEXT)
      SQL
      db.execute("BEGIN TRANSACTION") # ④
      CSV.open(zipfile, "r:Shift_JIS:UTF-8") do |csv|
        csv.each do |rec|
          data = Hash.new # ⑤
          CSV_COLUMN.each{|key, index| data[key] = rec[index] }
          data[:alladdr] = data[:pref] + data[:city] + data[:addr]
          db.execute(<<-SQL, data) # ⑥
            INSERT INTO zip_codes VALUES
              (:code, :pref, :city, :addr, :alladdr)
          SQL
        end
      end
      db.execute("COMMIT TRANSACTION") # ⑦
    end
    return true
  end
end
```

JZipCode 类开头的CSV_COLUMN 是一个常量，表示数据在CSV 文件中对应的列。

JZipCode#initialize 方法会接收数据库文件名作为参数。在后续处理中还会有多处需要访问数据库，因此这里我们只把文件名保存为实例变量（①）。JZipCode#create 方法用于创建表，并将KEN_ALL.CSV 的数据插入到数据库中。首先，如果文件已经存在，则什么也不做，直接return 结束程序（②）。如果文件不存在，则使用SQLite3::Database.open 方法打开新建的数据库文件，执行SQL 的CREATE TABLE 语句（③）。其次，像代码清单23.1 那样，指定编码打开CSV 文件读取数据。从⑤开始的3 行代码的作用是，按照CSV_COLUMN 定义的位置，从以数组形式得到的KEN_ALL.CSV 各记录中获取对应的数据，并创建以:code、:pref、:addr、alladdr 为键的散列。最后，把获取到的数据用INSERT 语句插入数据库。

另外，INSERT 语句执行前后的BEGIN TRANSACTION 语句（④ ） 以及COMMITTRANSACTION 语句用于把一连串相关的写入操作合并为一次写入。这样，每次执行INSERT 语句追加数据时就不再需要立即更新数据库文件，提高了程序处理的效率。

## 23.5 检索数据
接下来，我们来检索已经保存好的邮政编码。以下代码在上面代码的JZipCode类中追加了 find_by_code 方法以及 find_by_address 方法。
```ruby
class JZipCode
  …
  def find_by_code(code)
    ret = []
    SQLite3::Database.open(@dbfile) do |db|
      db.execute(<<-SQL, code){|row| ret <<row.join(" ") }
        SELECT code, alladdr
          FROM zip_codes
        WHERE code = ?
      SQL
    end
    return ret.map{|line| line + "\n"}.join
  end

  def find_by_address(addr)
    ret = []
    SQLite3::Database.open(@dbfile) do |db|
      like = "%#{addr}%"
      db.execute(<<-SQL, like){|row| ret <<row.join(" ") }
        SELECT code, alladdr
          FROM zip_codes
        WHERE alladdrLIKE ?
      SQL
    end
    return ret.map{|line| line + "\n"}.join
  end
end
```

find_by_code 方法以邮政编码为参数，返回该邮政编码对应的住址。 find_by_address 方法恰好相反，以住址为参数，返回包含该住址的邮政编码。

检索时也是使用SQLite3::Database#execute 方法，执行SELECT 语句。SELECT 语句以“where 条件”的形式获取与条件一致的记录。单纯检索一致的记录时，条件为“列名 =值”，只检索字符串的某部分一致的记录时，条件为“列名 LIKE"% 字符串%"”。在上述方法中，作为参数传递的值会替换掉WHERE code = ?、WHERE alladdr LIKE 中的条件部分?的值。因为已经用INSERT 语句把code 和alladdr 取出来了，所以需要用空白字符连接。有时检索的结果是多行数据，这种情况下就需要在每行数据末加上换行符，把所有结果连接成一个字符串对象，作为方法的返回值。

下面，我们使用irb 命令，测试一下 find_by_code 方法以及 find_by_address 方法。
```
> irb --simple-prompt
>> require "./jzipcode"
=> true
>> jzipcode = JZipCode.new("jzipcode.db")
=> #<JZipCode: 0x000000024769e8 @dbfile="jzipcode.db">
>> jzipcode.create("KEN_ALL.CSV")
=> true
>> puts jzipcode.find_by_code('1060031')
1060031 東京都港区西麻布
=> nil
>> puts jzipcode.find_by_address(" 東京都渋谷区神")
1500047 東京都渋谷区神山町
1500001 東京都渋谷区神宮前
1500045 東京都渋谷区神泉町
1500041 東京都渋谷区神南
=> nil
```

首先，通过 require "./jzipcode" 引用 jzipcode.rb 的内容。接着，用 JZipCode.new 方法创建实例，通过create 方法读取数据。之后，对 find_by_code 方法指定邮政编码，则会输出对应该邮政编码的住址。同样地，对 find_by_address 方法指定住址的某部分，则会输出包含该住址的邮政编码。

虽然向数据库插入数据会花不少时间，但检索速度应该变快了。有兴趣的读者可以像上使用 CSV 方式那样，利用Time 对象计算一下程序的处理时间加以确认。

## 23.6 小结
本章我们介绍了如何使用SQLite3 库提高检索大量数据时的速度。处理大量数据时，根据实际需要使用数据库等库，会大大提高程序编写效率。Ruby 提供了大量以GEM 形式封装的“开箱即用”的强大的库。

数据库产品中，除了SQLite3 外，MySQL、PostgreSQL 等也都被广泛使用。虽然不同的数据库产品的SQL 语法等在细节上存在差异，但基本结构是大致相同的。而且这里介绍的SQL 用法也是最基础的，有兴趣的读者可以继续深入学习数据库。
