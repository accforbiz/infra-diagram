package org.ruscayn.infradiagram.app;

/**
 * 항상 그려지는 범위. 이 범위 안의 입력은 글자를 입력 상한까지 붙여도 품질 조건을 모두 지킨다.
 *
 * 입력 상한(무리 12개, 한 무리의 요소 19개)은 넉넉하게 두었기 때문에, 상한 안이어도
 * 요소와 글자가 많으면 그림이 줄어들어 거부될 수 있다. 그래서 "여기까지는 걱정 없이 쓸 수 있다"는
 * 범위를 따로 적어 둔다. 과제의 예시 그림과 닮게 만든 예제(examples/reference-like.json)의 규모,
 * 곧 요소가 여럿인 무리 3개와 홀로 있는 요소 3개가 기준이다.
 *
 * 수학으로 증명한 범위가 아니라 그려 보아 확인한 범위다. 이 범위에서 가장 불리한 입력을 찾아보니
 * 글자가 19.2픽셀이었고(기준 18픽셀), GuaranteedRangeTest가 무리의 순서와 크기를 빠짐없이 그려 확인한다.
 * 글자는 한글과 영문, 숫자, 기호처럼 폭이 W 이하인 글자를 기준으로 한다.
 */
record GuaranteedRange(int maxClusters, int maxMultiElementClusters, int maxElementsPerCluster) {

    static GuaranteedRange standard() {
        return new GuaranteedRange(6, 3, 8);
    }

    String describe() {
        String sentence = "An input always fits when it has at most %d clusters, at most %d of them with two or more "
                + "elements, at most %d elements per cluster, and labels written in Korean or ASCII characters.";
        return sentence.formatted(maxClusters, maxMultiElementClusters, maxElementsPerCluster);
    }
}
