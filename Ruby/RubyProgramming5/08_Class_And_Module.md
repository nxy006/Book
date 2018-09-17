﻿# 第08章 类和模块
---

[TOC]

## 8.1 类是什么
**类**（class）是面向对象中的一个重要术语，我们已经在第4章做过简要说明，现在来进一步讨论一下如何在面向对象语言中使用类。

### 8.1.1 类和实例
类表示对象的种类，Ruby 中的一个对象一定都属于某个类。

相同类的对象所使用的方法也相同，类就像是对象雏形或设计图，决定了对象的行为。在生成新的对象时，一般会用到各个类的 new 方法。
> **备注**：像数组、字符串这样的类也可以使用字面量（像 `[1, 2, 3]`、`"abc"` 这样的写法）来生成对象。

当想知道都个对象属于哪个类时，我们可以使用 `class` 方法，当判断某个对象是否属于某个类时，可以使用 `instance_of?` 方法。
```ruby
ary = []
str = "Hello world."
p ary.class                 #=> Array
p str.class                 #=> String
p ary.instance_of?(Array)   #=> true
p str.instance_of?(String)  #=> true
p ary.instance_of?(String)  #=> false
p str.instance_of?(Array)   #=> false
```
### 8.1.2 继承
通过拓展已定义的类来创建新类称为**继承**。

继承后创建的新类称为**子类**（subclass），被继承的类称为**父类**（superclass）。通过继承，可以实现以下操作：
- 在不影响原有功能的前提下追加新功能
- 重定义原有功能，使名称相同的方法产生不同的效果
- 在已有功能的基础上追加处理，拓展已有功能

此外，我们还可以利用继承来创建多个具有相似功能的类。在 Ruby 中，`BasicObject` 类是所有类的父类，它定义了作为 Ruby 对象的最基本功能。
> `BasicObject` 是最基础的类，甚至连一般对象需要的功能都没有定义，因此普通对象一般都被定义为 `Object` 类，字符串、数组等都是 `Object` 类的子类。关于`BasicObject` 类和 `Object` 类，我们将在 8.3.2 节中详细说明。

本书中涉及的类的继承关系图如下（Exception 类下还有众多子类，这里省略）：
```
BasicObject
- Object
  - Array
  - String
  - Hash
  - Regexp
  - IO
    - File
  - Dir
  - Numeric
    - Integer
      - Fixnum
      - Bignum
    - Float
    - Complex
    - Rational
  - Exception
  - Time
```

子类与父类之间的关系称为 “is-a 关系”。根据类的继承关系判断对象是否属于某个类可以使用 `is_a?` 方法。

 `is_a?` 方法与之前提到过的 `instance_of?` 方法都已经在 Object 类中定义过了，因此普通的对象都可以使用这两个方法。

## 8.2 创建类

### 8.2.1 class 语句
class 语句在定义类时使用，其一般用法如下（类名的首字母必须大写）：
```ruby
class 类名
  类的定义
end
```

### 8.2.2 initialize 方法
在 class 语句中定义的方法称为该类的实例方法。

其中，名为 `initalize` 的方法比较特殊。使用 new 方法生成新的对象时， `initalize` 方法将被调用，同时 `new` 方法的参数会被原封不动地传给 `initialize` 方法。因此初始化对象时需要的处理一般都写在这里。
```ruby
def initialize(myname = "Ruby")
  @name = myname
end
```

### 8.2.3 实例变量与实例方法
我们把以 `@` 开头的变量称为**实例变量**。在不同的方法中，程序会把局部变量看做是不同的变量，而只要在同一个实例中，程序就可以超越方法定义，任意引用、修改实例变量的值。（但引用未初始化的实例变量返回 `nil`。）

不同实例的实例变量可以不同。只要实例存在，实例变量的值就不会消失，并且可以任意使用。

