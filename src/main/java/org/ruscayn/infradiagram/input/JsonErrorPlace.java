package org.ruscayn.infradiagram.input;

import tools.jackson.core.JacksonException;
import tools.jackson.core.TokenStreamLocation;

/**
 * JSON 오류가 난 곳을 안내문에 넣을 글로 만든다.
 *
 * 알 수 있는 만큼 알린다. 줄과 칸을 알면 줄과 칸을, 항목의 경로를 알면 경로를 적는다.
 * 예: " at line 3, column 14 (clusters[0].elements[1].scale)"
 * 예: 줄 번호를 모를 때는 " at clusters[0].elements[1].scale"
 * Jackson은 항목 이름이 틀린 오류에서 줄 번호를 알려 주지 않고 -1을 돌려준다.
 */
final class JsonErrorPlace {

    String of(JacksonException error) {
        String lineAndColumn = lineAndColumn(error.getLocation());
        String path = path(error);
        if (lineAndColumn.isEmpty()) {
            return path.isEmpty() ? "" : " at " + path;
        }
        return path.isEmpty() ? " at " + lineAndColumn : " at %s (%s)".formatted(lineAndColumn, path);
    }

    private String lineAndColumn(TokenStreamLocation location) {
        if (location == null || location.getLineNr() <= 0) {
            return "";
        }
        return "line %d, column %d".formatted(location.getLineNr(), location.getColumnNr());
    }

    /**
     * 오류가 난 항목까지의 경로를 만든다. 이름은 점으로 잇고 목록의 순서는 대괄호에 넣는다.
     * 예: 첫째 무리의 둘째 요소의 scale 항목은 "clusters[0].elements[1].scale"이다.
     */
    private String path(JacksonException error) {
        StringBuilder path = new StringBuilder();
        for (JacksonException.Reference step : error.getPath()) {
            boolean named = step.getPropertyName() != null;
            String separator = path.isEmpty() || !named ? "" : ".";
            path.append(separator).append(named ? step.getPropertyName() : "[" + step.getIndex() + "]");
        }
        return path.toString();
    }
}
