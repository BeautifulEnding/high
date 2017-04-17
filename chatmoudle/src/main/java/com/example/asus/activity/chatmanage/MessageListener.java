
package com.example.asus.activity.chatmanage;

import com.example.asus.client.entity.Message;
import com.example.asus.entity.Chat;

/**
 *
 */
public interface MessageListener {
    void processMessage(Chat chat, Message message);
}
