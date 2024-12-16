package library.example.libraryEdu.service;

import library.example.libraryEdu.client.AuthorClient;
import library.example.libraryEdu.dto.AuthorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorIntegrationService {

    private final AuthorClient authorClient;
    public AuthorDTO findOrCreateAuthor(AuthorDTO authorDTO) {
        if (authorDTO.getId() != null) {
            return authorClient.getAuthorById(authorDTO.getId());
        }
        return authorClient.createAuthor(authorDTO);
    }

    public AuthorDTO getAuthorById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Author ID cannot be null");
        }
        return authorClient.getAuthorById(id);
    }
    public AuthorDTO getOrCreateAuthor(AuthorDTO authorDTO) {
        if (authorDTO.getId() != null) {
            return getAuthorById(authorDTO.getId());
        }
        return authorClient.createAuthor(authorDTO);
    }
}