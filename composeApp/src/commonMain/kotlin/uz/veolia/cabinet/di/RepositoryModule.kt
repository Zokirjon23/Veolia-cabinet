package uz.veolia.cabinet.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import uz.veolia.cabinet.domain.repository.AppRepository
import uz.veolia.cabinet.repository.impl.AppRepositoryImpl

val repositoryModule = module {
    factoryOf(::AppRepositoryImpl) bind AppRepository::class
}