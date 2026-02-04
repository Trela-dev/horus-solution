package solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileCabinet implements Cabinet {

    private List<Folder> folders;
    private int structureCount = 0;


    public FileCabinet(List<Folder> folders){
        this.folders = folders;
    }

    /**
     * Wyszukuje dowolny folder o podanej nazwie w całej strukturze.
     * Przeszukiwanie odbywa się rekurencyjnie (wgłąb struktury).
     *
     * @param name nazwa folderu do wyszukania
     * @return Optional zawierający znaleziony folder lub pusty Optional,
     *         jeśli folder o podanej nazwie nie istnieje
     */

    @Override
    public Optional<Folder> findFolderByName(String name) {
        List<Folder> searchResult = new ArrayList<>();
        return getFolderByName(name, folders, searchResult);
    }



    /**
     * Rekurencyjna metoda pomocnicza do wyszukiwania folderu po nazwie.
     * Przechodzi przez całą strukturę folderów aż do znalezienia pierwszego
     * pasującego elementu.(zgodnie z warunkami zadania dowolny element)
     *
     * @param name nazwa folderu
     * @param folders aktualny poziom struktury folderów
     * @param searchResult lista przechowująca wynik wyszukiwania
     * @return Optional zawierający znaleziony folder lub pusty Optional
     */
    private Optional<Folder> getFolderByName(String name, List<Folder> folders, List<Folder> searchResult){
        for(Folder folder : folders){
            if(folder.getName().equals(name)){
                searchResult.add(folder);
                break; // bo nie ma potrzeby szukać dalej
            }
            if(folder instanceof MultiFolder multiFolder){
                getFolderByName(name, multiFolder.getFolders(), searchResult);
            }

        }

        boolean folderNotFound = searchResult.isEmpty();

        // zwróć pusty Optional, albo pierwszy znaleziony folder z searchResult
        return folderNotFound  ? Optional.empty() : Optional.of(searchResult.getFirst());
    }


    /**
     * Wyszukuje wszystkie foldery o podanym rozmiarze w całej strukturze.
     * Przeszukiwanie wykonywane jest rekurencyjnie.
     *
     * @param size rozmiar folderu (SMALL, MEDIUM, LARGE)
     * @return lista folderów o podanym rozmiarze
     */
    @Override
    public List<Folder> findFoldersBySize(String size) {
        return getFoldersBySize(size, folders, new ArrayList<>());
    }


    /**
     * Rekurencyjna metoda pomocnicza wyszukująca foldery o określonym rozmiarze.
     *
     * @param size rozmiar folderu
     * @param folders aktualny poziom struktury
     * @param searchResult lista wynikowa
     * @return lista folderów spełniających warunek
     */
    private List<Folder> getFoldersBySize(String size, List<Folder> folders, List<Folder> searchResult){
        for(Folder folder : folders){
            if(folder.getSize().equals(size)){
                searchResult.add(folder);
            }
            if(folder instanceof MultiFolder multiFolder){
                getFoldersBySize(size, multiFolder.getFolders(), searchResult);
            }
        }
        return searchResult;
    }


    /**
     * Zlicza wszystkie elementy struktury folderów,
     * zarówno Foldery jak i MultiFoldery.
     *
     * @return całkowita liczba elementów w strukturze
     */
    @Override
    public int count() {
        structureCount = 0;// resetowanie count, aby wynik nie kumulował się w wypadku wielokrotnego wywołania metody na tym samym obiekcie
        countStructure(folders);
        return structureCount;
    }

    /**
     * Rekurencyjnie zlicza elementy struktury folderów.
     *
     * @param folders aktualny poziom struktury
     */
    private void countStructure(List<Folder> folders){

        for (Folder folder : folders){
            if(folder instanceof MultiFolder multiFolder){
                countStructure(multiFolder.getFolders());
            }
            structureCount++;
        }


    }





    /**
     * Metoda demonstracyjna prezentująca działanie klasy FileCabinet.
     * Tworzy przykładową strukturę folderów oraz wykonuje:
     * - wyszukiwanie folderu po nazwie
     * - wyszukiwanie folderów po rozmiarze
     * - zliczanie wszystkich elementów struktury
     */    public static void testFileCabinet(){
        List<Folder> testFolders = new ArrayList<>();
        testFolders.add(new Folder() {  //Folder nr1
            @Override
            public String getName() {
                return "photos";
            }

            @Override
            public String getSize() {
                return "SMALL";
            }
        });

        testFolders.add(new Folder() {  //Folder nr2
            @Override
            public String getName() {
                return "documents";
            }

            @Override
            public String getSize() {
                return "MEDIUM";
            }
        });

        testFolders.add(new MultiFolder() {   //Folder nr3
            @Override
            public String getName() {
                return "albums";
            }

            @Override
            public String getSize() {
                return "LARGE";
            }

            @Override
            public List<Folder> getFolders() {
                return List.of(
                        new Folder() { //Folder nr4
                            @Override
                            public String getName() {
                                return "music";
                            }

                            @Override
                            public String getSize() {
                                return "LARGE";
                            }
                        },
                        new Folder() {  //Folder nr5
                            @Override
                            public String getName() {
                                return "videos";
                            }

                            @Override
                            public String getSize() {
                                return "MEDIUM";
                            }
                        },
                        new MultiFolder() { //Folder nr6
                            @Override
                            public String getName() {
                                return "projects";
                            }

                            @Override
                            public String getSize() {
                                return "LARGE";
                            }

                            @Override
                            public List<Folder> getFolders() {
                                return List.of(
                                        new Folder() { //Folder nr7
                                            @Override
                                            public String getName() {
                                                return "project1";
                                            }

                                            @Override
                                            public String getSize() {
                                                return "SMALL";
                                            }
                                        },
                                        new Folder() {  //Folder nr8
                                            @Override
                                            public String getName() {
                                                return "project2";
                                            }

                                            @Override
                                            public String getSize() {
                                                return "MEDIUM";
                                            }
                                        }
                                );
                            }
                        }
                );
            }
        });

        FileCabinet testFileCabinet = new FileCabinet(testFolders);

        String searchingName = "project1";
        String searchingSize = "SMALL";


        System.out.println("1.Searching for a folder with a name '" + searchingName + "...");
        Optional<Folder> found = testFileCabinet.findFolderByName(searchingName);
        found.ifPresentOrElse(
                f -> System.out.println("Found folder with a name " + searchingName + " name: " + f.getName() + " size: " + f.getSize()),
                () -> System.out.println("Folder with a name " + searchingName + " not found")
        );
        System.out.println();

        System.out.println("2.Searching for a folder with a size '" + searchingSize + "..");

        System.out.println("Folders with a size '" + searchingSize + "' total(" + testFileCabinet.findFoldersBySize(searchingSize).size() + "): ");
        for(Folder folder : testFileCabinet.findFoldersBySize(searchingSize)){
            System.out.println(folder.getName() + " " + folder.getSize());
        }

        System.out.println();
        System.out.println("3.Counting total structure count...");
        System.out.println("Total structure count: " + testFileCabinet.count());



    }


        /*

    Bardziej elegancka metoda countStructure zaproponowana przez chatGPT. Wówczas, nie trzeba używać
    zmiennej instancyjnej structureCount.

    private int countStructure(List<Folder> folders) {
        int count = 0;
        for (Folder folder : folders) {
            count++;
            if (folder instanceof MultiFolder multiFolder) {
                count += countStructure(multiFolder.getFolders());
            }
        }
        return count;
    }
    */


       /*

    Optymalizacja zaproponowana przez chatGPT żeby 2 metody połączyć w jedno za pomocą predykatu.
    Nie jest to moje rozwiązanie więc go nie zmieniam, ale dodaje do wglądu lepsze rozwiązanie.(mniej boilerplate)

    @Override
    public Optional<Folder> findFolderByName(String name) {
        List<Folder> result = new ArrayList<>();
        recursiveSearch(f -> f.getName().equals(name), folders, result);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {
        List<Folder> result = new ArrayList<>();
        recursiveSearch(f -> f.getSize().equals(size), folders, result);
        return result;
    }

    private void recursiveSearch(Predicate<Folder> filter, List<Folder> currentFolders, List<Folder> result){
            for (Folder folder: currentFolders){
                if(filter.test(folder)){
                    result.add(folder);
                }
                if(folder instanceof MultiFolder multiFolder){
                    recursiveSearch(filter, multiFolder.getFolders(), result);
            }
    }
*/



}
