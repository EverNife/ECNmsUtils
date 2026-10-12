# ECNmsUtils

A small bridge to the Minecraft server internals (NMS) of the CraftBukkit revisions a FinalCraft server runs,
from 1.7.10 to 1.21.1, behind one contract that does not change between them. EverNifeCore embeds it, so a
plugin that depends on `evernifecore-minecraft` already has it on its classpath.

| Revision   | Releases            | Ore dictionary                    |
|------------|---------------------|-----------------------------------|
| `V1_7_R4`  | 1.7.10              | Forge `OreDictionary`             |
| `V1_12_R1` | 1.12 - 1.12.2       | Forge `OreDictionary`             |
| `V1_16_R3` | 1.16.4, 1.16.5      | item tags                         |
| `V1_20_R1` | 1.20, 1.20.1        | item tags                         |
| `V1_20_R2` | 1.20.2              | item tags                         |
| `V1_21_R1` | 1.21, 1.21.1        | item tags                         |

## Usage

```java
import br.com.finalcraft.ecnmsutils.NMSUtils;

if (NMSUtils.isSupported()) {
    String id = NMSUtils.get().items().getRegistryName(itemStack);           // "minecraft:stone"
    ItemStack two = NMSUtils.get().items().fromRegistryName(id, 2, 0);
    Object handle = NMSUtils.get().entities().asMinecraft(player);
    List<String> names = NMSUtils.get().registries().oreDictionary().getOreNamesFrom(itemStack);
}
```

- `NMSUtils.get()` is never `null`. On a server it has no bridge for, every subject throws
  `UnsupportedOperationException` saying which server it saw; `isSupported()` is the check, `revision()` names the
  revision being served.
- Subjects: `items()` (registry name, localized name, Bukkit/Minecraft conversion, handle validation, tool/sword/armor
  checks), `entities()` (registry name, conversion, Forge/NeoForge fake player), `worlds()` (conversion) and
  `registries().oreDictionary()`.
- `items().asBukkit(...)` is a mirror of the Minecraft stack on 1.7.10 and a copy everywhere else.
- The ore dictionary answers in the names of the running revision: `logWood`, `ingotCopper` on the Forge revisions,
  `minecraft:logs`, `forge:ingots/copper`, `c:ingots/copper` on the tag ones. Nothing translates between them.

## Building

Gradle 9.5.1 on JDK 25. Every module compiles to Java 8 bytecode against the Java 8 API.

```powershell
$env:JAVA_HOME = "<your JDK 25>"
.\gradlew build                    # compiles, tests, and writes ecnmsutils/build/libs/ECNmsUtils-<version>.jar
.\gradlew publishToMavenLocal
```

The CraftBukkit jars are in no public repository; the build reads them from the directories listed in the
`ECNMSUTILS_CRAFTBUKKIT_LIBS` environment variable (separated by `;` on Windows, `:` elsewhere) and fails naming
it when it is unset: `craftbukkit1710`, `craftbukkit1122`, `craftbukkit1165`, `craftbukkit1201`, `craftbukkit1202` and
`craftbukkit1211`. `everylibs-reflection` resolves from mavenLocal.

## Layout

```
modules/common        br.com.finalcraft.ecnmsutils - NMSUtils, the contract, NmsRevision, the shared ore dictionaries
modules/pre_v1_20     v1_7_R4, v1_12_R1, v1_16_R3
modules/v1_20_1       v1_20_R1, v1_20_R2
modules/v1_21_1       v1_21_R1
modules/api-contracts compile-time stand-ins for Mojang classes the CraftBukkit jars reference; never packaged
ecnmsutils            packaging: the published shadow jar of the four modules above
```

Each revision is a package with one class per subject (`ItemsV1_21_R1`, `EntitiesV1_21_R1`, `WorldsV1_21_R1`) and a
`NmsV1_21_R1` that composes them. `NmsRevision` loads that class by name, so a new revision is a new package plus
one enum constant.

## Dependencies

The published jar declares none. At runtime it needs the server's Bukkit API and
`br.com.finalcraft.everylibs:everylibs-reflection`, both of which EverNifeCore provides.
