# Changelog

All notable changes to this project. Follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/) and
[Semantic Versioning](https://semver.org/).

Published as `br.com.finalcraft:ECNmsUtils`.

## [2.0.0] - 2026-10-08

ECNmsUtils becomes a library of its own: it no longer compiles against EverNifeCore, and its bridge is split by
subject - items, entities, worlds and registries - with one class per subject per revision.

### Changed

- **Package and entry point.** Everything moved from `br.com.finalcraft.evernifecore.minecraft.nms` to
  `br.com.finalcraft.ecnmsutils`. The entry point is `br.com.finalcraft.ecnmsutils.NMSUtils` (it used to live in the
  core), and the contract is reached by subject: `NMSUtils.get().items()`, `.entities()`, `.worlds()`, `.registries()`.

  | 1.x (`INMSUtils`)                               | 2.0.0                                                   |
  |-------------------------------------------------|---------------------------------------------------------|
  | `getItemRegistryName(item)`                     | `items().getRegistryName(item)`                         |
  | `getLocalizedName(item)`                        | `items().getLocalizedName(item)`                        |
  | `asBukkitItemStack(o)` / `asMinecraftItemStack(i)` | `items().asBukkit(o)` / `items().asMinecraft(i)`     |
  | `validateItemStackHandle(item)`                 | `items().validateHandle(item)`                          |
  | `isTool/isSword/isArmor/isHelmet/isChestplate/isLeggings/isBoots(item)` | `items().` same names         |
  | `getItemFromMinecraftIdentifier("<id> <amount> <meta> {nbt}")` | `items().fromRegistryName(id, amount, meta)` - the string grammar and the `{nbt}` part belong to the caller |
  | `getEntityRegistryName(e)`                      | `entities().getRegistryName(e)`                         |
  | `asBukkitEntity(o)` / `asMinecraftEntity(e)`    | `entities().asBukkit(o)` / `entities().asMinecraft(e)`  |
  | `isFakePlayer(p)`                               | `entities().isFakePlayer(p)`                            |
  | `asBukkitWorld(o)` / `asMinecraftWorld(w)`      | `worlds().asBukkit(o)` / `worlds().asMinecraft(w)`      |
  | `getOreRegistry()` (`IMCOreRegistry`)           | `registries().oreDictionary()` (`NmsOreDictionary`)     |

- **`NMSUtils.get()` is never `null`.** On a server with no bridge it returns one whose subjects throw
  `UnsupportedOperationException` naming the server it saw and the revisions served. Branch on
  `NMSUtils.isSupported()` where `get() != null` used to be checked; `NMSUtils.revision()` names the revision served.
- **The revision is resolved by the library** (`NmsRevision.resolve`), from the CraftBukkit package and
  `Bukkit.getBukkitVersion()`: a relocated package decides alone, the plain package of 1.20.5+ falls back to the
  reported release. Only the exact releases of each revision are served (1.21.4 used to load the 1.21.1 bridge).
- **The ore dictionary exists on every revision.** The Forge `OreDictionary` on 1.7.10 and 1.12.2, item tags
  (`minecraft:logs`, `forge:ingots/copper`, `c:ingots/copper`) from 1.16.5 on, always in the names of the running
  revision. A server with neither (plain Spigot 1.12.2) answers empty. The 1.16.5 placeholder that listed the first
  300 items is gone.
- **`getLocalizedName` on 1.20.x and 1.21.1** returns the item's name; it returned `"null"` (it read the tooltip
  image instead).
- **Read queries accept any stack.** `getRegistryName` and the `is*` checks work on a plain Bukkit `ItemStack` too, not
  only on a `CraftItemStack`; air answers `false`.
- **`isFakePlayer`** recognises the NeoForge `FakePlayer` as well as the Forge one.
- `OreDictEntry` is a plain final class (`getOreName()`, equality by name).
- **Build**: Gradle 9.5.1, one JDK 25 toolchain, Java 8 bytecode in every module (the 1.21.1 module was major 55).
  The published artifact declares no dependency.

### Removed

- `serializeItemStack` - NBT-API already reads an item's NBT.
- `autoRespawnOnDeath` - `Player.spigot().respawn()` does it on every supported server.
- `getBlockRegistry`, `getItemRegistry` and the types behind them (`IMCMaterialRegistry`, `IMcBlockWrapper`,
  `IMcItemWrapper`, `IHasMinecraftIdentifier`).
- The dependency on `evernifecore-minecraft`, the logger and the `isModded()` checks. Failures are exceptions.
- The public static `instance` field of each implementation, and the `ECNmsUtils` marker class.

### Added

- `NMSUtils.isSupported()`, `NMSUtils.revision()` and `NmsRevision`.
- Unit tests: revision resolution for every revision by both strings, the unsupported bridge, `OreDictEntry`, and a
  check that every revision's bridge class is in the packaged jar.
