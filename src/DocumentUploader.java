import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;


/**
 * Created by vzhang on 12/26/14.
 */
@WebServlet (name = "DocumentUploader", urlPatterns = "/upload.do")
public class DocumentUploader extends HttpServlet {
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List items = upload.parseRequest(request);
            Iterator iterator = items.iterator();
            while (iterator.hasNext()) {
                FileItem item = (FileItem)iterator.next();
                if (!item.isFormField()) {
                    if ((item.getName() != null) && (item.getName() != "")) {
                        String filename = request.getServletContext().getAttribute("save_path").toString() + "/" +
                                item.getName();

                        File file = new File(filename);
                        item.write(file);
                        out.println(item.getName() + " uploaded successfully!<br><br>");

                        String content = "";
                        InputStream inputStream = null;
                        try {
                            Parser parser = new AutoDetectParser();
                            Metadata metadata = new Metadata();
                            metadata.set(metadata.CONTENT_ENCODING,"utf-8");
                            metadata.set(metadata.RESOURCE_NAME_KEY, item.getName());

                            inputStream = new FileInputStream(file);
                            BodyContentHandler contentHandler = new BodyContentHandler(1024*1024*20);
                            ParseContext context = new ParseContext();
                            context.set(Parser.class, parser);
                            parser.parse(inputStream, contentHandler, metadata, context);

                            for (String name:metadata.names()) {
                                out.println(name + "=" + metadata.get(name) + "<br>");
                            }

                            out.println("<br>" + contentHandler.toString());


                        } catch (Exception e) { e.printStackTrace(out);}
                        finally {
                            inputStream.close();
                        }

                    }

                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


}
