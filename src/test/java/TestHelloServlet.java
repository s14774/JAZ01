import org.junit.Test;
import org.mockito.Mockito;
import servlets.HelloServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class TestHelloServlet extends Mockito {

    @Test
    public void test1() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HelloServlet servlet = new HelloServlet();
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        when(request.getParameter("name")).thenReturn(null);
        servlet.doPost(request, response);
        verify(response).sendRedirect("/");
    }

}
