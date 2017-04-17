

package com.example.asus.activity.chatmanage;

import com.example.asus.entity.Chat;

public interface ChatManagerListener {

    /**
     * Event fired when a new chat is created.
     *
     * @param chat the chat that was created.
     * @param createdLocally true if the chat was created by the local user and false if it wasn't.
     */
    void chatCreated(Chat chat, boolean createdLocally);
}