### 8.2.4 存取器
在 Ruby 中，对象外部不能直接访问实例变量或对实例变量赋值，需要通过方法来访问对象。
```ruby
class HelloWorld
  def name
    @name
  end

  def name=(value)
    @name = value
  end
  ...
end
```

利用这样的方法，我们就可以从外部自由地访问对象内部的实例变量。为了方便使用，Ruby 为我们提供了存取器：
| 定义 | 意义 |
| -- | -- |
| `attr_reader :name` | 只读（定义 `name` 方法） |
| `attr_writer :name` | 只写（定义 `name=` 方法） |
| `attr_accessor :name` | 读写（定义以上两个方法） |

> **术语**：Ruby 中一般把设定实例变量的方法称为 writer，读取实例变量的方法称为 reader，这两个方法合称为 accessor。有时也把 reader 称为 getter，把 writer 称为 setter，合称为 accessor method。

### 8.2.5 特殊变量 self
在实例方法中，可以用 `self` 这个特殊的变量来引用方法的接收者，调用方法时如果省略了接收者，Ruby 就会默认把 self 作为该方法的接收者。
> 虽然 self 本身与局部变量形式相同，但由于是引用对象本身的保留字，因此即便对它赋值也不会对其产生影响。像这样，已经被系统使用且不能被我们自定义的变量名还有 `nil`、 `true`、`false` 、 `__FILE__`、`__LINE__`、`__ENCODING__` 等。

### 8.2.6 类方法
方法的接收者就是类本身（类对象）的方法称为**类方法**。我们可以在 `class << 类名 ~ end` 结构中，以定义实例方法的形式定义类方法：
```ruby
class << HelloWorld
  def hello(name)
    puts "#{name} said hello."
  end
end

HelloWorld.holle("John")     #=> John said hello.
```

除了上述方法，还可以在类定义中追加类方法的定义，在 class 语句中使用 `self` 时，引用的对象是类本身，因此我们可以以 `class << self ~ end` 的形式定义类方法：
```ruby
class HelloWorld
  class << self
    def hello(name)
      puts "#{name} said hello."
    end
  end
end
```

除此之外，我们还可以在类定义中使用 `def 类名.方法名 ~ end` 的形式定义类方法。（类名可以用 `self` 替换）
> **术语**：`class << 类名 ~ end` 的写法的类定义称为**单例类定义**，单例类定义中定义的方法称为**单例方法**。

### 8.2.7 常量
在class 语句中可以定义常量，对于在类中定义的常量，可以以 `类名::常量名` 的形式实现外部访问。

### 8.2.8 类变量
以 `@@` 开头的变量称为类变量。类变量是该类所有实例的共享变量，这一点类似常量，但类变量可以任意修改。同实例变量一样，类变量从外部直接访问也需要存取器，而且由于 `attr_accessor` 等存取器不能使用，因此需要直接定义。

### 8.2.9 限制方法的调用
Ruby 提供了三种方法的访问级别：
- `public` 以实例方法向外部公开
- `private` 在指定接收者的情况下不能调用该方法（即只能使用缺省接收者的方法调用，因此不能外部调用）
- `protected` 在同一个类（及其子类）中时可以将该方法作为实例方法调用

使用以上访问级别时，只需要为这3个关键字指定表示该方法名的符号。
```ruby
class AccTest
  def pub
    puts "pub is a public method."
  end
  public :pub    # 设定为 public 方法（可省略）

  def priv
    puts "priv is a private method."
  end
  private :priv   # 设定为 private 方法
end
```

调用不符合访问级别的方法会产生 `NoMethodError` 异常。

如果希望统一定义多个方法的访问级别，可以不指定三种关键字的符号，这样，在它们下方的方法均会设定为指定的访问级别。
> **例外**：没有指定方位级别的方法默认为 public，但 initialize 方法例外，它通常默认定义为 private 访问级别

## 8.3 拓展类

