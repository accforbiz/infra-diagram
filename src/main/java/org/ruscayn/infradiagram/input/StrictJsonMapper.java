package org.ruscayn.infradiagram.input;

import tools.jackson.core.StreamReadFeature;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.cfg.CoercionInputShape;
import tools.jackson.databind.cfg.MutableCoercionConfig;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.LogicalType;

/**
 * 규칙에 어긋난 입력을 조용히 고쳐 읽지 않도록 설정한 JSON 읽기 도구를 만든다.
 *
 * Jackson은 기본 설정에서 입력을 너그럽게 읽는다. 그대로 두면 입력의 실수가 드러나지 않는다.
 * 예: "sclae": "large"처럼 항목 이름을 잘못 적으면 무시되어 규모가 기본값인 중이 된다.
 * 예: "id": 5처럼 글자 자리에 숫자를 적으면 "5"로 바뀌어 읽힌다.
 * 예: 한 요소에 "id"를 두 번 적으면 뒤의 값만 남는다.
 * 여기서는 이런 경우를 모두 오류로 만든다.
 */
final class StrictJsonMapper {

    private StrictJsonMapper() {
    }

    static JsonMapper create() {
        return JsonMapper.builder()
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                .enable(StreamReadFeature.STRICT_DUPLICATE_DETECTION)
                .disable(MapperFeature.ALLOW_COERCION_OF_SCALARS)
                .withCoercionConfig(LogicalType.Textual, StrictJsonMapper::rejectNonText)
                .build();
    }

    private static void rejectNonText(MutableCoercionConfig config) {
        config.setCoercion(CoercionInputShape.Integer, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                .setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail);
    }
}
