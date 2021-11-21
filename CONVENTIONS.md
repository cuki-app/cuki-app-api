# [ CUKI PROJECT CONVENTIONS ]

## 01. 스프링 컨벤션

1. global 설정
   - Application 위에 Annotation 달도록
2. DI 방식은 특별한 이유가 없다면 생성자 주입 방식으로 한다. (단, 테스트 코드는 예외)


## 02. 개발 컨벤션

1. 서비스 DTO 네이밍 컨벤션
   - 패턴 - `{verb}{domain}{Request[, Response]}`
     - ex : SendVerificationCodeForSignUpRequestDto
2. 서비스 DTO 구조
   - 기본 어노테이션
     - `@Getter`
     - `@ToString`
     - `@RequiredArgsConstructor`
   - 필드는 final로 선언
3. 무분별한 @Setter 사용은 지양
4. 다른 팀원의 코드를 임의로 수정 혹은 추가하지 않는다. 필요한 경우 요청한다.
5. 사용하지 않는 import와 불필요한 주석은 삭제한다.


## 03. JPA 엔티티 컨벤션

1. enum 타입
   - 아묻따 String 타입으로
2. N:N 관계
   - 자동으로 생성되는 테이블 금지
3. 요청과 응답으로 DTO 객체 사용 (엔티티 객체 절대 지양)


## 04. 테이블 네이밍 컨벤션

1. 테이블 이름
    - 엔티티와 동일한 이름(단, DB 예약어는 피한다.)
2. PK 이름
    - id로 통일
3. 제약조건 이름
    - `{constraint}_{table_name}_{column_name}`
      - ex : uq_member_email