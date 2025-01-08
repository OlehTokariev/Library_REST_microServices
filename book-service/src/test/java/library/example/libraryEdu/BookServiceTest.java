package library.example.libraryEdu;

import library.example.libraryEdu.dto.AuthorDTO;
import library.example.libraryEdu.dto.BookDTO;
import library.example.libraryEdu.model.Book;
import library.example.libraryEdu.service.AuthorIntegrationService;
import library.example.libraryEdu.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private AuthorIntegrationService authorIntegrationService;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void convertToDTO_ShouldReturnBookDTO_WithMockedAuthorIntegrationService() {

        Book book = new Book();
        book.setTitle("Test Title");
        book.setYear(2022);
        book.setGenre("Fiction");
        book.setAuthorId(1L);

        AuthorDTO mockAuthorDTO = AuthorDTO.builder()
                .id(1L)
                .firstname("Mock")
                .lastname("Author")
                .birthdate(LocalDate.of(1988, 8, 8))
                .build();

        when(authorIntegrationService.getAuthorById(1L)).thenReturn(mockAuthorDTO);

        BookDTO result = bookService.convertToDTO(book);

        assertEquals("Test Title", result.getTitle());
        assertEquals(2022, result.getYear());
        assertEquals("Fiction", result.getGenre());
        assertEquals(mockAuthorDTO, result.getAuthor());

        verify(authorIntegrationService, times(1)).getAuthorById(1L);
    }
}