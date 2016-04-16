import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

import edu.smu.tspell.wordnet.*;

public class Main {

    static String DATAFILE = "/Users/Kshitij/Desktop/CS/Scalable/Project/Mini-Project/data.txt";
    static String QUERY = "october";
    static ArrayList<String> MUTABLE_QUERY = new ArrayList<String>(Arrays.asList(QUERY.toLowerCase().split(" ")));

    public static void main(String[] args) {

        System.setProperty("wordnet.database.dir", "/Users/Kshitij/Desktop/CS/Scalable/Cleanup/WordNet-3.0/dict");

        //Searching for the query itself.

        try {
            File file = new File(DATAFILE);
            FileReader filereader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(filereader);

            String strLine;
            ArrayList<String> inputLines= new ArrayList<String>();
            strLine = bufferedReader.readLine();
            while (strLine != null) {
                inputLines.add(strLine);
                strLine = bufferedReader.readLine();
            }

            int results = 0;

            for(int k = 0; k< MUTABLE_QUERY.size(); k++){

                ArrayList<String> Synonyms = returnSynonyms(MUTABLE_QUERY.get(k));
                Synonyms.add((MUTABLE_QUERY.get(k)));
                Set<String> hs = new HashSet<>();
                hs.addAll(Synonyms);
                Synonyms.clear();
                Synonyms.addAll(hs);

                for(int m = 0;  m < Synonyms.size(); m++){

                    MUTABLE_QUERY.set(k, Synonyms.get(m));

                    String finalQuery = "";

                    for (String s : MUTABLE_QUERY) {
                        if(MUTABLE_QUERY.indexOf(s)+1 != MUTABLE_QUERY.size()) {
                            finalQuery += s + " ";
                        }
                        else{
                            finalQuery += s;
                        }
                    }

                    if (searchString(finalQuery, inputLines)){
                        results = results + 1 ;
                    }

                }

            }


            if(results == 0){
                System.out.println("No results found for " + QUERY);
            }
        }
        catch(IOException e) {
            System.out.println(e + " occurred due to " + e.getCause());
        }

    }

    //This method takes in a String and a BufferedReader and prints the occurrences of the string in the file.

    private static boolean searchString(String query, ArrayList<String> inputLines){
        int occurrence = 0;
        for(int i = 0; i< inputLines.size(); i++) {
            if (inputLines.get(i).toLowerCase().contains(" " + query+ " ")) {
                System.out.println("Line:" + i + " = " + inputLines.get(i) + " Query: " + query);
                occurrence = occurrence + 1;
            }
        }

        return (occurrence>=1);
    }
    //This method takes in a word and returns a String Array with synonyms of that word.

    private static ArrayList<String> returnSynonyms(String word) {
        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = database.getSynsets(word, SynsetType.NOUN);
        ArrayList<String> synonyms = new ArrayList<String>();
        if (synsets.length > 0) {
            int m = 0;
            for (int i = 0; i < synsets.length; i++) {
                for (int j = 0; j < synsets[i].getWordForms().length; j++) {
                    synonyms.add(m, synsets[i].getWordForms()[j]);
                    m = m + 1;
                }
            }
        }

        return synonyms;
    }

}
