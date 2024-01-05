import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import javax.swing.JOptionPane;



public class recipeFinder {

public void getRecipe(ReciperGUI gui) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://tasty.p.rapidapi.com/recipes/list?from=0&size=20&tags=under_30_minutes"))
                    .header("X-RapidAPI-Key", "415a0a6be1msh02aeb4182630329p1f3d90jsn458dd2fcc06b")
                    .header("X-RapidAPI-Host", "tasty.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            
            // Assuming you have a method in ReciperGUI to update the result
            gui.updateResultArea(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error fetching recipes.");
        }
    }

    
}


