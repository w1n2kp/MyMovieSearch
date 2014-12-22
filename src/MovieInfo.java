
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class MovieInfo {
    String connectionString = "";
    String filename = "";
    String Title = "";
    String Plot = "";
    String OriginalTitle = "";
    String Poster = "";
    Double Rating = 0.0;

    List<String> Actors = new ArrayList<String>();
    List<String> Directors = new ArrayList<String>();
    List<String> Genres = new ArrayList<String>();
    List<String> Countries = new ArrayList<String>();

    int Year = 1900;


    public MovieInfo(int id) {
        DatabaseService svc = DatabaseService.getInstance();
        Connection connection = svc.connect(this.connectionString);

        try {
            Statement statement = connection.createStatement();
            String sql = "";
            statement.executeQuery(sql);
        } catch (Exception e) {}

    }

    public MovieInfo(String filename) {
        this.filename = filename;

        Document doc;

        try {
            File file = new File(this.filename);
            SAXBuilder builder = new SAXBuilder();
            doc = builder.build(file);
        } catch (Exception e) {
            System.out.println("Can't open the nfo file for read");
            System.out.println(this.filename);
            return;
        }

        Element root = doc.getRootElement();

        this.Title = root.getChild("title").getText();
        this.Plot = root.getChild("plot").getText();
        this.Year = Integer.parseInt(root.getChild("year").getText(),10);
        this.Poster = this.filename.replace(".nfo", ".jpg");

        try {
            this.OriginalTitle = root.getChild("originaltitle").getText();
        } catch (Exception e) {}

        try {
            this.Rating = Double.parseDouble(root.getChild("rating").getText());
        } catch (Exception e) {}

        try {
            List<Element> actors = root.getChildren("actor");
            for (Element e:actors)
                this.Actors.add(e.getChildText("name"));
        } catch (Exception e) {}


        try {
            List<Element> countries = root.getChildren("countries");
            for (Element e:countries) {
                String[] c = e.getChildText("country").split(" ");
                for (int k=0; k<c.length; k++) this.Countries.add(c[k]);
            }

        } catch (Exception e) {}

        try {
            List<Element> directors = root.getChildren("director");
            for (Element e:directors)
                this.Directors.add(e.getChildText("name"));
        } catch (Exception e) {}

        try {
            String[] genres = root.getChild("genre").getText().split(" ");
            for (int n = 0; n < genres.length; n++)
                this.Genres.add(genres[n]);
        } catch (Exception e) {}

    }

    public void Import() throws SQLException {
        DatabaseService svc = DatabaseService.getInstance();
        Connection conn = svc.connect(this.connectionString);
        CallableStatement statement = conn.prepareCall("{call sp_create_movie(?,?,?,?,?,?,?,?,?,?)}");
        statement.setString(1, this.Title);
        statement.setString(2, this.OriginalTitle);
        statement.setInt(3, this.Year);
        statement.setString(4, this.Plot);
        statement.setFloat(5, Float.parseFloat(this.Rating.toString()));
        statement.setString(6, this.filename);
        statement.setString(7, this.CombineProperties(this.Actors));
        statement.setString(8, this.CombineProperties(this.Genres));
        statement.setString(9, this.CombineProperties(this.Directors));
        statement.setString(10, this.CombineProperties(this.Countries));
        statement.execute();
        statement.close();
        conn.close();
    }

    public String CombineProperties(List<String> property) {
        String result = "";
        for (String s: property) result += ("," + s);
        try {
            if (result.startsWith(",")) result = result.substring(1);
        }
        catch (Exception e) {}
        finally {
            return result;
        }
    }


}
