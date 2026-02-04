Application displays a list of games by selected genres from RAWG backend.

# Implementation details

- App architecture is MVI where each viewmodel contains 'handleIntent' method which receives an intent and does some business logic. StateFlow is used for notifying UI that data has been received. Screens contain their state variables based on which appropriate layouts are displayed (error, success, loading).
- Selected genres are saved in local database. When user clicks on genre, selected/deselected state is automatically saved in local Room db.
- If application is loaded without internet connection, on the screen where data is fetched from internet, an appropriate error layout is displayed.
- When data is being loaded, appropriate loading indicator is displayed.
- Application layout is made for phones and tablets. 
  - If application orientation is in landscape and app is running on tablet, an app rail is displayed, otherwise an navigation drawer is displayed.
  - In tablet with lanscape mode, an game list and details of first (or last selected game) is displayed in list-details pane.
- In the bottom of settings screen there is a label with app version which can be removed using Firebase RemoteConfig.
- Screen views are being tracked in Google Analytics
- During app development latest pagination3 library is used in main/games screen which automatically invokes pagination (and loading data from backend).
- For navigation latest navigation3 library is used with appropriate ListDetails screen strategies.
- An detect plugin is imported (and generated baseline.xml) so when ./gradlew detekt is invoked, it displays issues that it has found
- Unit tests for repositories and view models are created
- For dependency injection an Hilt library is used and for networking requests OkHttp and Retrofit are used

# Installation instructions
In order to run the app please make sure that you add a RAWG API and make Firebase project (for using Analytics and RemoteConfig):
1) Register your account in https://rawg.io and get api key.
2) Add RAWG api key in local.properties (on the root of project) in key 'RAWG_API_KEY' like:
    RAWG_API_KEY="key"
3) Make a new firebase application and add property 'show_app_version_label' in RemoteConfig (as you can see on the image: )
4) Make sure that you use the same package name in your Firebase project and this project.
4) Add google-services.json file to the project.

