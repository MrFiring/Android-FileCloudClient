package tpu.ru.filecloudclient.webcenter;

/**
 *
 * @author MrFiring
 */
public class ControlPoint extends RatingItem {
    private int num; //Показывает номер контрольной точки 1,2;
    
    
    public ControlPoint(){}
    
    public ControlPoint(int num, int score, int maxScore){
        super("Контрольная точка №" + num, score, maxScore, "");
        this.num = num;
    }
    
}
