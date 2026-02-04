# RAWG Games App

The application displays a list of games by selected genres from the RAWG backend.  
It is built using Android Compose, ViewModel, Room, Pagination3, Navigation3, Retrofit, Hilt, Coil, Firebase, Mockk, Turbine, Detekt, and other modern libraries.

---

## Screenshots

### Phone
| Games | Game Details | Genres |
|---------|----------|----------|
| ![](https://raw.githubusercontent.com/zsasko/rawg/main/docs/images/phone_games.png) | ![](https://raw.githubusercontent.com/zsasko/rawg/main/docs/images/phone_game_details.png) | ![](https://raw.githubusercontent.com/zsasko/rawg/main/docs/images/phone_genres.png) |

### Tablet
| Game & Game Details |
|--------|
| ![](https://raw.githubusercontent.com/zsasko/rawg/main/docs/images/tablet.png) |

---

## Installation Instructions

To run the app, make sure to add a RAWG API key and create a Firebase project (for Analytics and RemoteConfig):

1. Register your account at [RAWG](https://rawg.io) and get an API key.
2. Add your RAWG API key to `local.properties` (located in the root of the project) under the key `RAWG_API_KEY`, like this:

        gradle RAWG_API_KEY="your_key_here"

3. Create a Firebase application and add the property `show_app_version_label` in Firebase RemoteConfig.  
   ![Firebase RemoteConfig](https://raw.githubusercontent.com/zsasko/rawg/main/docs/images/firebase_remote_config.png)
4. Ensure that the package name in your Firebase project matches the package name of this project.
5. Add the `google-services.json` file to the project.

---

## Implementation Details

- **Architecture:** MVI pattern where each ViewModel contains a `handleIntent` method that executes business logic.
    - `StateFlow` is used to notify the UI of data updates.
    - Screens display appropriate layouts based on state (error, success, loading).
- **Genre Selection:** Selected genres are saved in the local Room database.
    - Clicking a genre automatically updates its selected/deselected state.
- **Offline Handling:** Displays an error layout if data cannot be fetched due to no internet connection.
- **Loading Indicators:** Shown while data is being loaded.
- **Responsive Layout:** Supports both phones and tablets.
    - Tablets in landscape mode show an app rail; otherwise, a navigation drawer is used.
    - Tablets in landscape mode display a list-details pane showing the game list and the first (or last selected) game details.
- **Settings Screen:** Displays the app version at the bottom, configurable via Firebase RemoteConfig.
- **Analytics:** Screen views are tracked in Google Analytics.
- **Pagination:** Uses the `pagination3` library for automatic pagination in the main games screen.
- **Navigation:** Uses the `navigation3` library with list-details screen strategies.
- **Static Analysis:** The `detekt` plugin is integrated with a generated `baseline.xml`. Running `./gradlew detekt` displays issues.
- **Testing:** Unit tests are implemented for repositories and ViewModels.
- **Dependency Injection & Networking:** Hilt for DI, OkHttp and Retrofit for networking.

---

## Libraries & Tools Used

- **UI & Architecture:** Android Compose, MVI, ViewModel
- **Persistence:** Room
- **Networking:** Retrofit, OkHttp
- **Dependency Injection:** Hilt
- **Image Loading:** Coil
- **Pagination & Navigation:** Pagination3, Navigation3
- **Testing:** Mockk, Turbine
- **Analytics & Config:** Firebase
- **Static Analysis:** Detekt  

