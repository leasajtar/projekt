package org.example.entities;


public sealed interface Card permits Cancellable {
    void cancel();
}
