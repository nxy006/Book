# 第18章 File类与Dir类
---

[TOC]

## 18.1 File 类
### 18.1.1 变更文件名
- `File.rename(before, after)`
  我们可以用 File.rename 方法变更文件名。
  ```ruby
  File.rename("before.txt", "after.txt")
  ```
  还可以将文件移动到已存在的目录下，目录不存在则程序会产生错误。
  ```ruby
  File.rename("data.txt", "backup/data.txt")
  ```
  注：File.rename 方法无法跨文件系统或者驱动器移动文件。
  如果文件不存在，或者没有适当的文件操作权限等，则文件操作失败，程序抛出异常。

### 18.1.2 复制文件
  只用一个Ruby 预定义的方法是无法复制文件的。我们可以利用 File.open 方法与 write 方法的组合来实现文件的复制。

  ```ruby
  def copy(from, to)
    File.open(from) do |input|
      File.open(to, "w") do |output|
        output.write(input.read)
      end
    end
  end
  ```
  不过，由于文件复制是常用的操作，如果每次使用时都需要自己重新定义一次的话就非常麻烦。因此，我们可以通过引用 fileutils 库，使用其中的 FileUtils.cp （文件复制），FileUtils.mv（ 文件移动）等方法来操作文件。

  ```ruby
  require "fileutils"
  FileUtils.cp("data.txt", "backup/data.txt")
  FileUtils.mv("data.txt", "backup/data.txt")
  ```
  File.rename 不能实现的跨文件系统、驱动器的文件移动，用 FileUtils.mv 方法则可以轻松实现。关于 fileutils 库，我们在后续章节中会详细介绍。

### 18.1.3 删除文件
- `File.delete(file)`, `File.unlink(file)`
  我们可以使用 File.delete 方法或 File.unlink 方法删除文件。

## 18.2 目录的操作
Dir 类中实现了目录相关的操作方法。在详细说明前，我们先来复习一下有关目录的一些基础知识。

目录中可以存放多个文件。除了文件之外，目录中还可以存放其他目录，其他目录中又可以再存放目录……如此无限循环。通过将多个目录进行排列、嵌套，就可以轻松地管理大量的文件。Windows 的资源管理器（图18.1）的左侧就是可视化的目录层次结构（树形结构）。用/ 连接目录名，即可指定目录中的文件。由于可以通过目录名指定文件的位置，因此表示文件位置的目录名就称为路径（path）或路径名。另外，我们把目录树的起点目录称为根目录（root directory），根目录只用/ 表示。

### 专栏：关于Windows 的路径名
在Windows 的命令行中，目录分隔符是\。由于使用\ 后不仅会使字符串难以读懂，而且也不能直接在Unix 中执行同一个程序，因此还是建议大家尽量使用/。但有一点请大家注意，像WIN32OLE 这样使用Windows 特有的功能时，使用/ 后可能会使程序变得无法执行。

在Windows 中，驱动器是目录的上层文件管理单位。 一般用1 个英文字母（盘符）表示与之相对应的驱动器，如C:、D: 表示硬盘。在这种情况下，请读者把各驱动器当成独立的根目录来看待。例如，显然C:/ 与D:/ 表示的是不同的驱动器，但如果只写/ 的话，根据程序执行位置的不同，其表示的驱动器也不同，继而所表示的目录也不同。

- `Dir.pwd`, `Dir.chdir(dir)`
程序可以获取运行时所在的目录信息，即**当前目录（current directory）**。使用 Dir.pwd 方法可以获取当前目录，使用 Dir.chdir 方法可以变更当前目录。我们可以对 Dir.chdir 方法的参数 dir 指定相对于当前目录的**相对路径**，也可以指定相对于根目录的**绝对路径**。
```ruby
p Dir.pwd #=> "/usr/local/lib"
Dir.chdir("ruby/2.3.0") #=> 根据相对路径移动
p Dir.pwd #=> "/usr/local/lib/ruby/2.3.0"
Dir.chdir("/etc") #=> 根据绝对路径移动
p Dir.pwd #=> "/etc"
```

