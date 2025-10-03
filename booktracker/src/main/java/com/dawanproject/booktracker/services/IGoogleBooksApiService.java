package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.BookDto;

import java.util.List;

public interface IGoogleBooksApiService {

    List<BookDto> getAllBy(int page, int size, String search) throws Exception;

}
