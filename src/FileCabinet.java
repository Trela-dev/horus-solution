import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


/*
Poniżej przekazujemy zadanie z prośbą o analizę poniższego kodu i samodzielne zaimplementowanie metod findFolderByName,
 findFolderBySize, count w klasie FileCabinet-
 najchętniej unikając powielania kodu i umieszczając całą logikę w klasie FileCabinet. Z uwzględnieniem w analizie i implementacji interfejsu MultiFolder!

 */


public class FileCabinet implements Cabinet {
    private List<Folder> folders;
    private int structureCount = 0;



    // zakładamy że ktoś dostarcza listę z folderami z zewnątrz dla uproszczenia
    public FileCabinet(List<Folder> folders){
        this.folders = folders;
    }



    @Override
    public Optional<Folder> findFolderByName(String name) {
        return getFolderByName(name, folders, new ArrayList<>());
    }


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
        return searchResult.isEmpty() ? Optional.empty() : Optional.of(searchResult.get(0));
    }



    @Override
    public List<Folder> findFoldersBySize(String size) {
        return getFoldersBySize(size, folders, new ArrayList<>());
    }

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


    @Override
    public int count() {
        structureCount = 0;// resetowanie count, aby wynik nie kumulował się w wypadku wielokrotnego wywołania metody na tym samym obiekcie
        countStructure(folders);
        return structureCount;
    }

    private void countStructure(List<Folder> folders){

        for (Folder folder : folders){
            if(folder instanceof MultiFolder multiFolder){
                countStructure(multiFolder.getFolders());
            }
            structureCount++;
        }


    }

    /*

    Również bardziej elegancka metoda countStructure zaproponowana przez chatGPT. Wówczas, nie trzeba używać
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




    // metoda testująca(użyłem klas anonimowych, ponieważ w wymogu zadania było żeby wszystko umieścić w klasie FileCabinet,
    // alternatywnie wygodniejszym rozwiązaniem mogłoby być po prostu stworzyć proste klasy implementujące interfejs Folder, a także MultiFolder.)
    public static void testFileCabinet(){
        List<Folder> testFolders = new ArrayList<>();
        testFolders.add(new Folder() {
            @Override
            public String getName() {
                return "photos";
            }

            @Override
            public String getSize() {
                return "SMALL";
            }
        });

        testFolders.add(new Folder() {
            @Override
            public String getName() {
                return "documents";
            }

            @Override
            public String getSize() {
                return "MEDIUM";
            }
        });

        testFolders.add(new MultiFolder() {
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
                        new Folder() {
                            @Override
                            public String getName() {
                                return "music";
                            }

                            @Override
                            public String getSize() {
                                return "LARGE";
                            }
                        },
                        new Folder() {
                            @Override
                            public String getName() {
                                return "videos";
                            }

                            @Override
                            public String getSize() {
                                return "MEDIUM";
                            }
                        },
                        new MultiFolder() { // MultiFolder w MultiFolderze
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
                                        new Folder() {
                                            @Override
                                            public String getName() {
                                                return "project1";
                                            }

                                            @Override
                                            public String getSize() {
                                                return "SMALL";
                                            }
                                        },
                                        new Folder() {
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


        Optional<Folder> found = testFileCabinet.findFolderByName(searchingName);

        found.ifPresentOrElse(
                f -> System.out.println("Found folder with a name " + searchingName + " name: " + f.getName() + " size: " + f.getSize()),
                () -> System.out.println("Folder with a name " + searchingName + " not found")
        );
        System.out.println();
        System.out.println("Folders with a size '" + searchingSize + "' total(" + testFileCabinet.findFoldersBySize(searchingSize).size() + "): ");
        for(Folder folder : testFileCabinet.findFoldersBySize(searchingSize)){
            System.out.println(folder.getName() + " " + folder.getSize());
        }
        System.out.println();
        System.out.println("Total structure count: " + testFileCabinet.count());



    }

}


    interface Cabinet {
        // zwraca dowolny element o podanej nazwie
        Optional<Folder>
        findFolderByName(String name);

        // zwraca wszystkie foldery podanego rozmiaru SMALL/MEDIUM/LARGE
        List<Folder> findFoldersBySize(String size);

        //zwraca liczbę wszystkich obiektów tworzących strukturę
        int count();
    }


    interface Folder {
        String getName();
        String getSize();
    }

    // skoro MultiFolder jest również folderem(Folder) to znaczy że może zawierać jeszcze swoje foldery czyli List<Folder> folders może mieć foldery(bez folderów) i MultiFoldery(z podfolderami)
    interface MultiFolder extends Folder {
        List<Folder> getFolders();
    }
