# filestore

> java library สำหรับ spring framework ใช้เพื่อ อ่าน/เขียน/อัพโหลด file system

# วิธีใช้งาน

### 1. ให้ extends abstract class `FileManagerAdapter`

โดยให้ implement method 

```
- getRootPath()  คือ root directory ของ file เช่น /tmp เป็นต้น 
```

### 2. ให้ extends abstract class `FileUploderAdapter`

โดยให้ implement method 

```
- getFileManager()  คือ file manager จากข้อ 1 
- getUserId()  คือ userId ปัจจุบันที่กำลัง Login อยู่ 
```

### 3. หากต้องการทำ controller เพื่อ upload file ให้ extends abstract class `FileHandlerAdapter`

โดยให้ implement method 

```
- getFileManager()  คือ file manager จากข้อ 1 
- getFileUploader()  คือ file uploader จากข้อ 2 
- getUserId()  คือ userId ปัจจุบันที่กำลัง Login อยู่  
```
