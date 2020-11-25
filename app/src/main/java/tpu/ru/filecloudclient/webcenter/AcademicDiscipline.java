package tpu.ru.filecloudclient.webcenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MrFiring
 */
class AcademicDiscipline {
    private String name; //Название предмета
    private String formOfControl; //Форма контроля (Экзамен, Дифф. зачет, зачет);
    
    private int curScore; //Баллы выставленные в аттестации
    private int calcScore; //Баллы по журналу
    private int maxScore;//Максимальнный балл
    
    private List<RatingItem> rating; 
    
    private List<Teacher> teachers;
    
    
    
   public AcademicDiscipline(){
       this("N/A", "N/A");
   } 
   
   public AcademicDiscipline(String name, String formOfControl){
      this(name, formOfControl, 0, 0);
   }
   
   public AcademicDiscipline(String name, String formOfControl, int curScore, int maxScore){
       this.name = name;
       this.formOfControl = formOfControl;
       this.curScore = curScore;
       this.maxScore = maxScore;
       rating = new ArrayList<>();
       teachers = new ArrayList<>();
   }
    
   public RatingItem getRatingItem(int index){
       try{
        return rating.get(index);
       }catch(IndexOutOfBoundsException ex){
           System.out.println(ex.toString());
       }
       return null;
   }

   public boolean addRatingItem(RatingItem item){
       
       return rating.add(item);
   }

   public boolean addTeacher(Teacher teacher){
       return teachers.add(teacher);
   }

   public Teacher getTeacher(int index){
       try{
           return teachers.get(index);
       }catch(IndexOutOfBoundsException ex){
           Log.e(this.getClass().getSimpleName(), ex.toString());
       }
       return new Teacher("N/A", "N/A");
   }
   
    public int getCalcScore() {
        return calcScore;
    }

    public void setCalcScore(int calcScore) {
        this.calcScore = calcScore;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormOfControl() {
        return formOfControl;
    }

    public void setFormOfControl(String formOfControl) {
        this.formOfControl = formOfControl;
    }

    public int getCurScore() {
        return curScore;
    }

    public void setCurScore(int curScore) {
        this.curScore = curScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
}
