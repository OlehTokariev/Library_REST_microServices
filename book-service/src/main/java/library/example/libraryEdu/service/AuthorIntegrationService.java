package library.example.libraryEdu.service;

import feign.FeignException;
import library.example.libraryEdu.client.AuthorClient;
import library.example.libraryEdu.dto.AuthorDTO;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorIntegrationService {

    private final AuthorClient authorClient;

    public AuthorDTO getOrCreateAuthor(AuthorDTO authorDTO) {
        try {
            if (authorDTO.getId() != null) {
                return authorClient.getAuthorById(authorDTO.getId());
            }
            return authorClient.createAuthor(authorDTO);
        } catch (FeignException e) {
            return null;
        }
    }

    public AuthorDTO getAuthorById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Author ID cannot be null");
        }
        try {
            return authorClient.getAuthorById(id);
        } catch (FeignException e) {
            return null;
        }
    }
}