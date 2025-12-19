package com.bolota.springnas.Entities;

import java.util.HashMap;

public class UUIDEntity {
    private static final char[] UUID_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    HashMap<String,FileUploadEntity> uuidMap;
    public UUIDEntity(){
        uuidMap = new HashMap<>();
    }
    public void insertFile(String uuid, FileUploadEntity file){
        uuidMap.put(uuid, file);
    }
    public FileUploadEntity returnFile(String uuid){
        return uuidMap.get(uuid);
    }
    public void removeFile(String uuid){
        uuidMap.remove(uuid);
    }
    //
    //se contem, retorna true
    public boolean checkUUID(String uuid){
        return uuidMap.containsKey(uuid);
    }
    public static String generateUUID(UUIDEntity list){
        StringBuilder s;
        do{
            s = new StringBuilder();
            for (int i = 0; i < 9;i++){
                s.append(UUID_CHARS[(int)((Math.random() * 100) % UUID_CHARS.length)]);
            }
        } while (list.checkUUID(s.toString()));
        return s.toString();
    }
}
