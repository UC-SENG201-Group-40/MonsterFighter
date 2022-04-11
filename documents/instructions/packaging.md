# Packaging

## Packaging to a jar file

Gradle also have capabilities to compile files into `jar` to be runnable under any computer with a JVM.

### Compiling to jar

```shell
./gradlew jar
```

The output folder will be [`app/build/libs`](/app/build/libs).

### Running the jar file

```shell
java -jar ./app/build/libs/app.jar
```