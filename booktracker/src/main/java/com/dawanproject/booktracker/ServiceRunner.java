package com.dawanproject.booktracker;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.IGoogleBooksApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ServiceRunner implements CommandLineRunner {

    private final IGoogleBooksApiService service;

    @Override
    public void run(String... args) throws Exception {

        List<BookDto> list = service.getAllBy(1,10, "");
        list.forEach(System.out::println);
    }
}
