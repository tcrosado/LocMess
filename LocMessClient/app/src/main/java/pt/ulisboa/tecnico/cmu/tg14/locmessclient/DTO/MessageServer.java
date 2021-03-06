package pt.ulisboa.tecnico.cmu.tg14.locmessclient.DTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import pt.ulisboa.tecnico.cmu.tg14.locmessclient.DataObjects.Profile;

/**
 * Created by basilio on 12-05-2017.
 */

public class MessageServer {

    private UUID id;
    private long creationTime;
    private long startTime;
    private long endTime;
    private String content;
    private String publisher;
    private String location;
    List<Profile> whiteList;
    List<Profile> blackList;


    public MessageServer(UUID id, long creationTime, long startTime, long endTime, String content, String publisher, String location, List<Profile> whiteList, List<Profile> blackList) {
        this.id = id;
        this.creationTime = creationTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.content = content;
        this.publisher = publisher;
        this.location = location;
        this.whiteList = whiteList;
        this.blackList = blackList;
    }

    public List<Profile> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<Profile> whiteList) {
        this.whiteList = whiteList;
    }

    public List<Profile> getBlackList() {
        return blackList;
    }

    public void setBlackList(List<Profile> blackList) {
        this.blackList = blackList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}
