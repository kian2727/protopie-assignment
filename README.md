# protopie

## structure
```

```

## Usages
### 1. docker를 통한 실행 ( app, database) 

```bash
# 서비스 실행
docker-compose up -d

# 컨테이너 중지, 이미지 삭제 후 새로 빌드하여 실행
docker-compose down --rmi all && docker-compose up -d --build
```