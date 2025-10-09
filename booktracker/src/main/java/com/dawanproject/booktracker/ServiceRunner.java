package com.dawanproject.booktracker;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.GoogleBooksApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
//@Component
public class ServiceRunner implements CommandLineRunner {

    private final GoogleBooksApiService service;

    @Override
    public void run(String... args) throws Exception {
//        Page<BookDto> list = service.getAll(0,10,"Stephen King");
//        list.forEach(System.out::println);
        BookDto bookDto = service.getBookById("5wBQEp6ruIAC");
        System.out.println(bookDto);
    }
}
