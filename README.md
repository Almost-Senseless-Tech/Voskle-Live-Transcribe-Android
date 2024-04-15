# Voskle Live Transcribe

A simple, accessible and offline real-time transcription app.

## Yet another transcription app?

It's true that many apps are capable of transcribing speech to text; increasingly, even mobile operating systems offer options to recognize speech in the vicinity and transcribe it. In our experience, though, there usually are one or more of the following caveats:

- The transcription only works if an internet connection exists,
- The transcript's quality is too poor,
- The app isn't accessible to the deaf-blind, i.e., not optimised for braille display users,
- The user interface is too complicated due to supporting more features than plain transcription of speech,
- Continuous transcribing isn't supported (e.g., pressing a button to restart transcribing is necessary),
- Transcribing imposes a significant drain on the device's battery,
- The transcription service costs money or processes potentially sensitive audio data in the cloud.

VLT is our answer to these caveats.

## Features

- *Continuous transcription*: Once started, the transcription keeps running until stopped by the user.
- *Completely offline*: VLT uses [VOSK](https://alphacephei.com/vosk/) to recognize and transcribe the speech on the user's device.
- *Sufficiently accurate*: The utilized small [VOSK models](https://alphacephei.com/vosk/models) offer a solid compromise between size of the models, memory usage and accuracy of the transcript; using noise-cancelling external microphones can improve the transcript accuracy farther.
- *Only what's needed*: All small VOSK models (currently 26 different languages) are supported and can get downloaded as needed; they remain on the device persistently until the app gets uninstalled.
- *Free and Open-Source*: The app is licensed under the Apache license, version 2.0.
- *Accessibility front and center*: VLT is completely accessible for braille display users and supports light and dark theme; the font sizes can get adjusted directly in the app settings and a zoom of up to 200 % should not break the UI. If you encounter any accessibility issues, let us know!
- *Negligible battery usage*: Under normal circumstances, the battery drain caused by VLT should be negligible.

**Android 9+ supported.**
