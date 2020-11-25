package tpu.ru.filecloudclient.webcenter;

import android.util.Log;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JournalsParser {
    private List<Journal> journals;

    private String webcenterBaseUrl;
    private WebClient client;

    private String username;
    private String password;

    public JournalsParser(String webcenterBaseUrl){
        this.webcenterBaseUrl = webcenterBaseUrl;

        this.journals = new ArrayList<>();

        client = new WebClient(BrowserVersion.CHROME);
        client.getOptions().setThrowExceptionOnScriptError(false);
    }

    public boolean parseJournals(){
        HtmlPage page = null;
        try{
            page = this.client.getPage(this.webcenterBaseUrl);

            HtmlTextInput usernameInput = page.getFirstByXPath("//input[@name='username']");
            HtmlPasswordInput passwordInput = page.getFirstByXPath("//input[@name='password']");
            HtmlSubmitInput submitInput = page.getFirstByXPath("//input[@type='submit']");

            usernameInput.type(username);
            passwordInput.type(password);

            page = this.getPageAndIgnoreJSErrors(submitInput);
            if(page == null) return false;
            while(client.getJavaScriptEngine().isScriptRunning()){
                client.waitForBackgroundJavaScript(1000);
            }

            HtmlTable attestTable = page.getFirstByXPath("//table[@ng-if='attType.type_id === 2']");
            if(attestTable == null) return false;

            Journal journal = parseJournal(attestTable);
            if(journal == null) return false;




        }catch(IOException ex){
            Log.e(getClass().getSimpleName(), ex.toString());
        }



        return (page != null);
    }

    private HtmlPage getPageAndIgnoreJSErrors(HtmlElement element) throws IOException{
        HtmlPage page = null;
        try{
            page = element.click();
        }catch(ScriptException scex){
            Log.e(this.getClass().getSimpleName(), scex.toString());
            page = scex.getPage();

        }

        return page;
    }

    private Journal parseJournal(HtmlTable table){
        List<HtmlTableRow> rows = table.getRows();
        Journal journal = new Journal(0,"");
        for(HtmlTableRow row : rows){
            AcademicDiscipline discipline = new AcademicDiscipline();


            List<HtmlTableCell> cells = row.getCells();

            int scoreKT1 = 0, scoreKT2 = 0;

            for(int i = 0; i < cells.size(); i++){
                switch(AttestationCell.values()[i]){
                    case DISCIPLINE_AND_ATTESTFORM:
                        String[] buf = cells.get(i).asText().split("/");
                        discipline.setName(buf[0]);
                        discipline.setFormOfControl(buf[1]);
                        break;
                    case KT1_SCORE:
                        try {
                            scoreKT1 = Integer.parseInt(cells.get(i).asText());
                        }catch(NumberFormatException ex){
                            scoreKT1 = 0;
                        }

                        break;
                    case KT1_MAX_SCORE:
                        break;
                    case KT2_SCORE:
                        try {
                            scoreKT2 = Integer.parseInt(cells.get(i).asText());
                        }catch(NumberFormatException ex){
                            scoreKT2 = 0;
                        }
                        break;
                    case KT2_MAX_SCORE:
                        int mscore = 0;
                        try {
                            mscore = Integer.parseInt(cells.get(i).asText());
                        }catch(NumberFormatException ex){
                            discipline.setCurScore(0);
                            break;
                        }
                        discipline.setCurScore(mscore);
                        break;
                    case ATTESTATION_SCORE_AND_MAXSCORE:
                        break;
                    case END_SCORE:
                        int score = 0;
                        try {
                            score = Integer.parseInt(cells.get(i).asText());
                        }catch(NumberFormatException ex){
                            discipline.setCurScore(scoreKT1 + scoreKT2);
                            break;
                        }
                        discipline.setCurScore(score);
                        break;
                    case TRADITIONAL_SCORE:
                        break;
                    case LITERAL_SCORE:
                        break;

                }
            }
            journal.addDiscipline(discipline.getName(), discipline);
        }

        return journal;
    }

    //private boolean parseDisciplines(Journal journal, Html)

    private void parseAcademicDiscipline(HtmlDivision item, Journal journal){
        AcademicDiscipline discipline = null;

        //Перебираем элемент аккордиона
        for(DomNode node : item.getChildren()){
            HtmlDivision div = (HtmlDivision)node;

            //получаем название предмета к конторому относятся баллы и находим его в журнале
            if(div.getAttribute("class").equals("title1")){
                //TODO Сделать что-то по-быстрее ибо дочерних элемента всего два, возможно даже только один
                for(DomNode domNode : div.getChildren()){
                    if(domNode instanceof HtmlStrong){
                        String name = domNode.getTextContent();
                        discipline = journal.getDiscipline(name);


                    }
                }
            //Проверка на случай если в журнале такой дисциплины нет
            }else if(discipline != null){
                //Получаем первый и единственный дочерний элемент - таблицу с баллами
                DomNode nd = div.getFirstChild();
                if(nd instanceof HtmlTable){ //проверяем правда ли это таблица или нас наебали
                    HtmlTable table = (HtmlTable)nd;

                    //Перебираем строки таблицы
                    for(HtmlTableRow row : table.getRows()){

                        //TODO проверка на контрольную точку, как-то припихнуть
                        //класс ControlPoint или выкинуть его за ненадобностью
                        RatingItem ratingItem = new RatingItem();

                        List<HtmlTableCell> cells = row.getCells();
                        for(int i = 0; i < cells.size(); i++){
                            switch (AccordionCell.values()[i]){
                                case NUMBER:
                                    break;
                                case FORM_OF_CONTROL:
                                    HtmlTableCell cell = cells.get(i);
                                    ratingItem.setFormOfControl(cell.getFirstChild().getTextContent());
                                    ratingItem.setTheme(cell.getLastChild().getTextContent());
                                    break;
                                case DATE:
                                    ratingItem.setDate(cells.get(i).asText());
                                    break;
                                case SCORE:
                                    try{
                                        ratingItem.setScore(Integer.parseInt(cells.get(i).asText()));

                                    }catch(NumberFormatException ex){
                                        ratingItem.setScore(0);
                                    }
                                    break;
                                case MAX_SCORE:
                                    try{
                                        ratingItem.setMaxScore(Integer.parseInt(cells.get(i).asText()));

                                    }catch(NumberFormatException ex){
                                        ratingItem.setMaxScore(0);
                                    }
                                    break;


                            }
                        }
                        discipline.addRatingItem(ratingItem);


                    }

                }
            }


        }


    }

    private enum AttestationCell{
        DISCIPLINE_AND_ATTESTFORM,
        KT1_SCORE,
        KT1_MAX_SCORE,
        KT2_SCORE,
        KT2_MAX_SCORE,
        ATTESTATION_SCORE_AND_MAXSCORE,
        END_SCORE,
        TRADITIONAL_SCORE,
        LITERAL_SCORE
    }

    private enum AccordionCell{
        NUMBER,
        FORM_OF_CONTROL,
        DATE,
        SCORE,
        MAX_SCORE
    }
}
