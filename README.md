# 원칙

- YAGNI: 필요 없으면 만들지 마라
- high order는 low order에게 의존해서는 안됨

# 일정

* action
    * 필수 정보는 모두 존재해야함: 제목, 날짜, 인원, 위치, 세부 설명
    * 
* data
    * 제목
    * 날짜
    * 인원
    * 위치
    * 세부 설명
  

# JWT

구성: header, payload

header -> b64.encoding
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9

```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

payload -> b64.encoding
eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022
}
```

* signature = HMAC512(encodedHeaderAndPayload, secretKey)  // SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
  * encodedHeaderAndPayload = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ",
  * secretKey = "dGVzdC10ZXN0dGVzdC10ZXN0LXRlc3R0ZXN0LXRlc3QtdGVzdHRlc3QtdGVzdC10ZXN0dGVzdC10ZXN0LXRlc3R0ZXN0LXRlc3QtdGVzdHRlc3QK"

jwt token  = encodedHeader + "." + encodedPayload + "." + signature


1. 왜 도대체 토큰을 저장하신거에요. 메커니즘 아세요?
2. secretKey는 base64로 지정하면 왜 나쁠까요?

토큰 발행 로직

1. header와 payload를 각각 base 64로 인코딩
2. 인코딩된 문자열을 .으로 연결 b64encode(header) + "." + b64encode(payload)
3. 2에서 계산된 값을 secret key를 가지고 암호화 시그니처 생성
4. b64encode(header) + "." + b64encode(payload) + "." + signature

# JWT pros & cons

pros
1. 서버에서 상태 저장을 위한 행위를 할 필요가 없음(DB, Redis 등)
2. 