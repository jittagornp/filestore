# filestore

> java library สำหรับ spring framework ใช้เพื่อ อ่าน/เขียน/อัพโหลด file system

# วิธีใช้งาน

1. ให้ extends abstract class 
```java
FileManagerAdapter
```

2. ให้ extends abstract class 
```java
FileUploderAdapter
```

3. หากต้องการทำ controller เพื่อ upload file ให้ extends abstract class 
```java
FileHandlerAdapter  
```
