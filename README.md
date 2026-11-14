# Home Screen — Android TV launcher

A simple remote-friendly Android TV home screen. It lists installed TV apps and opens them with the remote.

## Build the APK using GitHub

1. Create a new GitHub repository named `home-screen`.
2. Upload **all files and folders in this ZIP** to the repository (keep `.github/workflows/build-apk.yml` in that exact location).
3. Open the repository's **Actions** tab.
4. Select **Build Home Screen APK** and press **Run workflow**. You can also wait for the first push build to finish.
5. Open the completed workflow run, scroll to **Artifacts**, and download `Home-Screen-APK`.
6. Extract the downloaded artifact. It contains `app-debug.apk`.

## Install and choose it as the home screen

1. Copy `app-debug.apk` to your Android TV box and install it. If blocked, allow installation from that file manager.
2. Press the remote's **Home** button.
3. Choose **Home Screen**. If Android asks, select **Always** to make it the default launcher.
4. To switch back, go to Settings → Apps → Default apps → Home app (wording varies by Android version).

## Notes

- This is a starter launcher, not a full Google TV clone.
- It automatically lists apps that Android exposes as TV/launcher apps.
- Some boxes expose only a limited list of apps to third-party launchers.
- The APK is built by GitHub Actions; GitHub Pages is not needed.
