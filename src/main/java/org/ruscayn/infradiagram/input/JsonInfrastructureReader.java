package org.ruscayn.infradiagram.input;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.ruscayn.infradiagram.model.Infrastructure;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

public final class JsonInfrastructureReader implements InfrastructureReader {

    private static final String BYTE_ORDER_MARK = "\uFEFF";

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
            return withoutByteOrderMark(Files.readString(file, StandardCharsets.UTF_8));
        } catch (IOException error) {
            String message = "Cannot read input file '%s'. Check that it exists and is UTF-8 text.";
            throw new InvalidInputException(message.formatted(file));
        }
    }

    /**
     * 파일 맨 앞에 붙은 BOM을 떼어 낸다.
     *
     * BOM은 Windows의 일부 도구가 UTF-8 파일 맨 앞에 붙이는 보이지 않는 글자다.
     * 예: 메모장에서 "UTF-8(BOM)"으로 저장하거나, Windows PowerShell 5에서 Out-File -Encoding utf8로 만든 파일.
     * 내용은 올바른 UTF-8인데 JSON 문법으로는 맨 앞의 이 글자가 오류여서,
     * 떼지 않으면 "1번째 줄 1번째 칸에 알 수 없는 글자"라는, 눈에 보이지 않아 고치기 어려운 안내가 나온다.
     *
     * 맨 앞의 하나만 뗀다. 파일 중간에 있는 같은 글자는 저장 방식의 흔적이 아니므로
     * 그대로 두어 JSON 오류로 드러나게 한다.
     */
    private String withoutByteOrderMark(String text) {
        return text.startsWith(BYTE_ORDER_MARK) ? text.substring(BYTE_ORDER_MARK.length()) : text;
    }
}
