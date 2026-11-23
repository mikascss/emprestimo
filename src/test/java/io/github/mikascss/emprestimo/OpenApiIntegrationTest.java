package io.github.mikascss.emprestimo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:openapi-tests;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class OpenApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Test
    void documentaRotasSchemasEStatusReais() throws Exception {
        var response = get("/v3/api-docs");
        assertEquals(200, response.statusCode());
        JsonNode document = JsonMapper.builder().build().readTree(response.body());
        assertEquals("API de Empréstimo de Equipamentos", document.at("/info/title").asText());
        JsonNode paths = document.path("paths");
        assertEquals(7, paths.size());
        for (String path : new String[]{"/equipamentos", "/equipamentos/{id}", "/colaboradores",
                "/colaboradores/{id}", "/colaboradores/{id}/emprestimos", "/emprestimo",
                "/emprestimo/{id}/devolucao"}) {
            assertTrue(paths.has(path), path);
        }
        JsonNode responses = paths.path("/emprestimo").path("post").path("responses");
        for (String status : new String[]{"201", "400", "404", "409"}) {
            assertTrue(responses.has(status), status);
        }
        assertFalse(responses.has("200"));
        assertTrue(responses.path("201").path("content").toString().contains("ResultModelEmprestimo"));
        assertEquals("#/components/schemas/ErroResposta",
                responses.path("409").at("/content/application~1json/schema/$ref").asText());
        assertFalse(paths.path("/equipamentos/{id}").path("delete")
                .at("/responses/204").has("content"));
        assertEquals("NOTEBOOK", document.at("/components/schemas/EquipamentoRegisterRequest/properties/tipo/example").asText());
    }

    @Test
    void disponibilizaSwaggerUiEConfiguracao() throws Exception {
        var ui = get("/swagger-ui.html");
        assertEquals(200, ui.statusCode());
        assertTrue(ui.body().contains("swagger-ui"));
        var config = get("/v3/api-docs/swagger-config");
        assertEquals(200, config.statusCode());
        assertEquals("/v3/api-docs", JsonMapper.builder().build().readTree(config.body()).path("url").asText());
    }

    private HttpResponse<String> get(String path) throws Exception {
        try (var client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build()) {
            return client.send(HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                    .GET().build(), HttpResponse.BodyHandlers.ofString());
        }
    }
}
