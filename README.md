# Minecraft Modpack Loader

With Minecraft Modpack Loader it is no longer necessary to rely on legacy Modpack Downloaders.
You can get any Modpack on any platform. **This is just a library.**

**Currently supported:**
1. Curseforge

## Getting Started

Download the Library and create your own launcher. A client is coming soon.

### Prerequisites

You can download the library on Jitpack. You may use any Maven-basend Dependency Manager you like.

Maven:
```
<groupId>de.katzen48</groupId>
<artifactId>Minecraft-Modpack-Loader</artifactId>
<version>VERSION</version>
  
<name>Jitpack/name>
<url>https://jitpack.io</url>
```

Gradle:
```
repositories {
	maven {
		url "https://jitpack.io"
	}
}

dependencies {
	compile group: 'de.katzen48', name: 'Minecraft-Modpack-Loader', version: 'VERSION'
}
```

### Installing

It is easy to fetch Modpacks e.g. from Curseforge

```
PackLoader.loadModpack(Provider.CURSE, "minecraft time", "Time 0.92 Beta"));
```

This will return a Modpack Object with all required information

## Used Libs

* [JSoup](https://jsoup.org/) - HTML Parser
* [Gson](https://github.com/google/gson) - JSON Decoder

## Contributing

Feel free to create pull requests. I will look after and test them before they get approved. I appreciate every help to expand the count of compatible platforms.

## Authors

* **Katzen48** - *Initial work* - [Katzen48](https://github.com/Katzen48)

See also the list of [contributors](https://github.com/Katzen48/Minecraft-Modpack-Loader/contributors) who participated in this project.

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE.md](LICENSE.md) file for details
