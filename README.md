# GitHub Explorer

A modern Android application for exploring GitHub repositories, built with Kotlin Multiplatform (KMP) and Jetpack Compose.

![Platform](https://img.shields.io/badge/platform-Android-green)
![Kotlin](https://img.shields.io/badge/kotlin-2.0.21-purple)
![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-blue)
![Architecture](https://img.shields.io/badge/architecture-MVI-orange)

## Features

- **Repository Grid** — Browse popular repositories in a two-column grid layout
- **Search by Language** — Filter repositories by programming language (Kotlin, Rust, Python, etc.)
- **Sort Options** — Order results by stars, forks, or last updated
- **Repository Details** — View detailed information including stats, topics, and metadata
- **Dark Theme** — GitHub-inspired dark UI with smooth animations

## Screenshots

| Repositories | Detail |
|-------------|--------|
| <img width="382" height="856" alt="image" src="https://github.com/user-attachments/assets/332f0fd4-c72b-49b0-8500-fe23fcb937d3" /> | <img width="382" height="856" alt="image" src="https://github.com/user-attachments/assets/65bda073-ca11-47ba-8598-65cef7b57619" /> |

## Architecture

The project follows **Clean Architecture** principles with **MVI** (Model-View-Intent) pattern:

```
├── app/                          # Android application module
│   ├── core/
│   │   ├── presentation/         # Base MVI classes
│   │   └── theme/                # Compose theme (colors, typography)
│   ├── di/                       # Koin dependency injection
│   └── presentation/
│       ├── navigation/           # Compose Navigation
│       ├── screens/              # UI screens
│       │   ├── repositories/     # Main grid screen
│       │   └── detail/           # Repository detail screen
│       └── compose/              # Reusable composables
│
├── shared/                       # Kotlin Multiplatform module
│   ├── data/
│   │   ├── api/                  # Ktor HTTP client
│   │   ├── dto/                  # Domain models
│   │   └── mapper/               # Response to domain mappers
│   ├── model/                    # API response models
│   ├── repository/               # Data repository
│   └── usecase/                  # Business logic
│
└── build-logic/                  # Gradle convention plugins
```

## Tech Stack

### Android
- **Jetpack Compose** — Declarative UI toolkit
- **Material 3** — Modern design system
- **Compose Navigation** — Type-safe navigation
- **Coil** — Image loading
- **Koin** — Dependency injection

### Shared (KMP)
- **Ktor Client** — Multiplatform HTTP client
- **Kotlinx Serialization** — JSON parsing
- **Kotlinx Coroutines** — Async operations with Flow

### Build
- **Gradle Convention Plugins** — Reusable build logic
- **Version Catalog** — Centralized dependency management

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34

### Build & Run

```bash
# Clone the repository
git clone https://github.com/your-username/github-explorer.git

# Open in Android Studio and sync Gradle

# Run on device/emulator
./gradlew :app:installDebug
```

### Run Tests

```bash
# Unit tests for app module
./gradlew :app:testDebugUnitTest

# Unit tests for shared module
./gradlew :shared:testDebugUnitTest

# All tests
./gradlew test
```

## API

The app uses the [GitHub REST API](https://docs.github.com/en/rest) to fetch repository data:

| Endpoint | Description |
|----------|-------------|
| `GET /search/repositories` | Search repositories with filters |
| `GET /repos/{owner}/{repo}` | Get repository details |
| `GET /users/{username}/repos` | List user repositories |

## Project Structure

### MVI Pattern

```kotlin
// Intent - User actions
sealed class GithubIntent {
    data class LoadRepositories(val page: Int = 1) : GithubIntent()
    data class SearchByLanguage(val language: String) : GithubIntent()
    data class ChangeSortOption(val sortOption: SortOption) : GithubIntent()
}

// State - UI state
data class GithubState(
    val repositoriesResult: ViewResource<SearchResult>? = null,
    val selectedRepository: ViewResource<Repo>? = null,
    val searchLanguage: String = "",
    val sortOption: SortOption = SortOption.STARS
)

// ViewModel processes intents and updates state
class GithubViewModel(useCase: GithubReposUseCase) : BaseMviViewModel<...>() {
    override fun intent(intent: GithubIntent) { ... }
}
```

### Dependency Injection

```kotlin
object DI {
    private val presentation = module {
        viewModel { GithubViewModel(useCase = get()) }
    }
    
    val modules = listOf(presentation, AndroidDI.shared)
}
```

## Modules

| Module | Description |
|--------|-------------|
| `:app` | Android application with Compose UI |
| `:shared` | KMP module with business logic |
| `:build-logic` | Gradle convention plugins |

## Testing

The project includes unit tests for:

- **Mappers** — Data transformation logic
- **Use Cases** — Business logic delegation
- **ViewModels** — State management and intents
- **Utility Functions** — Formatting and helpers

```kotlin
@Test
fun loadRepositories_emitsSuccess() = runTest {
    viewModel.intent(GithubIntent.LoadRepositories())
    advanceUntilIdle()
    
    assertTrue(viewModel.currentState.repositoriesResult is ViewResource.Success)
}
```

## License

```
MIT License

Copyright (c) 2024

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

