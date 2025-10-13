package com.dawanproject.booktracker;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.entities.Role;
import com.dawanproject.booktracker.enums.RoleName;
import com.dawanproject.booktracker.services.AccountService;
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

    private final AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
//        Page<BookDto> list = service.getAll(0,10,"Stephen King");
//        list.forEach(System.out::println);
//        BookDto bookDto = service.getBookById("5wBQEp6ruIAC");
//        System.out.println(bookDto);
//
//        Role admin = new Role (RoleName.ADMIN);
//        Role user = new Role (RoleName.USER);
//
//        accountService.addNewRole(user);
//        accountService.addNewRole(admin);
    }
}
