<div align="center">
  <img
    src="src/main/resources/com/github/mskocz/vmgui/gui/VM-Gui.svg"
    alt="VM Gui"
    width="500"
  />
</div>

<div align="center">
  <a href="https://opensource.org/licenses/MIT">
    <img src="https://badgen.net/badge/license/MIT/green" alt="MIT License" />
  </a>

  <a href="https://github.com/michalskocz/VMGui">
    <img src="https://badgen.net/badge/status/in%20progress/orange" alt="Status: in progress" />
  </a>

  <a href="https://github.com/michalskocz/VMGui/releases">
    <img src="https://badgen.net/badge/version/1.0/blue" alt="Version 1.0" />
  </a>

  <a href="https://github.com/michalskocz/VMGui/stargazers">
    <img src="https://badgen.net/github/stars/michalskocz/VMGui" alt="Stars" />
  </a>

  <a href="https://github.com/michalskocz/VMGui/forks">
    <img src="https://badgen.net/github/forks/michalskocz/VMGui" alt="Forks" />
  </a>

  <a href="https://github.com/michalskocz/VMGui/issues">
    <img src="https://badgen.net/github/issues/michalskocz/VMGui" alt="Issues" />
  </a>
</div>

---

## Overview

<div align="center">
  <p>
    <b>VM Gui</b> is a project that aims to simplify virtual machine management
    through a graphical interface similar to
    <a href="https://www.netacad.com/cisco-packet-tracer">Cisco Packet Tracer</a>
    / <a href="https://www.gns3.com/">GNS3</a>.
  </p>
</div>

## Build

To quickly build and run the project, clone the repository along with the submodules and run the Gradle build:

```bash
git clone --recurse-submodules https://github.com/michalskocz/VMGui.git && cd VMGui
./gradlew shadowJar
java -jar build/libs/VMGui-1.0-all.jar
````

If you're building multiple times add the flag below to skip converting icons to PNG after the first build:

```bash
./gradlew shadowJar -PskipSvgConvert
```

## Requirements and Dependencies

* Gradle
* Java 17
* All project dependencies are listed in [libs.versions.toml](gradle/libs.versions.toml)
* The project uses [Octicons](https://github.com/primer/octicons) for its icons


