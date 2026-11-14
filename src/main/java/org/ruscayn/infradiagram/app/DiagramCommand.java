package org.ruscayn.infradiagram.app;

import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import org.ruscayn.infradiagram.input.InvalidInputException;
import org.ruscayn.infradiagram.settings.DrawingSettings;

/**
 * 명령줄 인자를 해석해 그림을 만든다. 끝난 상태를 숫자로 돌려준다.
 *
 * 0은 성공, 1은 입력이 잘못되었거나 그릴 수 없음, 2는 인자를 잘못 줌을 뜻한다.
 */
final class DiagramCommand {

    static final int SUCCESS = 0;
    static final int FAILURE = 1;
    static final int USAGE_ERROR = 2;

    private final PrintStream out;
    private final PrintStream err;

    DiagramCommand(PrintStream out, PrintStream err) {
        this.out = out;
        this.err = err;
    }

    int run(String[] arguments) {
        if (arguments.length != 2) {
            err.println("Usage: infra-diagram <input.json> <output.png>");
            err.println("Reads an infrastructure description and draws it as a PNG picture.");
            return USAGE_ERROR;
        }
        return draw(Path.of(arguments[0]), Path.of(arguments[1]));
    }

    private int draw(Path input, Path output) {
        try {
            return generate(input, output);
        } catch (InvalidInputException error) {
            return fail("Invalid input: " + error.getMessage());
        } catch (PoorQualityException | UncheckedIOException error) {
            return fail(error.getMessage());
        }
    }

    private int generate(Path input, Path output) {
        new DiagramGenerator(DrawingSettings.standard()).generate(input, output);
        out.println("Wrote " + output);
        return SUCCESS;
    }

    private int fail(String message) {
        err.println(message);
        return FAILURE;
    }
}
