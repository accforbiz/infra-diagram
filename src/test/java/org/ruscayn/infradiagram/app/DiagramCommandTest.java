package org.ruscayn.infradiagram.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.ruscayn.infradiagram.testing.TestInputs;

class DiagramCommandTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final DiagramCommand command = new DiagramCommand(new PrintStream(out), new PrintStream(err));

    @TempDir
    private Path directory;

    @Test
    @DisplayName("올바른 입력이면 그림 파일을 만들고 0으로 끝난다")
    void drawsValidInput() throws IOException {
        Path output = directory.resolve("picture.png");

        int status = command.run(new String[] {input("{\"clusters\":[{\"elements\":[{\"id\":\"a\"}]}]}"), output.toString()});

        assertEquals(DiagramCommand.SUCCESS, status);
        assertTrue(Files.size(output) > 0);
        assertTrue(out.toString().startsWith("Wrote "));
    }

    @Test
    @DisplayName("잘못된 입력이면 그림 파일을 만들지 않고 이유를 알린 뒤 1로 끝난다")
    void rejectsInvalidInput() throws IOException {
        Path output = directory.resolve("picture.png");

        int status = command.run(new String[] {input("{\"clusters\":[]}"), output.toString()});

        assertEquals(DiagramCommand.FAILURE, status);
        assertFalse(Files.exists(output));
        assertTrue(err.toString().startsWith("Invalid input: Input must contain at least one cluster."));
    }

    @Test
    @DisplayName("상한 안의 입력이라도 그려 보니 글자가 너무 작으면 그림 파일을 만들지 않고, 모자란 정도와 고치는 방법을 알린 뒤 1로 끝난다")
    void rejectsInputDrawnTooSmall() throws IOException {
        Path output = directory.resolve("picture.png");

        int status = command.run(new String[] {input(crowdedInput()), output.toString()});

        assertEquals(DiagramCommand.FAILURE, status);
        assertFalse(Files.exists(output));
        assertTooSmallMessage(err.toString().lines().toList());
    }

    @Test
    @DisplayName("인자의 수가 맞지 않으면 사용법을 알리고 2로 끝난다")
    void explainsUsageForWrongArguments() {
        int status = command.run(new String[] {"only-one-argument"});

        assertEquals(DiagramCommand.USAGE_ERROR, status);
        assertTrue(err.toString().startsWith("Usage: infra-diagram <input.json> <output.png>"));
    }

    private void assertTooSmallMessage(List<String> lines) {
        assertEquals(4, lines.size(), lines.toString());
        assertEquals("This input cannot be drawn with acceptable quality.", lines.get(0));
        assertTrue(lines.get(1).matches("  - Labels would be drawn \\d+\\.\\d px high\\. The minimum is 18\\.0 px\\."), lines.get(1));
        assertEquals("Use fewer elements, fewer labels, or shorter labels.", lines.get(2));
        assertEquals(GuaranteedRange.standard().describe(), lines.get(3));
    }

    /**
     * 요소 12개인 무리 6개와 홀로 있는 요소 6개. 무리마다 바깥쪽 요소 셋에 20자짜리 글자를 붙인다.
     * 입력 상한 안이지만 항상 그려지는 범위는 벗어나고, 그려 보면 글자가 15.6픽셀이 된다.
     */
    private String crowdedInput() {
        return TestInputs.infrastructure(IntStream.range(0, 12).mapToObj(this::crowdedCluster).toArray(String[]::new));
    }

    private String crowdedCluster(int cluster) {
        int size = cluster < 6 ? 12 : 1;
        return TestInputs.cluster(IntStream.range(0, size)
                .mapToObj(index -> crowdedElement("c" + cluster + "e" + index, index >= size - 3))
                .toArray(String[]::new));
    }

    private String crowdedElement(String id, boolean labeled) {
        return labeled ? TestInputs.element(id, "\"label\":\"" + "W".repeat(20) + "\"") : TestInputs.element(id);
    }

    private String input(String json) throws IOException {
        Path file = directory.resolve("input.json");
        Files.writeString(file, json, StandardCharsets.UTF_8);
        return file.toString();
    }
}
