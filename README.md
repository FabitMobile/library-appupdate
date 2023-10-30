# AppUpdate

[![](https://www.jitpack.io/v/FabitMobile/library-appupdate.svg)](https://www.jitpack.io/#FabitMobile/library-appupdate)

### Библиотека для проверки и инициации обновления приложения из разных источников.

содержит базовые классы для упрощения работы с обновлениями

* `ApplicationUpdateManager` с тремя
  реализациями: `GoogleApplicationUpdateManager`, `HuaweiAppUpdateManager`
  и `RuStoreApplicationUpdateManager`
* `AppVersion` с реализацией `SemanticVersion`
* `InstallerVendor` и функиця `getInstallerVendor()`
* `UpdateStatus` и функиця `checkUpdate()`

## Использование

Добавить реализации `ApplicationUpdateManager`, воспользовавшись готовыми классами

```kotlin
class GoogleApplicationUpdateManagerImpl(
    context: Context,
    private val checkUpdateUseCase: CheckUpdateUseCase,
    private val isAppInBackgroundUseCase: IsAppInBackgroundUseCase
) : GoogleApplicationUpdateManager(context) {

    override fun checkUpdate(): UpdateStatus {
        //Реализация для GooglePlay дополнительно проверяет версию по внешнему источнику (ваша собственная конфигурация)
        //Если это не требуется, можно сразу вернуть желаемый статус
        return checkUpdateUseCase.build(InstallerVendor.googlePlay)
    }

    override fun isAppInBackground(): Boolean {
        //Для запуска начала обновления можно дополнительно проверить находится ли приложение в фоне, чтобы сразу начать установку
        return isAppInBackgroundUseCase.build()
    }
}
```

Выбрать необходимый источник обновлений используя функцию библиотеки

```kotlin
val installerPackage =
    context.packageManager.getInstallerPackageName(context.applicationInfo.packageName) ?: ""
when (getInstallerVendor(installerPackage)) {
    InstallerVendor.googlePlay -> GoogleApplicationUpdateManagerImpl(...)
    else -> NoOpApplicationUpdateManager(UpdateStatus.actual)
}
```

## Подключение зависимостей

Библиотека распространяется средствами JitPack

#### Доступны несколько модулей для подключения

* Сore - чистые kotlin классы

`implementation("com.github.FabitMobile.library-appupdate:core:{latest-version}")`

* GooglePlay

`implementation("com.github.FabitMobile.library-appupdate:googleplay:{latest-version}")`

* AppGallery

`implementation("com.github.FabitMobile.library-appupdate:appgallery:{latest-version}")`

* RuStore

`implementation("com.github.FabitMobile.library-appupdate:rustore:{latest-version}")`
