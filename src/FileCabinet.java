import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


    /*
Poniżej przekazujemy zadanie z prośbą o analizę poniższego kodu i samodzielne zaimplementowanie metod findFolderByName,
 findFolderBySize, count w klasie FileCabinet-
 najchętniej unikając powielania kodu i umieszczając całą logikę w klasie FileCabinet. Z uwzględnieniem w analizie i implementacji interfejsu MultiFolder!

 */


public class FileCabinet implements Cabinet {
    private List<Folder> folders;
    private int structureCount = 0;



    // zakładamy że ktoś dostarcza listę z folderami z zewnatrz dla uprosczenia
    public FileCabinet(List<Folder> folders){
        this.folders = folders;
    }



    // znajdz folder po podanej nazwie
    @Override
    public Optional<Folder> findFolderByName(String name) {
        return getFolderByName(name, folders, new ArrayList<>());
    }


    public Optional<Folder> getFolderByName(String name, List<Folder> folders, List<Folder> searchResult){
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


    // znajdz foldery po rozmiarze
    @Override
    public List<Folder> findFoldersBySize(String size) {
//        return folders.stream()
//                .filter(f -> f.getSize().equals(size))
//                .toList();

        return getFoldersBySize(size, folders, new ArrayList<>());
    }

    public List<Folder> getFoldersBySize(String size, List<Folder> folders, List<Folder> searchResult){
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


    @Override
    public int count() {
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

        String searchingName = "project2";
        String searchingSize = "LARGE";


        System.out.println("Folder with a name " + searchingName + " name: " + testFileCabinet.findFolderByName(searchingName).get().getName() + " size: " + testFileCabinet.findFolderByName(searchingName).get().getSize());
        System.out.println();
        System.out.println("Folders with a size " + searchingSize);
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
        // obiekt folder ma swoja nazwe
        String getName();
        // i ma rozmiar
        String getSize();
    }

    // skoro MultiFolder jest również folderem to znaczy że może zawierać jeszcze swoje foldery czyli List<Folder> folders moze miec foldery(bez folderów) i MultiFoldery(z podfolderami)
    interface MultiFolder extends Folder {
        List<Folder> getFolders();
    }
