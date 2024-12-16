package library.example.libraryEdu.client;

import library.example.libraryEdu.dto.AuthorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "author-service", url = "http://localhost:8081/api/authors")
public interface AuthorClient {
    @PostMapping
    AuthorDTO createAuthor(@RequestBody AuthorDTO authorDTO);
    @GetMapping("/{id}")
    AuthorDTO getAuthorById(@PathVariable Long id);
}


