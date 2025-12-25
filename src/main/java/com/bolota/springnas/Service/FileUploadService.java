package com.bolota.springnas.Service;

import com.bolota.springnas.Entities.UuidEntity;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

public class FileUploadService{
    public static String getFilePath(String fileName){
        String path = System.getProperty("user.dir") + "/Files/uploads" + '/' + fileName;
        if (path.indexOf('.') != -1){
            return path.substring(0,path.indexOf('.'));
        }
        return System.getProperty("user.dir") + "/Files/uploads" + '/' + fileName;
    }

    public static String getTempPath(String uuid){
        return System.getProperty("user.dir") + "/Files/tempUploads" + '/' + uuid;
    }
    public static boolean createFolder(String fileName, String uuid){
        File temp_dir = new File(System.getProperty("user.dir")+"/Files/tempUploads");
        File upload_dir = new File(System.getProperty("user.dir")+"/Files/uploads");
        if (!upload_dir.exists()){
            upload_dir.mkdirs();
        }
        if (!temp_dir.exists()){
            temp_dir.mkdirs();
        }
        if (new File(getFilePath(fileName)).mkdir() && new File(getTempPath(uuid)).mkdir()){
            return true;
        }
        return false;
    }
    public static void createTempFile(String uuid, long chunkNumber, MultipartFile file){
        File fl = new File(getTempPath(uuid) + "/" + uuid + ".part"+chunkNumber);
        try(FileOutputStream f = new FileOutputStream(fl)){
            try(BufferedOutputStream b = new BufferedOutputStream(f)){
                b.write(file.getBytes());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void createRealFile(String uuid, String fileName){
        int nmr = 1;
        String path = getTempPath(uuid) + "/" + uuid + ".part";
        File uploadedFile = new File(getFilePath(fileName) + "/" + fileName);
        File tempFile;
        byte[] fileBytes;
        try (FileOutputStream trueFile = new FileOutputStream(uploadedFile)){
            try(BufferedOutputStream trueBuffered = new BufferedOutputStream(trueFile)){
                tempFile = new File(path + nmr);
                while (tempFile.exists()){
                    try (FileInputStream tempStream = new FileInputStream(tempFile)){
                        try(BufferedInputStream b = new BufferedInputStream(tempStream)){
                            fileBytes  = new byte[UuidEntity.fileChunkSize];
                            int size = b.read(fileBytes);
                            if (size ==-1){
                                continue;
                            }
                            trueBuffered.write(fileBytes,0,size);
                            nmr++;
                            tempFile.delete();
                            tempFile = new File(path + nmr);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tempFile = new File(getTempPath(uuid));
        tempFile.delete();
    }
}