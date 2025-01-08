package library.example.libraryEdu.service;

import library.example.libraryEdu.dto.AuthorDTO;
import library.example.libraryEdu.dto.BookDTO;
import library.example.libraryEdu.exception.AuthorInUseException;
import library.example.libraryEdu.exception.NotFoundException;
import library.example.libraryEdu.model.Author;
import library.example.libraryEdu.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookIntegrationService bookIntegrationService;
    private AuthorDTO convertToDTO(Author author) {
        log.info("Converting author: {}", author);
        return AuthorDTO.builder()
                .id(author.getId())
                .firstname(author.getFirstname())
                .lastname(author.getLastname())
                .birthdate(author.getBirthdate())
                .build();
    }

    public List<AuthorDTO> getAllAuthors() {
        log.info("Getting all authors...");
        return authorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) {
        log.info("Getting author with ID: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author with id " + id + " not found"));
        return convertToDTO(author);
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        log.info("Creating author with last name: {}", authorDTO.getLastname());
        Optional<Author> existingAuthor = authorRepository
                .findByFirstnameAndLastname(authorDTO.getFirstname(), authorDTO.getLastname());

        if (existingAuthor.isPresent()) {
            return convertToDTO(existingAuthor.get());
        }

        Author author = new Author();
        author.setFirstname(authorDTO.getFirstname());
        author.setLastname(authorDTO.getLastname());
        author.setBirthdate(authorDTO.getBirthdate());

        Author savedAuthor = authorRepository.save(author);
        log.info("Author with last name {} is successfully created", authorDTO.getLastname());

        return convertToDTO(savedAuthor);
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        log.info("Updating author with ID: {}", id);
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author with id " + id + " not found"));

        if (authorDTO.getFirstname() != null) {
            existingAuthor.setFirstname(authorDTO.getFirstname());
        }
        if (authorDTO.getLastname() != null) {
            existingAuthor.setLastname(authorDTO.getLastname());
        }
        if (authorDTO.getBirthdate() != null) {
            existingAuthor.setBirthdate(authorDTO.getBirthdate());
        }

        Author updatedAuthor = authorRepository.save(existingAuthor);
        log.info("Author with ID {} is successfully updated", id);

        return convertToDTO(updatedAuthor);
    }

    public void deleteAuthor(Long authorId) {
        log.info("Initiating deletion process for author with ID: {}", authorId);

        List<BookDTO> books = bookIntegrationService.getBooksByAuthorId(authorId);
        log.debug("Books associated with author ID {}: {}", authorId, books);

        if (!books.isEmpty()) {
            throw new AuthorInUseException("Cannot delete author with ID " + authorId + " as they have associated books.");
        }

        log.info("No associated books found. Proceeding with deletion.");
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found with id " + authorId));

        authorRepository.delete(author);
        log.info("Author with ID {} deleted successfully.", authorId);
    }
}