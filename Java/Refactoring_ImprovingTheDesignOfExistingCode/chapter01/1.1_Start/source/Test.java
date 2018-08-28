package com.nxy006.book.refactoring.chapter01.source_1;

public class Test {	
	
	public static void main(String[] args){
		Customer customer = new Customer("Test Customer");
		
		// ���ޡ��������׸������·�����3��
		Movie movie;
		movie= new Movie("Hello Mr. Billionaire", Movie.NEW_RELEASE);
		customer.addRental(new Rental(movie, 3));
		
		// ���ޡ�ǧ��ǧѰ������ͯƬ��1��
		movie = new Movie("Spirited Away", Movie.CHILDRENS);
		customer.addRental(new Rental(movie, 1));
		
		System.out.println(customer.statement());
	}

}

// �������ۣ�
//    Customer�� Movie�� Rental ������������˹˿����޵�Ӱ�Ĺ�ϵ������ʵ�ֱ������ݲ�ʵ�����ĳһ��
// �͵������б����������޷��ü��������ֵĹ��ܡ�
//    ���ϵͳ������������ڣ�    Customer ����� statement ��������̫�๤������Щ������������������
// ��ɵġ�����Ҫʵ�ֲ�ͬ�ġ������ǲ��е������ʽʱ������ HTML��ʽ�������������ȫ���ܸ��ã�ֻ�����±�д
// �µķ���������ά�����������ʱ�򣬽�һ���أ��ڼƷѹ����ӰƬ������Ҫ�޸�ʱ���ò�����˫���޸ġ�����ϵͳ
// ���ݽ�������Խ��Խ���ӣ��Ӷ������޸�Խ��Խ���ѣ�ҲԽ��Խ���׷���
//    ��Ȼ���ӹ����Ͻ������д����ǿ���ʤ�����ǵ�����ģ���ϵͳ�ݱ�����У����ǽ����ò������޸ģ��򵥵ĸ�
// �������������Ǳ����в��