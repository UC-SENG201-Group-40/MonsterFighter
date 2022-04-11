# Running Gradle project using the CLI

## Just running 
```shell
./gradlew run
```

## Installing to the local machine

### Into zip
```shell
./gradlew distZip
```

### Into tar
```shell
./gradlew distTar
```

### Into an executable as-is
```shell
./gradlew installDist
```

### Into jar

[Compiling to jar](packaging.md)

## Testing

```shell
./gradlew test
```

## Compile

```shell
./gradlew build
```

## Cleaning the build

This is useful is some undefined behavior

```shell
./gradlew clean
```
