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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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
    @DisplayName("인자의 수가 맞지 않으면 사용법을 알리고 2로 끝난다")
    void explainsUsageForWrongArguments() {
        int status = command.run(new String[] {"only-one-argument"});

        assertEquals(DiagramCommand.USAGE_ERROR, status);
        assertTrue(err.toString().startsWith("Usage: infra-diagram <input.json> <output.png>"));
    }

    private String input(String json) throws IOException {
        Path file = directory.resolve("input.json");
        Files.writeString(file, json, StandardCharsets.UTF_8);
        return file.toString();
    }
}
