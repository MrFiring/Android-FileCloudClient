/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpu.ru.filecloudclient.webcenter;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MrFiring
 */
public class Journal {
    private int semester;
    private String dateOfLastUpdate;
    private Map<String,AcademicDiscipline> disciplines;
    
    
    public Journal(){
    }
    
    public Journal(int semester,String dateOfUpdate){
        this.semester = semester;
        this.dateOfLastUpdate = dateOfUpdate;
        this.disciplines = new HashMap<>();
    }
    
    public void addDiscipline(String name,AcademicDiscipline discipline){
        disciplines.put(name,discipline);
    }
    
    public AcademicDiscipline getDiscipline(String name){
        try{
            return disciplines.get(name);
        }catch(IndexOutOfBoundsException ex){
            Log.e(this.getClass().getSimpleName(), ex.toString());
        }
        return new AcademicDiscipline();
    }

    public int getSemester() {
        return semester;
    }
    
    
    
    
    
}
