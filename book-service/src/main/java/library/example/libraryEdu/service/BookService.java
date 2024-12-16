package library.example.libraryEdu.service;

import library.example.libraryEdu.dto.AuthorDTO;
import library.example.libraryEdu.dto.BookDTO;
import library.example.libraryEdu.exception.NotFoundException;
import library.example.libraryEdu.model.Book;
import library.example.libraryEdu.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorIntegrationService authorIntegrationService;

    private BookDTO convertToDTO(Book book) {
        AuthorDTO authorDTO = authorIntegrationService.getAuthorById(book.getAuthorId());
        return BookDTO.builder()
                .title(book.getTitle())
                .year(book.getYear())
                .genre(book.getGenre())
                .author(authorDTO)
                .build();
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
        return convertToDTO(book);
    }

    public BookDTO createBook(BookDTO bookDTO) {
        AuthorDTO authorDTO = authorIntegrationService.getOrCreateAuthor(bookDTO.getAuthor());

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setYear(bookDTO.getYear());
        book.setGenre(bookDTO.getGenre());
        book.setAuthorId(authorDTO.getId());

        Book savedBook = bookRepository.save(book);

        return convertToDTO(savedBook);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));

        if (bookDTO.getTitle() != null) {
            existingBook.setTitle(bookDTO.getTitle());
        }
        if (bookDTO.getYear() != null) {
            existingBook.setYear(bookDTO.getYear());
        }
        if (bookDTO.getGenre() != null) {
            existingBook.setGenre(bookDTO.getGenre());
        }
        if (bookDTO.getAuthor() != null) {

            AuthorDTO updatedAuthor = authorIntegrationService.getOrCreateAuthor(bookDTO.getAuthor());
            existingBook.setAuthorId(updatedAuthor.getId());
        }

        Book updatedBook = bookRepository.save(existingBook);
        return convertToDTO(updatedBook);
    }

    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + bookId));

        bookRepository.delete(book);
    }
}