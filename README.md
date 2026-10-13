# Deadly World

Fabric mod za Minecraft Java 26.3.

- **Tick rate**: server radi na 40 TPS (konfigurabilno, 0 = ne dira).
- **Steta cudovista x1.2** prema igracu (konfigurabilno), pokriva i projektile i eksplozije.
- **Skaliranje kroz vreme**: na svakih X realnih minuta cudovista dobijaju +Y% zdravlja (pri spawnu) i +Y% stete (pri udarcu), sa gornjom granicom.
- **Smrt skracuje zivot**: svaka smrt oduzima pola srca maksimalnog zdravlja, do minimuma.

## Pokretanje

Potreban je JDK 25.

1. Generisi prazan projekat na https://fabricmc.net/develop/template/ (Minecraft 26.3) da dobijes Gradle wrapper.
    Zatim iz ovog foldera prekopiraj `src/`, `build.gradle` i `gradle.properties` u generisani projekat,
    tako da zamene istoimene fajlove i folder tamo. Alternativno, pokreni `gradle wrapper` u ovom folderu
    ako imas Gradle 9.6.0.
2. `./gradlew runServer` (ili `runClient`) za testiranje.
3. `./gradlew build` pravi jar u `build/libs/`.

## Instalacija u Minecraft

1. Instaliraj Fabric Loader za Minecraft 26.3 preko Fabric Installer-a. Installer ce napraviti Fabric profil
    u Minecraft Launcher-u.
2. Preuzmi Fabric API jar i kopiraj ga u `mods` folder.
3. Iz `build/libs/` kopiraj `deadly-world-1.0.0.jar` u isti `mods` folder. Ne kopiraj fajl sa `-sources` u imenu.
    Na Windows-u se `mods` folder nalazi na `%appdata%\.minecraft\mods`.
4. U Minecraft Launcher-u izaberi Fabric profil za Minecraft 26.3 i pokreni igru.

Za samo igranje nije potreban JDK 25; on je potreban samo za build moda.

## Pokretanje servera

Za server koristi `fabric-server-launch.jar` i u njegov `mods` folder kopiraj Fabric API jar i
`deadly-world-1.0.0.jar` iz `build/libs/`.

## Podesavanja

Pri prvom pokretanju nastaje `config/deadly_world.json`. Vrednosti su opisane u `DeadlyConfig.java`.

## Napomena o tick rate-u

Pri 40 TPS sve u igri radi duplo brze u realnom vremenu, ukljucujuci igraca, biljke i redstone.
Interval skaliranja se racuna u realnim minutima preko podesenog tick rate-a.
