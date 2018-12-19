# 简介
这是湫水山项目后端，采用spring boot和spring cloud，实践微服务架构。

# 组成部分
1.  eurekasvr是spring cloud的服务发现代理，可以从端口8761访问，它的实例已经部署在[http://192.168.1.37:8761/](http://192.168.1.37:8761/)
2.  confsvr是spring cloud的配置服务器，其它微服务都是从它这儿读取配置信息的，它开放8888端口。目前它用[https://gitee.com/mianniu/config-repo](https://gitee.com/mianniu/config-repo)作为后端存储库。

        以resources servive的default profile为例
        请求：（get）http://192.168.1.37:8888/resourcesservice/default
        回应：
        {
            "name": "resourcesservice",
            "profiles": [
                "default"
            ],
            "label": null,
            "version": "b4a305d054c1a199ec0c07bbeb77929f9b44a898",
            "state": null,
            "propertySources": [
                {
                    "name": "https://gitee.com/mianniu/config-repo/resourcesservice/resourcesservice.yml",
                    "source": {
                        "spring.jpa.database": "MYSQL",
                        "spring.jpa.show-sql": "true",
                        "spring.database.driverClassName": "com.mysql.jdbc.Driver",
                        "spring.datasource.url": "jdbc:mysql://192.168.1.38:3306/mtqiushui.resources",
                        "spring.datasource.username": "mtqiushui_resources",
                        "spring.datasource.password": "qiushui88"
                    }
                }
            ]
        }

3.  zuulsvr是spring cloud的服务网关，它会与eureka服务发现代理配合工作，转发对各个微服务的请求。它的实例部署在[http://192.168.1.37:5555/](http://192.168.1.37:5555/)

        查询当前所有路由
        请求：（get）http://192.168.1.37:5555/routes
        回应：
        {
            "/api/organization/**": "organizationservice",
            "/api/licensing/**": "licensingservice",
            "/api/auth/**": "authenticationservice",
            "/api/configserver/**": "configserver",
            "/api/resourcesservice/**": "resourcesservice"
        }
        此时访问http://192.168.1.37:5555/api/resourcesservice/v1/materials/1，会转发到http://resourcesservice:port/v1/materials/1

4.  resources service是资源服务，包括物料、机台、工艺等各项资源的相关功能。
5.  auth service是用户和组织服务，负责用户注册、维护用户与组织之间的关系。
6.  dispatching service是派报工服务，包括计划、任务、指派等工单相关的功能。

# 创建docker image

在根目录，或者分别进入各服务目录运行以面的命令.  这个命令会运行定义在pom.xml中的 [Spotify docker 插件](https://github.com/spotify/docker-maven-plugin).  

   **mvn clean package docker:build**
 
# 运行项目

用docker-compose启动docker image.

   **docker-compose -f docker/common/docker-compose.yml up**
