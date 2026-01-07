FROM openjdk:17-jdk-alpine

# 设置系统编码 防止中文乱码 当前基础镜像仅支持C.UTF-8编码格式
ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8

# 设置时区
ENV TZ=Asia/Shanghai

ADD target/user-service-1.0.0.jar app.jar
ENTRYPOINT [ "sh", "-c", "exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]