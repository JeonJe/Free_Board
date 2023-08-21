# 자유게시판 (23.05.20 ~ 23.06.17, 1인)



## 서비스 소개 & 주요 기능


아래와 같은 기능을 제공하는 게시판 서비스입니다.

- **게시판 목록**
    - 등록일, 카테고리, 검색어 조건을 이용한 게시글 필터링
    - 페이지네이션
    - 페이지 이동 후 다시 돌아올 때 기존 검색 조건 유지
- 게시글 보기
    - 댓글 출력, 첨부파일 존재 시 다운로드 제공
    - 수정 또는 삭제 시 비밀번호 검증
- 게시글 등록
    - 입력조건에 따른 유효성 검증
    - 첨부파일 등록
- 게시판 수정
    - 첨부파일 추가 폼은 동적으로 생성 (최대 3개)
    - 첨부파일 변경 시 변경된 첨부파일만 적용
    - 비밀번호 검증 실패 시 수정 미 반영
- ERD
    ![SCR-20230622-jjqx](https://github.com/JeonJe/Board/assets/43032391/ad7e2584-9704-41e4-8eed-935dd51579a9)

    
## 아키텍쳐
- Front-End
   - @vue/cli 5.0.8(사용자), JSP
- Back-End
  - SpringBoot v3.1.0, JDK17, Mybatis3
- Database
  - MariaDB 10.11.3

1. JSP 게시판 (Model-1)
    - JSP,  JAVA, JDBC,  MariaDB(Docker)
2. Servlet 게시판(Model-2 Command Pattern 적용)
    - Servlet, JSP, JAVA, Mybatis, MariaDB(Docker)
3. SrpingBoot 게시판
    - SpringBoot, MariaDB, Mybatis
4. SpringBoot API + Vue.js 게시판
    - Restful API를 통한 Server/Frontend 분리
    - SpringBoot, Vue.js, MariaDB, Mybatis


## **사전학습**

Code Condtion(Java, Javascript) - [링크](https://www.notion.so/Code-Convention-Java-Javscript-68d31ecf66a14cf3bfe7dbf3ac46ea1e?pvs=21) 

JSP & Servlet 개념 - [링크](https://www.notion.so/JSP-Servlet-581c9b517fb0491583b9e04a80764a53?pvs=21)

Design Pattern - [링크](https://www.notion.so/Design-Pattern-17de6ff6001d4876a382e5f68dd3ca71?pvs=21)

## 어려웠던 점
**Multipart/FormData 수신** 

게시글 내용 저장/수정 시, 게시글에 포함된 첨부파일 정보도 함께 서버로 전송되어야 하기 때문에 클라이언트에서 서버로 보내는 HTTP Request의 `HTTP Header`의 `Content-Type`을 `Multipart/form-data`으로 지정하여 정해진 형식에 따라 메시지를 인코딩하여 전송해야합니다

SpringBoot에서 Multipart Request를 다룰 수 있는 @ModelAttribute, @RequestPart, @Requestparam을 활용하여 게시글 정보, 파일 목록, 삭제되는 첨부파일 아이디 목록을 받으려고 아래와 같이 작성하였습니다. 

[참고 - spring boot multipart](https://www.baeldung.com/sprint-boot-multipart-requests)

```java
public ResponseEntity<APIResponse> updateBoard(
		ModelAttribute BoardVO newBoard,
    @RequestParam(value = "files", required = false ) List<MultipartFile> files,
   @RequestPart(value = "deletedAttachmentIds", required = false) List<Integer> deletedAttachmentId)

```

Postman을 통한 데이터 전달은 정상적으로 수신하였지만, Vue.js에서 FormData 객체에 값을 담아 서버로 전달시엔 500에러가 발생하였습니다. 

이 과정에서 문제를 해결하기 위해 Multipart 학습 진행하였고, BoardVO와 List<MultipartFile> files, List<Integer> deletedAttachmentIds를 가지고 있는 VO를 통해 폼데이터를 값을 수신할 수 있도록 변경하여 문제를 해결할 수 있었습니다.

   
## 배운점
- JSP, Servlet, JAVA, Spring, Mybatis, Multipart, Command Pattern 등 `새로운 프로그래밍 언어`, `프레임워크` 등을 학습하여 게시판 서비스를 구현하였습니다.
- 주말마다 실무에 계신 개발자님으로부터 제가 작성한 코드에 대해 피드백을 받으며 코드의 가독성과 품질이 많이 부족하다는 것을 깨달았습니다. 이 과정을 통해 `협업을 위한 주석 작성, 코드 컨벤션 학습, 변수/함수 네이밍 고민` 등 어떤 부분을 더 많이 보완해야 할지 방향성을 잡을 수 있었습니다.
- 동일한 기능을 JSP Model-1 버전부터 스프링에서 제공하는 Annotation과 Lombok 라이브러리를 활용한 버전까지 개발해 보며 프레임워크, 라이브러리가 왜 필요한지에 대해 몸소 깨달을 수 있었고 프로젝트를 진행하면서 `불필요한 코드의 양을 줄이면서 코드의 품질을 높이는 경험`을 하였습니다.
  

## 주차별 코드 리뷰


*공통 피드백*
---
### 중요 피드백 
**`변수명, 메소드명, 주석을 컨벤션에 맞춰 다른사람이 명확하게 식별할 수 있게 꼼꼼히 작성 필요`**

### 코딩 컨벤션
1. DB는 `snake case`
2. `SQL` 키워드는 `대문자`, 그 외에는 `소문자 사용 `
3. 자바 `변수`명은 소문자 
4. 모든 멤버변수, 메소드, 클래스에 `javadoc` 주석을 다는 습관
5. 함수의 매개변수가 많으면 좋지 않기 때문에 Map이나 객체에 담아 전달
6. 단건조회 메소드명은 뒤에 ById, ByBoard 등으로 표현

### 프로젝트 
1. `request.getParamter(”변수”).equals(””)` 은 NullPointException이 발생하는 구문
    - `“”.equals(request.getParameter(“변수“))`로 작성해야 NullPointException이 발생하지 않고 비교
2. null point Exception은 발생하면 안되지만 발생할 가능성도 있기 때문에 글로벌 exception으로 담아서 화면에 전달 
3. mybatis에서 $보다 #을 활용해서 사용
    - #을 사용하면 문자열로 변환되어 들어가기 때문에 SQL Injection같은 공격구문에 안전
4. 파일 다운로드(스트림)은 컨트롤러에서 처리
5. 파일을 통째로읽어서 통째로 내리거나 올리지 말고 버퍼를 담아, 일부분씩 전달하도록 해야 메모리를 한번에 많이 사용하지 않음 
6. undefined와 null의 차이 공부 필요


*JSP 게시판 (Model-1)*
---
### 코딩 컨벤션
1. 테이블명은 스네이크 케이스로 작성
### 프로젝트 
1. 카테고리 테이블 별도의 테이블로 분리
2. Properties를 사용해 DB 정보 관리해야 한다. 배포 시 해당 파일을 제외 필요 
3. 많이 사용하는 코드는 자신만의 유틸리티 코드로 작성해 사용해 보는 것이 좋음
4. 비밀번호가 틀릴 땐 보통 `exception`을 발생. 현재 비밀번호가 틀릴 때 `return false`하는 것을 exception으로 처리할 수 있게 변경 필요
5. runtime exception과 valid exception을 구분해서 적용
6. 웹서버 파일 업로드 경로가 `서버 배포 폴더` 내부로 잡혀있음. 재 배포시 업로드된 파일들이 모두 삭제 되기 때문에 `웹서버와 관련없는 경로`를 지정하여 파일들을 저장 필요 (예: /var/repo… /)
7. `첨부파일 업로드 시`, 현재 첨부파일명과 동일한 첨부파일명이 이미 있을 수 있음.
이 경우 중복되지 않도록 숫자나 문자를 붙여 유니크한 파일이름을 생성 필요.
8. 현재 게시글 상세보기에서 목록으로 돌아가면 기존 검색 정보가 유지되지 않음. 기존 검색정보가 유지 될 수 있도록 변경필요
9. hash된 패스워드를 받은거면 SQL 쿼리 문안에서 비교연산하는 것이 코드가 더 깔끔


### DB
1. 새로운 DB 커넥션을 생성 후 instance를 획득하여 사용 필요 
2. `finally`에서 close 메소드를 사용해 Connection Close를 꼭 해줘야 함

  
*Servlet 게시판(Model-2 Command Pattern 적용)*
---
### 코딩 컨벤션
1. delete-file / addComment 와 같이 코드 통일성이 없음
    - URL은 delete-file, 자바코드는 deleteFile로 변경
2. 클래스명은 동사로 시작하지 않음.
    - `DeleteBoardCommand` → `BoardDeleteCommand` 로 변경
3. search 라는 파라미터의 이름은 불명확
    - `seachText` 등 구체적으로 변경 필요 
4. JSP  파일은 명명규칙은 없지만 소문자, 명사로 시작하는 것이 보기 좋음   
### 프로젝트
1. Command Pattern에서 URL 식별 시 `startwith`함수는 뒤에 URL 식별 문자 외 추가 문자열이 있을 경우 문제가 발생될 수 있는 여지가 있음
    - `parameter`로 `action`을 추가로 전달하여 서버에서 확인하면 위와 같은 문제를 방지하고 `CommandPattern` 코드를 줄일 수도 있음
2. setAttribute에 여러 값을 넣어 반환하기 보다, JSON처럼 필요한 key,value를 값들을 담아서 내릴 수 있도록 Map, VO 형태로 적용 필요 
3. 사용자가 “`null`” 이라는 문자열을 검색할 수도 있기 때문에 문자열 “`null`”을 예외처리 하면 안됨.
    -  파라미터에 null이 보이면 문자열”null”을 예외처리하는게 아니라, 오류페이지를 보여주는게 나은 판단
        - searchText = null 이면 Mybatis에서 “” 문자열로 변경
4. 첨부파일 수정 기능 구현에 관한 피드백 
    - hidden으로 attachment ID를 숨겨놓고, 사용자가 X버튼을 누르면 해당 attachment ID를 리스트에 담아 서버로 삭제 요청
    - 새로 전달받은 파일은 서버에 새로 업로드
5. File 저장, 업로드에서 item fieldName을 확인 후 switch문을 통해 값을 가져오는 부분은 좋은 코드가 아님
    - 게시글 저장, 수정은 멀티파트로 오기 때문에 getParametr로 action 값을 읽을 수 없음
    - Multipart 입력 스트림을 읽어 action을 뽑을 수는 있지만, 입력 스트림을 한번 읽게 되면 command에서 전달받은 `Request`에서 입력 스트림 내용이 없어저 제대로 된 값을 추출하지 못함
    - Map과 get 메소드를 사용하여 값을 추출하도록 코드 변경
            
  
*SrpingBoot 게시판*
---
### 코딩 컨벤션 
1. `interface {**}Mapper` 에서 Mapper보단 `repository`나 `DAO`로 작성해야 하는게 네이밍이 적절
2. save에서 category_id로 받는 파라미터도 있고, confirmPassword로 받는 파라미터도 있음. 이름 통일 
### SpringBoot
1. 멤버 변수 Autowired보다, `생성자 Autowired`를 활용
    - 생성자 주입을 사용 시, 순환참조가 발생할 때 컴파일 단계에서 오류가 발생하기 때문에 프로그램이 `더 안전`
2. 파라미터 유효성 확인 위치는 `컨트롤러` 
3. `model.addAttribute`에서  board, attachments, comments로 나눠서 추가해도 괜찮음
4. 명시적 타입 캐스팅은 불필요
4. 중복되는 내용이 많으면 `private` 함수로 빼서 사용 
5. 스프링에서 인자를 전달 받을 때 `HttpServletRequest`로 받지 말고  `@RequestParam등 스프링이 제공하는 기능을 사용해 코드를 줄여야 함`
8. 리턴 값을 `Map`이 아닌 특정 타입으로 명확하게 반환하는 것이 코드 가독성에 좋음
9. VO의 포맷팅은 화면에서 하는 것이 좋음
    -날짜는 표준상태로 데이터베이서에 저장하고 화면에서 필요에 따라 포맷팅
  
  
*SpringBoot API + Vue.js 게시판*
---
### API
1. URL 상세 작성
    - /list 보다 /board/list가 더 명확
### Vue.js 
1. 클라이언트로부터 페이지 사이즈와 현재 페이지 넘버만 전달받으면 페이지네이션 가능
2. URL은 JS에서 import해서 사용하는게 아니라, properties에서 가져와 사용
3. axios 요청은 services.js로 모아서 관리해야 AOP 적용 등 관리에 용이 
4. `async + await`를 활용하면 promise를 사용하는 코드를 더 줄일 수 있음

### SpringBoot
1. `null` 체크를 라이브러리를 사용해 짧게 변경 가능
2. 컨트롤러에서 파라미터에 대한 Null 체크보다, Parameter을 서비스에 바로 넘겨 코드의 명확성을 갖는 것이 더 좋은 코드로 판단
3. 결과 클래스 만들어 값을 맵핑하여 반환하는 식으로 작성하는 편이 명확한 코드
    - 클래스 안에서 seach 관련 코드를 묶을 수도 있고, 서버 쪽은 해당 해당 클래스만 알고 있으면 됨
    ```java
    public class APIresult {
        int result // 0 은 실패, 1은 성공 
    ....
        }
    ```
4. 테이블 생성시간을 객체에 현재 날짜로 적용하는 set 부분은 Mybatis에서 `Now()`를 함수를 사용하여 구현할 수 있기 때문에 불필요
5. `@Value` 어노테이션을 사용하여 Properties에서 값을 가져와 uploadPath로 사용 
6. Mybatis에서 중복되는 쿼리는 `sql, include ref` 키워드를 사용하여 줄일 수 있음


