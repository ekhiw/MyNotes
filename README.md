# MyNotes

Android task app. Basic CRUD operations with filtering.

## Tech Stack

- Kotlin + Jetpack Compose 
- Room database
- MVVM + Hilt DI
- Coroutines

## Features

- Add/edit/delete tasks
- Mark complete/pending  
- Filter by status (All/Completed/Pending)
- Task counters

## Architecture Notes

Filter logic is implemented at Room DAO level instead of processing lists in ViewModel. Trade-off: slightly more complex queries but better performance for large datasets and reactive updates via Flow.

## Build

```bash
./gradlew build
./gradlew installDebug
```

## Testing

```bash
./gradlew test
```

Test coverage includes:
- TaskViewModelTest - business logic with MockK
- TaskDaoTest - database operations with in-memory Room
