# filestore

> java library สำหรับ spring framework ใช้เพื่อ อ่าน/เขียน/อัพโหลด file

# วิธีใช้งาน

### 1. ให้ extends abstract class `FileManagerAdapter`

โดยให้ implement method 

```
- getRootPath()  คือ root directory ของ file คือเราจะเก็บ file upload นี้ไว้ที่ไหน เช่น /tmp เป็นต้น 
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempFileManager.java)

### 2. ให้ extends abstract class `ApiPathFileRequestConverterAdapter`

เอาไว้ convert api path -> file request -> local path file   
โดยให้ implement method 

```
- getApiPrefix() ตัวอย่างเช่น /file/temp หรือ /api/v1/file/temp เป็นต้น 
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempApiPathFileRequestConverter.java)  

### 3. ให้ extends abstract class `FileUploaderAdapter`

โดยให้ implement method 

```
- getFileManager()  คือ file manager จากข้อ 1 
- getApiPathFileRequestConverter() คือ api path file request convert จากข้อ 2 
- getUserId()  คือ userId ปัจจุบันที่กำลัง Login อยู่ 
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempFileUploader.java)  

### 4. หากต้องการทำ controller เพื่อ upload file ให้ extends abstract class `FileHandlerAdapter`

โดยให้ implement method 

```
- getFileManager()  คือ file manager จากข้อ 1 
- getApiPathFileRequestConverter() คือ api path file request convert จากข้อ 2 
- getFileUploader()  คือ file uploader จากข้อ 3 
- getUserId()  คือ userId ปัจจุบันที่กำลัง Login อยู่  
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempFileHandlerCtrl.java)  

# ตัวอย่างการใช้งาน 

https://github.com/pamarin-tech/filestore-example 
