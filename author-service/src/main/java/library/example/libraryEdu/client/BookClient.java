package library.example.libraryEdu.client;

import library.example.libraryEdu.dto.BookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "book-service", url = "http://localhost:8080/api/books")
public interface BookClient {
    @GetMapping
    List<BookDTO> getAllBooks();
    @GetMapping("/{id}")
    BookDTO getBookById(@PathVariable Long id);
}