package com.example.asus.client;

import com.example.asus.client.entity.User;
import com.example.asus.service.BootService;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class ManageClientThread {
    public static HashMap<User,Client> clients =new HashMap<User,Client>();
    public static HashMap<User,BootService> services=new HashMap<>();
    public static void addClientConServerThread(User user,Client client){
        clients.put(user,client);
    }

    public static Client getClientConServerThread(User user){
//        return  client.get(user);
        return (Client) clients.get(0);
    }

    public static void addBootService(User user, BootService service){
        services.put(user,service);
    }
    public static BootService getBootService(User user){
        return (BootService)services.get(user);
    }
}
