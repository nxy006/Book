package com.nxy006.book.refactoring.chapter01.source_1;

/**
 * Rental 表示某个顾客租了一部影片
 * @author nxy006
 */
public class Rental {

	private Movie _movie;
	private int _daysRented;

	public Rental(Movie movie, int daysRented) {
		_movie = movie;
		_daysRented = daysRented;
	}

	public int getDaysRented() {
		return _daysRented;
	}

	public Movie getMovie() {
		return _movie;
	}

}