当前目录下的文件可以通过指定文件名直接打开，但如果变更了当前目录，则还需要指定目录名。
```ruby
p Dir.pwd #=> "/usr/local/lib/ruby/2.3.0"
io = File.open("find.rb")
#=> 打开"/usr/local/lib/ruby/2.3.0/find.rb"
io.close
Dir.chdir("../..") # 移动到上两层的目录中
p Dir.pwd #=> "/usr/local/lib"
io = File.open("ruby/2.3.0/find.rb")
#=> 打开"/usr/local/lib/ruby/2.3.0/find.rb"
io.close
```
### 18.2.1 目录内容的读取
像介绍文件的时候一样，我们先来了解一下如何读取已存在的目录。读取目录内容的方法与读取文件的方法基本上是一样的：
1. 打开目录。
2. 读取内容。
3. 关闭。

- `Dir.open(path)`, `Dir.close`
  与 File 类一样， Dir 类也有 open 方法与 close 方法。
  我们先试试读取/usr/bin 目录。
  ```ruby
  dir = Dir.open("/usr/bin")
  while name = dir.read
    p name
  end
  dir.close
  ```
  也可以像下面这样用 Dir#each 方法替换 while 语句部分。
  ```ruby
  dir = Dir.open("/usr/bin")
  dir.each do |name|
    p name
  end
  dir.close
  ```
  和 File.open 一样，对 Dir.open 使用块后也可以省略 close 方法的调用。这时程序会将生成的 Dir 对象传给块变量。
  ```ruby
  Dir.open("/usr/local/lib/ruby/2.3.0") do |dir|
    dir.each do |name|
      p name
    end
  end
  ```
  程序会输出以下内容。
  ```
  "."
  ".."
  "tsort/rb"
  "irb"
  "rexml"
  "racc"
  …
  ```

- `dir.read`：与 File 类一样， Dir 类也有 read 方法。
  执行 Dir#read 后，程序会逐个读取最先打开的目录下的内容。这里读取的内容可分为：
  - 表示当前目录的.
  - 表示上级目录的..
  - 其他目录名
  - 文件名

  请注意/usr/bin 与/usr/bin/. 表示同一个目录。

  如下程序会操作指定目录下的所有路径。命令行参数 ARGV[0] 的路径为目录时，会对该目录下的文件进行递归处理，除此以外（文件）的情况下则调用 process_file 方法。 traverse 方法会输出指定目录下的所有文件名，执行结果只显示在控制台中。
  注释中带 ※ 的代码表示忽略当前目录和上级目录，不这么做的话，就会陷入无限循环中，不断地重复处理同一个目录。

  ```ruby
  def traverse(path)
    if File.directory?(path) # 如果是目录
      dir = Dir.open(path)
      while name = dir.read
        next if name = = "." # ※
        next if name = = ".." # ※
        traverse(path + "/" + name)
      end
      dir.close
    else
      process_file(path) # 处理文件
    end
  end
  def process_file(path)
    puts path # 输出结果
  end
  traverse(ARGV[0])
  ```

- `Dir.glob`
使用 Dir.glob 方法后，就可以像shell 那样使用 * 或者 ? 等通配符（wildcard character）来获取文件名。 Dir.glob 方法会将匹配到的文件名（目录名）以数组的形式返回。

下面我们列举一些常用的匹配例子。
- 获取当前目录中所有的文件名（无法获取Unix中以"."开头的隐藏文件名）:Dir.glob("*")
- 获取当前目录中所有的隐藏文件名：Dir.glob(".*")
- 获取当前目录中扩展名为.html或者.htm的文件名。可通过数组指定多个模式：Dir.glob(["*.html", "*.htm"])
- 模式中若没有空白，则用%w(...)生成字符串数组会使程序更加易懂：Dir.glob(%w(*.html *.htm))
- 获取子目录下扩展名为.html或者.htm的文件名：Dir.glob(["*/*.html", "*/*.htm"])
- 获取文件名为 foo，扩展名只有 1 个字符的文件：Dir.glob(["foo.?"])
- 获取文件名为foo.c、foo.h、foo.o的文件：Dir.glob("foo.[cho]")
- 获取当前目录及其子目录中所有的文件名，递归查找目录：Dir.glob("**/*")
- 获取目录foo及其子目录中所有扩展名为.html的文件名，递归查找目录：Dir.glob("foo/**/*.html")

