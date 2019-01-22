import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import javax.swing.*;
import java.net.URL;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by ecc_can on 3/13/2015.
 */
public class MainGUI  {


    public JButton buttonSearch;
    public JComboBox comboBoxWhereToSearch;
    private JPanel panel;
    private JList list1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Steam Free Game Search");
        frame.setContentPane(new MainGUI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }

    public static boolean isInternetReachable() {
        try {
            //make a URL to a known source
            URL url = new URL("https://store.steampowered.com/news/?headlines=1");

            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public MainGUI() {

        comboBoxWhereToSearch.addItem(new ComboItem("Steam","s"));
        comboBoxWhereToSearch.addItem(new ComboItem("Indie","i"));
        buttonSearch.requestFocus();
        try {
            Thread.sleep(10L);
        }
        catch (InterruptedException e) {

        }
        buttonSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isInternetReachable()) {
                    getResultsFromSites();
                } else {
                    JOptionPane.showMessageDialog(null, "Connection Failed", "InfoBox: " + "Alert", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        buttonSearch.doClick();
        comboBoxWhereToSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isInternetReachable()) {
                    getResultsFromSites();
                } else {
                    JOptionPane.showMessageDialog(null, "Connection Failed", "InfoBox: " + "Alert", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public void getResultsFromSites(){
        try {
            if (String.valueOf(comboBoxWhereToSearch.getSelectedItem().toString()) == "Steam") {
                Document doc = Jsoup.connect("https://store.steampowered.com/news/?headlines=1").get();
                Elements listOfNews = doc.select("div.title");
                DefaultListModel model = new DefaultListModel();
                for(int i=0; i<listOfNews.size(); i++){
                    if( listOfNews.get(i).childNodes().get(0).childNodes().get(0).toString().contains("Free") ||
                            listOfNews.get(i).childNodes().get(0).childNodes().get(0).toString().contains("free") )
                        model.addElement(listOfNews.get(i).childNodes().get(0).childNodes().get(0).toString());
                }
                list1.setModel(model);
            } else if (String.valueOf(comboBoxWhereToSearch.getSelectedItem().toString()) == "Indie"){
                Document doc = Jsoup.connect("https://www.indiegamebundles.com/category/free/").get();
                Elements listOfNews = doc.select("div.item-details");
                DefaultListModel model = new DefaultListModel();
                for(int i=0; i<listOfNews.size(); i++){
                    model.addElement(listOfNews.get(i).childNodes().get(0).childNodes().get(0).childNodes().toString().replaceAll("\\]|\\[",""));
                }
                list1.setModel(model);
            }
        } catch (IOException ie) {
            throw new RuntimeException(ie);
        }
    }
}
