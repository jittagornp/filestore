# filestore

[![Build Status](https://travis-ci.org/pamarin-tech/filestore.svg?branch=master)](https://travis-ci.org/pamarin-tech/filestore)  

> java library สำหรับ spring framework ใช้เพื่อ อ่าน/เขียน/อัพโหลด (local) file

# แนวคิดการออกแบบ  

> ออกแบบให้ Simple ที่สุด และมีประสิทธิภาพมากที่สุด  

- เรียบง่าย สามารถอ่าน และทำความเข้าใจได้ง่าย  
- ปลอดภัย สามารถจำกัดสิทธิ์การเข้าถึง file ได้ด้วยโครงสร้างไฟล์เอง  
- เร็ว ไม่จำเป็นต้องไปเช็คสิทธิ์จาก database  

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

# โครงสร้างการจัดเก็บไฟล์  

### Store Path  

File System จะเก็บเป็น  

> ../{context}/{userId}/{createdDate}/{uuid}/file.{extension}  
  
มุมมอง tree จะเป็น  
  
> ../{context}    
------ + /{userId}    
------------ + /{createdDate}  
------------------- + /{uuid}  
------------------------ + /file.{extension}  
------------------- + /{uuid}  
------------------------ + /file.{extension}  
------------------- + /{uuid}  
------------------------ + /file.{extension}  

**ตัวอย่าง**   

> C:\filestore\temp\1\2018-03-29\77ab5f8406aa441da6ee6e80fe02f17a\file.pdf  

- C:\filestore คือ root 
- 1 คือ userId (id เจ้าของไฟล์)   
- temp คือ context  
- 2018-03-29 คือ createdDate  
- 77ab5f8406aa441da6ee6e80fe02f17a คือ uuid  
- file.pdf คือ ไฟล์ที่จัดเก็บ  

**คำอธิบาย**

1. ขึ้นต้นด้วย `context` เพราะต้องการแยกประเภทของไฟล์ เช่น /temp /perm หรืออื่นๆ 

2. ต่อมา `userId` 
- ทำให้จำกัดสิทธิ์การเข้าถึงไฟล์ได้ง่าย โดยไม่จำเป็นต้องไปอ่านค่ามาจาก database เพื่อเช็คสิทธิ์ (เพราะฉะนั้นจะอ่าน/เขียน/ลบ หรือทำอะไรเกี่ยวกับไฟล์ได้เร็ว)  
- สามารถเอามาหา volumn ได้ว่า user คนนี้ใช้ storage ไปแล้วเท่าไหร่  
- และเช็ค หรือทำอะไรกับไฟล์ เราสามารถทำเป็นราย user ได้ง่าย เช่นการ migrate ข้อมูลเฉพาะ user เป็นต้น  

3. ที่คั่นด้วยวัน `createdDate` format yyyy-MM-ddd 
- เพื่อเอาไว้ sort ด้วยวันที่ 
- มองด้วยตาเปล่า ก็รู้ว่าวันนี้ user upload มาเท่าไหร่
- สามารถเช็ค หรือทำอะไรกับข้อมูลวันนั้นของ user ได้ง่าย  
  
4. ต่อมา `uuid` เพื่อการันตีความ unique 

5. ที่ตั้งชื่อ file เป็น `file` เพราะไม่อยากให้เกิดปัญหาชื่อไฟล์ภาษาไทย หรืออักขระพิเศษบางตัว ที่อาจทำให้ไฟล์เสียหาย  ซึ่งถ้าอยากได้ชื่อไฟล์จริงๆ ที่ upload มาให้ไปเอาที่ database (column display name)    

6. กำหนดให้ 1 directory uuid จะจัดเก็บไฟล์เพียงแค่ 1 ไฟล์ เพื่อให้ง่ายต่อการ manage  

### Access Path  

> /{context}/{createdDate}/{uuid}/{baseName}.{extensionFile}  

1. ขึ้นต้นด้วย `context` เพื่อแยกประเภทของไฟล์  
2. `createdDate` เพื่อให้รู้ว่าไฟล์นี้ upload เมื่อไหร่    
3. `uuid` เพื่อให้ unique  
4. `baseName` เพื่อเอาไว้แสดงชื่อไฟล์  จริงๆ จะแก้เป็นอะไรก็ได้  ไม่มีผลในการ access file  (เป็นแค่ display name เท่านั้น)
5. `extensionFile` นามสกุลไฟล์  

**ตัวอย่าง**

> /api/file/temp/2018-03-29/6706609d98a3484fa3e0d5c5c5e0657a/เอกสารการจัดตั้งหน่วยงาน_ข้อ_3_.pdf  

# API

**อัพโหลดไฟล์ (Upload File)**  

> http `POST` => /{context}/upload

form-data paremeter  
```
file : [FILE]  
```

output  
```json
{
    "displayName": "test.pdf",
    "mimeType": "application/pdf",
    "fileSize": 147406,
    "displayFileSize": "143 KB",
    "accessPath": "/api/file/temp/2018-03-30/50b9f9c0f3404a7d93e9bf8ffedbbef6/test.pdf",
    "storePath": "/temp/1/2018-03-30/50b9f9c0f3404a7d93e9bf8ffedbbef6/file.pdf",
    "createdDate": "2018-03-30T13:29:23.025",
    "numberOfPages": 1,
    "numberOfPictures": 0,
    "userId": "1"
}
```

**เข้าถึงไฟล์ (Access File)**  

> http `GET` => /{context}/{createdDate}/{uuid}/{baseName}.{extension}  /* สำหรับ download file */  
> http `GET` => /{context}/{createdDate}/{uuid}/{baseName}.{extension}?preview  
> http `GET` => /{context}/{createdDate}/{uuid}/{baseName}.{extension}?token=xxxxx /* สำหรับเข้าถึง file ที่มีการแชร์ด้วย token */  

ตัวอย่าง

> /api/file/temp/2018-03-30/50b9f9c0f3404a7d93e9bf8ffedbbef6/test.pdf?preview  

**ตรวจสอบไฟล์ (Check File)**

> http `GET` => /{context}/{createdDate}/{uuid}/{baseName}.{extension}?exist   

output

```json
{
    "existed": true
}
```

ตัวอย่าง

> /api/file/temp/2018-03-30/50b9f9c0f3404a7d93e9bf8ffedbbef6/test.pdf?exist  

**ลบไฟล์ (Delete File)**

> http `DELETE` => /{context}/{createdDate}/{uuid}/{baseName}.{extension}

output

```json
{
    "deleted": true
}
```

**แชร์ไฟล์ (Share File)**

> http `POST` => /{context}/{createdDate}/{uuid}/{baseName}.{extension}?share

output

```json
{
    "link": "/api/file/temp/2018-03-30/0c38f989a970442fa4b03c071173edfc/test.pdf?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpc3MiOiIyIn0.r1nNDri0AJ73DMst30dTPLtIKq6sw2BpjJNLzghKt__LypvRpiiSsgNgz8NCuuyU"
}
```

# วิธีใช้งาน

### 1. ให้ extends abstract class `StorePathFileRequestConverterAdapter`

ไว้ใช้สำหรับแปลง file request -> local path file (system)       
โดยให้ implement method   

```
- getContextPath()  คือ directory ที่เอาไว้เก็บไฟล์ เช่น /temp หรือ /perm เป็นต้น
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempStorePathFileRequestConverter.java)

### 2. ให้ extends abstract class `FileManagerAdapter`

เป็นตัวกลางในการ read/write/delete file  

โดยให้ implement method 

```
- getStorePathFileRequestConverter() คือ ตัวแปลง local path จากข้อ 1
- getRootPath()  คือ root directory ของ file เราจะเก็บ file upload นี้ไว้ที่ไหน เช่น C:\filestore เป็นต้น   
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempFileManager.java)

### 3. ให้ extends abstract class `AccessPathFileRequestConverterAdapter`

เอาไว้ convert access path -> file request -> local path file   
โดยให้ implement method 

```
- getContextPath() ตัวอย่างเช่น /api/v1/file/temp หรือ /api/v1/file/perm เป็นต้น 
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempAccessPathFileRequestConverter.java)  

### 4. ให้ extends abstract class `FileUploaderAdapter`

เป็น manual upload file เผื่อเอาไว้ upload file ผ่าน java code   
โดยให้ implement method 

```
- getFileManager()  คือ file manager จากข้อ 2 
- getAccessPathFileRequestConverter() คือ ตัวแปลง access path จากข้อ 3
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempFileUploader.java)  

### 5. หากต้องการทำ controller เพื่อ upload file ให้ extends abstract class `FileHandlerAdapter`

โดยให้ implement method 

```
- getFileUploader()  คือ file uploader จากข้อ 4   
- getUserId(HttpServletRequest httpReq)  คือ userId ปัจจุบันที่กำลัง Login อยู่  
```

[ตัวอย่าง](https://github.com/pamarin-tech/filestore-example/blob/master/src/main/java/com/pamarin/filestore/example/TempFileHandlerCtrl.java)  

# ตัวอย่างการใช้งาน 

https://github.com/pamarin-tech/filestore-example 
