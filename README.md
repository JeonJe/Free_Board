# 자유게시판

Tags: JAVA, JSP, Mybatis, SpringBoot, Servlet, Vue.js


# 서비스 소개 & 주요 기능


아래와 같은 주요 기능을 제공하는 게시판 구현

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

    
# 개발 기간 & 범위

2023.05.20 ~ 2023.06.17, 1인

1. JSP 게시판 (Model-1)
    - JSP,  JAVA, JDBC,  MariaDB(Docker)
2. Servlet 게시판(Model-2 Command Pattern 적용)
    - Servlet, JSP, JAVA, Mybatis, MariaDB(Docker)
3. SrpingBoot 게시판
    - SpringBoot, MariaDB, Mybatis
4. SpringBoot API + Vue.js 게시판
    - Restful API를 통한 Server/Frontend 분리
    - SpringBoot, Vue.js, MariaDB, Mybatis


# **사전학습**

Code Condtion(Java, Javascript) - [https://www.notion.so/whssodi/Code-Convention-Java-Javscript-68d31ecf66a14cf3bfe7dbf3ac46ea1e?pvs=4](https://www.notion.so/Code-Convention-Java-Javscript-68d31ecf66a14cf3bfe7dbf3ac46ea1e?pvs=21) 

JSP & Servlet 개념 - [https://www.notion.so/whssodi/JSP-Servlet-581c9b517fb0491583b9e04a80764a53?pvs=4](https://www.notion.so/JSP-Servlet-581c9b517fb0491583b9e04a80764a53?pvs=21)

Design Pattern - [https://www.notion.so/whssodi/Design-Pattern-17de6ff6001d4876a382e5f68dd3ca71?pvs=4](https://www.notion.so/Design-Pattern-17de6ff6001d4876a382e5f68dd3ca71?pvs=21)

# 어려웠던 점
**Multipart/FormData 수신** 

게시글 내용 저장/수정 시, 게시글에 포함된 첨부파일 정보도 함께 서버로 전송되어야 하기 때문에 클라이언트에서 서버로 보내는 HTTP Request의 `HTTP Header`의 `Content-Type`을 `Multipart/form-data`으로 지정하여 정해진 형식에 따라 메시지를 인코딩하여 전송해야합니다

SpringBoot에서 Multipart Request를 다룰 수 있는 @ModelAttribute, @RequestPart, @Requestparam을 활용하여 게시글 정보, 파일 목록, 삭제되는 첨부파일 아이디 목록을 받으려고 아래와 같이 작성하였습니다. 

([https://www.baeldung.com/sprint-boot-multipart-requests](https://www.baeldung.com/sprint-boot-multipart-requests))

```java
public ResponseEntity<APIResponse> updateBoard(
		ModelAttribute BoardVO newBoard,
    @RequestParam(value = "files", required = false ) List<MultipartFile> files,
   @RequestPart(value = "deletedAttachmentIds", required = false) List<Integer> deletedAttachmentId)

```

Postman을 통한 데이터 전달은 정상적으로 수신하였지만, Vue.js에서 FormData 객체에 값을 담아 서버로 전달시엔 500에러가 발생하였습니다. 

이 과정에서 문제를 해결하기 위해 Multipart 학습 진행하였고, BoardVO와 List<MultipartFile> files, List<Integer> deletedAttachmentIds를 가지고 있는 VO를 통해 폼데이터를 값을 수신할 수 있도록 변경하여 문제를 해결할 수 있었습니다.

   
# 배운점
- JSP, Servlet, JAVA, Spring, Mybatis, Multipart, Command Pattern 등 새로운 프로그래밍 언어, 프레임워크 등을 학습하여 게시판 서비스를 구현하였습니다.
- 주말마다 실무에 계신 개발자님으로부터 제가 작성한 코드에 대해 피드백을 받으며 코드의 가독성과 품질이 많이 부족하다는 것을 깨달았습니다. 이 과정을 통해 협업을 위한 주석 작성, 코드 컨벤션 학습, 변수/함수 네이밍 고민 등 어떤 부분을 더 많이 보완해야 할지 방향성을 잡을 수 있었습니다.
- 동일한 기능을 JSP Model-1 버전부터 스프링에서 제공하는 Annotation과 Lombok 라이브러리를 활용한 버전까지 개발해 보며 프레임워크, 라이브러리가 왜 필요한지에 대해 몸소 깨달을 수 있었고 점점 코드의 양을 줄이고 품질을 높이는 경험을 하였습니다.
  

# 주차별 코드 리뷰

*JSP 게시판 (Model-1)*
---
1. 카테고리 테이블 분리 & 테이블명 스네이크 케이스로 변경하다.
2. Properties를 사용해 DB 정보 관리해야 한다. 배포 시 해당 정보는 미 포함되어야 한다.
3. 새로 DB 커넥션을 생성 후 instance를 획득하여 사용해야한다.
4. 많이 사용하는건 util로 빼서 자신만의 유틸리티를 작성해보는 것이 좋다. 
5. Connection Close가 하나도 없다. `finally`에서 close 메소드를 사용한다
6. 비밀번호가 틀릴 땐 보통 `exception`을 던진다. 현재 `return false`하는 것을 exception으로 처리할 수 있게 변경해라.
    - runtime exception과 valid exception을 구분해서 적용해보자.
7. 현재 웹서버 파일 업로드 경로가 `서버 배포 폴더`에 잡혀있다. 이 경우 재 배포시 업로드된 파일들이 모두 삭제 되기 때문에 `웹서버와 관련없는 경로`를 지정하여 파일들을 저장해야 한다. (보통 /var/repo… /)
8. `첨부파일 업로드 시`, 현재 첨부파일명과 동일한 첨부파일명이 이미 있을 수 있다. 이 경우 중복되지 않도록 숫자나 문자를 붙여 유니크한 파일이름을 생성해야한다. 즉, fileName과 originFileName 두개 필드로 관리하여 사용자한테는 업로드 시 첨부파일명인 originFileName을 보여줘야하고 파일 식별 시에는 중복되지 않은 fileName으로 파일을 찾을 수 있도록 한다.
9. 현재 게시글 보기에서 목록으로 돌아가면 기존 검색 정보가 유지되지 않는다. 기존 검색정보가 유지 될 수 있도록 변경한다.
10. hash된 패스워드를 받은거면 SQL 쿼리 문안에서 비교연산하는 것이 코드가 더 깔끔하다.
  
*Servlet 게시판(Model-2 Command Pattern 적용)*
---
1. delete-file / addComment 와 같이 코드 통일성이 없다. 
    - URL은 delete-file, 자바코드는 deleteFile로 변경
2. Command Pattern에서 URL 식별 시 `startwith`함수는 /list 뒤에 추가문자열이 있을 경우 문제가 발생될 수 있는 여지가 있다.
    - parameter로 action을 추가적으로 받도록 변경하면 분기하는 위와 같은 상황을 방지하고 CommandPattern 코드를 줄일 수도 있다. 
3. 클래스명은 동사로 시작하지 않는다. `DeleteBoardCommand` → `BoardDeleteCommand` 로 변경해라.
4. search 라는 파라미터의 이름은 불명확하다.  `seachText` 등 구체적으로 변경해라.
5. 메소드명은 동사로 시작한다.
6. JSP  파일은 명명규칙은 없지만 소문자, 명사로 시작하는 것이 보기 좋다.   
7. setAttribute에 여러개의 값을 넣기보다, JSON처럼 여러개의 key,value를 담아서 하나로 내릴 수 있도록 Map, VO 등을 적용해보자 
    - 사용자가 “`null`” 이라는 문자열을 검색할 수도 있기 때문에 문자열 “`null`”을 예외처리 하면 안된다. 파라미터에 null이 보이면 문자열”null”을 예외처리하는게 아니라, 오류페이지를 보여주는게 낫다.
        - searchText = null 일 시 “” 문자열로 변경하여 DB 쿼리 수행
8. 첨부파일 수정 기능 구현
    - hidden으로 attachment ID를 숨겨놓고, 사용자가 X버튼을 누르면 해당 attachment ID를 리스트에 담아둔다. 수정 버튼을 눌러 비밀번호 확인 후 삭제 첨부파일 리스트를 서버에 전달 해, 삭제가 필요한 attachment ID를 가진 데이터베이스와 파일을 삭제한다. 새로 전달받은 파일은 서버에 새로 업로드한다.
9. 수정페이지 `Cancel` 기능은 Javascript로 작성할 수 있지만, form형태로 바로 전달 할 수 있다. form형태로 변경하면 불필요한 스크립트를 작성하지 않아도 된다.
10. File save, upload에서 item fieldName을 확인 후 switch문을 통해 값을 가져오는 부분은 좋은 코드가 아니다. 
    - 게시글 저장, 수정은 멀티파트로 오기 때문에 getParametr로 action 값을 읽을 수 없다.
    - Multipart 입력 스트림을 읽어 action을 뽑을 수는 있지만, 입력 스트림을 한번 읽게 되면 command에서 전달받은 `Request`에서 입력 스트림 내용이 없어저 제대로 된 값을 추출하지 못하였다.
    - Map과 get 메소드를 사용하여 값을 추출하도록 코드 변경
            
  
*SrpingBoot 게시판*
---
1. 멤버 변수 Autowired보다, 생성자 Autowired를 활용하자.
    - 생성자 주입을 사용 시 순환참조가 발생할 때 컴파일 단계에서 오류가 발생하기 때문에 프로그램이 더 안전하다.

2. 파라미터 유효성 확인 위치는 컨트롤러
3. model.addAttribute에서  board, attachments, comments로 나눠서 써도 된다. 묶어서 써봤자 뷰에서 또 나누기 때문이다.
    - 명시적 타입캐스팅은 안써도 된다. 어차피 model에서 타입캐스팅이 필요없기 때문이다.
4. 중복되는 내용이 많으면 private 함수로 빼서 사용하는 것이 좋다.
5. 스프링에서 인자를 전달 받을 때 HttpServletRequest가 아니라, `@RequestParam등 스프링 기능을 사용해 코드를 줄여라`
6. `interface {**}Mapper` 에서 Mapper보단 repository나 DAO로 작성해야 하는게 네이밍이 적절하다.
7. save에서 category_id로 받는 파라미터도 있고,. confirmPassword로 받는 파라미터도 있다. 이름 통일이 필요하다.
8. 리턴 값을 `Map`이 아닌 특정 타입으로 명확하게 작성하는게 코드를 읽기에 좋다.
9. VO의 포맷팅은 화면에서 하는 것이 좋다. 데이터베이스는 표준상태로 저장하자. 즉 → VO의 getter에서 포맷터를 이용해 변경하는 건 불필요하다.
  
  
*SpringBoot API + Vue.js 게시판*
---
1. searchText `null` 체크를 라이브러리를 사용해 짧게 변경 가능하다.
2. URL 상세 작성
    - /list 보다 /board/list가 더 명확하다.
3. 컨트롤러에서 파라미터에 대한 Null 체크보다, Parameter 들을 서비스에 바로 넘겨 코드의 명확성을 갖는 것이 더 좋은 코드로 판단된다.
4. 결과를 Map으로 하기 보다 예로 `APIResult` 클래스 만들어 값을 맵핑하여 반환하는 식으로 작성하는 편이 좋다. 클래스 안에서 seach 관련 코드를 묶을 수도 있고, 서버쪽은 해당 해당 클래스만 알고 있으면 반환타입을 모두 알 수 있기 때문이다.

    ```java
    public class APIresult {
        int result // 0 은 실패, 1은 성공 
    ....
        }
    ```

5.  화면에서 게시글을 몇 개씩 보여줄 것인지, 현재 페이지가 몇 페이지인지만 전달해주면 서버에서 조건에 따른 데이터를 검색하고 프론트에 전달할 수 있다. 즉, 그 외 페이지네이션 값은 화면에서 조절 가능하다.
6. 객체를 만들고 생성된 시간을 현재 날짜로 적용하는 set 부분은 Mybatis에서 Now()를 함수를 사용하여 구현할 수 있기 때문에 불필요하다.
7. 아래와 같이 사용하면 스프링부트의 제어에 벗어날 가능성이 있다.`@Value` 어노테이션을 사용하여 Properties에서 값을 가져와 uploadPath를 멤버 변수로 담아 사용한다.
8. URL은 JS에서 import해서 사용하는게 아니라, properties에서 가져와 사용해야한다.
9. axios 요청은 services.js로 따로 묶어야 유지보수에 용이하다. (AOP 적용 등)
10. promise보다 async + await를 활용하면 코드를 더 줄일 수 있다.
11. Multipart 파일은 formdata형태로도 보낼 수 있지만, base64 인코딩을 하여 파일을 전달 할 수 도 있다.
12. Mybatis에서 중복되는 쿼리는 sql, include ref 키워드를 사용하여 줄일 수 있다.
13. BoardSearchResponse와 BoardInfoResponse는 하나로 묶어서 처리하는 것이 코드적으로 좋을 듯하다. 

*스터디원 공통 피드백*
---
1. DB 테이블 필드명은 snake case 사용 
2. SQL 키워드는 대문자, 그 외에는 소문자 사용 
3. 자바 변수명은 소문자사용한다.
4. 모든 멤버변수, 메소드, 클래스에 javadoc 주석을 다는 습관을 들이자.
5. 함수의 매개변수가 많으면 좋지 않기 때문에 Map이나 객체에 담아 전달한다.
6. JSP에 Print 문등이 남아 있으면 안된다.
7. DB기준으로 타임존을 설정해도 무방하다. 
8. **변수명, 메소드명, 주석 등을 다른사람이 명확하게 식별할 수 있게 컨벤션에 맞춰 꼼꼼히 작성이 필요하다.**
9. `request.getParamter(”변수”).equals(””)` 은 NullPointException이 발생하는 구문이다.
    - `“”.equals(request.getParameter(“변수“))` 이런식으로 작성해야NullPointException이 발생하지 않고 비교할 수 있다.
10. 단건조회 메소드명은 뒤에 ById, ByBoard 이렇게 사용하면 좋다.
11. null point 익셉션 같은건 있으면 안되지만, 발생할 가능성도 있기 때문에 글로벌 exception으로 담아서 포장해서 화면에 전달한다.
12. mybatis에서 $보다 #을 활용해서 사용해라
    - #을 사용하면 문자열로 변환되어 들어가기 때문에 SQL Injection같은 공격구문에 강하다.
13. 페이지네이션 정보는 묶여서 model attribute에 전달하면 좋다.
14. 파일 다운로드(스트림)은 컨트롤러에서 해주는게 맞다.
15. 파일을 통째로읽어서 통째로 내리거나 올리지 말고 버퍼를 담아 일부분씩 전달하도록 해야 메모리를 많이 잡아 먹지 않는다.
16. undefined와 null의 차이 공부가 필요하다.
17. 카테고리 이름을 가져올 때 서브쿼리를 활용해라.
18. 보통 실무에서는 Map보다 List<Object> 형태로 많이 사용한다.
19. Multipart 구조를 공부해두면 좋다.



