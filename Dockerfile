FROM gradle:8.14.3-jdk17 AS build

WORKDIR /app

# Gradle 파일들을 복사
COPY gradle/ gradle/
COPY gradlew gradlew.bat build.gradle.kts settings.gradle.kts gradle.properties ./

# 프로젝트 소스 코드 복사
COPY subproject/ subproject/

# Gradle 빌드 실행
RUN ./gradlew :subproject:boot:installShadowDist --no-daemon

# 실행 단계
FROM openjdk:17-slim

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/subproject/boot/build/install/boot-shadow/lib/boot-all.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "app.jar", "-port=8080"] 