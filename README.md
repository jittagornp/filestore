# filestore

> java library สำหรับ spring framework ใช้เพื่อ อ่าน/เขียน/อัพโหลด file

# โครงสร้างการจัดเก็บไฟล์  

จะเก็บเป็น

> ../{userId}/{createdDate}/{uuid}/file.{extension}  
  
มุมมอง tree จะเป็น  
  
> ../{userId}  
------ + {createdDate}  
----------- + {uuid}  
-------------- + file.{extension}  
----------- + {uuid}  
-------------- + file.{extension}  
----------- + {uuid}  
-------------- + file.{extension}  

1. เหตุผลที่ต้อง ขึ้นต้นด้วย `userId` 
- เพราะทำให้เช็คสิทธิ์การเข้าถึงไฟล์ได้ง่าย  โดยไม่จำเป็นต้องไปอ่านค่ามาจาก database เพื่อเช็คสิทธิ์  
- สามารถเอามาหา volumn ได้ว่า user คนนี้ใช้ storage ไปแล้วเท่าไหร่  
  
2. ที่คั่นด้วยวัน `createdDate` format yyyy-MM-ddd เพื่อเอาไว้ sort ด้วยวันที่ (มองด้วยตาเปล่า ก็รู้ว่าวันนี้ user upload มาเท่าไหร่)  
  
3. ต่อมา `uuid` เพื่อการันตีความ unique 

4. ที่ตั้งชื่อ file เป็น `file` เพราะไม่อยากให้เกิดปัญหาชื่อไฟล์ภาษาไทย หรืออัขระพิเศษบางตัว ที่อาจทำให้ไฟล์เสียหาย  ซึ่งถ้าอยากได้ชื่อไฟล์จริงๆ ที่ upload มาให้ไปเอาที่ database (column display name)    

5. 1 directory uuid จะจัดเก็บไฟล์เพียงแค่ 1 ไฟล์ เพื่อให้ง่ายต่อการ read file  

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

เป็น manual upload file เผื่อเอาไว้ upload file ผ่าน java code   
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
