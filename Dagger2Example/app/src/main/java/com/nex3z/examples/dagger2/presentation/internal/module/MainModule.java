package com.nex3z.examples.dagger2.presentation.internal.module;

import com.nex3z.examples.dagger2.domain.interactor.MovieInteractor;
import com.nex3z.examples.dagger2.domain.interactor.MovieInteractorImpl;
import com.nex3z.examples.dagger2.presentation.internal.PerActivity;
import com.nex3z.examples.dagger2.presentation.presenter.MainPresenter;
import com.nex3z.examples.dagger2.presentation.presenter.MainPresenterImpl;
import com.nex3z.examples.dagger2.data.rest.service.MovieService;
import com.nex3z.examples.dagger2.presentation.view.MovieGridView;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {
    private MovieGridView mMovieGridView;

    public MainModule(MovieGridView movieGridView) {
        mMovieGridView = movieGridView;
    }

    @Provides @PerActivity
    MovieGridView provideMovieGridView() {
        return mMovieGridView;
    }

    @Provides @PerActivity
    MovieInteractor provideMovieInteractor(MovieService movieService) {
        return new MovieInteractorImpl(movieService);
    }

    @Provides @PerActivity
    MainPresenter provideMainPresenter(MovieGridView movieGridView,
                                       MovieInteractor movieInteractor) {
        return new MainPresenterImpl(movieGridView, movieInteractor);
    }
}