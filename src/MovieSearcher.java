

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class MovieSearcher
 */
@WebServlet("/search.do")
public class MovieSearcher extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String INDEX_PATH = "";

    private Directory directory = null;
    private Analyzer analyzer = null;
    private IndexSearcher searcher = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieSearcher() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub

        INDEX_PATH = config.getServletContext().getAttribute("index_path").toString();
    }

    /**
     * @see Servlet#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // TODO Auto-generated method stub
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();

        try {
            directory = FSDirectory.open(new File(INDEX_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        analyzer = new SmartChineseAnalyzer(Version.LATEST);

        IndexReader reader = null;

        try {
            reader = IndexReader.open(directory);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        searcher = new IndexSearcher(reader);

        String keyword = request.getParameter("q");

        String[] fields = { "title", "plot", "originaltitle" };

        QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);

        Query query = null;
        try {
            query = queryParser.parse(keyword);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Filter filter = null;
        TopDocs topDocs = searcher.search(query, filter, 1000);


        out.println("<table>");
        for (ScoreDoc scoreDoc: topDocs.scoreDocs) {
            int index = scoreDoc.doc;

            Document doc = searcher.doc(index);
            out.println("<tr><td>");
            out.println("<img src='poster.do?id=" + index + "' width='150px'>");
            out.println("</td><td>");
            out.println("<h2>" + doc.get("title") + "</h2><br>");

            out.println("<p>" + doc.get("plot") + "</p>");
            out.println("</td></tr>");

        }
        out.println("</table>");


    }

}
