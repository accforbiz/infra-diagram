package org.ruscayn.infradiagram.testing;

import org.ruscayn.infradiagram.input.InputLimits;
import org.ruscayn.infradiagram.input.InputRules;
import org.ruscayn.infradiagram.input.JsonInfrastructureReader;
import org.ruscayn.infradiagram.model.Infrastructure;

public final class TestInputs {

    private static final InputLimits ROOMY = new InputLimits(100, 100, 100, 100, 100);

    private TestInputs() {
    }

    public static Infrastructure read(String json) {
        return new JsonInfrastructureReader(InputRules.standard(ROOMY)).parse(json);
    }

    public static String infrastructure(String... clusters) {
        return "{\"clusters\":[" + String.join(",", clusters) + "]}";
    }

    public static String cluster(String... elements) {
        return "{\"elements\":[" + String.join(",", elements) + "]}";
    }

    public static String element(String id) {
        return "{\"id\":\"" + id + "\"}";
    }

    public static String element(String id, String otherFields) {
        return "{\"id\":\"" + id + "\"," + otherFields + "}";
    }
}
