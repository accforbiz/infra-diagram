package org.ruscayn.infradiagram.layout;

/**
 * 바닥의 한 자리에 선 기둥이다. 화면으로 옮기기 전의 값이며, height는 화면에서 보이는 높이다.
 */
record Standing(GroundPoint spot, double radius, double height) {

    Standing at(GroundPoint newSpot) {
        return new Standing(newSpot, radius, height);
    }

    double distanceTo(Standing other) {
        return Math.hypot(spot.x() - other.spot.x(), spot.y() - other.spot.y());
    }

    boolean inFrontOf(Standing other) {
        return spot.y() > other.spot.y();
    }
}
