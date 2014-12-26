

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Servlet implementation class PosterViewer
 */
@WebServlet(name = "PosterViewer", urlPatterns = "/poster.do")
public class PosterViewer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */

    private Directory directory = null;
    private IndexSearcher searcher = null;

    public PosterViewer() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String index_path = request.getServletContext().getInitParameter("index_path").toString();

        try {
            directory = FSDirectory.open(new File(index_path));
        } catch (IOException e) {
            e.printStackTrace();
        }


        IndexReader reader = null;

        try {
            reader = IndexReader.open(directory);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        searcher = new IndexSearcher(reader);

        int index = Integer.parseInt(request.getParameter("id"));
        Document doc = searcher.doc(index);
        String poster = doc.get("filename").replace(".nfo",".jpg");
        File jpeg = new File(poster);
        FileInputStream s = new FileInputStream(jpeg);
        byte[] bytes = new byte[s.available()];
        s.read(bytes);
        s.close();

        response.setContentType("image/jpeg");

        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();


    }

}
