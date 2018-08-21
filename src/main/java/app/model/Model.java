package app.model;

import app.dao.DBmanager;
import app.entity.*;

import javax.persistence.PersistenceException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class Model {

    private static Model instance = new Model();
    public DBmanager dbManager = null;
    public boolean updatedTaskTable;



    private List<Task> tasksList;
    private List<ExtendedTask> extendedTasksList;
    private Hashtable<Integer, Integer> assigneeMap = null;//department - assignee map


    public static Model getInstance() {
        return instance;
    }

    private Model()  {
        //DB connection
        try{

            dbManager = new DBmanager();
        }
        catch(PersistenceException pE){
            System.out.println("Error in DB connection: "+pE.getMessage());
        }

    }
    private void createAssigneeMap(){
        List<PizzaGuy> assignees = dbManager.obtainAllFromTable(PizzaGuy.class);
        this.assigneeMap = new Hashtable<>();
        System.out.println("Assignee Map:");
        if(assignees == null){
            System.out.println("NULL MAP");
        }
        assignees.forEach((a)-> System.out.println("A"+a.getDepart_id()+" "+a.getPizzaguy_id()));
        for(PizzaGuy pg : assignees){
            this.assigneeMap.put(pg.getDepart_id(), pg.getPizzaguy_id());
        }
        System.out.println("MAP");
        System.out.println(assigneeMap);





    }

    public int getAssignee(int departId){
        if(this.assigneeMap==null){
            //get from DB
            createAssigneeMap();
        }

        return this.assigneeMap.get(departId);

    }

//    public List<Task> getTasksList(){
//        if((tasksList==null) || (!updatedTaskTable)){
//            tasksList = dbManager.obtainAllFromTable(Task.class);
//            updatedTaskTable = true;
//        }
//        return tasksList;
//    }

    public void addTask(Task task){
        dbManager.insertEntity(task);
    }

    public Calendar getDueDate(Calendar createDate, Priority priority){
        Calendar dueDate = (Calendar)createDate.clone();
        switch (priority){
            case blocker: dueDate.add(Calendar.DAY_OF_YEAR,3);break;
            case critical: dueDate.add(Calendar.DAY_OF_YEAR,5);break;
            case major: dueDate.add(Calendar.DAY_OF_YEAR,8);break;
            case normal: dueDate.add(Calendar.DAY_OF_YEAR,15);break;
            case minor: dueDate.add(Calendar.DAY_OF_YEAR,40);break;
        }
        return dueDate;
    }

    public void extendedTasks(){
        //dbManager.getExtendedTasks();
        List<ExtendedTask> etasksList = dbManager.obtainAllFromTable(ExtendedTask.class);
        System.out.println("Hi");

    }
    public List<ExtendedTask> getExtendedTasksList(){
        if((tasksList==null) || (!updatedTaskTable)){
            extendedTasksList = dbManager.obtainAllFromTable(ExtendedTask.class);
            extendedTasksList.sort(Comparator.comparing(ExtendedTask::getCreatedate));
            updatedTaskTable = true;
        }
        return extendedTasksList;
    }


    public void removeTask(int id){
        dbManager.removeEntity(Task.class, id);
    }



    private boolean validateStatus(Status oldStatus, Status newStatus){
        switch (oldStatus){
            case Open: switch (newStatus){
                case Accepted: return true;
                case AIR: return true;
                case Move: return true;
                default: return false;
            }
            case Accepted: switch (newStatus){
                case Assessment: return true;
                case Move: return true;
                default: return false;
            }
            case Assessment: switch (newStatus){
                case In_progress: return true;
                case AIR: return true;
                case Move: return true;
                default: return false;
            }
            case In_progress: switch (newStatus){
                case Technical_review: return true;
                case AIR: return true;
                case Resolved: return true;
                case Move: return true;
                default: return false;
            }
            case Technical_review: switch (newStatus){
                case In_progress: return true;
                default: return false;
            }
            case AIR: switch (newStatus){
                case In_progress: return true;
                default: return false;
            }
            case Resolved: switch (newStatus){
                case Rejected: return true;
                case Closed: return true;
                default: return false;
            }
            case Rejected: switch (newStatus){
                case In_progress: return true;
                default: return false;
            }
            case Closed: switch (newStatus){
                case Reopen: return true;
                default: return false;
            }
            case Reopen: switch (newStatus){
                case Closed: return true;
                default: return false;
            }
            case Move: switch (newStatus){
                default: return false;
            }

        }
        return false;
    }

    public void changeStatus(int id, Status status){
//        Task updTask;
//        for(Task task: tasksList){
//            if(task.getId()==id){
//                updTask = task;
//                updTask.setStatus(status);
//                dbManager.updateEntity(updTask);
//                return;
//            }
//        }
        Task updTask;
        for(ExtendedTask task: extendedTasksList){
            if(task.getId()==id){
                updTask = task.getSimpleTask();
                if(!validateStatus(updTask.getStatus(), status )){
                    break;
                }
                updTask.setStatus(status);
                dbManager.updateEntity(updTask);
                return;
            }
        }
        throw new IllegalArgumentException("Wrong ID value or status value");

    }



    public void clearResources(){
        dbManager.close();
    }
}
//TODO разные move
//TODO close DB connection
//TODO сдедать выпадающие исключения