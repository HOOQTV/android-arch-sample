package com.imadji.arch.sample;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.imadji.arch.domain.model.Movie;
import com.imadji.arch.domain.usecase.GetPopularMovies;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "coba-rx";

    @Inject
    GetPopularMovies getPopularMovies;

    @BindView(R.id.rootView)
    ConstraintLayout constraintLayout;
    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainApplication) getApplication()).getMainComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        textView.setText("App Version: " + BuildConfig.VERSION_NAME);

//        Observable.just(1, 2, 3, 4, 5)
//                .subscribeOn(Schedulers.io())
//                .doOnNext(integer -> Log.d(TAG, "Emitting item on: " + Thread.currentThread().getName()))
//                .map(integer -> {
//                    Log.d(TAG, "Processing item on: " + Thread.currentThread().getName());
//                    return integer * 2;
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableObserver<Integer>() {
//                    @Override
//                    public void onNext(@NonNull Integer integer) {
//                        Log.d(TAG, "Consuming item " + integer + " on: " + Thread.currentThread().getName());
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });

        getPopularMovies.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Movie>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Movie> movies) {
                        Log.d(TAG, "onSuccess " + movies.size());
                        Snackbar.make(constraintLayout, "Result size is " + movies.size(), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

//        Disposable disposable = homeInteractor.getPopularMovies().toFlowable()
//                .subscribeOn(Schedulers.io())
//                .flatMapIterable(movies -> movies)
//                .flatMap(Flowable::just)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(movie -> Log.d(TAG, movie.toString()),
//                        throwable -> throwable.printStackTrace());

//        Observable.create((ObservableOnSubscribe<String>) emitter -> {
//            throw new IllegalArgumentException();
//        }).onErrorResumeNext(throwable -> {
//            sendAnalytics(throwable);
//            return Observable.just("A", "B", "C");
//        }).subscribe(new DefaultObserver<String>() {
//            @Override
//            public void onNext(String s) {
//                Log.d(TAG, "onNext! " + s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete!");
//            }
//        });

//        Single.create((SingleOnSubscribe<String>) emitter -> {
//            throw new IllegalArgumentException("X");
//        }).onErrorResumeNext(throwable -> {
////            sendAnalytics(throwable);
//            return Single.error(new NetworkErrorException());
//        }).subscribe(new SingleObserver<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                Log.d(TAG, "onSuccess " + s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//        });

    }

//    private void sendAnalytics(Throwable e) {
//        Log.d("ADJI", "ERROR!!!!");
//        e.printStackTrace();
//    }
}
