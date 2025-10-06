package com.dawanproject.booktracker.services;

import com.dawanproject.booktracker.dtos.BookDto;
import org.springframework.data.domain.Page;

public interface GoogleBooksApiService {

    Page<BookDto> getAllLivres(int page, int size, String search) throws Exception;

}
