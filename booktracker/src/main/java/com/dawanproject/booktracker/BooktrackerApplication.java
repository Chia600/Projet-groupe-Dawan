package com.dawanproject.booktracker;

import com.dawanproject.booktracker.dtos.BookDto;
import com.dawanproject.booktracker.services.IGoogleBooksApiService;
import com.dawanproject.booktracker.services.impl.GoogleBooksApiServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BooktrackerApplication {

	public static void main(String[] args) throws Exception {

		SpringApplication.run(BooktrackerApplication.class, args);

		List<BookDto> list = new ArrayList<>();
        IGoogleBooksApiService service = new GoogleBooksApiServiceImpl();
		list =service.getAllBy(1,10, "");

		list.forEach(System.out::println);


//		RestClient rc = RestClient.create();
//
//		String res = rc.get()
//				.uri("https://www.googleapis.com/books/v1/volumes?q=huner+games&max-result=100&key=AIzaSyCbJZJyMK5-BDVBiCTBXzykJBlDzEKMSkA")
//				.retrieve().body(String.class);
//
//		ObjectMapper mapper =new ObjectMapper();
//
//		JsonNode node = mapper.readTree(res);
//		JsonNode item = node.get("items").get(0);
//
//		String cover = (item.path("volumeInfo").path("imageLinks").path("thumbnail").asText()).split("&zoom=1")[0];
//
//
//		System.out.println(cover);


	}

}
