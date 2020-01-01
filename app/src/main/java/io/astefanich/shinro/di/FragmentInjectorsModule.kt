package io.astefanich.shinro.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.astefanich.shinro.ui.AboutFragment
import io.astefanich.shinro.ui.GameFragment
import io.astefanich.shinro.ui.InstructionsFragment
import io.astefanich.shinro.ui.TitleFragment

@Module
abstract class FragmentInjectorsModule {

    @ContributesAndroidInjector
    abstract fun providesTitleFragment(): TitleFragment

    @ContributesAndroidInjector
    abstract fun providesGameFragment(): GameFragment

    @ContributesAndroidInjector
    abstract fun providesInstructionsFragment(): InstructionsFragment

    @ContributesAndroidInjector
    abstract fun providesAboutFragment(): AboutFragment
}