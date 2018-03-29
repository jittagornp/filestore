# filestore

> java library สำหรับ spring framework ใช้เพื่อ อ่าน/เขียน/อัพโหลด file

# แนวคิดการออกแบบ  

> ออกแบบให้ Simple ที่สุด และมีประสิทธิภาพมากที่สุด  

# โครงสร้างการจัดเก็บไฟล์  

### Store Path  

จะเก็บเป็น

> ../{userId}/{createdDate}/{uuid}/file.{extension}  
  
มุมมอง tree จะเป็น  
  
> ../{userId}  
------ + {createdDate}  
------------- + {uuid}  
------------------ + file.{extension}  
------------- + {uuid}  
------------------ + file.{extension}  
------------- + {uuid}  
------------------ + file.{extension}  

1. เหตุผลที่ต้อง ขึ้นต้นด้วย `userId` 
- เพราะทำให้เช็คสิทธิ์การเข้าถึงไฟล์ได้ง่าย  โดยไม่จำเป็นต้องไปอ่านค่ามาจาก database เพื่อเช็คสิทธิ์ (เพราะฉะนั้นจะอ่าน/เขียน/ลบ หรือทำอะไรเกี่ยวกับไฟล์ได้เร็ว)  
- สามารถเอามาหา volumn ได้ว่า user คนนี้ใช้ storage ไปแล้วเท่าไหร่  
- และเช็ค หรือทำอะไรกับไฟล์ เราสามารถทำเป็นราย user ได้ง่าย เช่นการ migrate ข้อมูลเฉพาะ user เป็นต้น  

2. ที่คั่นด้วยวัน `createdDate` format yyyy-MM-ddd 
- เพื่อเอาไว้ sort ด้วยวันที่ 
- มองด้วยตาเปล่า ก็รู้ว่าวันนี้ user upload มาเท่าไหร่
- สามารถเช็ค หรือทำอะไรกับข้อมูลวันนั้นของ user ได้ง่าย  
  
3. ต่อมา `uuid` เพื่อการันตีความ unique 

4. ที่ตั้งชื่อ file เป็น `file` เพราะไม่อยากให้เกิดปัญหาชื่อไฟล์ภาษาไทย หรืออักขระพิเศษบางตัว ที่อาจทำให้ไฟล์เสียหาย  ซึ่งถ้าอยากได้ชื่อไฟล์จริงๆ ที่ upload มาให้ไปเอาที่ database (column display name)    

5. 1 directory uuid จะจัดเก็บไฟล์เพียงแค่ 1 ไฟล์ เพื่อให้ง่ายต่อการ manage  

### Access Path  

TODO  

# Output การ upload  

```json
{
    "displayName": "เอกสารการจัดตั้งหน่วยงาน (ข้อ.3).pdf",
    "mimeType": "application/pdf",
    "fileSize": 17759,
    "displayFileSize": "17 KB",
    "accessPath": "/api/file/temp/2018-03-29/6706609d98a3484fa3e0d5c5c5e0657a/เอกสารการจัดตั้งหน่วยงาน_ข้อ_3_.pdf",
    "storePath": "/temp/1/2018-03-29/6706609d98a3484fa3e0d5c5c5e0657a/file.pdf",
    "createdDate": "2018-03-29T18:34:52.94",
    "numberOfPages": 1,
    "numberOfPictures": 0,
    "userId": "1"
}
```
  
numberOfPages - กรณีเป็น pdf จะทำการนับหน้าให้อัตโนมัติ  
numberOfPictures - กรณีที่เป็นรูปภาพ attribute นี้เป็นจะเป็น 1 เสมอ 

# วิธีใช้งาน

### 1. ให้ extends abstract class `StorePathFileRequestConverterAdapter`

ไว้ใช้สำหรับแปลง local path file     
โดยให้ implement method   

```
- getPrefix()  คือ directory ที่เอาไว้เก็บไฟล์ เช่น /temp หรือ /perm เป็นต้น
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempStorePathFileRequestConverter.java)

### 2. ให้ extends abstract class `FileManagerAdapter`

โดยให้ implement method 

```
- getStorePathFileRequestConverter() คือ ตัวแปลง local path จากข้อ 1
- getRootPath()  คือ root directory ของ file เราจะเก็บ file upload นี้ไว้ที่ไหน เช่น /home/efiling เป็นต้น   
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempFileManager.java)

### 3. ให้ extends abstract class `AccessPathFileRequestConverterAdapter`

เอาไว้ convert access path -> file request -> local path file   
โดยให้ implement method 

```
- getPrefix() ตัวอย่างเช่น /api/v1/file/temp หรือ /api/v1/perm เป็นต้น 
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempAccessPathFileRequestConverterAdapter.java)  

### 4. ให้ extends abstract class `FileUploaderAdapter`

เป็น manual upload file เผื่อเอาไว้ upload file ผ่าน java code   
โดยให้ implement method 

```
- getFileManager()  คือ file manager จากข้อ 2 
- getAccessPathFileRequestConverter() คือ ตัวแปลง access path จากข้อ 3
- getStorePathFileRequestConverter()  คือ คือ ตัวแปลง local path จากข้อ 1
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempFileUploader.java)  

### 5. หากต้องการทำ controller เพื่อ upload file ให้ extends abstract class `FileHandlerAdapter`

โดยให้ implement method 

```
- getFileManager()  คือ file manager จากข้อ 1 
- getAccessPathFileRequestConverter() คือ ตัวแปลง access path จากข้อ 3
- getFileUploader()  คือ file uploader จากข้อ 4   
- getUserId()  คือ userId ปัจจุบันที่กำลัง Login อยู่  
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempFileHandlerCtrl.java)  

# ตัวอย่างการใช้งาน 

https://github.com/pamarin-tech/filestore-example 
