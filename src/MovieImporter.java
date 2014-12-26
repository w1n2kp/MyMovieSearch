import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by vzhang on 14/12/21.
 */
@WebServlet(name = "MovieImporter", urlPatterns = ("/import.do"))
public class MovieImporter extends HttpServlet {


    private int fileCount = 0;

    private String connectionString = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();

        String library_path = request.getServletContext().getInitParameter("library_path").toString();
        connectionString = request.getServletContext().getInitParameter("connection_string").toString();

        // Begin search files
        try {
            fileCount = 0;
            findFiles(library_path, out);
        } catch (Exception e) {
            e.printStackTrace(out);
        }

        out.print(fileCount + " Movie import completed!");

    }

    private void findFiles(String path, PrintWriter out) throws Exception {

        File folder = new File(path);
        File[] files = folder.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                findFiles(f.getAbsolutePath(), out);
            } else {

                String fn = f.getAbsolutePath();

                if (getExtension(fn).toLowerCase().equals("nfo")) {
                    MovieInfo nfo = new MovieInfo(fn);
                    nfo.connectionString = connectionString;
                    nfo.Import();

                    fileCount++;
                    out.print(nfo.filename + "<br>");
                    out.flush();

                }
            }
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
