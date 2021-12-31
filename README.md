# Electric Brain

A very early days exploration of kotlin multiplatform making a hybrid desktop and android app.

Uses a [DGS](https://netflix.github.io/dgs/) based graphql backend with apollo kotlin in the common UI.  

## Running

- Start the backend (requires mongodb running on mongodb://localhost:27001): `./gradlew backend:bootRun`
- Run the desktop app (make sure you don't have a headless jdk!): `./gradlew desktop:run`
- Android Emulator Run (TODO: Needs to proxy back to the backend in the emulator)
