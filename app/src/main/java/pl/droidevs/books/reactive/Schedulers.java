package pl.droidevs.books.reactive;

import io.reactivex.Scheduler;

public interface Schedulers {
    Scheduler getSubscriber();

    Scheduler getObserver();
}
