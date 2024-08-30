package com.meeting.scheduler.exception;

public class CollisionDetectedException extends RuntimeException {
    public CollisionDetectedException(String message) {
        super(message);
    }
}
