package client;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.beans.property.SimpleStringProperty;

public class Message implements Comparable<Message>{
	
	private final SimpleStringProperty date;
    private final SimpleStringProperty author;
    private final SimpleStringProperty message;
    private Calendar date_cal;
    
    public Message(Calendar date, String author, String message) {
   	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    	
    	this.date_cal = date;
        this.date = new SimpleStringProperty(sdf.format(date.getTime()));
        this.author = new SimpleStringProperty(author);
        this.message = new SimpleStringProperty(message);
    }

    public Calendar getDate_cal() {
		return date_cal;
	}


	public void setDate_cal(Calendar date_cal) {
		this.date_cal = date_cal;
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

	@Override
	public int compareTo(Message arg0) {
		return this.date_cal.compareTo(arg0.getDate_cal());
	}
}