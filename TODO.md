# Bee Industry - TODO & Ideen
Hier ist eine strukturierte Übersicht der geplanten Features und Konzepte für die Bee Industry Mod.

## Bienen-Konzepte
### Ressourcen-Bienen
- [ ] **Basis-Ressourcen:**
    - [x] Dirt-Biene
    - [x] Sand-Biene
    - [x] Gravel-Biene
    - [x] Stone-Biene
    - [x] Mossy-Biene (Moos)
- [ ] **Holz-Biene:**
    - [ ] Implementierung einer generischen Holz-Biene, deren Holz-Typ vom Bestäubungsblock abhängt (z.B. Eichensetzling -> Eichenholz).
- [ ] **Mineral-Bienen:**
    - [x] Coal-Biene (Kohle)
    - [x] Iron-Biene (Eisen)
    - [x] Copper-Biene
    - [x] Gold-Biene
    - [ ] Redstone-Biene
    - [ ] Lapis-Biene
    - [x] Diamond-Biene
    - [x] Emerald-Biene
    - [ ] Amethyst-Biene
- [ ] **Nether-Bienen:**
    - [ ] Netherrack-Biene
    - [ ] Quartz-Biene
    - [ ] Bone-Biene (Knochen)
    - [ ] Slime-Biene
    - [ ] Netherite-Biene (Netherite-Schrott)
- [ ] **End-Bienen:**
    - [ ] Endstone-Biene
- [ ] **Wasser/Ozean-Bienen:**
    - [ ] Coral-Biene
    - [ ] Aquamarin-Biene (Prismarin)
    - [ ] Water-Biene
- [ ] **Spezial-Bienen:**
    - [ ] Lava-Biene
    - [ ] Honey-Biene
    - [ ] Obsidian-Biene

### Arbeiterbienen
- [x] **Mining-Biene:** Baut in einem bestimmten Bereich Blöcke ab (eventuell in Verbindung mit dem "Beepost").
- [x] **Farming-Biene:** Erntet reife Feldfrüchte in einem bestimmten Bereich.
- [ ] **Fishing-Biene:** Fischt in einem nahegelegenen Gewässer.
- [x] **Fighting-Biene:** Verteidigt einen Bereich oder den Spieler.

### Sonstige Bienen
- [ ] **Reitbare Biene:** Eine größere Biene, auf der der Spieler reiten kann.
- [ ] **Licht-Biene:** Eine Biene, die Licht aussendet.

### Zucht & Mutationen
- [x] System für Bienen-Mutationen entwerfen.
- [ ] **High-Level-Idee:** Ein Genetik-System, ähnlich wie bei Jurassic Park, um Bienen zu verändern.

## Blöcke & Maschinen

### Verbesserter Bienenstock (Advanced Beehive)
- [x] Eigenen Bienenstock-Block erstellen, der als Basis für Upgrades dient.
- [x] **Upgrade-System:**
    - [ ] **Size-Upgrade:** Erhöht die Kapazität des Stocks (+1/2/3/5 Bienen).
    - [x] **Effizienz-Upgrade:** Bienen benötigen weniger Zeit im Stock (5/10/15/25 % schneller).
    - [x] **Mengen-Upgrade:** Chance auf doppelte Item-Menge (20/40/60/90 %).

### Beepost (Arbeitsstation für Bienen)
- [x] **Block "Beepost" erstellen:**
    - [x] Dient als Ausgangspunkt und Hub für Arbeiterbienen.
    - [x] Bienen operieren in einem definierten Bereich um den Block (z.B. 10x10x5).
    - [x] Benötigt "SweetHoney" als Treibstoff.
- [x] **GUI für den Beepost:**
    - [x] Entwerfen basierend auf der Skizze.
    - [x] Slots für bis zu 3 Bienen (via Bienen-Container).
    - [x] Aktivierungs-/Deaktivierungs-Schalter für jede Biene.
    - [x] Slots für bienenspezifische Upgrades (z.B. Filter, Effizienz).
    - [x] Slots für Block-Upgrades (z.B. Reichweite).
    - [x] Anzeige für den "SweetHoney"-Treibstoff.

### Welt-generierte Nester
- [ ] Nester für Basis-Bienen (Dirt, Stone, Sand, Gravel) in der Welt spawnen lassen.
- [ ] Unterschiedliche Holz-Nester für die Holz-Biene in den entsprechenden Biomen (Eiche, Birke etc.).

### Sonstige Blöcke
- [ ] **Mögliche Idee:** Eigene Werkbank zum Herstellen von Charms.

## Items

### Rüstung (Beekeeper Armor)
- [x] **Set-Bonus:** Bienen werden nicht mehr aggressiv bei Zerstörung von Nestern oder Angriffen.
- [x] **Herstellung:** Basis Lederrüstung + Honig.
- [x] **Einzelteil-Boni:**
    - [x] **Helm:** Bienen verlieren ihre Aggressivität 90 % schneller.
    - [x] **Brustplatte:** Erhöht den Anziehungsradius für Bienen um 5 Blöcke.
    - [x] **Hose:** Löst bei erlittenem Schaden einen Knochenmehl-Effekt in der Umgebung aus.
    - [x] **Schuhe:** +5 % Geschwindigkeit auf Gras, reduziert Fallschaden leicht.

