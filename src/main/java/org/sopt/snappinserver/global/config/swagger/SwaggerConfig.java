package org.sopt.snappinserver.global.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    servers = {
        @Server(url="http://localhost:8080", description = "로컬 서버 주소")
    }
)
@Configuration
public class SwaggerConfig {

    private static final String LOCAL_ORIGIN = "http://localhost:8080";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(apiInfo());
    }

    @Bean
    public GroupedOpenApi all() {
        return GroupedOpenApi.builder()
            .group("all")
            .pathsToMatch("/**")
            .build();
    }

    @Bean
    public GroupedOpenApi v1Api() {
        return groupedApi("v1", "/api/v1");
    }

    @Bean
    public GroupedOpenApi v2Api() {
        return groupedApi("v2", "/api/v2");
    }

    private GroupedOpenApi groupedApi(String group, String fullPrefix) {
        return GroupedOpenApi.builder()
            .group(group)
            .pathsToMatch(fullPrefix + "/**")
            .addOpenApiCustomizer(stripPrefixAndSetServer(fullPrefix))
            .build();
    }

    private OpenApiCustomizer stripPrefixAndSetServer(String fullPrefix) {
        final Pattern exact = Pattern.compile("^" + Pattern.quote(fullPrefix) + "/?$");
        final String prefixWithSlash = fullPrefix.endsWith("/") ? fullPrefix : fullPrefix + "/";

        return openApi -> {
            Paths source = openApi.getPaths();
            if (source == null || source.isEmpty()) {
                return;
            }

            Paths rewritten = new Paths();
            source.forEach((path, item) -> {
                String newPath = path;
                if (exact.matcher(path).matches()) {
                    newPath = "/";
                } else if (path.startsWith(prefixWithSlash)) {
                    newPath = path.substring(fullPrefix.length());
                }
                rewritten.addPathItem(newPath, item);
            });

            openApi.setPaths(rewritten);

            List<io.swagger.v3.oas.models.servers.Server> servers = new ArrayList<>();

            servers.add(
                new io.swagger.v3.oas.models.servers.Server()
                    .url(LOCAL_ORIGIN + fullPrefix)
                    .description("Local")
            );

            openApi.setServers(servers);
        };
    }

    private Info apiInfo() {
        return new Info()
            .title("Snappin Swagger")
            .description("DIVE SOPT 37기 앱잼 | Snappin Swagger")
            .version("1.0.0");
    }
}
