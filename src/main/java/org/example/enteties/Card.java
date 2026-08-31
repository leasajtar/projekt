package org.example.enteties;


public sealed interface Card permits Cancellable {
    void cancel();
}