### 8.3.1 在原有类的基础上添加方法
Ruby 允许我们在已经定义好的类上添加方法：
```ruby
class String
  def count_word
    ary = self.split(/\s+/)  # 用空格分隔
    return ary.size
  end
end

p "Just Another Ruby Newbie".count_word  #=> 4
```

### 8.3.2 继承
利用继承可以在不改变已有类的前提下，通过增加新功能或重定义已有功能来创建新的类：
```ruby
class 类名 < 父类名
  类定义
end
```

利用继承，我们可以把共同的功能定义在父类，把各自独有的功能定义在子类。如果没有指定父类，Ruby 会默认父类为 Object。

类对象调用 `instance_methods` 方法后，就会以符号形式返回该类的实例方法列表，下面我们比较一下 Object 类和 BasicObject 类：
```ruby
> Object.instance_methods
=> [:instance_of?, :public_send, :instance_variable_get, :instance_variable_set, :instance_variable_defined?, :remove_instance_variable, :private_methods, :kind_of?, :instance_variables, :tap, ...]
> BasicObject.instance_methods
=> [:!, :==, :!=, :__send__, :equal?, :instance_eval, :instance_exec, :__id__]
```

据此也可以看出，相对于 Object 类持有多种方法， BacsicObject 类所拥有的功能都是最基本的。

## 8.4 alias 与 undef

### 8.4.1 alias
有时我们会希望给已经存在的方法设置别名，这种情况就需要使用 alias 方法：
```ruby
alias 别名 原名    # 直接使用方法名
alias :别名 :原名  # 使用符号名
```

在重定义已经存在的方法时，为了能用别名调用原来的方法，也需要用到 alias。

### 8.4.2 undef
undef 用于删除已有的方法，与 alias 一样，参数可以指定方法名或符号名：（例如，在子类中希望删除父类定义的方法时，可以使用 undef）

```ruby
undef 方法名   # 直接使用方法名
undef :方法名  # 使用符号名
```



## 8.5 单例类
在 8.2.6 节，我们提到了单例类的定义，而通过单例类定义就可以给对象添加方法（单例方法）。
```ruby
str1 = "Ruby"
str2 = "Ruby"

class << str1  # 注意：这里是实例
  def hello
    "Hello, #{self}!"
  end
end

p str1.hello   #=> "Hello, Ruby!"
p str2.hello   #=> 错误：NoMethodError
```

## 8.6 模块是什么
**模块**是 Ruby 的特色功能之一，如果说类是表达事物的实体及其行为，那么模块表现的就只是类的行为。模块与类有两点不同：
- 模块不能拥有实例
- 模块不能被继承

## 8.7 模块的使用方法

### 8.7.1 利用 Mix-in 拓展功能
Mix-in 就是将模块混合到类中，在定义类使用 `include` ，模块中的方法、常量就能被类使用。Mix-in 可以灵活地解决下面的问题：
- 虽然两个类拥有相似的功能，但是不希望把它们作为相同的种类（class）来考虑
- Ruby 不支持类的多重继承，因此无法对已经继承的类添加共同的功能

```ruby
mdoule MyModule
  # 共通的方法等
end

class MyClass1
  include MyModule
  # MyClass1 中的独有方法
end

class MyClass2
  include MyModule
  # MyClass2 中的独有方法
end
```

### 8.7.2 提供命名空间
所谓**命名空间**（namespace）就是对方法、常量、类等名称进行区分及管理的单位。模块能够提供独立的命名空间，因此不同模块的同名方法和常量会认为是不同的方法、常量。通过在模块内定义名称，可以解决命名冲突的问题。

我们可以使用 `模块名.方法名` 的形式调用模块定定义的方法，这样的方法称为 **模块函数**；还可以使用 `模块名::常量名` 形式获取模块中的常量。如果没有定义与模块内的方法、常量重名的名称，引用时可以省略模块名。通过 include 可以将模块内的方法、常量名合并到当前命名空间。

