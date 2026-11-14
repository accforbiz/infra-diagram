package org.ruscayn.infradiagram.scene;

import java.util.List;

public record Box(double left, double top, double width, double height) {

    public static Box between(ScreenPoint first, ScreenPoint second) {
        double left = Math.min(first.x(), second.x());
        double top = Math.min(first.y(), second.y());
        return new Box(left, top, Math.abs(first.x() - second.x()), Math.abs(first.y() - second.y()));
    }

    public static Box around(List<Box> boxes) {
        return boxes.stream().reduce(Box::union).orElseThrow();
    }

    public double right() {
        return left + width;
    }

    public double bottom() {
        return top + height;
    }

    public double centerY() {
        return top + height / 2;
    }

    public boolean overlaps(Box other) {
        return left < other.right() && other.left < right() && top < other.bottom() && other.top < bottom();
    }

    public boolean contains(Box other) {
        return left <= other.left && other.right() <= right() && top <= other.top && other.bottom() <= bottom();
    }

    public Box union(Box other) {
        double newLeft = Math.min(left, other.left);
        double newTop = Math.min(top, other.top);
        double newRight = Math.max(right(), other.right());
        double newBottom = Math.max(bottom(), other.bottom());
        return new Box(newLeft, newTop, newRight - newLeft, newBottom - newTop);
    }

    public Box moved(double dx, double dy) {
        return new Box(left + dx, top + dy, width, height);
    }

    public Box shrunkBy(double margin) {
        return new Box(left + margin, top + margin, width - 2 * margin, height - 2 * margin);
    }
}
