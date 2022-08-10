package com.lol.di

import android.content.Context
import com.data.lol.repository.DataDragonApiRepositoryImpl
import com.data.lol.repository.LocalDatastore
import com.data.lol.repository.RiotApiRepositoryImpl
import com.data.lol.service.DataDragonApiService
import com.data.lol.service.RiotApiService
import com.domain.lol.repository.DataDragonApiRepository
import com.domain.lol.repository.RiotApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideRiotApiRepository(
        riotApiService: RiotApiService
    ): RiotApiRepository = RiotApiRepositoryImpl(riotApiService)

    @Provides
    fun provideDataDragonApiRepository(
        dataDragonApiService: DataDragonApiService
    ): DataDragonApiRepository = DataDragonApiRepositoryImpl(dataDragonApiService)

//    @Provides
//    fun provideDatastore(
//        datastore: LocalDatastore
//    ): DatastoreRepository = DatastoreRepositoryImpl(datastore)

    @Provides
    fun provideLocalDatastore(
        @ApplicationContext
        context: Context
    ): LocalDatastore = LocalDatastore(context)
}