## 8.8 创建模块
我们使用 `module` 语句创建模块，其定义如下：
```ruby
module 模块名
  模块定义
end
```

### 8.8.1 常量
和类一样，在模块中定义的处理可以通过模块名访问。

### 8.8.2 方法的定义
和类一样，我们也可以在 module 中定义方法。然而如果只定义类名，虽然在模块内部与包含此模块的语句中可以直接调用，但却不能用 `模块名.方法名` 的形式被调用。如果希望公开方法给外部使用，需要用到 `module_function` 方法，其参数是表示方法名的符号。

以 `模块名.方法名` 的形式调用时，如果在方法中调用 `self`，就会获得该模块的对象。而如果 Mix-in 了模块，就相当于为该类添加了实例方法，这种情况下，self 代表的是被 Mix-in 的类的对象。

> **警示**：即使是相同的方法，在不同的上下文调用时，其含义也会不一样，因此对于Mix-in 的模块，我们要注意根据实际情况判断是否使用模块函数功能。一般不建议在定义为模块函数的方法中使用 self 。

## 8.9 Mix-in
我们详细讨论一下 Mix-in，假设类 C 包含了模块 M，模块 M 中的方法就可以作为类 C 的实例方法被调用。类 C 的实例程序在被程序调用时，会按照 **类C -> 模块 M -> 类C的父类 Object** 的顺序查找方法并执行第一个找到的方法。

我们可以用 `include?` 判断某个类是否包含某个模块；还可以用 `ancestors` 方法和 `superclass` 调查类的继承关系，其中 `ancestors` 返回按从子类到父类顺序的类的数组，`superclass` 返回直接父类。
> 备注：`ancestors` 方法返回的类中可能包含 Kernel，这是 Ruby 的一个核心模块，Ruby 程序运行时所需的共通函数都封装在这里，如 p 方法、raise 方法等都是 Kernel 模块提供的模块函数。

虽然 Ruby 采用的是**单一继承**模型，但通过 Mix-in，就既可以保持单一继承关系，又同时让多个类共享功能。一个典型例子是，Ruby 标准类库中 `Enumerable` 模块。使用 each 方法的类中包含  `Enumerable` 模块后就可以使用 `each_with_index` 方法，`collect` 方法等对元素进行排序处理的方法。Array、Hash、IO 等都包含了 `Enumerable` 模块。

单一继承的优点是关系简单，不会因为过多基础导致类间关系变得过于复杂，但另一方面，我们又希望更积极地重用已有的类，或者把多个类的特性合并为更加高级的类，在这时灵活使用单一继承和 Mix-in，既能使类结构简单易懂，又能灵活面对各种需求。

### 8.9.1 查找方法的规则

使用 Max-in 时，方法有如下查找顺序：
1. 同继承关系一样，原类中已经定义了同名的方法时，优先使用该方法；
2. 在同一个类中包含多个模块时，优先使用最后一个包含的模块；
3. 嵌套 include 时（父类也包含模块、或包含的模块也包含了模块），操作顺序也是线性的，即先子类（模块）后父类（模块）；
4. 相同的模块被包含两次以上时，第二次以后的会被省略。

### 8.9.2 extend 方法
8.5 节中，我们已经定义了如何逐个定义单例方法，而利用 Object#extend 方法，还可以实现批量定义单例方法。extend 方法可以使单例类包含模块，并把模块中的功能拓展到对象中。
```ruby
module Edition
  def edition(n)
    "#{self} 第 #{n} 版"
  end
end
str = "Ruby 基础教程"
str.extend(Edition)  #=> 将模块Mix-in 进对象
p str.edition(5)     #=> "Ruby 基础教程第 5 版"
```
include 可以帮助我们突破继承的限制，通过模块拓展类的功能；entend 则可以帮助我们跨过类直接通过模块拓展对象的功能。

### 8.9.3 类与 Mix-in
在 Ruby 中，所有类本身都是 class 类的对象，我们之前也介绍过接收者为类本身的方法就是类方法，也就是说，类方法的类对象的实例方法：
- Class 类的实例方法
- 类对象的单例方法

