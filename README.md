# Groups

## Aufgabenbeschreibung
Das Plugin soll als Gruppen/Berechtigungssystem wie zum Beispiel Pex oder LuckyPerms fungieren. Dem Spieler soll sich standardmäßig in einer Gruppe befinden. Ggf. kann diesem eine andere Gruppe permanent oder für eine gewisse Zeit zugewiesen werden. Alle nötigen Informationen sollen in einer relationalen Datenbank gespeichert werden.

## Mindestanforderung

### Gruppen können im Spiel erstellt und verwaltet werden
Gruppen werden mit dem Hauptcommand group verwaltet.
Untercommands sind unter anderem:
- group create
- group delete
- group prefix set
- group prefix add
- group prefix clear
- group weight set
- group weight add
- group weight clear
- group perm set
- group perm remove

### Die Gruppe muss mindestens folgende Eigenschaften haben
#### Name
Der Name einer Gruppe ist fest kann nicht verändert werden. Er ist zugleich auch
Identifikator einer Gruppe

#### Präfix
Präfixe werden über Permissions mit einer bestimmten Syntax geregelt.
Diese lautet: `prefix.<weight>.<prefix>`. Der Präfix mit der höchsten Gewichtung wird verwendet

### Spieler soll einer Gruppe zugewiesen werden können (Permanent und mit einer Zeitangabe)
Spielern/Users werden mit dem Hautpcommand user verwaltet
Untercommands sind unter anderem:
- user group set
- user group remove
- user group add
- user perm set
- user perm remove
- user info

Bei allen Kommandos kann eine Zeitangabe mit angegeben werden. Diese
aber im Englischen Format. Bsp: 5d 5s = 5 Tage 5 Sekunden

### Präfix von der Gruppe soll im Chat und beim Betreten des Servers angezeigt werden
### Wenn der Spieler eine neue Gruppe zugewiesen bekommt, soll diese sich unmittelbar ändern (Spieler soll nicht gekickt werden)
### Alle Nachrichten sollen in einer Konfigurationsdatei anpassbar sein
Siehe hierzu den Unterpunkt: Messages

### Durch einen Befehl erfährt der Spieler seine aktuelle Gruppe und ggf. wie lange er diese noch hat
Der Spieler hat die Möglichkeit (sofern er die Berechtigung dazu hat)
Mit dem Befehl `/rank`folgende Ausgabe (Konfigurierbar) zu erhalten:
// TODO: Image

### Ein oder mehrere Schilde sollen hinzugefügt werden können, die Informationen eines einzelnen Spielers wie Name & Rang anzeigen
Spieler können ein Schild welches folgendes Schlüsselwort enthalten muss: `group-sign``
aufstellen. Danach formatiert sich das Schild wie folgt (konfigurierbar inklusive Sprache):
// TODO: Image

### Alle nötigen Information werden in einer relationalen Datenbank gespeichert (konfigurierbare Texte nicht)
Eine bereits erstellte Datenbank ist erforderlich. Momentan steht dem Benutzer nur MYSQL und MARIADB als Datenbank zur verfügung

## Bonus Aufgaben
### Für eine Gruppe können Berechtigungen festgelegt und sollen dem Spieler dementsprechend zugewiesen werden. Abfrage über #hasPermission sollte funktionieren
Nicht Implementiert. Momentan wird keine Kolleration/Erbe von Usern und deren Gruppen hergestellt

### * Berechtigung
Berechtigungen werden als Wildcards aufgelöst. * Berechtigung würde funktionieren.

### Unterstüzung von mehreren Sprachen
Siehe hierzu den Unterpunkt: Messages

### Tabliste und Scoreboard
Beides sind im Branch scoreboard implementiert. Doch aufgrund der instabilität des genutzten Frameworks
wird dies nicht in der Aufgabe mit abgegebn.

## Messages
// TODO: 

## Setup
// TODO:
