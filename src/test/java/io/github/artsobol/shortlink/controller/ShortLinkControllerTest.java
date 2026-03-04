package io.github.artsobol.shortlink.controller;

import io.github.artsobol.shortlink.shortlink.dto.CreateShortLinkRequest;
import io.github.artsobol.shortlink.shortlink.dto.ShortLinkResponse;
import io.github.artsobol.shortlink.shortlink.service.ShortLinkService;
import io.github.artsobol.shortlink.shortlink.web.ShortLinkController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShortLinkController.class)
@AutoConfigureMockMvc(addFilters = false)
@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class ShortLinkControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ShortLinkService service;

    private final String originalUrl = "https://www.google.com";
    private final String code = "D3xF9";

    @Test
    @SneakyThrows
    @DisplayName("createShortUrl returns 201 with body")
    void createShortUrl_returns201_withBody() {
        // given
        CreateShortLinkRequest request = new CreateShortLinkRequest(originalUrl);
        ShortLinkResponse response = new ShortLinkResponse(originalUrl, code);
        when(service.create(request)).thenReturn(response);

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
        ShortLinkResponse response = new ShortLinkResponse(originalUrl, code);
        when(service.resolve(code)).thenReturn(response);

        // when + then
        mockMvc.perform(get("/r/{shortCode}", code))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(originalUrl));
    }
}
