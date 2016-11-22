# System Monitor
Real-time monitoring tool

## Runbook

### Development Environment
1. Install Git ([Git SCM](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git))
2. Install Java Standard Edition Development Kit  8 ([Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or [OpenJDK](http://openjdk.java.net/install/))
3. Install Maven 3 ([Apache Maven Project](https://maven.apache.org/download.cgi))

### Download
    git clone git@github.com:ilya-medvedev/system-monitor.git

### Build
    cd system-monitor
    mvn clean package

### Run
    java -jar application/target/application.jar ${properties}

### Properties
|        Property         | Required |                                                Description                                                 |
|:-----------------------:|:--------:|:----------------------------------------------------------------------------------------------------------:|
|  --sensors.disk.device  |   True   | Device name (See [/proc/diskstats](https://www.kernel.org/doc/Documentation/ABI/testing/procfs-diskstats)) |
| --sensors.net.interface |   True   |                                       Interface (See /proc/net/dev)                                        |

#### Links:
1. [Spring Boot Externalized Configuration](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)
2. [Spring Boot Common application properties](http://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)

### Run Docker image

#### Development Environment
1. Install Docker Compose ([Docker Compose](https://docs.docker.com/compose/install/))

#### Build
    cd docker
    docker-compose build

#### Run
    docker-compose -up -d

#### Stop
    docker-compose down