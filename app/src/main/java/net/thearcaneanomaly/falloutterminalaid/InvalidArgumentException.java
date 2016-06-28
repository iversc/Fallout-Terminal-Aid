package net.thearcaneanomaly.falloutterminalaid;

/**
 * Created by Chris on 6/19/2015.
 */
public class InvalidArgumentException extends Exception {
    /**
     *
     */
    private String message = "";

    public void add(String a)
    {
        message += "\n" + a;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public boolean isEmpty()
    {
        return message.equals("");
    }
}