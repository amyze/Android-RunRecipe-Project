package xzheng2.cmu.edu.hw3.Model;

/**
 * Created by zhengqian1 on 6/5/16.
 */
public class Event {

    private String id;


    private String startTime;
    private String endTime;


    public String getStartTime() {
        return startTime;
    }
    public void setDateTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getId(){
        return id;
    }

    public String setId(String id){
        return id;
    }

    public String toString() {
        return id;
    }
}
