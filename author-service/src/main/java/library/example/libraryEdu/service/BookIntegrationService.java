package library.example.libraryEdu.service;

import library.example.libraryEdu.client.BookClient;
import library.example.libraryEdu.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookIntegrationService {
    private final BookClient bookClient;

    public List<BookDTO> getBooksByAuthorId(Long authorId) {
        return bookClient.getAllBooks()
                .stream()
                .filter(book -> book.getAuthor() != null && book.getAuthor().getId().equals(authorId))
                .toList();
    }
}