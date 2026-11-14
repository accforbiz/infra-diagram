package org.ruscayn.infradiagram.layout;

import java.util.List;
import java.util.stream.IntStream;

/**
 * 한 무리 안에서 이웃한 자리 사이의 거리를 정한다. 조건을 지키는 가장 촘촘한 거리를 찾는다.
 *
 * 지켜야 할 조건은 두 가지다.
 * 조건 1. 기둥의 바닥끼리 틈을 두고 떨어진다.
 * 조건 2. 앞 기둥이 뒤 기둥의 윗면을 가리지 않는다.
 *
 * 자리 사이의 거리를 넓힐수록 두 조건은 더 잘 지켜지고, 한번 지켜지면 더 넓혀도 계속 지켜진다.
 * 그래서 "지켜지지 않는 거리"와 "지켜지는 거리" 사이를 반씩 좁혀 가며 경계를 찾는다(이분 탐색).
 *
 * 예: 반지름 1인 기둥만 있는 무리에서 틈이 0.3이면, 이웃한 자리의 기둥이 2.3 떨어져야
 * 바닥이 틈을 두고 떨어진다. 높이가 같으면 윗면도 함께 떨어지므로 답은 2.3이다.
 * 예: 가운데에 높은 기둥이 있고 그 뒤에 낮은 기둥이 있으면, 높은 기둥이 낮은 기둥의 윗면을
 * 가리지 않을 때까지 더 넓혀야 하므로 답이 2.3보다 커진다.
 */
final class ClusterPitch {

    private static final int HALVINGS = 40;

    private final TopClearance topClearance;
    private final double pillarGap;

    ClusterPitch(TopClearance topClearance, double pillarGap) {
        this.topClearance = topClearance;
        this.pillarGap = pillarGap;
    }

    /**
     * @param unitPillars 자리 사이의 거리가 1일 때의 자리에 선 기둥들
     */
    double of(List<Standing> unitPillars) {
        return narrow(unitPillars, 0, widestSafeGuess(unitPillars), HALVINGS);
    }

    private double narrow(List<Standing> unitPillars, double tooClose, double wideEnough, int halvingsLeft) {
        if (halvingsLeft == 0) {
            return wideEnough;
        }
        double middle = (tooClose + wideEnough) / 2;
        if (fits(unitPillars, middle)) {
            return narrow(unitPillars, tooClose, middle, halvingsLeft - 1);
        }
        return narrow(unitPillars, middle, wideEnough, halvingsLeft - 1);
    }

    private double widestSafeGuess(List<Standing> unitPillars) {
        double guess = 1;
        while (!fits(unitPillars, guess)) {
            guess *= 2;
        }
        return guess;
    }

    private boolean fits(List<Standing> unitPillars, double pitch) {
        List<Standing> pillars = unitPillars.stream().map(pillar -> pillar.at(pillar.spot().scaled(pitch))).toList();
        return IntStream.range(0, pillars.size()).allMatch(index -> fitsWithLater(pillars, index));
    }

    private boolean fitsWithLater(List<Standing> pillars, int index) {
        Standing pillar = pillars.get(index);
        return pillars.subList(index + 1, pillars.size()).stream().allMatch(other -> fits(pillar, other));
    }

    private boolean fits(Standing first, Standing second) {
        boolean footprintsApart = first.distanceTo(second) >= first.radius() + second.radius() + pillarGap;
        Standing front = first.inFrontOf(second) ? first : second;
        Standing back = front == first ? second : first;
        return footprintsApart && topClearance.leavesTopVisible(front, back);
    }
}
