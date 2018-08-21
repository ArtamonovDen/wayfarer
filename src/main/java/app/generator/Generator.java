package app.generator;

import app.dao.DBmanager;
import app.entity.Priority;
import app.entity.Status;
import app.entity.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Random;

public class Generator{
    final static int depNum = 4;
    final static int custNum = 8;
    final static int statusNum = Status.values().length;
    final static int priorNum = Priority.values().length;



    public static void main(String[] args) {
        int n=20;
        DBmanager Lui = new DBmanager();
        ArrayList<Task>  tasks = new ArrayList<Task>();
         for(int i=1;i<=n;i++){
            tasks.add(generateTask());
        }

        tasks.sort(Comparator.comparing(Task::getCreatedate));
         //ArrayList<EntityObject> eoList = new ArrayList<>(tasks);

        try{
            Lui.insertLEntities(new ArrayList<>(tasks));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            Lui.close();
        }

       Lui.close();
    }


    public static int randBetween(int start, int end,Random random) {
        return start + random.nextInt(end-start);
    }

    public static Task generateTask(){
        Task task = new Task();
        Random random = new Random();
        String name = "Task ";
        Calendar cal = Calendar.getInstance();

        task.setName(name + (char)(random.nextInt(100)+20)+(char)(random.nextInt(100)+20));
        task.setDepart_id(random.nextInt(depNum)+1);
        task.setCustomer_id(random.nextInt(custNum)+1);
        task.setDescription("Test Description");
        task.setAssignee(task.getDepart_id());//very bad stuff
        task.setStatus(Status.getByOrder(random.nextInt(statusNum)+1));
        task.setPriority(Priority.getByOrder(random.nextInt(priorNum)+1));

//        cal.set(Calendar.YEAR, 2017);
//        cal.set(Calendar.MONTH,random.nextInt(12)+1 );
//        cal.set(Calendar.DATE,random.nextInt(29)+1 );
        cal.set(Calendar.YEAR,2017);
        cal.set(Calendar.WEEK_OF_YEAR,randBetween(22,26,random));
        cal.set(Calendar.DAY_OF_WEEK,random.nextInt(29)+1);

        task.setCreatedate(cal.getTime());
        switch (task.getPriority()){
            case normal: cal.add(Calendar.DAY_OF_YEAR, 15); break;
            case major: cal.add(Calendar.DAY_OF_YEAR,8 ); break;
            case minor: cal.add(Calendar.DAY_OF_YEAR, 40); break;
            case blocker: cal.add(Calendar.DAY_OF_YEAR,3); break;
            case critical: cal.add(Calendar.DAY_OF_YEAR, 5); break;
            default:cal.add(Calendar.DAY_OF_YEAR, 15); break;

        }
        task.setDuedate(cal.getTime());






        return task;
    }


}