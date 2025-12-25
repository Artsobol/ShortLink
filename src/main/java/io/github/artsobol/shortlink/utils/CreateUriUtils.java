package io.github.artsobol.shortlink.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public final class CreateUriUtils {

    private CreateUriUtils() {}

    public static URI buildRedirectUri(String path, String code) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).buildAndExpand(code).toUri();
    }
}
