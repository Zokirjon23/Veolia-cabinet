package uz.veolia.cabinet.di

import org.koin.dsl.module
import uz.veolia.cabinet.data.local.ApplicationComponent


val localDataModule = module { single { ApplicationComponent.coreComponent.localStorage } }