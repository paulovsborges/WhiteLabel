package br.com.douglasmotta.whitelabeltutorial.config.di

import br.com.douglasmotta.whitelabeltutorial.ConfigImpl
import br.com.douglasmotta.whitelabeltutorial.config.Config
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ConfigModule {

    @Binds
    fun bindConfig(config: ConfigImpl): Config
}