除了之前介绍的定义类方法的语法外，使用 extend 方法也同样能为类对象追加类方法。

> 备注：在 Ruby 中，所有方法的执行，都需要有作为接收者的某个对象的调用。换句话说，Ruby 的方法（包括单例方法）都一定属于某个类，并且作为接收者的对象的实例方法被程序调用。因此，我们可以说，所有方法都是实例方法，人们只是为了便于识别接收者的类型，才使用了 “实例方法”、“类方法”这样的说法。

## 8.10 面向对象程序设计

### 8.10.1 对象是什么
包括 Ruby 在内，世界有很多面向对象的程序设计语言，不同语言的语法和功能千差万别，但却几乎都将程序处理的主体作为 “对象” 考虑。

一般情况下，程序处理的主体是数据，而面向对象语言中的 “对象” 就是指数据（或称数据的集合）及操作该方法的组合。通过把数据以及处理数据的操作方法，作为对象合并在一起，能够使程序看起来更加清晰。

在开发大型程序时，如果不能有效地将数据集合到一起并整理，程序的统一性就将荡然无存。面向对象语言会把这种归类统一的数据作为各种各样的对象来看待。另外，远程数据也可以作为程序的处理对象来考虑，不同的应用程序都有自己的规范，Ruby 提供了现成的 `Net::HTTP`，`Net::POP` 等类，能够轻松地编写程序。

### 8.10.2 面向对象的特征
面向对象有以下特征：
- 封装
  所谓**封装**（encapsulation），就是指使对象管理的数据不能直接从外部进行操作，数据的更新、查询等操作必须通过调用方法的形式完成。通过封装，可以防止因把非法数据设置给对象而使程序产生异常的情况发生。
  Ruby 对象在默认情况下是强制封装的，因此无法从外部直接访问 Ruby 对象的实例变量。虽然有 `attr_accessor` 这样简单定义访问级别的方法，但也不要过度使用，只在需要时候才使用。
  封装的另一个好处是，可以隐藏实现细节，把内部逻辑抽象地表现为出来。
- 多态
  对象提供了将数据及其处理组合起来的“功能”，对象知道数据的处理方法。同名的方法属于多个对象的现象（不同对象的处理结果也不一样），就被称为**多态**（polymorphism）。

### 8.10.3 鸭子模型
**鸭子模型**（duck typing）是一种结合对象特征，灵活运用多态的思考方法。其说法来自于“能像鸭子一样走路，能像鸭子一样叫的，那一定就是鸭子” 这句话，意思是，对象的特征并不是由其种类（类及其继承关系）决定的，而是由对象本身具有什么样的行为（拥有什么样的方法）决定的。

例如，假设我们希望从字符串数组中取出元素，并将字母转成小写后返回结果：
```ruby
def fetch_and_downcase(ary, index)
  if str = ary[index]
    return str.downcase
  end
end

ary = ["Boo", "Foo", "Woo"]
p fetch_and_downcase(ary, 1) #=> "foo"
```

实际上，除了数组，我们也可以像下面这样将散列传递给该方法处理：
```ruby
hash = {0 => "Boo", 1 => "Foo", 2 => "Woo"}
p fetch_and_downcase(hash , 1) #=> "foo"
```
`fetch_and_downcase` 方法对传进来的参数只有两个要求：
- 能以 ary[index] 形式获取元素
- 获取的元素可以执行 downcase 方法

只要参数符合这两个要求，`fetch_and_downcase` 方法并不关心传进来的到底是数组还是散列。Ruby 中没有变量类型限制，因此不会出现不是某个特定类的对象就无法给变量赋值的情况。因此，在程序执行之前我们都无法知道，变量指定的对象的方法调用是否正确。

