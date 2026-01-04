package io.github.artsobol.shortlink.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.i18n")
public record I18nProperties(
        String basename,
        String encoding,
        boolean fallbackToSystemLocale,
        int cacheSeconds
) {
}
