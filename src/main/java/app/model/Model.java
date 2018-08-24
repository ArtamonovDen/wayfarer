package app.model;

import app.dao.DBmanager;
import app.entity.*;
import app.statistics.ExtendedWeekStatistics;
import app.statistics.StatisticsPerWeek;

import javax.persistence.PersistenceException;
import java.util.*;

public class Model {

    private static Model instance = new Model();
    public DBmanager dbManager = null;
    public boolean updatedTaskTable;
    public boolean updatedStatistics;


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
        if(assignees == null){
            System.out.println("NULL MAP");
        }

        for(PizzaGuy pg : assignees){
            this.assigneeMap.put(pg.getDepart_id(), pg.getPizzaguy_id());
        }
    }

    public int getAssignee(int departId){
        if(this.assigneeMap==null){
            //get from DB
            createAssigneeMap();
        }

        return this.assigneeMap.get(departId);

    }


    public void updateAssigneeStatistics(int id, Status status){
        AssigneeStatistics as;
        as = dbManager.getById(AssigneeStatistics.class, id);
        if(as==null){
            as = new AssigneeStatistics();
            as.setId(id);
            as.inc(status);
            dbManager.insertEntity(as);
        }
        else{
            as.inc(status);
            dbManager.updateEntity(as);
        }


    }


    public void updateStatistics(Calendar cal,Status status){
        //TODO update statistics

        //Update statistics in the DB
        WeekStatistics ws;

        ws = dbManager.getById(WeekStatistics.class,
                new WeekStatisticsKey (cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR)) );

        if(ws==null){
            ws = new WeekStatistics();
            ws.setKey(new WeekStatisticsKey (cal.get(Calendar.YEAR), cal.get(Calendar.WEEK_OF_YEAR)) );
            ws.inc(status);
            dbManager.insertEntity(ws);
        }
        else{
            ws.inc(status);
            dbManager.updateEntity(ws);
        }
        updatedStatistics = false;

    }

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

    private void updateTaskList(){
        if((tasksList==null) || (!updatedTaskTable)){
            extendedTasksList = dbManager.obtainAllFromTable(ExtendedTask.class);
            extendedTasksList.sort(Comparator.comparing(ExtendedTask::getCreatedate));
            updatedTaskTable = true;
        }
    }

    public List<ExtendedTask> getExtendedTasksList(){
        updateTaskList();
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
            case Move_income:switch (newStatus){
                case Open: return true;
                default: return false;
            }

        }
        return false;
    }

    public void changeStatus(int id, Status status){
        Task updTask;
        updateTaskList();

        for(ExtendedTask task: extendedTasksList){
            if(task.getId()==id){
                updTask = task.getSimpleTask();
                if(!validateStatus(updTask.getStatus(), status )){
                    throw  new IllegalArgumentException("Wrong status");
                }
                Status oldStatus = updTask.getStatus();
                updTask.setStatus(status);
                dbManager.updateEntity(updTask);
                if(validateStatusUpdate(oldStatus,status))
                    updateAssigneeStatistics(updTask.getAssignee(), status);
                return;
            }
        }
        throw  new IllegalArgumentException("Wrong ID");

    }

    private boolean validateStatusUpdate(Status oldStatus, Status status) {

        switch (status){
            case Open:
                switch (oldStatus){
                    case Move_income:return false;
                    default:return true;
                }
                default:return true;
        }
    }

    public ArrayList<Status> getAvailableStatus(int  id) {
        updateTaskList();

        Status status = null;
        ArrayList<Status> avStatus = new ArrayList<>();

        for (ExtendedTask task : extendedTasksList) {
            if (task.getId() == id) {
                status = task.getStatus();
                break;
            }
        }

        switch (status) {
            case Open:
                avStatus.addAll(Arrays.asList(Status.Move, Status.Accepted, Status.AIR));
                return avStatus;
            case Accepted:
                avStatus.addAll(Arrays.asList(Status.Move, Status.Assessment));
                return avStatus;
            case AIR:
                avStatus.addAll(Arrays.asList(Status.In_progress));
                return avStatus;
            case Resolved:
                avStatus.addAll(Arrays.asList(Status.Rejected, Status.Closed));
                return avStatus;
            case In_progress:
                avStatus.addAll(Arrays.asList(Status.Move, Status.AIR, Status.Resolved, Status.Technical_review));
                return avStatus;
            case Technical_review:
                avStatus.addAll(Arrays.asList(Status.In_progress));
                return avStatus;
            case Rejected:
                avStatus.addAll(Arrays.asList(Status.In_progress));
                return avStatus;
            case Closed:
                avStatus.addAll(Arrays.asList(Status.Reopen));
                return avStatus;
            case Reopen:
                avStatus.addAll(Arrays.asList(Status.Closed));
                return avStatus;
            case Assessment:
                avStatus.addAll(Arrays.asList(Status.In_progress,Status.AIR,Status.Move));
                return avStatus;
            case Move_income:
                avStatus.addAll(Arrays.asList(Status.Open));
                return avStatus;
            case Move:
                return avStatus;
            default:
                return avStatus;
        }



    }

    public ArrayList<ExtendedWeekStatistics> getStatistics(){

        List<WeekStatistics> wsList = dbManager.obtainAllFromTable(WeekStatistics.class);
        wsList.sort(WeekStatistics::compareTo);
        ArrayList<ExtendedWeekStatistics> ewsList = dbManager.getExtendedStatistics(wsList);
        return ewsList;
    }

    public ArrayList<AssigneeStatistics> getAssigneeStatistics(){

        List<AssigneeStatistics> asList = dbManager.obtainAllFromTable(AssigneeStatistics.class);

        asList.sort(AssigneeStatistics::compareTo);
        ArrayList<AssigneeStatistics> assList =new ArrayList<>();
        assList.addAll(asList);
        return assList;
    }
    public List<ExtendedAssigneeStatistics> getExtendedAssigneeStatistics(){

        List<ExtendedAssigneeStatistics> asList = dbManager.obtainAllFromTable(ExtendedAssigneeStatistics.class);

        asList.sort(ExtendedAssigneeStatistics::compareTo);

        return asList;
    }



    public void clearResources(){
        dbManager.close();
    }
}

//TODO close DB connection
