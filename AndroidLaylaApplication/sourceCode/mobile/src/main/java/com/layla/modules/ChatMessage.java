package com.layla.modules;

public class ChatMessage
{
    private String fromName, message;
    private boolean isSelf;

    public ChatMessage(String fromName, String message, boolean isSelf)
    {
        this.fromName = fromName;
        this.message = message;
        this.isSelf = isSelf;
    }

    public String getMessage()
    {
        return message;
    }

    public boolean isSelf()
    {
        return isSelf;
    }

}
