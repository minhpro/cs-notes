package my_group.web.controller;

import my_group.myapp.Book;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class SimpleRestController {

    @GetMapping
    public Book getBook() {
        return new Book("My book", "anonymous", 2023);
    }
}
