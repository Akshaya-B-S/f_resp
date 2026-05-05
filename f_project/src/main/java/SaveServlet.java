import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/saveWork")
public class SaveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonData = request.getParameter("data");
        
        // Define your local file path
        // Change "C:/DevOpsProject/" to your preferred folder
        String filePath = "F:/New folder/np lab/DevopsNM/saved_work.json"; 
        
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.println(jsonData);
            response.getWriter().write("Successfully saved to: " + filePath);
        } catch (Exception e) {
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
