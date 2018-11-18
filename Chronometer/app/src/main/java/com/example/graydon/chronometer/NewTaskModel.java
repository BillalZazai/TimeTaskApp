package com.example.graydon.chronometer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.util.ArrayList;

public class NewTaskModel extends AppCompatActivity{

    private StoredTaskManager storedTaskManager = new StoredTaskManager();
    private String name;
    private Context context;
    private Event event;
    private Duration startDuration, endDuration;
    private int taskListSize, currentStartTimeHour, currentEndTimeHour, currentStartTimeMinute, currentEndTimeMinute;
    private NewTaskActivity taskActivity;


    /**
     * Constructor for the Task Model. Makes a taskActivity object to access UI related components and send requested information back.
     * @param context
     */
    public NewTaskModel(Context context){
        this.context = context;
        this.taskActivity = new NewTaskActivity();
    }


    /**
     * Returns a boolean value which checks all tasks in a given Event for uniqueness of user entered name.
     * @param event
     * @param taskName
     * @return boolean
     */
    public boolean checkName(Event event, String taskName){

        if(event == null){
            throw new IllegalArgumentException("event cannot be null");
        }
        else if(event.isEmpty()){
            return true;
        }
        else{
            ArrayList<Task> tasks = event.getTasks();
            int numberOfTasks = tasks.size();
            boolean validName = false;
            for(int i = 0; i < numberOfTasks; i++){

                if(event.getTask(i).getName().equals(taskName)){
                    validName = false;
                    Log.d("ghope04999", "checkName: Name is equal to another name");
                    break;
                }
                else{
                    validName = true;
                }
            }
            return validName;
        }
    }


    /**
     * Returns a boolean value to ensure a unique task name.
     * @param task
     * @return boolean
     */
    public boolean checkSavedTasks(Task task){
        boolean canSaveTask = false;
        ArrayList<Task> tasks = storedTaskManager.getAllTasks(context);
        String taskName = task.getName();
        for(int i = 0; i < tasks.size(); i++){

            if(!tasks.get(i).getName().equals(taskName)){
                canSaveTask = false;
                break;
            }
            else{
                canSaveTask = true;
            }
        }
        return canSaveTask;
    }


    /**
     * Returns if the user entered time frame is valid. The user cannot enter conflicting or overlapping times.
     * @param eventPassed
     * @param startDuration
     * @param endDuration
     * @return boolean
     */
    public boolean checkTimeFrame(Event eventPassed, Duration startDuration, Duration endDuration){
        event = eventPassed;
        boolean newEvent = (event == null);

        if(!newEvent){

            if(!event.isEmpty()){
                ArrayList<Task> tasks = event.getTasks();
                boolean validTime = true;

                if(tasks.size() == 0){
                    validTime = true;
                }
                else{
                    taskListSize = tasks.size();
                    this.currentStartTimeHour   = startDuration.getHour();
                    this.currentEndTimeHour     = endDuration.getHour();
                    this.currentStartTimeMinute = startDuration.getMinute();
                    this.currentEndTimeMinute   = endDuration.getMinute();
                    for(int i = 0; i < taskListSize; i++){
                        Task taskAtIteration = tasks.get(i);
                        int startHour   = taskAtIteration.getStartHour();
                        int endHour     = taskAtIteration.getEndHour();

                        if(currentStartTimeHour == -1 && currentStartTimeMinute == -1 && currentEndTimeHour == -1 && currentEndTimeMinute == -1){
                            validTime = false;
                        }

                        if((currentStartTimeHour < startHour) && (currentEndTimeHour > endHour)){
                            validTime = false;
                        }

                        if(((currentStartTimeHour < startHour) && ((currentEndTimeHour > startHour) && (currentEndTimeHour <= endHour))) ||  (startHour < currentStartTimeHour) && ((endHour > currentStartTimeHour) && (endHour <= currentEndTimeHour))){
                            validTime = false;
                        }

                        if(currentStartTimeHour == startHour){
                            validTime = false;
                        }

                        if((currentStartTimeHour > currentEndTimeHour) || ((currentStartTimeHour == currentEndTimeHour) && (currentStartTimeMinute >= currentEndTimeMinute)) ){
                            validTime = false;
                        }

                        if(((currentStartTimeHour > startHour) && (currentStartTimeHour < endHour)) || ((currentEndTimeHour > startHour) && (currentEndTimeHour < endHour))){
                            validTime = false;
                        }
                    }
                }
                return validTime;
            }
            else{
                return true;
            }
        }
        else{
            return true;
        }
    }


    /**
     * Accessing the stored task manager to save the specific task.
     * @param context
     * @param task
     */
    public void saveTask(Context context, Task task){
        storedTaskManager.addTask(context, task);
    }


    /**
     * Setting the task name.
     * @param name
     */
    public void setTaskName(String name){

        if(name == null){
            throw new IllegalArgumentException();
        }
        else{
            this.name = name;
        }
    }


    /**
     * Accessing the duration object to set the start time value.
     * @param startDuration
     */
    public void setStart(Duration startDuration){

        if(startDuration != null){
            this.startDuration = startDuration;
        }
        else{
            throw new IllegalArgumentException("Duration cannot be null");
        }
    }


    /**
     * Accessing the duration object to set the end time value.
     * @param endDuration
     */
    public void setEnd(Duration endDuration){

        if(endDuration != null){
            this.endDuration = endDuration;
        }
        else{
            throw new IllegalArgumentException("Duration cannot be null");
        }
    }
}
