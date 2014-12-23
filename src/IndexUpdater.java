

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class IndexUpdater
 */
@WebServlet("/index.do")
public class IndexUpdater extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Directory directory = null;
    private Analyzer analyzer = null;
    private IndexWriterConfig config = null;
    private IndexWriter writer = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexUpdater() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();

        String library_path = request.getServletContext().getAttribute("library_path").toString();
        String index_path = request.getServletContext().getAttribute("index_path").toString();

        directory = FSDirectory.open(new File(index_path));
        analyzer = new SmartChineseAnalyzer(Version.LATEST);
        config = new IndexWriterConfig(Version.LATEST, analyzer);
        writer = new IndexWriter(directory, config);

        try {
            writer.deleteAll();

            findFiles(library_path, out);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        writer.close();

    }

    private void findFiles(String path, PrintWriter out) throws Exception {
        File folder = new File(path);
        File[] files = folder.listFiles();
        for (File f:files) {
            if (f.isDirectory()){
                findFiles(f.getAbsolutePath(), out);
            } else {

                String fn = f.getAbsolutePath();

                if (getExtension(fn).toLowerCase().equals("nfo")) {
                    MovieInfo nfo = new MovieInfo(fn);

                    Document doc = new Document();
                    doc.add(new Field("filename", fn, TextField.TYPE_STORED));
                    doc.add(new Field("title", nfo.Title, TextField.TYPE_STORED));
                    doc.add(new Field("plot", nfo.Plot, TextField.TYPE_STORED));
                    doc.add(new Field("year", String.valueOf(nfo.Year), TextField.TYPE_STORED));
                    doc.add(new Field("original_title", nfo.OriginalTitle, TextField.TYPE_STORED));
                    doc.add(new Field("rating", nfo.Rating.toString(), TextField.TYPE_STORED));
                    doc.add(new Field("actors", nfo.CombineProperties(nfo.Actors), TextField.TYPE_STORED));
                    doc.add(new Field("directors", nfo.CombineProperties(nfo.Directors), TextField.TYPE_STORED));
                    doc.add(new Field("countries", nfo.CombineProperties(nfo.Countries), TextField.TYPE_STORED));
                    doc.add(new Field("genres", nfo.CombineProperties(nfo.Genres), TextField.TYPE_STORED));

                    writer.addDocument(doc);
                    out.println(fn + "<br>");

                    // writer.optimize();
                }
            }
        }
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

}
