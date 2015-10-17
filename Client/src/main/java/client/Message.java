package client;

import java.util.Date;

import javafx.beans.property.SimpleStringProperty;

public class Message implements Comparable<Message>{
	
	private final SimpleStringProperty date;
    private final SimpleStringProperty author;
    private final SimpleStringProperty message;
    private Long dateMilli;

    public Message(Long date, String author, String message) {
    	this.dateMilli = date;
    	Date temp = new Date(this.dateMilli);
        this.date = new SimpleStringProperty(temp.toGMTString());
        this.author = new SimpleStringProperty(author);
        this.message = new SimpleStringProperty(message);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

	public Long getDateMilli() {
		return dateMilli;
	}

	public void setDateMilli(Long dateMilli) {
		this.dateMilli = dateMilli;
	}

	@Override
	public int compareTo(Message arg0) {
		Date d = new Date(getDateMilli());
		Date d2 = new Date(arg0.getDateMilli());
		return d.compareTo(d2);
	}
}