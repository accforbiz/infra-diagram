package org.ruscayn.infradiagram.model;

public record Link(String firstId, String secondId) {

    public boolean joinsElementToItself() {
        return firstId.equals(secondId);
    }

    public boolean joinsSameElementsAs(Link other) {
        return sameDirection(other) || oppositeDirection(other);
    }

    private boolean sameDirection(Link other) {
        return firstId.equals(other.firstId) && secondId.equals(other.secondId);
    }

    private boolean oppositeDirection(Link other) {
        return firstId.equals(other.secondId) && secondId.equals(other.firstId);
    }
}