这样的做法增加了程序运行前检查的难度，但从另一角度考虑，则可以简单地使没有明确继承关系的对象之间的处理变得通用。只要能执行相同的操作，我们并不介意执行者是否一样；相反，虽然是不同的执行者，但通过定义相同名称的方法，也可以实现处理通用化。这就是鸭子类型思考问题的方法。

利用鸭子模型实现处理通用化，并不要求对象之间有明确的基础关系，因此，灵活运用可能有一定难度，我们可以由简单到深入，慢慢就可以抓住窍门了。

### 8.10.4 面向对象的例子
下面我们演示一个用 Net::HTTP 类获取Ruby 官网首页的HTML，并将其输出到控制台的例子。
```ruby
require "net/http"
require "uri"
url = URI.parse("http://www.ruby-lang.org/ja/")
http = Net::HTTP.start(url.host, url.port)
doc = http.get(url.path)
puts doc.body
```

程序中使用的 URI 模块 parse 方法可以用于解析URL字符串，并返回 URI::HTTP 类的对象。同时，根据 URL 的编写规则，URL 会被分解成多个属性：
```ruby
require "uri"
url = URI.parse("http://www.ruby-lang.org/ja/")
p url.scheme #=> "http" （体系：URL 的种类）
p url.host   #=> "www.ruby-lang.org" （主机名）
p url.port   #=> 80 （端口号）
p url.path   #=> "/ja/" （路径）
p url.to_s   #=> "http://www.ruby-lang.org/ja/"
```

**体系**（scheme）是指使用哪种通信协议。连接网络上的服务器时，需要知道服务器的主机名及端口号。路径用于定位服务器上管理的文件。 URI::HTTP 类的作用就是，把URL 字符串解析后分解出来的信息，以对象的形式整合在一起。（需要注意的是，模块名是 URI 而不是 URL 。URL 指的是URI 标识符中某种特定种类的东西。）

调用 Net::HTTP#get 方法时，对象内部会进行以下处理。
1. 使用主机名和端口号，与服务器建立通信（叫作套接字）。
2. 使用路径，创建代表请求信息的 Net::HTTPRequest 对象。
3. 对套接字写入请求信息。
4. 从套接字中读取数据，并将其保存到代表响应信息的 Net::HTTPResponse 对象中。
5. 利用 Net::HTTPResponse 本身提供的功能，解析响应信息，提取文档部分并返回。



在这个例子中，URL 解析由 URI::HTTP 负责，网络连接由套接字负责，与信息交换相关的操作由 Net::HTTPRequest 和 Net::HTTPResponse 负责，通信中必要的套接字、请求、响应等相关操作由 Net::HTTP 负责。不同的对象各司其职，决定应该如何配置参数、该执行什么样的处理等事项。

在程序处理中同样应该遵循这些方针，对象之间通过方法交换信息，至于这些信息在彼此内部是如何处理的则不需要关心。重要的是如何自然的写出程序（即符合人脑思维的程序），这除了需要丰富的程序设计经验外，还需要拥有设计模式等类结构相关的知识。通过只把事物的外部特征作为参考依据，我们就可以使用与实际事物相近的模型去组织、构建程序。

### 专栏：面向对象编程的图书推荐
现在市面上已经有很多面向普通开发者的面向对象编程的书，入门学习很容易。
- 平泽章的《对象是怎样工作的（第 2 版）》比较通俗易懂，通过这本书可以全面了解面向对象程序设计。
- 结城浩的《图解设计模式》是一本通俗易懂的有关设计模式的书。
- Alan Shalloway 和 James R. Trott 合著的《设计模式精解》则在设计模式的基础上介绍了面向对象编程的相关内容。
- 如果想更加深入地学习面向对象的原则、原理，推荐阅读 Bertrand Meye 的 *Object-Oriented Software Construction(2nd Edition)*。

不过这些书都是基于Java 等静态语言来写的，因此有些地方需要花点工夫修改才能应用到 Ruby 上，这点请读者注意。