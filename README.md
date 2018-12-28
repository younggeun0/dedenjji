# 뒤집어라 엎어라 

* Client-Server 프로그래밍 연습을 위한 toy project
* 짝수 인원이 서버에 접속, 앞/뒤 선택 후 서버에 전송
  * 앞/뒤 선택 쌍이 맞다면 서버가 결과를 클라이언트들에게 출력
  
## 초기 UI 디자인

### 클라이언트

* Connect Server로 접속, 앞, 뒤 선택 후 Send로 서버에 결과 전송
  * 서버에서 Show Result를 누르면 접속한 사람이 짝수고 결과가 모두 전송받았다면 짜여진 팀 결과를 전달받는다.

![clinetUI](https://github.com/younggeun0/dedenjji/blob/master/img/clientUI.png?raw=true)

### 서버

* Open버튼은 구동시 Close로 바뀜
  * 한 버튼으로 On/Off기능을 구현
* 4인 이상 짝수 유저가 서버에 접속 후 선택을 서버에 모두 보내면 Show Result를 클릭
  * 전달받은 앞, 뒤 값이 쌍이 맞으면 같은 값을 선택한 클라이언트끼리 팀을 맺게되었다는 결과를 출력
  * 앞, 뒤 값이 쌍이 안맞으면 다시 선택하라는 요청을 출력

![serverUI](https://github.com/younggeun0/dedenjji/blob/master/img/serverUI.png?raw=true)



