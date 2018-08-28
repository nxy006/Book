# 1.1 起点
---

[TOC]

## 示例项目代码
```java
// Movie 只是一个简单的数据类，存取方法省略
public class Movie {
    // 电影类型常量，对应 priceCode 参数，根据此类型会有不同计费和积分规则
    public static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private String _title;
    private int _priceCode;
}
```

```java
// Rental 表示某个顾客租了一部影片，存取方法省略
public class Rental {

    private Movie _movie;
    private int _daysRented;
}
```

```java
// Customer 用来表示顾客，除了基本的数据和访问函数，还提供了一个用于生成详单的函数
public class Customer {
 private String _name;
 private Vector _rentals = new Vector();

 public Customer(String name) {
  _name = name;
 }

 public void addRental(Rental arg) {
  _rentals.addElement(arg);
 }

 public String getName() {
  return _name;
 }

 //生成详单的函数
public class Customer {
    private String _name;
    private Vector<Rental> _rentals = new Vector<Rental>();

    public Customer(String name) {
        _name = name;
    }

    public void addRental(Rental arg) {
        _rentals.addElement(arg);
    }

    public String getName() {
        return _name;
    }

    // 生成详单
    public String statement() {
        double totalAmount = 0;
        int frequentRenterPoints = 0;
        Enumeration<Rental> rentals = _rentals.elements();
        String result = "Rental Record for " + getName() + "\n";

        while (rentals.hasMoreElements()) {
            double thisAmount = 0;
            Rental each = (Rental) rentals.nextElement();

            // 对不同的电影类型使用不同的费用计算规则
            switch (each.getMovie().getPriceCode()) {
                case Movie.REGULAR:
                    thisAmount += 2;
                    if (each.getDaysRented() > 2) {
                        thisAmount += (each.getDaysRented() - 2) * 1.5;
                    }
                    break;
                case Movie.CHILDRENS:
                    thisAmount += each.getDaysRented() * 3;
                    break;
                case Movie.NEW_RELEASE:
                    thisAmount += 1.5;
                    if (each.getDaysRented() > 3) {
                        thisAmount += (each.getDaysRented() - 3) * 1.5;
                    }
                    break;
                default:
                    break;
            }

            // 每个租赁增加1积分点
            frequentRenterPoints++;
            // 如果是新发布电影且租赁达到两天，奖励1积分点
            if ((each.getMovie().getPriceCode() == Movie.NEW_RELEASE) && each.getDaysRented() > 1) {
                frequentRenterPoints++;
            }

            // 展示租赁信息
            result += "\t" + each.getMovie().getTitle() + "\t" + String.valueOf(thisAmount) + "\n";
            totalAmount += thisAmount;
        }

        // 添加尾部行
        result += "Amount owed is " + String.valueOf(totalAmount) + "\n";
        result += "You earned " + String.valueOf(frequentRenterPoints) + " frequent renter points";
        return result;
    }
}
```



## 项目分析
### 本例类图

### 本例 statement 方法交互过程


## 代码分析
Customer、 Movie、 Rental 几个对象组成了顾客租赁电影的关系，可以实现保存数据并实现输出某一顾客的租赁列表，并计算租赁费用及奖励积分的功能。

这个系统的设计问题在于， Customer 对象的 statement 方法做了太多工作，有些工作本该是由其他类完成的。在需要实现不同的、尤其是并行的输出方式时（比如 HTML格式），这个方法完全不能复用，只能重新编写新的方法。而在维护两份输出的时候，进一步地，在计费规则或影片分类需要修改时不得不进行双份修改。随着系统的演进，规则将越来越复杂，从而导致修改越来越困难，也越来越容易犯错。

当然，从功能上讲，现有代码是可以胜任我们的需求的，但系统演变过程中，我们将不得不面临修改，简单的复制黏贴会造成许多潜在威胁。
