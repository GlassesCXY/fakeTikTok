## 本地启动minio
```shell
docker run -p 9000:9000 -p 9001:9001 
-v /home/z/minio:/data minio/minio server /data --console-address ":9001"
```

## minio 管理地址
https://minio.yueyueyu.one  
username: minioadmin  
password: minioadmin
