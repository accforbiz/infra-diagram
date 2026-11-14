package org.ruscayn.infradiagram.layout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import org.ruscayn.infradiagram.model.Element;
import org.ruscayn.infradiagram.scene.Box;
import org.ruscayn.infradiagram.scene.LabelMark;
import org.ruscayn.infradiagram.scene.Pillar;

/**
 * 한 무리의 글자를 무리의 바깥 둘레에 놓는다.
 *
 * 글자는 기둥에 가까운 쪽에 놓는다. 기둥이 무리의 가운데보다 왼쪽에 있으면 왼쪽 바깥에,
 * 그렇지 않으면 오른쪽 바깥에 놓는다. 글자가 모든 기둥의 바깥에 있으므로 기둥과 겹치지 않는다.
 *
 * 높이는 기둥의 윗면 높이에 맞춘다. 같은 쪽에 글자가 여럿이면 위에서부터 차례로 놓고,
 * 앞의 글자와 한 줄 높이보다 가까우면 한 줄 아래로 내린다.
 * 예: 윗면 높이가 10과 10.2인 두 기둥의 글자는 줄 높이가 1.2일 때 10과 11.2에 놓인다.
 */
final class LabelPlacer {

    private final LabelBuilder builder;
    private final double lineHeight;

    LabelPlacer(LabelBuilder builder, double lineHeight) {
        this.builder = builder;
        this.lineHeight = lineHeight;
    }

    List<LabelMark> place(List<Element> elements, List<Pillar> pillars) {
        Box area = Box.around(pillars.stream().map(Pillar::bounds).toList());
        List<LabelTarget> targets = targets(elements, pillars);
        List<LabelMark> marks = new ArrayList<>(stack(targets, LabelSide.LEFT, area));
        marks.addAll(stack(targets, LabelSide.RIGHT, area));
        return marks;
    }

    private List<LabelTarget> targets(List<Element> elements, List<Pillar> pillars) {
        return IntStream.range(0, elements.size())
                .filter(index -> elements.get(index).hasLabel())
                .mapToObj(index -> new LabelTarget(pillars.get(index), elements.get(index).label().orElseThrow()))
                .toList();
    }

    private List<LabelMark> stack(List<LabelTarget> targets, LabelSide side, Box area) {
        List<LabelMark> marks = new ArrayList<>();
        double lineCenter = Double.NEGATIVE_INFINITY;
        for (LabelTarget target : fromTop(targets, side)) {
            lineCenter = Math.max(target.topHeight(), lineCenter + lineHeight);
            marks.add(builder.build(target, lineCenter, area));
        }
        return marks;
    }

    private List<LabelTarget> fromTop(List<LabelTarget> targets, LabelSide side) {
        return targets.stream()
                .filter(target -> target.side() == side)
                .sorted(Comparator.comparingDouble(LabelTarget::topHeight))
                .toList();
    }
}
