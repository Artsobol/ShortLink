package io.github.artsobol.shortlink.controller;

import io.github.artsobol.shortlink.entity.dto.RequestOriginalUrl;
import io.github.artsobol.shortlink.entity.dto.ResponseShortUrl;
import io.github.artsobol.shortlink.service.api.UrlShortenerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlShortenerController.class)
public class UrlShortenerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UrlShortenerService service;
    
    private final String originalUrl = "https://www.google.com";
    private final String code = "D3xF9";

    @Test
    @SneakyThrows
    @DisplayName("createShortUrl returns 201 with body")
    void createShortUrl_returns201_withBody() {
        // given
        RequestOriginalUrl request = new RequestOriginalUrl(originalUrl);
        ResponseShortUrl response = new ResponseShortUrl(originalUrl, code);
        when(service.createShortUrl(request)).thenReturn(response);

        // when + then
        mockMvc.perform(post("/short-links")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/r/" + code))
               .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.code").value(code));
    }

    @Test
    @SneakyThrows
    @DisplayName("createShortUrl returns 400 when url is invalid")
    void createShortUrl_returns400_whenUrlIsInvalid() {
        mockMvc.perform(post("/short-links")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @DisplayName("redirect returns 302 and location header to original url when code exists")
    void redirect_returns302_toOriginalUrl_whenCodeExists() {
        // given
        ResponseShortUrl response = new ResponseShortUrl(originalUrl, code);
        when(service.getOriginalUrl(code)).thenReturn(response);

        // when + then
        mockMvc.perform(get("/r/{shortCode}", code))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(originalUrl));
    }
}
