package org.ruscayn.infradiagram.input;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.ruscayn.infradiagram.model.Infrastructure;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

public final class JsonInfrastructureReader implements InfrastructureReader {

    private final JsonMapper mapper = StrictJsonMapper.create();
    private final InfrastructureAssembler assembler = new InfrastructureAssembler();
    private final JsonErrorTranslator errorTranslator = new JsonErrorTranslator();
    private final InputRule rules;

    public JsonInfrastructureReader(InputRule rules) {
        this.rules = rules;
    }

    @Override
    public Infrastructure read(Path file) {
        return parse(text(file));
    }

    public Infrastructure parse(String json) {
        Infrastructure infrastructure = assembler.assemble(raw(json));
        rules.check(infrastructure);
        return infrastructure;
    }

    private RawInfrastructure raw(String json) {
        if (json.isBlank()) {
            throw new InvalidInputException("The input is empty.");
        }
        try {
            return mapper.readValue(json, RawInfrastructure.class);
        } catch (JacksonException error) {
            throw errorTranslator.translate(error);
        }
    }

    private String text(Path file) {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException error) {
            String message = "Cannot read input file '%s'. Check that it exists and is UTF-8 text.";
            throw new InvalidInputException(message.formatted(file));
        }
    }
}
