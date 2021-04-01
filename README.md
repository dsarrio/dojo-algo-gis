# Description

Au menu un challenge sur la gestion de ressources. Vous devrez prendre le contrôle des bases ennemies tout en maintenant un niveau de troupes suffisant dans les votres pour contrer les attaques. Releverez-vous le défi ?

Le challenge est proposé par [Codingame](https://www.codingame.com/) et sera révélé en début de session. Egalement, un projet complet prêt à l'emploi est disponible en Kotlin et prend en charge pour vous la totalité du parsing, création des structures de données, tests unitaires ou encore la soumission des actions à chaque tour. Tout ce que vous avez à faire c'est de programmer la logique même de votre bot !

Vous ne souhaitez pas programmer en Kotlin ? Pas de soucis vous pouvez tout à fait utiliser l'un des nombreux langages proposés par Codingame directement. Un peu plus challengeant sur le timing mais il suffit parfois de seulement quelques lignes pour se hisser en haut du classement !

## Mise en place

Vous devez tout d'abord créer un compte sur [Codingame](https://www.codingame.com/). De manière à pouvoir vous retrouver et jouer entre vous facilement pendant la session, pensez à indiquer le nom de votre entreprise et/ou école dans votre profil.

Egalement il est nécessaire d'installer [l'extension Chrome](https://chrome.google.com/webstore/detail/codingame-sync-app/nmdombhgnofjnnaenegcdehnbkajfgbh?hl=fr) pour pouvoir envoyer et tester vos solutions directement depuis votre propre IDE. A defaut vous devrez soit travailler directement dans l'IDE en ligne soit copier/coller manuellement votre code dedans.

### Kotlin

> :bulb: Certains fichiers sont cachés par defaut pour ne pas révéler avant l'heure l'exercice mais le projet reste parfaitement fonctionnel. Vous pouvez le tester sur n'importe quel exercice de Codingame pour valider votre setup.

N'importe quelle IDE supportant les projets Kotlin/Gradle est utilisable mais il est fortement recommandé d'utiliser [IntelliJ de JetBrains](https://www.jetbrains.com/fr-fr/idea/) (la version community est amplement suffisante).

Pour démarrer il suffit d'importer le projet via `Open/Import` et choisir le dossier `kotlin`.
Ensuite, pour tester votre solution, lancez la tâche gradle `other/export` dans le menu tiroir sur le bord droit de l'IDE et nommé `Gradle` ou bien directement `./gradlew export` en ligne de commande depuis le dossier du projet. Cela aura pour effet de packager votre solution dans un seul fichier `build/solution.kt` sur lequel vous pouvez brancher l'extension Chrome pour une synchro automatique.

### Typescript

Pas encore disponible mais :crossed_fingers:

```
npm i
npm run export
```