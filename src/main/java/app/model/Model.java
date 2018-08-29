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

    /**
     * Default constructor, connects to the DB and creates dbmanager
     */
    private Model()  {
        //DB connection
        try{
            dbManager = new DBmanager();
        }
        catch(PersistenceException pE){
            System.out.println("Error in DB connection: "+pE.getMessage());
        }

    }

    /**
     * Creates Assignee Map from the PizzaGuy Table
     */
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

    /**
     * @param departId the id of department
     * @return the default assignee of the given department
     */
    public int getAssignee(int departId){
        if(this.assigneeMap==null){
            //get from DB
            createAssigneeMap();
        }
        return this.assigneeMap.get(departId);
    }


    /**
     * This method updates assignee-statistics table
     * @param id id of assignee
     * @param status changed status of the task which is under assignee's control
     */
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

    /**
     * This method updates week-statistics table due to the status of the task
     */
    public void updateStatistics(Calendar cal,Status status){

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

    /**
     * Inserts new Task to the DB
     */
    public void addTask(Task task){

        dbManager.insertEntity(task);
        updatedTaskTable = false;
    }

    /**
     * Creates and returns DueDate of the Task according to business logic
     */
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

    /**
     * Updates TaskList from DB request if already created.
     * Otherwise, creates TaskList from DB request if
     */
    private void updateTaskList(){
        if((extendedTasksList==null) || (!updatedTaskTable)){
            extendedTasksList = dbManager.obtainAllFromTable(ExtendedTask.class);
            extendedTasksList.sort(Comparator.comparing(ExtendedTask::getCreatedate));
            updatedTaskTable = true;
        }
    }

    /**
     * @return updated List of ExtendedTasks
     */
    public List<ExtendedTask> getExtendedTasksList(){
        updateTaskList();
        return extendedTasksList;
    }

    /**
     * This method removes a task from the Task table
     * @param id id of the removed tasks
     */
    public void removeTask(int id){
        dbManager.removeEntity(Task.class, id);
    }


    /**
     * Validates the ability to change oldStatus for newStatus.
     * Determined by business logic
     * @return true of false due to check
     */
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

    /**
     * This method changes the status of the task with given id
     * and updates assignee-statistics
     * @param id id of the task to be changed
     * @param status new value of the status of this task
     * Throws IllegalArgumentException if no task with given id found or if
     * new status doesn't match the current one
     */
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
                updatedTaskTable = false;
                if(validateStatusUpdate(oldStatus,status))
                    updateAssigneeStatistics(updTask.getAssignee(), status);
                return;
            }
        }
        throw  new IllegalArgumentException("Wrong ID");

    }


    /**
     * Validates if it needs to update Statistics after changing status
     */
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


    /**
     * @param id the taskId of Task
     * @return the ArrayList of Status-objects, which can change the current status
     *  of the Task
     *  Throw IllegalArgumentException if task with given id npt found
     */
    public ArrayList<Status> getAvailableStatus(int  id) {
        updateTaskList();

        Status status = null;
        ArrayList<Status> avStatus = new ArrayList<>();
        for (ExtendedTask task : extendedTasksList) {
            if (task.getId() == id) {
                status = task.getStatus();

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
        }
        throw new IllegalArgumentException("Wrong ID");
    }


    /**
     * Gets statics from DB request and returns WeekStatistics list
     * @return updated WeekStatistics List
     */
    public ArrayList<ExtendedWeekStatistics> getStatistics(){

        List<WeekStatistics> wsList = dbManager.obtainAllFromTable(WeekStatistics.class);
        wsList.sort(WeekStatistics::compareTo);
        ArrayList<ExtendedWeekStatistics> ewsList = dbManager.getExtendedStatistics(wsList);
        return ewsList;
    }


    /**
     * Gets ExtendedAssigneeStatistics from DB, sorts it and returns it
     * @return sorted List of ExtendedAssigneeStatistics
     */
    public List<ExtendedAssigneeStatistics> getExtendedAssigneeStatistics(){

        List<ExtendedAssigneeStatistics> asList = dbManager.obtainAllFromTable(ExtendedAssigneeStatistics.class);

        asList.sort(ExtendedAssigneeStatistics::compareTo);

        return asList;
    }


    /**Returns ArrayList of available staff to be an assignee of the task.
     *Check the depart_id of the task and then return all staff from this department
     *Throws IllegalArgumentException if no task with given id found
     **/
    public ArrayList<ExtendedAssignee> getAvailableAssignees(int id){

        updateTaskList();
        int depart;
        ArrayList<ExtendedAssignee> avAssignee = new ArrayList<>();

        for (ExtendedTask task : extendedTasksList) {
            if (task.getId() == id) {
                depart = task.getDepart_id();

                for(ExtendedAssignee se: dbManager.obtainAllFromTable(ExtendedAssignee.class)){
                    if(se.getDepart_id() == depart){
                        avAssignee.add(se);
                    }
                }
                return avAssignee;

            }
        }
        throw  new IllegalArgumentException("Wrong ID");
    }


    /**
     * This method changes the assignee of the task with given id
     * and updates assignee-statistics
     * @param taskId id of the task to be changed
     * @param aID new value of the assignee id of this task
     * Throws IllegalArgumentException if no task with given id found
     */
    public void changeAssignee(int taskId, int aID){
        Task updTask;
        updateTaskList();

        for(ExtendedTask task: extendedTasksList){
            if(task.getId()==taskId){
                updTask = task.getSimpleTask();
                int oldaId = updTask.getAssignee();
                updTask.setAssignee(aID);
                dbManager.updateEntity(updTask);
                this.updatedTaskTable=false;
                switch (updTask.getStatus()){
                    case Closed: return;//no inc statistics if assignee got closed Task
                    default:updateAssigneeStatistics(aID,Status.Open);
                }
                return;
            }
        }
        throw  new IllegalArgumentException("Wrong ID");
    }


    /**
     * @return Depart list obtained from DB
     */
    public ArrayList<Depart> getDepartList(){
        ArrayList<Depart> alDepart = new ArrayList<>();
        alDepart.addAll(dbManager.obtainAllFromTable(Depart.class));
        return alDepart;
    }



    /**
     * Changes depart of the given tasks to depart
     */
    public void changeDepart(int taskId, String depart){
        updateTaskList();
        createAssigneeMap();
        Task updTask;
        int departId;

        //find departId by name
        for(Depart d : getDepartList()){
            if(d.getName().equals(depart)){
                departId = d.getId();
                //find task
                for (ExtendedTask task : extendedTasksList) {
                    if (task.getId() == taskId) {
                        updTask = task.getSimpleTask();

                        updTask.setDepart_id(departId);
                        dbManager.updateEntity(updTask);
                        updatedTaskTable = false;

                        //Set default Assignee
                        changeAssignee(taskId, this.assigneeMap.get(departId));
                        return;

                    }
                }
                throw new IllegalArgumentException("Wrong ID");
            }

        }

        throw new IllegalArgumentException("Wrong Depart Name");


    }


    /**
     * Closes the data base connection and clean up resources
     */
    public void clearResources(){
        dbManager.close();
    }
}

//TODO close DB connection
