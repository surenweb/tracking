package fastLibrary;

public interface FastGpsListener {
    void onLocationChangedEvent();
    void onTrackAddedEvent();
    // or void onEvent(); as per your need
}