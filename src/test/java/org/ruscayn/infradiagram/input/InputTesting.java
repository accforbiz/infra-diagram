package org.ruscayn.infradiagram.input;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import org.ruscayn.infradiagram.model.Infrastructure;

final class InputTesting {

    private static final InputLimits ROOMY = new InputLimits(100, 100, 100, 100);

    private InputTesting() {
    }

    static Infrastructure read(String json) {
        return read(json, ROOMY);
    }

    static Infrastructure read(String json, InputLimits limits) {
        return new JsonInfrastructureReader(InputRules.standard(limits)).parse(json);
    }

    static String rejectionOf(String json) {
        return rejectionOf(json, ROOMY);
    }

    static String rejectionOf(String json, InputLimits limits) {
        return assertThrows(InvalidInputException.class, () -> read(json, limits)).getMessage();
    }

    static String clusters(String... clusters) {
        return "{\"clusters\":[" + String.join(",", clusters) + "]}";
    }

    static String oneCluster(String elements) {
        return "{\"clusters\":[{\"elements\":[" + elements + "]}]}";
    }

    static String clustersOfOneElement(int count) {
        String clusters = String.join(",", Collections.nCopies(count, "{\"elements\":[%s]}"));
        return "{\"clusters\":[" + numbered(clusters, count) + "]}";
    }

    static String elements(int count) {
        return numbered(String.join(",", Collections.nCopies(count, "%s")), count);
    }

    private static String numbered(String template, int count) {
        Object[] elements = new Object[count];
        for (int index = 0; index < count; index++) {
            elements[index] = "{\"id\":\"e" + index + "\"}";
        }
        return template.formatted(elements);
    }
}
