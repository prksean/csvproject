# 웹(풀스택) 개발 프로젝트

## 주요 구현 기능

- 대량 파일 (CSV 10만건 이상) 업로드를 통한 Data 저장 기능.
- drag & drop 파일 업로드 기능.
- 업로드된 파일 데이터베이스에 insert 기능.
- 업로드 진행 상태 progress bar 로 표시 기능.

------------

### 개발 환경

- IntelliJ IDEA Ultimate 2020.03
- macOS Mojave 10.14.6

------------

### 개발 스택

- ReactJS 17.0.1
- Spring Boot 2.4.3
- Java 11
- MySQL 8.0.23

------------

### 빌드전 application.properties 주요 설정 확인

- 프로젝트 사용 포트 8090
```
server.port = 8090
```
- database 명 testdb 사용. 사용자 root password 설정 필요.
```
spring.datasource.url=jdbc:mysql://localhost:3306/testdb?useSSL=false&serverTimezone=Asia/Seoul&cachePrepStmts=true&useServerPrepStmts=true&rewriteBatchedStatements=true
spring.datasource.username=root
spring.datasource.password={YOUR_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
- 필요한 table은 자동으로 생성 되도록 설정됨.
```
spring.jpa.hibernate.ddl-auto= update
```
- 100MB 이상 파일 업로드 테스트시 max file size 수정
```
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```
------------

### 빌드 및 실행

1. 프로젝트 clone

```
> git clone https://github.com/prksean/csvproject.git
> cd csvproject
```

2. deploy.sh 실행 (윈도우의 경우 실행 전 수동으로 8090 포트 사용 확인 및 kill)

###### MAC/Linux

```
> sh deploy.sh
```

###### Windows

```
> netstat -ano | findstr 8090
> taskill /pid 5944 /f 
> sh deploy.sh
```

------------

### API

- CSV파일을 받아 repository를 사용하여 db table에 레코드 저장.

```
REQUEST
POST /csv/upload
Content-Type: multipart/form-data

RESPONSE
Header Content-Type: application/json
{
    "message":"File uploaded successfully: 데이터셋.csv",
    "cnt":100000,
    "time":33.711325992
} 
```

- CsvController
    - `/upload` 요청 처리.
    - `csvUploader`가 `@RequestPart("file") MultipartFile file`을 input으로 받아 처리.
    - `CsvUtil`의 `isCsvType` 메소드를 사용하여 CSV 포멧 여부 확인.
    - `CsvService`의 `persistPeople` 메소드를 사용하여 CSV 파일 DB Insert.
    - `ResponseEntity<ResponseMessage>`를 return.

- PeopleEntity
    - `long id` (PK), `String firstname`, `String lastname`, `String email`

- CsvService
    - persistPeopleData
        - JPA `saveAll()` 사용 DB에 데이터 삽입.
        - `@Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")`로 지정된 `batch_size`를 사용하여 분할 `saveAll()` 진행
        - `StopWatch`를 사용하여 데이터 삽입 시간 측정.
    - persistPeople
        - `CsvUtil`의 `parseCsvToPeople` 메소드를 사용하여 CSV를 `PeopleEntity` 리스트로 파싱.
        - 파싱된 리스트를 `persistPeopleData` 메소드를 사용하여 DB에 데이터 삽입.
    
- CsvUtil
    - isCsvType
        - input으로 받은 파일의 type이 `text/csv` 인지 확인.
    - parseCsvToPeople
        - `opencsv`의 `CSVReader`를 사용하여 input으로 받은 CSV 파일을 `PeopleEntity` 리스트로 파싱.

------------

### UI Workflow 스크린샷
- (./src/main/webapp/README.md) 참고.
