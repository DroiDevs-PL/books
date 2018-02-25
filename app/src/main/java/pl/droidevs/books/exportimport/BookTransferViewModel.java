package pl.droidevs.books.exportimport;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import pl.droidevs.books.Resource;
import pl.droidevs.books.repository.DatabaseBookRepository;
import pl.droidevs.books.repository.csv.CsvBookRepository;

public final class BookTransferViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<Resource<Void>> transferResult = new MutableLiveData<>();
    private DatabaseBookRepository databaseRepository;
    private CsvBookRepository csvRepository;

    @Inject
    BookTransferViewModel(@NonNull final DatabaseBookRepository databaseRepository,
                          @NonNull final CsvBookRepository csvRepository) {
        this.databaseRepository = databaseRepository;
        this.csvRepository = csvRepository;
    }

    public void exportBooks() {
        databaseRepository.fetchAll()
                .take(1)
                .doOnSubscribe(it -> transferResult.setValue(Resource.loading()))
                .subscribe(
                        books -> csvRepository.saveAll(books).subscribe(
                                () -> transferResult.setValue(Resource.success()),
                                throwable -> transferResult.setValue(Resource.error(throwable))
                        ),
                        throwable -> transferResult.setValue(Resource.error(throwable))
                );
    }


    public void importBooks() {
        csvRepository.fetchAll()
                .take(1)
                .doOnSubscribe(it -> transferResult.setValue(Resource.loading()))
                .subscribe(
                        books -> databaseRepository.saveAll(books).subscribe(
                                () -> transferResult.setValue(Resource.success()),
                                throwable -> transferResult.setValue(Resource.error(throwable))
                        ),
                        throwable -> transferResult.setValue(Resource.error(throwable))
                );
    }


    public LiveData<Resource<Void>> getResult() {
        return transferResult;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
