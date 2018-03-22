package pl.droidevs.books.reactive;

import io.reactivex.Scheduler;

public final class TestSchedulers implements Schedulers {
    @Override
    public Scheduler getSubscriber() {
        return io.reactivex.schedulers.Schedulers.trampoline();
    }

    @Override
    public Scheduler getObserver() {
        return io.reactivex.schedulers.Schedulers.trampoline();
    }
}
