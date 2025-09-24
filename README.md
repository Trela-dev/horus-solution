### Informacje o zadaniu

To jest **zadanie rekrutacyjne**, którego celem było:

* zaimplementowanie metod `findFolderByName`, `findFolderBySize` oraz `count` w klasie `FileCabinet`,
* uniknięcie powielania kodu (rekurencja, wspólna logika wyszukiwania),
* uwzględnienie interfejsu `MultiFolder` (zagnieżdżone foldery).

---

### Uruchamianie

Program można uruchomić po prostu wywołując metodę:

```java
public static void main(String[] args) {
    FileCabinet.testFileCabinet();
}
```

---

### Dane testowe

* W metodzie `testFileCabinet` zdefiniowana jest **testowa lista folderów** (`testFolders`) – stworzona przy pomocy **klas anonimowych**.
* W tym miejscu można łatwo modyfikować dane wejściowe, np. dodawać kolejne foldery, podfoldery, czy zmieniać ich rozmiar.

Przykładowe zmienne sterujące wyszukiwaniem:

```java
String searchingName = "project2";
String searchingSize = "SMALL";
```

* `searchingName` – nazwa folderu, którego szukamy,
* `searchingSize` – rozmiar folderów, które chcemy wyszukać.

---



