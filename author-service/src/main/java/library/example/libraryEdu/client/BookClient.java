package library.example.libraryEdu.client;

import library.example.libraryEdu.dto.BookDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "book-service", url = "${book.service.url}")
public interface BookClient {
    @GetMapping
    List<BookDTO> getAllBooks();
    @GetMapping("/{id}")
    BookDTO getBookById(@PathVariable Long id);
}