### 18.2.2 目录的创建与删除
- `Dir.mkdir(path)`：用 Dir.mkdir 方法创建新目录：`Dir.mkdir("temp")`
- `Dir.rmdir(path)`：用 Dir.rmdir 方法删除目录。要删除的目录必须为空目录：`Dir.rmdir("temp")`

## 18.3 文件与目录的属性
文件与目录都有所有者、最后更新时间等属性。接下来我们就来看看如何引用和更改这些属性。

- `File.stat(path)`
  使用 File.stat 方法可以获取文件、目录的属性。 File.stat 方法返回的是 File::Stat 类的实例。 File::Stat 类的实例方法如表所示。

    | 方法 | 返回值的含义 |
    | -- | -- |
    | dev | 文件系统的编号 |
    | ino i-node | 编号 |
    | mode | 文件的属性 |
    | nlink | 链接数 |
    | uid | 文件所有者的用户ID |
    | gid | 文件所属组的组ID |
    | rdev | 文件系统的驱动器种类 |
    | size | 文件大小 |
    | blksize | 文件系统的块大小 |
    | blocks | 文件占用的块数量 |
    | atime | 文件的最后访问时间 |
    | mtime | 文件的最后修改时间 |
    | ctime | 文件状态的最后更改时间 |
  其中，除了 atime 方法、mtime 方法、ctime 方法返回 T ime 对象外，其他方法都返回整数值。
  通过 uid 方法与 gid 方法获取对应的用户ID 与组ID 时，需要用到 Etc 模块。我们可以通过 require 'etc' 来引用 Etc 模块。
  Unix 的密码文件（/etc/passwd）包含用户ID、用户名、组ID、主目录等信息，Etc.getpwuid方法可以返回对应用户ID 的密码文件内容。同样地，组文件（/etc/group）包含组ID、组名等信息，Etc.getgrgid 方法可以返回对应组ID 的组文件内容。

  > **备注**：mswin32 版的Ruby 不能使用 Etc 模块。
  
  下面是显示文件/usr/local/bin/ruby 的用户名与组名的程序。
  ```ruby
  require 'etc'
  st = File.stat("/usr/local/bin/ruby")
  pw = Etc.getpwuid(st.uid)
  p pw.name #=> "root"
  gr = Etc.getgrgid(st.gid)
  p gr.name #=> "wheel"
  ```

- `File.ctime(path)`, `File.mtime(path)`, `File.atime(path)`：这三个方法的执行结果与实例方法 File::Stat#ctime 、 File::Stat#mtime 、 File::Stat#atime 是一样的。需要同时使用其中两个以上的方法时，选择实例方法会更加有效率。
- `File.utime(atime, mtime, path)`：改变文件属性中的最后访问时间atime、最后修改时间mtime。时间可以用整数值或者 Time 对象指定。另外，还可以同时指定多个路径。下面是修改文件foo 的最后访问时间以及最后修改时间的程序。通过 Time.now 方法创建表示当前时间的 Time 对象，然后将时间设为当前时间减去100 秒。
- `File.chmod(mode, path)`：修改文件path 的访问权限（permission）。mode 的值为整数值，表示新的访问权限值。可以同时指定多个路径。
  > **备注**：在Windows 中只有文件的所有者才有写入的权限。执行权限则是由扩展名（.bat、.exe 等）决定的。访问权限是表示是否可以进行执行、读取、写入操作的 3 位数。访问权限又细分为所有者、所属组、其他3 种权限，因此完整的访问权限用 9 位数表示。

  以下程序是将文件test.txt 的访问权限设为0755（所有者拥有所有权限，其他用户为只读权限）的例子。
  File.chmod(0755, "test.txt") 在追加特定位时，例如在现在的访问权限中追加执行权限时，需要对从 File.stat 方法得到的访问权限位进行追加位的位运算，然后再用计算后的新值重新设定。使用按位或运算追加权限位。
  ```ruby
  rb_file = "test.rb"
  st = File.stat(rb_file)
  File.chmod(st.mode | 0111, rb_file) # 追加执行权限
  File.chown(owner, group, path)
  ```

  改变文件path 的所有者。owner 表示新的所有者的用户ID，group 表示新的所属组ID。  同时也可以指定多个路径。执行这个命令需要有系统管理员的权限。
  > **备注**：Windows 中虽然也提供了这个方法，但调用时什么都不会发生。

