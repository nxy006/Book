﻿# 第02章 便利的对象
---

[TOC]

## 2.1 数组
**数组**（Array）是一个按照顺序保存多个对象的对象，它是基本的容器之一。

### 2.1.1 数组的创建
创建数组乣各数组的元素用逗号隔开，再用中括号把它们括起来。
```ruby
names = ["小林", "林", "高野", "森冈"]
```

### 2.1.2 数组对象
在数组元素对象还未确定的情况下，可以用 `[]` 表示一个空数组对象。

### 2.1.3 从数组中抽取对象
保存在数组中的对象都有一个表示位置的编号，称为**索引**（index）。利用索引我们可以将对象保存到数组，或从数组中取出对象。

要从数组中抽取元素（对象），可以使用如下方法 `数组名 [ 索引 ]`

> 使用数组时务必注意，数组的索引值从 0 开始。

### 2.1.4 将对象保存到数组中
将数组中的某个元素替换为其他对象时，可以使用如下方法： `数组名 [ 索引 ] = 希望保存的对象`

在保存对象时，如果指定了数组中不存在的索引值，则数组大小将自动改变。**Ruby 数组的大小是按照实际情况自动调整的。**

### 2.1.5 数组的元素
任何对象都可以作为数组的元素。

### 2.1.6 数组的大小
我们可以用 `zise` 表示数组的大小。

### 2.1.7 数组的循环
Ruby 提供了 `each` 方法遍历数组元素，语法如下所示：
```ruby
数组.each do | 变量 |
  希望循环的处理
end
```

each 后面在 `do ~ end` 之间的部分称为 **块**（block）。因此，each 这样的方法也可以成为 **带块的方法**。块的开始部分为 `| 变量 |`，each 方法会把数组元素逐个取出来，赋值给 `| 变量 |` 指定的变量，然后循环执行块中的方法。


## 2.2 散列
**散列**（hash）是一个程序中常用的容器。散列中一般以字符串或者符号作为键，来保存对象的对象。

### 2.2.1 什么是符号
在 Ruby 中，**符号**（symbol）与字符串对象很相似（可以简单理解为轻量级的字符串），符号也是对象，一般作为名称标签使用，表示方法等的对象的名称。
要创建符号，只需要在标识符的开头加上 `:` 就可以了。

符号能实现的功能，大部分字符串也能实现，但在像散列的键这样，只是单纯判断“是否相等”的处理中，使用符号会比字符串更有效率，因此在实际编程中我们也会时常用到符号。另外，符号的字符串可以相互转换。对符号使用 `to_s` 方法，可以得到对应的字符串；反之对字符串使用 `to_sym` 方法，则可以得到对应的符号。

### 2.2.2 散列的创建
创建散列的方法与创建数组差不多，不同是使用 `{ }` 将创建的内容括起来。散列用 “键=>对象” 这种格式定义获取对象时所需的键（key），以及键对应的对象（object）。通常我们会使用符号、字符串、数值作为散列的键。

我们常常把符号当做键使用，因此Ruby 提供了一个简短写法。下面两行程序的含义相同：
```ruby
person_1 = { :name => "后藤", :pinyin => "houteng"}
person_2 = { name: "后藤", pinyin: "houteng"}
```

### 2.2.3 散列的使用
从散列取出对象，将对象保存到散列等操作方法和数组非常相似。我们使用下面的方法取出对象： `散列名 [ 键 ]`，保存使用 `散列名 [ 键 ] = 希望保存的对象`

### 2.2.4 散列的循环
使用 each 方法可以遍历散列中的元素，逐个取出其元素的键和值。散列的 each 语法如下所示：
```ruby
散列.each do | 键变量, 值变量 |
  希望循环的处理
end
```

## 2.3 正则表达式
**正则表达式**（regular expression）可以简单地实现以下功能：
- 将字符串与模式（pattem）相匹配
- 使用模式分隔字符串

### 模式与匹配
判断字符串是否属于某模式的过程称为 **匹配**。如果字符串适用于该模式则称为匹配成功。像这样的字符串模式用编程语言来表示就是正则表达式。
创建正则表达式的语法如下所示：`/ 模式 /`

把希望匹配的内容直接写出来，就可以进行匹配。我们用 `=~` 匹配正则表达式和字符串。`/ 模式 / =~ 希望匹配的字符串`。
> **更多**：除此之外，正则表达式还有更多用法，详见第16章。

### 专栏：`nil` 是什么？
`nil` 是一个特殊的值，表示对象不存在。像在正则表达式中表示无法匹配成功一样，方法不能返回有意义的值的时候就会返回 `nil`。从数组或者散列中获取对象使用不存在的索引或键则得到的返回值是 `nil`。

if 语句和 while 语句判断条件时，如果遇到 false 和 nil，则会认为是假，除此以外的都认为是真。因此除了可以使用返回 `true` 或 `false` 的方法，也可以使用返回 “某个值” 或 “nil” 的方法作为判断条件的表达式。
