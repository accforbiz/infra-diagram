package org.ruscayn.infradiagram.scene;

/**
 * 무리의 색이다. "몇 번째 색"이거나 "색 없음"이다.
 *
 * 요소가 둘 이상인 무리는 0번부터 차례로 색을 받고, 무리끼리 색이 겹치지 않는다.
 * 요소가 하나뿐인 무리는 묶을 것이 없으므로 색을 받지 않고 회색으로 그린다.
 */
public record ClusterColor(int number) {

    private static final int NONE = -1;

    public ClusterColor {
        if (number < NONE) {
            throw new IllegalArgumentException("Color number cannot be negative: " + number);
        }
    }

    public static ClusterColor numbered(int number) {
        return new ClusterColor(number);
    }

    public static ClusterColor neutral() {
        return new ClusterColor(NONE);
    }

    public boolean isNeutral() {
        return number == NONE;
    }
}