### Talismane (Charms)
- [x] **Charm-Template** als Basis-Crafting-Item.
- [ ] **Effizienz-Charm:** (Rezept: 1x Template, 4x Goldblöcke, ...).
- [ ] **Glücks-Charm:** (Rezept: 1x Template, 1x Diamantblock, 1x Smaragdblock, ...).
- [ ] **Pollination-Charm:** Garantiert eine Zusatzressource neben der Hauptressource.
- [ ] **Babee-Charm:** Erhöht die Chance auf Mutationen bei der Zucht.
- [ ] **Honey-Charm:** Erhöht die Honigproduktion.
- [x] **Night-Charm:** Lässt Bienen auch nachts arbeiten.
- [ ] **Rainy-Charm:** Lässt Bienen auch bei Regen arbeiten.
- [ ] **Speed-Charm:** Arbeiterbienen fliegen schneller.

### Sonstige Items
- [x] **Bienen-Container:** Item, um Bienen einzufangen und im Beepost zu platzieren.
- [x] **SweetHoney:** Treibstoff für den Beepost.

## Gameplay-Mechaniken & World-Integration

### Bestäubungs-Mechanik (Pollination)
- [x] **Kerndesign-Entscheidung:** Anstatt neuer Blumen, werden Blöcke zur "Bestäubung" und Produktion verwendet (z.B. Eisenblock für Eisenbiene). Dies muss für jede Biene definiert werden.

### Villager
- [x] **Neuer Beruf: Beekeeper (Imker):**
    - [x] Arbeitsstation: Advanced Beehive.
    - [x] Handelt mit Spawn-Eggs, Honig und Bienen-Containern.

## Offene Design-Fragen & Konzepte
- [ ] **Holz-Bienen:** Wie genau wird unterschieden, welches Holz produziert wird? Abhängig vom nächstgelegenen Setzling/Holzblock?
- [x] **Beepost-Design:** Wie soll der Block genau aussehen?
- [ ] **Reitbare Biene:** Wie wird sie gesteuert? Hat sie besondere Fähigkeiten?
- [ ] **Balancing:** Wie werden die Produktionsraten, Upgrade-Kosten und Zucht-Chancen ausbalanciert, um fair und motivierend zu sein?
- [ ] **Crafting-Rezepte:** Detaillierte Rezepte für alle Items und Blöcke festlegen.

# BUGS
- [x] Bee Container wenn man mit einer Biene drin eine andere rechtsklick, wird die neue eingesammelt und die alte einfach gelöscht.

# Zukunft
- [ ] **Ressourcen-System überarbeiten**: Anstatt dass Bienen direkt Items wie "Iron Nuggets" produzieren, wollen wir ein einzigartiges System schaffen. Ideen sind ein Essenz/Infuser-System (Eisen-Biene produziert "Eisen-Essenz", die einen Steinblock in Eisenerz verwandelt) oder ein Flüssigkeits-System (Bienen produzieren spezielle Honig-Arten, die in Maschinen weiterverarbeitet werden).

- [ ] **Fortgeschrittene Zucht**: Eine "Breeding Chamber"-Maschine, die die manuelle Paarung ersetzt, mehr Kontrolle über Mutationen bietet und selbst aufwertbar ist.

- [ ] **Genetik-System**: Keine "Bienen-zerquetschen"-Mechanik. Stattdessen eine "Gen-Modifikator"-Maschine, in die man eine Biene und spezielle Items (z.B. ein "Smelting-Upgrade") legt, um ihr neue Fähigkeiten oder verbesserte Attribute zu geben.

- [ ] **Nest-Locator**: Ein einzigartiges Werkzeug wie die "Bienen-Flöte", die beim Benutzen ein leises Summen von nahen, unentdeckten Nestern als Antwort erhält.

- [ ] **Mehr Inhalte**: Der vollständige Zuchtbaum muss noch entworfen und als JSON-Dateien implementiert werden. Es fehlen noch viele Charms, Upgrades und Bienenarten, um die Welt zu füllen.

# Projektziel & Kernkonzept:
"Bee Industry" ist eine Mod, die die Bienenhaltung zu einem tiefgründigen, industriellen System ausbaut. Das Kern-Gameplay dreht sich um das Entdecken, Züchten und Nutzen von dutzenden einzigartigen Bienenarten. Spieler bauen fortschrittliche Maschinen, um die Fähigkeiten dieser Bienen zu nutzen und alles zu automatisieren – von der Ressourcengewinnung und Landwirtschaft bis hin zur Verteidigung der eigenen Basis. Die grundlegende Design-Philosophie ist, ein tiefes Fortschrittssystem zu schaffen und die Mod durch datengesteuerte Systeme extrem konfigurierbar für Modpack-Ersteller zu machen.