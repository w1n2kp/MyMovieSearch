import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class MovieSearcher
 */
@WebServlet(name="IndexSearcher", urlPatterns = "/search.do")
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
     * @see javax.servlet.Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub

        INDEX_PATH = config.getServletContext().getAttribute("index_path").toString();
    }

    /**
     * @see Servlet#destroy()
     */
    @Override
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

        List<MovieInfo> info = new ArrayList<MovieInfo>();


        for (ScoreDoc scoreDoc: topDocs.scoreDocs) {
            int index = scoreDoc.doc;
            Document doc = searcher.doc(index);

            MovieInfo movieInfo = new MovieInfo();
            movieInfo.Id = index;
            movieInfo.filename = doc.get("filename");
            movieInfo.Title = doc.get("title");
            movieInfo.Poster = doc.get("poster");
            movieInfo.OriginalTitle = doc.get("original_title");
            movieInfo.Year = Integer.parseInt(doc.get("year"));
            movieInfo.Rating = Double.parseDouble(doc.get("rating"));
            movieInfo.Actors = movieInfo.SplitProperties(doc.get("actors"));
            movieInfo.Genres = movieInfo.SplitProperties(doc.get("genres"));
            movieInfo.Directors = movieInfo.SplitProperties(doc.get("directors"));
            movieInfo.Countries = movieInfo.SplitProperties(doc.get("countries"));
            movieInfo.Plot = doc.get("plot");

            info.add(movieInfo);

        }

        JSONArray jsonArray = JSONArray.fromObject(info);

        out.print(jsonArray);

    }

}
