package library.example.libraryEdu.service;

import library.example.libraryEdu.dto.AuthorDTO;
import library.example.libraryEdu.dto.BookDTO;
import library.example.libraryEdu.exception.AuthorServiceUnavailableException;
import library.example.libraryEdu.exception.NotFoundException;
import library.example.libraryEdu.model.Book;
import library.example.libraryEdu.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorIntegrationService authorIntegrationService;

    public BookDTO convertToDTO(Book book) {
        log.info("Converting book: {}", book);
        AuthorDTO authorDTO = authorIntegrationService.getAuthorById(book.getAuthorId());
        return BookDTO.builder()
                .title(book.getTitle())
                .year(book.getYear())
                .genre(book.getGenre())
                .author(authorDTO)
                .build();
    }

    public List<BookDTO> getAllBooks() {
        log.info("Getting all books...");
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        log.info("Getting book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
        return convertToDTO(book);
    }

    public BookDTO createBook(BookDTO bookDTO) {
        log.info("Creating a new book with title: {}", bookDTO.getTitle());
        AuthorDTO authorDTO = Optional.ofNullable(authorIntegrationService.getOrCreateAuthor(bookDTO.getAuthor()))
                .orElseThrow(() -> new AuthorServiceUnavailableException("Author service is currently unavailable."));

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setYear(bookDTO.getYear());
        book.setGenre(bookDTO.getGenre());
        book.setAuthorId(authorDTO.getId());

        Book savedBook = bookRepository.save(book);
        log.info("Book created successfully with ID: {}", savedBook.getId());

        return convertToDTO(savedBook);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        log.info("Updating book with ID: {}", id);
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
        log.info("Book with ID {} is successfully updated", id);

        return convertToDTO(updatedBook);
    }

    public void deleteBook(Long bookId) {
        log.info("Deleting book with ID: {}", bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Book not found with id " + bookId));

        bookRepository.delete(book);
        log.info("Book with ID {} deleted successfully.", bookId);
    }
}