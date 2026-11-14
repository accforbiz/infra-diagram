package org.ruscayn.infradiagram.app;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.ruscayn.infradiagram.input.InputLimits;
import org.ruscayn.infradiagram.model.Scale;

/**
 * 항상 그려지는 범위 안에서 무리의 순서와 크기가 다른 입력을 빠짐없이 만든다.
 *
 * 무리의 크기를 앞에서부터 하나씩 정해 나간다. 요소가 둘 이상인 무리가 이미 한도만큼 있으면
 * 그 뒤의 무리는 요소 하나짜리만 된다.
 * 예: 무리 2개, 요소가 둘 이상인 무리 1개까지, 한 무리의 요소 3개까지이면
 * [1,1], [1,2], [1,3], [2,1], [3,1]의 다섯 가지가 나온다.
 */
final class GuaranteedInputs {

    /** 글꼴에서 한글과 영문, 숫자, 기호를 통틀어 가장 넓은 글자다. */
    private static final String WIDEST_LETTER = "W";

    private final GuaranteedRange range;
    private final InputLimits limits;

    GuaranteedInputs(GuaranteedRange range, InputLimits limits) {
        this.range = range;
        this.limits = limits;
    }

    Stream<GuaranteedInput> withClusters(int count) {
        Stream<List<Integer>> sizes = Stream.of(List.of());
        for (int cluster = 0; cluster < count; cluster++) {
            sizes = sizes.flatMap(this::withOneMoreCluster);
        }
        return sizes.flatMap(this::shapes);
    }

    private Stream<List<Integer>> withOneMoreCluster(List<Integer> sizes) {
        long multiElementClusters = sizes.stream().filter(size -> size > 1).count();
        boolean roomForMore = multiElementClusters < range.maxMultiElementClusters();
        int largest = roomForMore ? range.maxElementsPerCluster() : 1;
        return IntStream.rangeClosed(1, largest).mapToObj(size -> appended(sizes, size));
    }

    private List<Integer> appended(List<Integer> sizes, int size) {
        return Stream.concat(sizes.stream(), Stream.of(size)).toList();
    }

    private Stream<GuaranteedInput> shapes(List<Integer> sizes) {
        String label = WIDEST_LETTER.repeat(limits.maxLabelLength());
        return Stream.of(Scale.SMALL, Scale.LARGE)
                .map(centerScale -> new GuaranteedInput(sizes, centerScale, limits.maxLabelsPerCluster(), label));
    }
}
