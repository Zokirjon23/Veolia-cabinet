package uz.veolia.cabinet.di

import org.koin.dsl.koinApplication
//import org.koin.dsl.module

//val appModule = module {
//    includes(
//
//    )
//}


fun koinConfiguration() = koinApplication {
    modules(
        listOf(
            networkModule, localDataModule, networkModule,
            repositoryModule
        )
    )
}