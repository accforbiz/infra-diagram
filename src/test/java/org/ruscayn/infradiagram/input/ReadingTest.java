package org.ruscayn.infradiagram.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.ruscayn.infradiagram.model.Cluster;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.model.Infrastructure;
import org.ruscayn.infradiagram.model.Link;
import org.ruscayn.infradiagram.model.Scale;

class ReadingTest {

    private static final String FULL = """
            {
              "clusters": [
                { "elements": [
                    { "id": "lake", "scale": "large", "core": true, "label": "데이터 레이크" },
                    { "id": "app" }
                ] },
                { "elements": [ { "id": "tool", "scale": "small" } ] }
              ],
              "links": [ { "between": ["app", "tool"] } ]
            }
            """;

    @Test
    @DisplayName("모든 항목을 적은 요소와 생략한 요소를 손으로 구한 결과 그대로 읽는다")
    void readsEveryFieldAndDefaults() {
        Element lake = new Element("lake", Scale.LARGE, true, Optional.of("데이터 레이크"));
        Element app = new Element("app", Scale.MEDIUM, false, Optional.empty());
        Element tool = new Element("tool", Scale.SMALL, false, Optional.empty());
        List<Cluster> clusters = List.of(new Cluster(List.of(lake, app)), new Cluster(List.of(tool)));
        Infrastructure expected = new Infrastructure(clusters, List.of(new Link("app", "tool")));

        assertEquals(expected, InputTesting.read(FULL));
    }

    @Test
    @DisplayName("연결 목록을 생략하면 연결이 없는 구성으로 읽는다")
    void readsMissingLinksAsNone() {
        Infrastructure infrastructure = InputTesting.read(InputTesting.oneCluster("{\"id\":\"a\"}"));

        assertTrue(infrastructure.links().isEmpty());
    }

    @Test
    @DisplayName("맨 앞에 BOM이 붙은 UTF-8 파일도 BOM이 없는 입력과 같게 읽는다")
    void readsFileStartingWithByteOrderMark(@TempDir Path directory) throws IOException {
        Path file = directory.resolve("with-bom.json");
        Files.writeString(file, "\uFEFF" + FULL, StandardCharsets.UTF_8);
        JsonInfrastructureReader reader = new JsonInfrastructureReader(infrastructure -> { });

        assertEquals(InputTesting.read(FULL), reader.read(file));
    }

    @Test
    @DisplayName("없는 파일을 읽으라고 하면 파일 이름을 담은 오류로 거부한다")
    void rejectsMissingFile(@TempDir Path directory) {
        Path missing = directory.resolve("missing.json");
        JsonInfrastructureReader reader = new JsonInfrastructureReader(infrastructure -> { });

        InvalidInputException error = assertThrows(InvalidInputException.class, () -> reader.read(missing));

        assertTrue(error.getMessage().contains("missing.json"));
    }
}
