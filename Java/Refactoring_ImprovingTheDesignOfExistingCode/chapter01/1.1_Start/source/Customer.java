package com.nxy006.book.refactoring.chapter01.source_1;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Customer 用来表示顾客，除了基本的数据和访问函数，还提供了一个用于生成详单的函数
 * @author nxy006
 *
 */
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
            
            // 对不同的电影使用不同的费用计算规则
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
            
            // 每个租赁增加1积分点（add grequent renter points ）
            frequentRenterPoints++;
            // 如果是新发布电影且租赁达到两天，奖励1积分点（add bonus for a two day new release rental ）
            if ((each.getMovie().getPriceCode() == Movie.NEW_RELEASE) && each.getDaysRented() > 1) {
                frequentRenterPoints++;
            }
            
            // 展示租赁信息（show fingures for this rental）
            result += "\t" + each.getMovie().getTitle() + "\t" + String.valueOf(thisAmount) + "\n";
            totalAmount += thisAmount;
        }
        
        // 添加尾部行（add footer lines）
        result += "Amount owed is " + String.valueOf(totalAmount) + "\n";
        result += "You earned " + String.valueOf(frequentRenterPoints) + " frequent renter points";
        return result;
    }
}