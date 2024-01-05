import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.SwingConstants;


import java.awt.Color;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReciperGUI extends JFrame{

    public recipeFinder outputRecipe;
    private static final Random random = new Random();

    private JTextArea resultBox;
    
    public ReciperGUI() {
        super("Recipe Finder GUI");

        this.setSize(600,700);
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        outputRecipe = new recipeFinder();

        addGuiComponents();
    }

    public static void main(String[] args) {
        ReciperGUI recipeFinder = new ReciperGUI();
        recipeFinder.setVisible(true);
    }

    private void addGuiComponents() {

        JLabel title = new JLabel("Recipe Generator");

        title.setFont(new Font("Roboto", Font.BOLD, 30 ));

        title.setHorizontalAlignment(SwingConstants.CENTER);

        title.setBounds(0, 13, 560, 45);

        add(title);

        JLabel userInstructions = new JLabel("Generate Random Recipe");
        userInstructions.setFont(new Font("Roboto", Font.PLAIN, 18));
        userInstructions.setBounds(30, 80,400, 25);
        userInstructions.setHorizontalAlignment(SwingConstants.CENTER);
        add(userInstructions);



        resultBox = new JTextArea();
        resultBox.setFont(new Font("Roboto", Font.PLAIN, 20));
        resultBox.setEditable(false);

        JScrollPane resultBoxPane = new JScrollPane(resultBox);
        

        resultBoxPane.setBounds(70, 130, 450, 455);

        resultBoxPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        resultBox.setLayout(null);

        resultBox.setLineWrap(true);
        resultBox.setWrapStyleWord(true);
        add(resultBoxPane);



        JButton searchButton1 = new JButton("Search");
        searchButton1.setBounds(405, 70, 100, 50);
        searchButton1.setHorizontalAlignment(SwingConstants.CENTER);
        searchButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                        outputRecipe.getRecipe(ReciperGUI.this);
            }
        });

        add(searchButton1);
        
    }

    public void updateResultArea(String resultText) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject apiResponse = (JSONObject) parser.parse(resultText);

            long count = (long) apiResponse.get("count");
            JSONArray results = (JSONArray) apiResponse.get("results");

            if (count > 0 && results.size() > 0) {
                // Generate a random index within the range of available results
                int randomIndex = random.nextInt(results.size());
                JSONObject randomResult = (JSONObject) results.get(randomIndex);
                System.out.println(randomIndex);
                String steps = (String) ((JSONObject) ((JSONArray) randomResult.get("instructions")).get(0)).get("display_text");
                System.out.println(steps);

                String totalTime =  (String) ((JSONObject) randomResult.get("total_time_tier")).get("display_tier");
                System.out.println(totalTime);

                JSONObject nutritionObj = (JSONObject) randomResult.get("nutrition");
                StringBuilder nutritions = new StringBuilder();

                for (Object key : nutritionObj.keySet()) {
                    String keyString = (String) key;

                    if (!keyString.equals("updated_at")) {
                        Long value = (Long) nutritionObj.get(keyString);
                        nutritions.append(keyString).append(": ").append(value).append("\n");
                    }
                }

                System.out.println(nutritions.toString());

                String description = (String) randomResult.get("description");
                System.out.println(description);


                JSONArray sections = (JSONArray) randomResult.get("sections");
                StringBuilder ingredientsList =  new StringBuilder();

                if (sections != null && sections.size() > 0) {
                    
                    JSONObject section0 = (JSONObject) sections.get(0);
    
                    if (section0 != null && section0.containsKey("components")) {
                        
                        JSONArray components = (JSONArray) section0.get("components");
                       
                        
                        for (Object componentObj : components) {
                            JSONObject component = (JSONObject) componentObj;
                            
                            
                            if (component != null && component.containsKey("raw_text")) {
                                String rawText = (String) component.get("raw_text");
                                ingredientsList.append(rawText);
                            }
                        }
                    }
                }

                String displayText =  "Description: \n" + description + "\n\n" +
                        "Nutritions: \n" + nutritions.toString() +  "\n\n" + "Ingredients: \n" + ingredientsList.toString() + "\n\n" +
                        "Total time: \n" + totalTime + "\n\n" +
                        "Instructions: \n" + steps + "\n\n";

                        resultBox.setText(displayText.toString());
            } else {
                resultBox.setText("Error: no result found \n");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error parsing JSON response.");
        }
    }

    
}
        