package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/kalkulatorKredytowy")
public class KalkulatorKredytowyServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        //super.doGet(req, resp);
        String id = req.getParameter("kwota");
        resp.setContentType("text/html");
        resp.getWriter().println("HelloWorld: " + id);

		/*Enumeration params = req.getParameterNames();
        while(params.hasMoreElements()){
		 	String paramName = (String)params.nextElement();
			resp.getWriter().println("Parameter Name - "+paramName+", Value - "+req.getParameter(paramName));}*/

    }

    boolean isempty(String s) {
        if (s == null || s.equals("")) return true;
        else return false;
    }

    boolean isempty(Integer s) {
        if (s == null) return true;
        else return false;
    }

    boolean isempty(Float s) {
        if (s == null) return true;
        else return false;
    }

    public static Integer tryParseInteger(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Float tryParseFloat(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String kwota = req.getParameter("kwota");
        String raty = req.getParameter("raty");
        String oprocentowanie = req.getParameter("oprocentowanie");
        String oplata = req.getParameter("oplata");
        String rodzaj = req.getParameter("rodzaj");

        if (isempty(kwota) || isempty(raty) || isempty(oprocentowanie) || isempty(oplata) || isempty(rodzaj))
            resp.sendRedirect("/kalkulatorKredytowy.html?");

        Integer iKwota = tryParseInteger(kwota);
        Integer iOplata = tryParseInteger(oplata);
        Integer iRaty = tryParseInteger(raty);
        Float iOprocentowanie = tryParseFloat(oprocentowanie);

        if (isempty(iKwota) || isempty(iRaty) || isempty(iOprocentowanie) || isempty(iOplata))
            resp.sendRedirect("/kalkulatorKredytowy.html");

        if (! rodzaj.equals("stale") && ! rodzaj.equals("malejace"))
           resp.sendRedirect("/kalkulatorKredytowy.html");

        resp.setContentType("text/html");
        resp.getWriter().println("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Dom1</title>\n" +
                "<style>\n" +
                "        .right {\n" +
                "            text-align: right;\n" +
                "        }\n" +
                "    </style>" +
                "</head>\n" +
                "<body>");

        resp.getWriter().println(
                "<table border =\"1\">" +
                "<tr><td class=\"right\">"+"Parametry wejściowe:" + "</td></tr>\n" +
                "<tr><td class=\"right\">"+"Kwota:" + "</td><td>" + kwota + " zł" + "</td></tr>\n" +
                "<tr><td class=\"right\">"+"Raty:" + "</td><td>" + raty + "</td></tr>\n" +
                "<tr><td class=\"right\">"+"Oprocentowanie:" + "</td><td>" + oprocentowanie + " %" + "</td></tr>\n" +
                "<tr><td class=\"right\">"+"Opłata:" + "</td><td>" + oplata + " zł" + "</td></tr>\n" +
                "<tr><td class=\"right\">"+"Rodzaj:" + "</td><td>" + rodzaj + "</td></tr>\n" +
                "</table>"
        );

        /*Wzór na obliczenie raty stałej kredytu
        rata = S * q^n * (q-1)/(q^n-1)
        S – kwota zaciągniętego kredytu
        n – ilość rat
        q – współczynnik równy 1 + (r / m),
        r – oprocentowanie kredytu
        m – ilość rat w okresie dla którego obowiązuje oprocentowanie “r”.
        Najczęściej oprocentowanie podawanej jest w skali roku, a raty płacone są co miesiąc, więc “m” wtedy jest równe 12.
        */
        List<String[]> tabela = new ArrayList<>();
        String element[]={"Nr raty","Kwota Kapitału","Kwota odsetek","Opłaty stałe","Całkowita kwota raty"}; //Nr raty, Kwota Kapitału, Kwota odsetek, Oplaty stały, Całkowita kwota raty
        tabela.add(element);

        //Pomocnicze
        Float fKwota = Float.valueOf(iKwota), odsetka;
        Integer fRata = iRaty;
        String czescKapitalowa,czescOdsetkowa;

        switch (rodzaj) {
            case "stale":
                Float q = 1+((iOprocentowanie/100)/12),S;
                S = fKwota * (float) (Math.pow(q,iRaty) * (q-1)/(Math.pow(q,iRaty)-1));

                for(Integer i=1;i<=iRaty;i++) {
                    czescOdsetkowa = String.format("%.2f",fKwota*q-fKwota);
                    czescKapitalowa = String.format("%.2f",S-Float.parseFloat(czescOdsetkowa));

                    element= new String[]{
                            i.toString(),
                            czescKapitalowa,
                            czescOdsetkowa,
                            iOplata.toString()+" zł",
                            String.format("%.2f",Float.valueOf(S+iOplata))};
                    fKwota -= Float.parseFloat(czescKapitalowa);
                    tabela.add(element);
                }
                break;
            case "malejace":
                for(Integer i=1;i<=iRaty;i++){
                    czescKapitalowa = String.format("%.2f",fKwota/(iRaty-i+1));
                    odsetka = (iOprocentowanie/1200+1)*fKwota-fKwota; //procent/12+1*kasa-kasa

                    element= new String[]{
                            i.toString(),
                            czescKapitalowa+" zł",
                            String.format("%.2f",odsetka)+" zł",
                            iOplata.toString()+" zł",
                            String.format("%.2f",Float.parseFloat(czescKapitalowa)+odsetka+iOplata)+" zł"};
                    fKwota -= Float.parseFloat(czescKapitalowa);
                    tabela.add(element);
                }
                break;
        }

        resp.getWriter().println("<table border =\"1\">");
        for(String[] s : tabela){
            resp.getWriter().println("<tr>");
            for(String e : s){
                resp.getWriter().println("<td>"+e+"</td>");
            }
            resp.getWriter().println("</tr>\n");
        }
        resp.getWriter().println("</table>");
    }
}
