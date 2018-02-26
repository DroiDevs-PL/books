package pl.droidevs.books.exportimport;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import pl.droidevs.books.Resource;
import pl.droidevs.books.repository.DatabaseBookRepository;
import pl.droidevs.books.repository.csv.CsvBookRepository;
import pl.droidevs.books.ui.RxViewModel;

public final class BookTransferViewModel extends RxViewModel {

    private final MutableLiveData<Resource<Void>> exportResult = new MutableLiveData<>();
    private final MutableLiveData<Resource<Void>> importResult = new MutableLiveData<>();
    private DatabaseBookRepository databaseRepository;
    private CsvBookRepository csvRepository;

    @Inject
    BookTransferViewModel(@NonNull final DatabaseBookRepository databaseRepository,
                          @NonNull final CsvBookRepository csvRepository) {
        this.databaseRepository = databaseRepository;
        this.csvRepository = csvRepository;
    }

    public LiveData<Resource<Void>> exportBooks() {
        add(databaseRepository.fetchAll()
                .take(1)
                .doOnSubscribe(it -> exportResult.setValue(Resource.loading()))
                .subscribe(
                        books -> add(csvRepository.saveAll(books).subscribe(
                                () -> exportResult.setValue(Resource.success()),
                                throwable -> exportResult.setValue(Resource.error(throwable)))
                        ),
                        throwable -> exportResult.setValue(Resource.error(throwable))
                )
        );

        return exportResult;
    }

    public LiveData<Resource<Void>> importBooks() {
        add(csvRepository.fetchAll()
                .take(1)
                .doOnSubscribe(it -> importResult.setValue(Resource.loading()))
                .subscribe(
                        books -> add(databaseRepository.saveAll(books).subscribe(
                                () -> importResult.setValue(Resource.success()),
                                throwable -> importResult.setValue(Resource.error(throwable)))
                        ),
                        throwable -> importResult.setValue(Resource.error(throwable))
                )
        );

        return importResult;
    }
}