**FileTest 模块**
FileTest 模块中的方法用于检查文件的属性（表18.2）。该模块可以在include 后使用，也可以直接作为模块函数使用。其中的方法也可以作为 File 类的类方法使用。
| 方法 | 返回值 |
| -- | -- |
| exist?( path) | path 若存在则返回 true |
| file?(path) | path 若是文件则返回 true |
| directory?( path) | path 若是目录则返回 true |
| owned?( path) | path 的所有者与执行用户一样则返回 true |
| grpowned?( path) | path 的所属组与执行用户的所属组一样则返回 true |
| readable?(path) | path 可读取则返回 true |
| writable? ( path) | path 可写则返回 true |
| executable? ( path) | path 可执行则返回 true |
| size (path) | 返回path 的大小 |
| size?(path) | path 的大小比0 大则返回 true ，大小为0 或者文件不存在则返回 nil |
| zero?( path) | path 的大小为0 则返回 true |

## 18.4 文件名的操作
操作文件时，我们常常需要操作文件名。Ruby 为我们提供了从路径名中获取目录名、文件名的方法，以及由目录名和文件名生成路径名的方法。

- `File.basename( path[, suffix])`：返回路径名path 中最后一个 "/" 以后的部分。如果指定了扩展名suffix，则会去除返回值中扩展名的部分。在从路径名中获取文件名时会使用本方法。
- `File.dirname(path)`：返回路径名path 中最后一个 "/" 之前的内容。路径不包含 "/" 时则返回 "." 。在从路径
名中获取目录名时会使用本方法。
-  `File.extname( path)`：返回路径名path 中 basename 方法返回结果中的扩展名。没有扩展名或者文件名以"."
开头时则返回空字符串。
- `File.split(path)`：将路径名path 分割为目录名与文件名两部分，并以数组形式返回。在知道返回值的数量
时，使用多重赋值会方便得多。
- `File.join(name1[, name2, …])`：用 File::SEPARATOR 连接参数指定的字符串。 File::SEPARATOR 的默认值设为 "/" 。
- `File.expand_path(path[, default_dir])`：根据目录名default_dir，将相对路径名path 转换为绝对路径名。不指定default_dir 时，则根据当前目录进行转换。
  在Unix 中，可以用“~ 用户名”的形式获取用户的主目录（home directory）。 ~/ 表示当前用户的主目录。

## 18.5 与文件操作相关的库
### 18.5.1 find 库
find 库中的 find 模块用于对指定目录下的目录或文件进行递归处理。
- `Find.find(dir){| path| …}`, `Find.prune`：Find.find 方法会将目录dir 下的所有文件路径逐个传递给path。
  使用 Find.find 方法时，调用 Find.prune 方法跳过当前查找目录下的所有路径（只使用 next 时，则只会跳过当前目录，子目录的内容还是会继续查找）。

下面是一个显示命令行参数指定目录下的内容的脚本。 listdir 方法会显示参数 top 指定的路径下的所有目录名。将不需要查找的目录设定到 IGNORES 后，就可以通过 Find.prune 方法省略那些目录下的内容的处理。
```ruby
require 'find'
IGNORES = [ /^\./, /^\.svn$/, /^\.git$/ ]
def listdir(top)
  Find.find(top) do |path|
    if FileTest.directory?(path) # 如果path 是目录
      dir, base = File.split(path)
      IGNORES.each do |re|
        if re =~ base # 需要忽略的目录
          Find.prune # 省略该目录下的内容的查找
        end
      end
      puts path # 输出结果
    end
  end
end
listdir(ARGV[0])
```

