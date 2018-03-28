# filestore

> java library สำหรับ spring framework ใช้เพื่อ อ่าน/เขียน/อัพโหลด file system

# วิธีใช้งาน

### 1. ให้ extends abstract class `FileManagerAdapter`

โดยให้ implement method 

```java
getRootPath() /* คือ root directory ของ file เช่น /tmp เป็นต้น */
```

### 2. ให้ extends abstract class `FileUploderAdapter`


### 3. หากต้องการทำ controller เพื่อ upload file ให้ extends abstract class `FileHandlerAdapter`

