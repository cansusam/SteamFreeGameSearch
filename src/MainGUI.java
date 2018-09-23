import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import java.net.URL;

/**
 * Created by ecc_can on 3/13/2015.
 */
public class MainGUI  {


    public JButton btnAra;
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

    public static boolean isInternetReachable()
    {
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
        btnAra.requestFocus();
        try {
            Thread.sleep(10L);
        }
        catch (InterruptedException e) {

        }
        btnAra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isInternetReachable()) {
                    try {
                        String theWord = "Free";
                        String newsStartsAfterThisLine = "<div class=\"newsmaincol\">";
                        String newsEndsWithThisLine = "<div class=\"newsrightcol responsive_local_menu\">";
                        String whichURL = "https://store.steampowered.com/news/?headlines=1";
                        Boolean lockIsOpen = false;
                        URL theWebSite = new URL(whichURL);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader((theWebSite.openStream()), "utf-8")
                        );
                        String inputLine;
                        Pattern p = Pattern.compile(theWord);
                        //List<String> allMatches = new ArrayList<String>();
                        DefaultListModel model = new DefaultListModel();
                        while ((inputLine = in.readLine()) != null) {
                            if (inputLine.contains(newsStartsAfterThisLine) && !lockIsOpen)
                                lockIsOpen = true;
                            if (inputLine.contains(theWord) && lockIsOpen) {
                                Matcher m = p.matcher(inputLine);
                                while (m.find()) {
                                    String fetched = inputLine.replaceAll("\\<.*?>","");
                                    String arr[] = fetched.split("---", 2);
                                    model.addElement(arr[0]);
                                }
                                //break;
                            }
                            if (inputLine.contains(newsEndsWithThisLine) && lockIsOpen)
                                break;
                        }
                        list1.setModel(model);
                        in.close();
                    } catch (IOException ie) {
                        throw new RuntimeException(ie);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Connection Failed", "InfoBox: " + "Alert", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnAra.doClick();
    }
}
