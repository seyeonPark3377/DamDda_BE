# Database Initial Setting

## Docker

1. CMD 열기.

### 도커 버전 확인 (설치 여부 확인)

`docker -v`

### 도커 이미지 다운로드

`docker pull mariadb:latest`

### MariaDB Docker 컨테이너 생성 및 실행

`docker run --name damdda-mariadb-container -e MARIADB_ROOT_PASSWORD=damdda -d -p 6506:3306 mariadb:latest`

### Docker 컨테이너 리스트 출력 (damdda-maraiadb-container 실행 여부 확인)

`docker ps -a`

### Option 1 - Container 시작

`docker start damdda-mariadb-container`

### Option 2 - Container 재시작

`docker restart damdda-mariadb-container`

### Option 3 - Container 중지

`docker stop damdda-mariadb-container`

## DBeaver

### 데이터베이스 생성

1.  “Connect to a database”.
2.  “MariaDB” 선택.
3.  “Connect to a database” 정보 입력 (Password: damdda).

    ![DBeaver_create_database.png](DBeaver_create_database.png)

4.  “Test Connection” 확인.
5.  “[localhost](http://localhost)” (생성된 localhost) 선택.
6.  “Databases” 우클릭.
7.  “Create New Database” 선택.
8.  “Database name: damdda
    Charset: utf8mb4
    Collation: utf8mb4_general_ci”
    정보 입력.

### 사용자 계정 생성

1.  “Users” 우클릭.
2.  “Users” 선택.
3.  “Create New User” 선택.
4.  User Name: damdda
    Host: %
    Password: damdda
    Confirm: damdda
    정보 입력.
5.  “Check All” 선택.
6.  “Save” 선택.
