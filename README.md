# simple-curl
텍스트 웹 브라우저 만들기 

HTTP Reqeest Message 

Linux command curl means Client URL

## Usage
Usage: scurl [options] url
Options:
- -v                 verbose, 요청, 응답 헤더를 출력
- -H <line>          임의의 헤더를 서버로 전송
- -d <data>          POST, PUT 등에 데이터를 전송
- -X <command>       사용할 method 를 지정합니다. 지정되지 않은 경우 기본값은 GET
- -L                 서버의 응답이 30x 계열이면 다음 응답을 따라 감(리다이렉션)

## 커맨드 창에 CLI 입력
### 일반 입력
> $ scurl http://httpbin.org/get

### HTTP 요청 메시지 지정
> $ scurl -X GET http://httpbin.org/get

### 임의의 헤더 추가
> $ scurl -v -H "X-Custom-Header: NA" http://httpbin.org/get

### HTTP 요청 메시지 POST, PUT (body 추가)
> $ curl -v -X PUT -d { "hello": "world" } -H "Content-Type: application/json" http://httpbin.org/post

### 리다이렉션
> $ scurl -v  -L http://httpbin.org/status/302
