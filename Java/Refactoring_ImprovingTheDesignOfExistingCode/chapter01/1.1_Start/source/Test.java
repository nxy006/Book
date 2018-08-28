package com.nxy006.book.refactoring.chapter01.source_1;

public class Test {	
	
	public static void main(String[] args){
		Customer customer = new Customer("Test Customer");
		
		// 租赁《西虹市首富》（新发布）3天
		Movie movie;
		movie= new Movie("Hello Mr. Billionaire", Movie.NEW_RELEASE);
		customer.addRental(new Rental(movie, 3));
		
		// 租赁《千与千寻》（儿童片）1天
		movie = new Movie("Spirited Away", Movie.CHILDRENS);
		customer.addRental(new Rental(movie, 1));
		
		System.out.println(customer.statement());
	}

}

// 代码评论：
//    Customer、 Movie、 Rental 几个对象组成了顾客租赁电影的关系，可以实现保存数据并实现输出某一顾
// 客的租赁列表，并计算租赁费用及奖励积分的功能。
//    这个系统的设计问题在于，    Customer 对象的 statement 方法做了太多工作，有些工作本该是由其他类
// 完成的。在需要实现不同的、尤其是并行的输出方式时（比如 HTML格式），这个方法完全不能复用，只能重新编写
// 新的方法。而在维护两份输出的时候，进一步地，在计费规则或影片分类需要修改时不得不进行双份修改。随着系统
// 的演进，规则将越来越复杂，从而导致修改越来越困难，也越来越容易犯错。
//    当然，从功能上讲，现有代码是可以胜任我们的需求的，但系统演变过程中，我们将不得不面临修改，简单的复
// 制黏贴会造成许多潜在威胁。