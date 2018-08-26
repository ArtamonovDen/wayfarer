package app;

import app.dao.DBmanager;
import app.entity.WeekStatistics;
import app.entity.WeekStatisticsKey;
import app.statistics.StatisticsPerWeek;

import java.util.ArrayList;
import java.util.Calendar;

public class App {
    public static void main(String[] args) {
        DBmanager Eddard = new DBmanager();
        WeekStatistics ws = new WeekStatistics();


        try {
            Eddard.obtainAllFromTable(ws.getClass()).forEach((t)-> System.out.println(t.getKey().getYear()+" "+t.getKey().getWeek()));

        }catch (Exception e ){
            System.out.println(e.getMessage());
            System.out.println("Not Got");
        }
        Eddard.close();


//        try{
////start day - begins with Monday!
////the fisrt week always begins with the 1st Jan
////Available to set as the date yyyy-m-d or yyyy-weak-Monday
////January is 0 !
//            System.out.println("Start");
//            Calendar cal1 = Calendar.getInstance();
//            cal1.set(Calendar.YEAR,2017);
////cal1.set(Calendar.WEEK_OF_YEAR,1);
////cal1.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);//so this week begins with 26th Dec 2016
//
//            cal1.set(Calendar.WEEK_OF_YEAR,2);
//            cal1.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
//
//
//
////cal1.set(Calendar.DAY_OF_WEEK,2);//1st - sunday, 2nd - monday
//
//            Calendar cal2 = Calendar.getInstance();
//            cal2.set(Calendar.YEAR,2017);
//            cal2.set(Calendar.WEEK_OF_YEAR,25);
//            cal2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//// cal2.set(Calendar.DAY_OF_WEEK,1);
//            System.out.println("Start date "+ cal1.getTime());
//            System.out.println("Finish date "+cal2.getTime());
//
//
//
//            ArrayList<StatisticsPerWeek> stata = Eddard.getTasksOverPeriod(cal1,cal2);
//
//            stata.forEach((s)-> System.out.println(s));
//
////
//
//
////
////
//        }
//        catch (Exception e){
//            System.out.println("ERROR");
//            System.out.println(e.getMessage());
//            System.out.println(e.getCause());
//
//        }
//        finally {
//
//            Eddard.close();
//        }
//
//    }
    }
}