### 18.5.2 tempfile 库
tempfile 库用于管理临时文件。

在处理大量数据的程序中，有时会将一部分正在处理的数据写入到临时文件。这些文件一般在程序执行完毕后就不再需要，因此必须删除，但为了能够确保删除文件，就必须记住每个临时文件的名称。此外，有时程序还会同时处理多个文件，或者同时执行多个程序，考虑到这些情况，临时文件还不能使用相同的名称，这是非常麻烦的问题。

使用 tempfile 库中的 Tempfile 类，就可以解决上述问题。

- `Tempfile.new(basename[, tempdir])`：创建临时文件。实际生成的文件名的格式为“basename+ 进程ID+ 流水号”。因此，即使使用同样的basename，每次调用 new 方法生成的临时文件也都是不一样的。如果不指定目录名tempdir，则会按照顺序查找E NV["TMPDIR"]、 E NV["TMP"]、 E NV["TEMP"]、 " /tmp"，并把最先找到的目录作为临时目录使用。
- `tempfile.close(real)`：关闭临时文件。real 为 true 时则马上删除临时文件。即使没有明确指定删除，Tempfile
对象也会在GC（详情请参考本章最后的专栏）时被一并删除。real 的默认值为 false 。
- `tempfile.open`：再次打开 close 方法关闭的临时文件。
- `tempfile.path`：返回临时文件的路径名。

### 18.5.3 fileutils 库
在之前的内容中，我们已经介绍了 fileutils 库的 FileUtils.cp 、 FileUtils.mv 方法。通过 require 引用 fileutils 库后，程序就可以使用 FileUtils 模块中提供的各种方便的方法来操作文件。
- `FileUtils.cp(from, to)`：把文件从from 复制到to。to 为目录时，则在to 下面生成与from 同名的文件。此外，
也可以将from 作为数组来一次性复制多个文件，这时to 必须指定为目录。
- `FileUtils.cp_r(from, to)`：功能与 FileUtils.cp 几乎一模一样，不同点在于from 为目录时，会进行递归复制。
- `FileUtils.mv(from, to)`：把文件（或者目录）从from 移动到to。to 为目录时，则将文件作为与from 同名的文件
移动到to 目录下。也可以将from 作为数组来一次性移动多个文件，这时to 必须指定为目录。
- `FileUtils.rm(path)`, `FileUtils.rm_f(path)`：删除path。path 只能为文件。也可以将path 作为数组来一次性删除多个文件。FileUtils.rm 方法在执行删除处理的过程中，若发生异常则中断处理，而 FileUtils.rm_f 方法则会忽略错误，继续执行。
- `FileUtils.rm_r(path)`, `FileUtils.rm_rf(path)`：删除path。path 为目录时，则进行递归删除。此外，也可以将path 作为数组来一次性删除多个文件（或者目录）。 FileUtils.rm_r 方法在执行处理的过程中，若发生异常则中断处理，而 FileUtils.rm_rf 方法则会忽略错误，继续执行。
- `FileUtils.compare(from, to)`：比较from 与 to 的内容，相同则返回 true ，否则返回 false 。
- `FileUtils.install(from, to[, option])`：把文件从from 复制到to。如果to 已经存在，且与from 内容一致，则不会复制。option参数用于指定目标文件的访问权限，如下所示。
  ```ruby
  FileUtils.install(from, to, :mode => 0755)
  ```
- `FileUtils.mkdir_p(path)`：使用 Dir.mkdir 方法创建 "foo/bar/baz" 这样的目录时，需要像下面这样按顺序逐个创建上层目录。
  ```ruby
  Dir.mkdir("foo")
  Dir.mkdir("foo/bar")
  Dir.mkdir("foo/bar/baz")
  ```

  而如果使用 FileUtils.mkdir_p 方法，则只需调用一次就可以自动创建各层的目录。此外，也可以将path 作为数组来一次性创建多个目录。
  ```ruby
  FileUtils.mkdir_p("foo/bar/baz")
  ```