package tpu.ru.filecloudclient.webcenter;

/**
 *
 * @author MrFiring
 * 
 */

public class RatingItem {
    private String formOfControl; //Название формы контроля (ИДЗ, КР и т.п.)
    private int maxScore; //Максимально возможное кол-во баллов
    private int score; //Показывает набранные баллы
    private String date; //Дата проведения
    private String theme;

    public RatingItem(){
        this("N/A", 0, 0, "N/A", "N/A");
    }
    
    public RatingItem(String formOfControl, int score, int maxScore,String date, String theme){
        this.formOfControl = formOfControl;
        this.score  = score;
        this.maxScore = maxScore;
        this.date = date;
        this.theme = theme;
    }
    
    
    //TODO Мб найти другой вариант вывода;
    @Override
    public String toString(){
        return formOfControl + " " + date + " " + score + "/" + maxScore;
    }

    public String getFormOfControl() {
        return formOfControl;
    }

    public void setFormOfControl(String formOfControl) {
        this.formOfControl = formOfControl;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
