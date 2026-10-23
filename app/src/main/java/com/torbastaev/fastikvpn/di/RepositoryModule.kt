package com.torbastaev.fastikvpn.di

import com.torbastaev.fastikvpn.data.repository.IpRepositoryImpl
import com.torbastaev.fastikvpn.data.repository.SettingsRepositoryImpl
import com.torbastaev.fastikvpn.domain.repository.IpRepository
import com.torbastaev.fastikvpn.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository
    @Binds
    @Singleton
    abstract fun bindIpRepository(
        impl: IpRepositoryImpl
    ): IpRepository
}