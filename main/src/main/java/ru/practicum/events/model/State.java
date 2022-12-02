package ru.practicum.events.model;

public enum State {
    PENDING,
    PUBLISHED,
    REJECTED,
    CANCELED;

    public static State from(String text) {
        for (State state : State.values()) {
            if (state.name().equalsIgnoreCase(text)) {
                return state;
            }
        }
        return null;
    }
}
