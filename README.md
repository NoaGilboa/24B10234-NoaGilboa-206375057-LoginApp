

# LoginApp

   LoginApp is an Android application that requires users to enter a password based on the device's battery percentage.
      Additionally, the device's orientation and location are checked to ensure it is in portrait mode
      and in the Northern Hemisphere before granting access.

## Features

   - **Password-based on Battery Percentage**: The app verifies the entered password against the device's current battery percentage.
   - **Orientation Check**: Ensures the device is in portrait mode.
   - **Location Check**: Verifies that the device is in the Northern Hemisphere.

## Permissions

   The application requires the following permissions:
   
   - `ACCESS_FINE_LOCATION`
   - `ACCESS_COARSE_LOCATION`
   - `ACCESS_BACKGROUND_LOCATION`

## Installation

   1. Clone the repository:
      git clone https://github.com/NoaGilboa/24B10234-NoaGilboa-206375057-LoginApp.git
   
   2. Open the project in Android Studio.
   
   3. Build and run the app on an Android device or emulator.

## Usage

   1. On the main screen, enter the password which should match the current battery percentage.
   2. Ensure the device is in portrait mode.
   3. Make sure the device is located in the Northern Hemisphere.
   4. Click the "Login" button to access the Home screen.

## Video Demo

   Watch the https://drive.google.com/file/d/1bSkNL8I62iiSqShYQzYqj604v6nmMzfi/view?usp=sharing to see the app in action.

## Files

   ### MainActivity.java
   
      Main activity handling user login by checking battery percentage, orientation, and location.
   
   
   ### HomeActivity.java
   
      The activity displayed after successful login.
   
   
   ### activity_main.xml
   
      Layout for the main activity.
   
   
   ### activity_home.xml
   
      Layout for the home activity.
   
   
   ### AndroidManifest.xml
   
      Manifest file containing required permissions and activity declarations.


   ### ic_launcher_background.xml & ic_launcher_foreground.xml

      Launcher icon assets.

