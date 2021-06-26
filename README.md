# Learn Programming With Lua
This is the source code of the "Learn Programming with Lua" Android app.
It was written for the 5 points Bagrut exam about Software Engineering and Android App Development.

This app depends on the following libraries:  
- LuaJ, for running Lua code.
- Gson, Google's JSON serialization and deserialization.
- Markwon, to render Markdown code in articles and exercises within the app.
- Prism4J, to provide syntax highlighting for code in Markdown code blocks.
- Room, for the local database.
- Firebase RTDB, for the cloud storage.
- AndroidX, for various functions.

Unfortunately, due to limitations in the guidelines for the Bagrut,
this app had to be written in Java.
Otherwise, It would have been probably developed in Kotlin.

To start a tour of the code,
it is recommended to start with the following file [SplashScreenActivity.java](./app/src/main/java/net/vogman/learnprogramming/SplashScreenActivity.java) and just follow the used classes.

Licensed under the [GPLv3 license](./LICENSE).
